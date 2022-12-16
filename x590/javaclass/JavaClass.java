package x590.javaclass;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import x590.javaclass.attribute.Attributes;
import x590.javaclass.attribute.annotation.AnnotationsAttribute;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.exception.ClassFormatException;
import x590.javaclass.exception.IllegalModifiersException;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.GenericParameter;
import x590.javaclass.type.Type;
import x590.javaclass.util.WhitespaceStringBuilder;
import x590.javaclass.util.IWhitespaceStringBuilder;
import x590.javaclass.util.Util;
import x590.jdecompiler.JDecompiler;
import x590.util.Pair;

import static x590.javaclass.Modifiers.*;

public class JavaClass extends JavaClassElement {
	
	public final Version version;
	public final ConstantPool pool;
	public final int modifiers;
	
	public final ClassType thisType, superType;
	public final List<ClassType> interfaces;
	
	public final List<JavaField> fields, constants;
	public final List<JavaMethod> methods;
	public final Attributes attributes;
	public final ClassInfo classinfo;
	
	public JavaClass(Version version, ConstantPool pool, int modifiers,
			ClassType thisType, ClassType superType, List<ClassType> interfaces,
			List<JavaField> fields, List<JavaMethod> methods, Attributes attributes, List<GenericParameter> generics) {
		
		this.version = version;
		this.pool = pool;
		this.modifiers = modifiers;
		this.thisType = thisType;
		this.superType = superType;
		this.interfaces = interfaces;
		this.fields = fields;
		this.methods = methods;
		this.constants = fields.stream().filter(JavaField::isConstant).toList();
		this.attributes = attributes;
		this.classinfo = new ClassInfo(this, version, pool, modifiers, thisType, superType, interfaces);
	}
	
	public JavaClass(InputStream in) {
		this(new ExtendedDataInputStream(in));
	}
	
	public JavaClass(ExtendedDataInputStream in) {
		if(in.readInt() != 0xCAFEBABE)
			throw new ClassFormatException("Illegal class header");
		
		this.version = new Version(in);
		var pool = this.pool = new ConstantPool(in);
		this.modifiers = in.readUnsignedShort();
		this.thisType = ClassType.valueOf(pool.get(in.readUnsignedShort()));
		this.superType = ClassType.valueOf(pool.get(in.readUnsignedShort()));
		
		int interfacesCount = in.readUnsignedShort();
		List<ClassType> interfaces = new ArrayList<>(interfacesCount);
		for(int i = 0; i < interfacesCount; i++)
			interfaces.add(pool.getClassConstant(in.readUnsignedShort()).toClassType());
		
		this.interfaces = Collections.unmodifiableList(interfaces);
		
		this.classinfo = new ClassInfo(this, version, pool, modifiers, thisType, superType, interfaces);
		
		this.fields = Collections.unmodifiableList(JavaField.readFields(in, classinfo, pool));
		this.methods = Collections.unmodifiableList(JavaMethod.readMethods(in, classinfo, pool));
		
		this.constants = fields.stream().filter(JavaField::isConstant).toList();
		
		this.attributes = new Attributes(in, pool);
		
		classinfo.setAttributes(attributes);
		
		methods.forEach(method -> method.decompile(classinfo, pool));
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		attributes.addImports(classinfo);
		fields.forEach(field -> field.addImports(classinfo));
		methods.forEach(method -> method.addImports(classinfo));
	}
	
	
	@Override
	public boolean canStringify(ClassInfo classinfo) {
		return super.canStringify(classinfo); // TODO
	}
	
	@Override
	public int getModifiers() {
		return modifiers;
	}
	
	public void writeTo(StringifyOutputStream out) {
		classinfo.setOutStream(out);
		this.writeTo(out, classinfo);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		
		if(JDecompiler.getInstance().printClassVersion())
			out.print("/* Java version: ").print(version.toString()).println(" */");
		
		if(!thisType.getPackageName().isEmpty())
			out.print("package ").print(thisType.getPackageName()).println(';').println();
		
		classinfo.uniqImports();
		classinfo.writeImports(out);
		
		writeAnnotations(out, classinfo, attributes);
		
		out.printIndent().print(modifiersToString(classinfo), classinfo).print(thisType, classinfo)
				.print(" {").increaseIndent();
		
		
		List<JavaField> stringableFields = fields.stream().filter(field -> field.canStringify(classinfo)).toList();
		List<JavaMethod> stringableMethods = methods.stream().filter(method -> method.canStringify(classinfo)).toList();
		
		
		if(!stringableFields.isEmpty())
			out.println().println();
		else if(!stringableMethods.isEmpty())
			out.println();
		
		writeFields(stringableFields, out, classinfo);
		
		stringableMethods.forEach(method -> out.println().write(method, classinfo));
		
		out.reduceIndent().print('}');
	}
	
	private IWhitespaceStringBuilder modifiersToString(ClassInfo classinfo) {
		WhitespaceStringBuilder str = new WhitespaceStringBuilder().printTrailingSpace();
		
//		boolean isInnerProtected = false;
//		
//		if(thisType.kind.isNested()) {
//			InnerClassesAttribute innerClasses = attributes.get("InnerClasses");
//			if(innerClasses != null) {
//				InnerClass innerClass = innerClasses.find(thisType);
//				if(innerClass != null) {
//					int innerClassModifiers = innerClass.modifiers;
//					
//					if((innerClassModifiers & ACC_PRIVATE) != 0) str.append("private");
//					if((innerClassModifiers & ACC_PROTECTED) != 0) {
//						str.append("protected");
//						isInnerProtected = true;
//					}
//					
//					if((innerClassModifiers & ACC_STATIC) != 0) str.append("static");
//					
//					if((innerClassModifiers & ~(ACC_ACCESS_FLAGS | ACC_STATIC | ACC_SUPER)) != (modifiers & ~(ACC_ACCESS_FLAGS | ACC_SUPER)))
//						warning("modifiers of class " + thisType.getName() + " are not matching to the modifiers in InnerClasses attribute:"
//								+ Util.hex4WithPrefix(innerClassModifiers) + " " + Util.hex4WithPrefix(modifiers));
//				}
//			}
//		}
		
		switch(modifiers & ACC_ACCESS_FLAGS) {
			case ACC_VISIBLE -> {}
			case ACC_PUBLIC -> {
//				if(!isInnerProtected)
					str.append("public");
			}
				
			default ->
				throw new IllegalModifiersException("in class " + thisType.getName() + ": " + Util.hex4WithPrefix(modifiers));
		}
		
		
		if((modifiers & ACC_STRICT) != 0)
			str.append("strictfp");
		
		switch(modifiers & (ACC_FINAL | ACC_ABSTRACT | ACC_INTERFACE | ACC_ANNOTATION | ACC_ENUM)) {
			case 0 ->
				str.append("class");
			case ACC_FINAL ->
				str.append("final class");
			case ACC_ABSTRACT ->
				str.append("abstract class");
			case ACC_ABSTRACT | ACC_INTERFACE ->
				str.append("interface");
			case ACC_ABSTRACT | ACC_INTERFACE | ACC_ANNOTATION ->
				str.append("@interface");
			case ACC_ENUM, ACC_FINAL | ACC_ENUM, ACC_ABSTRACT | ACC_ENUM ->
				str.append("enum");
			default ->
				throw new IllegalModifiersException("in class " + thisType.getName() + ": " + Util.hex4WithPrefix(modifiers));
		}
		
		return str;
	}
	
	
	private void writeFields(List<JavaField> fields, StringifyOutputStream out, ClassInfo classinfo) {
		
		int modifiers = 0;
		Type type = null; // NullPointerException никогда не возникнет для этой переменной
		Pair<AnnotationsAttribute, AnnotationsAttribute> annotationAttributes = null;
		boolean prevFieldWritten = false;
		
		for(JavaField field : fields) {
			
			if(prevFieldWritten) {
				if(field.modifiers == modifiers && field.descriptor.type.equals(type) && field.getAnnotationAttributes().equals(annotationAttributes)) {
					out.print(", ").print(field.descriptor.name);
				} else {
					out.writeln(';');
					
					if(field.hasAnnotation())
						out.writeln();
						
					field.writeWithoutSemicolon(out, classinfo);
					
					modifiers = field.modifiers;
					type = field.descriptor.type;
					annotationAttributes = field.getAnnotationAttributes();
				}
				
			} else {
				
				field.writeWithoutSemicolon(out, classinfo);
				
				modifiers = field.modifiers;
				type = field.descriptor.type;
				annotationAttributes = field.getAnnotationAttributes();
				
				prevFieldWritten = true;
			}
		}
		
		if(prevFieldWritten)
			out.writeln(';');
	}
}