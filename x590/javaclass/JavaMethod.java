package x590.javaclass;

import java.util.ArrayList;
import java.util.List;

import x590.javaclass.attribute.Attributes;
import x590.javaclass.attribute.CodeAttribute;
import x590.javaclass.attribute.EmptyCodeAttribute;
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
import x590.util.function.LazyLoadingBooleanValue;

import static x590.javaclass.Modifiers.*;

public class JavaMethod extends JavaClassMember {
	
	public final MethodDescriptor descriptor;
	public final Attributes attributes;
	public final CodeAttribute codeAttribute;
	public final ClassInfo classinfo;
	
	private DisassemblerContext disassemblerContext;
	private DecompilationContext decompilationContext;
	private StringifyContext stringifyContext;
	
	protected MethodScope scope;
	
	private boolean isAutogenerated(ClassInfo classinfo) {
		LazyLoadingBooleanValue hasNoOtherConstructors =
				new LazyLoadingBooleanValue(() -> !classinfo.getMethods().stream().anyMatch(method -> method != this && method.descriptor.isConstructor()));
		
		return (descriptor.isConstructorOf(classinfo.thisType) && descriptor.argumentsEmpty() && hasNoOtherConstructors.getAsBoolean() &&
					scope.isEmpty() && (modifiers & ACC_ACCESS_FLAGS) == (classinfo.modifiers & ACC_ACCESS_FLAGS)) // constructor by default
				
				|| (classinfo.thisType.isAnonymous() && descriptor.isConstructorOf(classinfo.thisType)) // anonymous class constructor
				
				|| ((classinfo.modifiers & ACC_ENUM) != 0 && (
					(scope.isEmpty() && (modifiers & ACC_PRIVATE) != 0 &&
						descriptor.isConstructorOf(classinfo.thisType) && descriptor.argumentsEquals(ClassType.STRING, PrimitiveType.INT) // enum constructor by default
						&& hasNoOtherConstructors.getAsBoolean()) ||
					descriptor == new MethodDescriptor(classinfo.thisType, "valueOf", classinfo.thisType, ClassType.STRING) || // Enum valueOf(String name)
					descriptor == new MethodDescriptor(classinfo.thisType, "values", new ArrayType(classinfo.thisType)) // Enum[] values()
				));
	}
	
	public JavaMethod(int modifiers, MethodDescriptor descriptor, Attributes attributes, ClassInfo classinfo, ConstantPool pool) {
		super(modifiers);
		this.descriptor = descriptor;
		this.attributes = attributes;
		this.codeAttribute = attributes.getOrDefault("Code", EmptyCodeAttribute.INSTANCE);
		this.classinfo = classinfo;
		
		System.out.println("Disassembling of method " + descriptor);
		this.disassemblerContext = DisassemblerContext.disassemble(pool, codeAttribute.code);
		
		this.scope = MethodScope.of(classinfo, descriptor, modifiers, disassemblerContext.getInstructions().size(), codeAttribute.maxLocals);
		
		this.stringifyContext = new StringifyContext(disassemblerContext, classinfo, descriptor, modifiers);
	}
	
	
	public JavaMethod(ExtendedDataInputStream in, ClassInfo classinfo, ConstantPool pool) {
		this(in.readUnsignedShort(), new MethodDescriptor(classinfo.thisType, in, pool),
				new Attributes(in, pool), classinfo, pool);
	}
	
	
	public void decompile(ClassInfo classinfo, ConstantPool pool) {
		System.out.println("Decompiling of method " + descriptor);
		decompilationContext = DecompilationContext.decompile(disassemblerContext, classinfo, descriptor, modifiers, scope, disassemblerContext.getInstructions(), codeAttribute.maxLocals);
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		attributes.addImports(classinfo);
		descriptor.addImports(classinfo);
		decompilationContext.operations.forEach(operation -> operation.addImports(classinfo));
	}
	
	
	@Override
	public boolean canStringify(ClassInfo classinfo) {
		return !((!JDecompiler.getInstance().showAutogenerated() && isAutogenerated(classinfo)) ||
				(descriptor.isStaticInitializer() && scope.isEmpty())) && // empty static {}
				
				((modifiers & ACC_SYNTHETIC_OR_BRIDGE) == 0 ||
				((modifiers & ACC_SYNTHETIC) != 0 && JDecompiler.getInstance().showSynthetic()) ||
				((modifiers & ACC_BRIDGE) != 0 && JDecompiler.getInstance().showBridge()));
	}
	
	
	public StringifyContext getStringifyContext() {
		return stringifyContext;
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		
		writeAnnotations(out, classinfo, attributes);
		
		out.printIndent().print(modifiersToString(classinfo), classinfo).print(descriptor, classinfo)
				.printIfNotNull(attributes.get("Exceptions"), classinfo)
				.printIfNotNull(attributes.get("AnnotationDefault"), classinfo);
		
		if(codeAttribute.isEmpty()) {
			out.write(';');
		} else {
			out.write(scope, stringifyContext);
		}
		
		out.println();
	}
	
	private IWhitespaceStringBuilder modifiersToString(ClassInfo classinfo) {
		
		if(descriptor.isStaticInitializer()) {
			if(modifiers == ACC_STATIC)
				return WhitespaceStringBuilder.empty();
			
			throw new IllegalModifiersException("Static initializer must have only static modifier");
		}
		
		WhitespaceStringBuilder str = new WhitespaceStringBuilder().printTrailingSpace();
		
		int modifiers = this.modifiers,
			classModifiers = classinfo.modifiers;
		
		switch(modifiers & ACC_ACCESS_FLAGS) {
			case ACC_VISIBLE -> {}
			
			case ACC_PUBLIC -> { // Все нестатические методы интерфейса по умолчанию имеют модификатор public, поэтому в этом случае нам не нужно выводить public
				if(!((classModifiers & ACC_INTERFACE) != 0 && (classModifiers & ACC_PUBLIC) == 0 && (modifiers & ACC_STATIC) == 0))
					str.append("public");
			}
			
			case ACC_PRIVATE -> { // Конструкторы Enum по умолчанию имеют модификатор private, поэтому нам не нужно выводить private
				if(!((classModifiers & ACC_ENUM) != 0 && descriptor.isConstructor() && descriptor.clazz.equals(classinfo.thisType)))
					str.append("private");
			}
			
			case ACC_PROTECTED -> {
				str.append("protected");
			}
			
			default ->
				throw new IllegalModifiersException(modifiers);
		}
		
		if((modifiers & ACC_STATIC) != 0)
			str.append("static");
		
		if((modifiers & ACC_ABSTRACT) != 0) {
			
			if((modifiers & (ACC_STATIC | ACC_FINAL | ACC_SYNCHRONIZED | ACC_NATIVE | ACC_STRICT)) != 0)
				throw new IllegalModifiersException(modifiers);
			
			if((classModifiers & ACC_INTERFACE) == 0)
				str.append("abstract");
			
		} else {
			if((classModifiers & ACC_INTERFACE) != 0 && (modifiers & ACC_STATIC) == 0 && (modifiers & ACC_PRIVATE) == 0)
				str.append("default");
		}
		
		if((modifiers & ACC_FINAL) != 0) str.append("final");
		if((modifiers & ACC_SYNCHRONIZED) != 0) str.append("synchronized");
		if((modifiers & ACC_NATIVE) != 0 && (modifiers & ACC_STRICT) != 0) throw new IllegalModifiersException(modifiers);
		if((modifiers & ACC_NATIVE) != 0) str.append("native");
		if((modifiers & ACC_STRICT) != 0) str.append("strictfp");
		
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