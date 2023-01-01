package x590.javaclass;

import java.util.ArrayList;
import java.util.List;

import x590.javaclass.attribute.Attributes;
import x590.javaclass.attribute.CodeAttribute;
import x590.javaclass.attribute.EmptyCodeAttribute;
import x590.javaclass.attribute.Attributes.Location;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.exception.IllegalModifiersException;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.scope.MethodScope;
import x590.javaclass.type.ArrayType;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.util.IWhitespaceStringBuilder;
import x590.javaclass.util.WhitespaceStringBuilder;
import x590.jdecompiler.JDecompiler;
import x590.util.lazyloading.LazyLoadingBooleanValue;

import static x590.javaclass.Modifiers.*;

public class JavaMethod extends JavaClassMember {
	
	public final MethodDescriptor descriptor;
	public final Attributes attributes;
	public final CodeAttribute codeAttribute;
	public final ClassInfo classinfo;
	
	private DisassemblerContext disassemblerContext;
	private DecompilationContext decompilationContext;
	private StringifyContext stringifyContext;
	
	protected MethodScope methodScope;
	
	private boolean isAutogenerated(ClassInfo classinfo) {
		var hasNoOtherConstructors =
				new LazyLoadingBooleanValue(() -> !classinfo.getMethods().stream().anyMatch(method -> method != this && method.descriptor.isConstructor()));
		
		var descriptor = this.descriptor;
		var thisClassType = classinfo.thisType;
		
		if(descriptor.isConstructorOf(thisClassType) && descriptor.argumentsEquals() && modifiers.and(ACC_ACCESS_FLAGS) == classinfo.modifiers.and(ACC_ACCESS_FLAGS) &&
				methodScope.isEmpty() && hasNoOtherConstructors.getAsBoolean()) { // constructor by default
			return true;
		}
		
		if(descriptor.isStaticInitializer() && methodScope.isEmpty()) { // empty static {}
			return true;
		}
		
		if(thisClassType.isAnonymous() && descriptor.isConstructorOf(thisClassType)) { // anonymous class constructor
			return true;
		}
		
		if(classinfo.modifiers.isEnum()) {
			
			// enum constructor by default
			if(descriptor.isConstructorOf(thisClassType) && descriptor.argumentsEquals(ClassType.STRING, PrimitiveType.INT)
				&& modifiers.isPrivate() && methodScope.isEmpty() && hasNoOtherConstructors.getAsBoolean()) {
				
				return true;
			}
				
			if( descriptor.equals("valueOf", thisClassType, ClassType.STRING) || // Enum valueOf(String name)
				descriptor.equals("values", new ArrayType(thisClassType))) { // Enum[] values()
				return true;
			}
		}
		
		return false;
	}
	
	public JavaMethod(Modifiers modifiers, MethodDescriptor descriptor, Attributes attributes, ClassInfo classinfo, ConstantPool pool) {
		super(modifiers);
		this.descriptor = descriptor;
		this.attributes = attributes;
		this.codeAttribute = attributes.getOrDefault("Code", EmptyCodeAttribute.INSTANCE);
		this.classinfo = classinfo;
		
		System.out.println("Disassembling of method " + descriptor);
		this.disassemblerContext = DisassemblerContext.disassemble(pool, codeAttribute.code);
		
		this.methodScope = MethodScope.of(classinfo, descriptor, modifiers, codeAttribute,
				disassemblerContext.getInstructions().size(), codeAttribute.isEmpty() ? descriptor.countLocals(modifiers) : codeAttribute.maxLocals);
		
		this.stringifyContext = new StringifyContext(disassemblerContext, classinfo, descriptor, methodScope, modifiers);
	}
	
	
	public JavaMethod(ExtendedDataInputStream in, ClassInfo classinfo, ConstantPool pool) {
		this(new Modifiers(in.readUnsignedShort()), new MethodDescriptor(classinfo.thisType, in, pool),
				new Attributes(in, pool, Location.METHOD), classinfo, pool);
	}
	
	
	public StringifyContext getStringifyContext() {
		return stringifyContext;
	}
	
	
	public void decompile(ClassInfo classinfo, ConstantPool pool) {
		System.out.println("Decompiling of method " + descriptor);
		decompilationContext = DecompilationContext.decompile(disassemblerContext, classinfo, descriptor, modifiers, methodScope, disassemblerContext.getInstructions(), codeAttribute.maxLocals);
		methodScope.reduceTypes();
		methodScope.defineVariables();
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		attributes.addImports(classinfo);
		descriptor.addImports(classinfo);
		decompilationContext.operations.forEach(operation -> operation.addImports(classinfo));
	}
	
	
	@Override
	public boolean canStringify(ClassInfo classinfo) {
		return (!modifiers.isSyntheticOrBridge() ||
				modifiers.isSynthetic() && JDecompiler.getInstance().showSynthetic() ||
				modifiers.isBridge() && JDecompiler.getInstance().showBridge()) &&
				(JDecompiler.getInstance().showAutogenerated() || !isAutogenerated(classinfo));
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		
		methodScope.assignVariablesNames();
		
		writeAnnotations(out, classinfo, attributes);
		
		out.printIndent().print(modifiersToString(classinfo), classinfo);
		descriptor.write(out, stringifyContext, attributes);
		out.printIfNotNull(attributes.get("Exceptions"), classinfo)
			.printIfNotNull(attributes.get("AnnotationDefault"), classinfo);
		
		if(codeAttribute.isEmpty()) {
			out.write(';');
		} else {
			out.write(methodScope, stringifyContext);
		}
		
		out.writeln();
	}
	
	private IWhitespaceStringBuilder modifiersToString(ClassInfo classinfo) {
		
		if(descriptor.isStaticInitializer()) {
			if(modifiers.value == ACC_STATIC)
				return WhitespaceStringBuilder.empty();
			
			throw new IllegalModifiersException("Static initializer must have only static modifier");
		}
		
		IWhitespaceStringBuilder str = new WhitespaceStringBuilder().printTrailingSpace();
		
		var modifiers = this.modifiers;
		var classModifiers = classinfo.modifiers;
		
		switch(modifiers.and(ACC_ACCESS_FLAGS)) {
			case ACC_VISIBLE -> {}
			
			case ACC_PUBLIC -> { // Все нестатические методы интерфейса по умолчанию имеют модификатор public, поэтому в этом случае нам не нужно выводить public
				if(classModifiers.isNotInterface() || classModifiers.isPublic() || modifiers.isStatic())
					str.append("public");
			}
			
			case ACC_PRIVATE -> { // Конструкторы Enum по умолчанию имеют модификатор private, поэтому нам не нужно выводить private
				if(!(classModifiers.isEnum() && descriptor.isConstructor() && descriptor.clazz.equals(classinfo.thisType)))
					str.append("private");
			}
			
			case ACC_PROTECTED -> {
				str.append("protected");
			}
			
			default -> {
				throw new IllegalModifiersException(modifiers);
			}
		}
		
		if(modifiers.isStatic())
			str.append("static");
		
		if(modifiers.isAbstract()) {
			
			if(modifiers.is(ACC_STATIC | ACC_FINAL | ACC_SYNCHRONIZED | ACC_NATIVE | ACC_STRICT))
				throw new IllegalModifiersException(modifiers);
			
			if(classModifiers.isInterface())
				str.append("abstract");
			
		} else {
			if(classModifiers.isInterface() && modifiers.isNotStatic() && modifiers.isNotPrivate())
				str.append("default");
		}
		
		if(modifiers.isFinal()) str.append("final");
		if(modifiers.isSynchronized()) str.append("synchronized");
		
		if(modifiers.isNative() && modifiers.isStrictfp()) throw new IllegalModifiersException(modifiers);
		if(modifiers.isNative()) str.append("native");
		else if(modifiers.isStrictfp()) str.append("strictfp");
		
		return str;
	}
	
	protected static List<JavaMethod> readMethods(ExtendedDataInputStream in, ClassInfo classinfo, ConstantPool pool) {
		int length = in.readUnsignedShort();
		List<JavaMethod> methods = new ArrayList<>(length);
		
		for(int i = 0; i < length; i++) {
			methods.add(new JavaMethod(in, classinfo, pool));
		}
		
		return methods;
	}
}