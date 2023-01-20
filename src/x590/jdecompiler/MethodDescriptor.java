package x590.jdecompiler;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;

import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.attribute.annotation.ParameterAnnotationsAttribute;
import x590.jdecompiler.attribute.signature.MethodSignatureAttribute;
import x590.jdecompiler.constpool.ClassConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.constpool.NameAndTypeConstant;
import x590.jdecompiler.constpool.ReferenceConstant;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.IllegalMethodHeaderException;
import x590.jdecompiler.exception.InvalidMethodDescriptorException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedStringReader;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.modifiers.Modifiers;
import x590.jdecompiler.type.ArrayType;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Type;
import x590.util.IntHolder;
import x590.util.Util;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public final class MethodDescriptor extends Descriptor implements Importable {
	
	public static final int IMPLICIT_ENUM_ARGUMENTS = 2;
	
	final @Immutable
	private List<Type> arguments;
	private final Type returnType;
	
	private enum MethodKind {
		CONSTRUCTOR, STATIC_INITIALIZER, PLAIN
	}
	
	private final MethodKind kind;
	
	private MethodKind typeForName(String name) {
		MethodKind kind = switch(name) {
			case "<init>"   -> MethodKind.CONSTRUCTOR;
			case "<clinit>" -> MethodKind.STATIC_INITIALIZER;
			default         -> MethodKind.PLAIN;
		};
		
		if(kind != MethodKind.PLAIN) {
			
			if(returnType != PrimitiveType.VOID)
				throw new InvalidMethodDescriptorException("Method " + this.toString() + " must return void");
			
			if(!getDeclaringClass().isBasicClassType())
				throw new InvalidMethodDescriptorException("Class " + getDeclaringClass() + " cannot have " + this.toString() + " method");
		}
		
		return kind;
	}
	
	
	public @Immutable List<Type> getArguments() {
		return arguments;
	}
	
	public int getArgumentsCount() {
		return arguments.size();
	}
	
	public Type getReturnType() {
		return returnType;
	}
	
	
	public boolean isConstructor() {
		return kind == MethodKind.CONSTRUCTOR;
	}
	
	public boolean isStaticInitializer() {
		return kind == MethodKind.STATIC_INITIALIZER;
	}
	
	public boolean isConstructorOf(ReferenceType clazz) {
		return this.isConstructor() && getDeclaringClass().equals(clazz);
	}
	
	public boolean isStaticInitializerOf(ReferenceType clazz) {
		return this.isStaticInitializer() && getDeclaringClass().equals(clazz);
	}
	
	
	public MethodDescriptor(ReferenceConstant referenceConstant) {
		this(referenceConstant.getClassConstant().toReferenceType(), referenceConstant.getNameAndType());
	}
	
	public MethodDescriptor(String className, NameAndTypeConstant nameAndType) {
		this(className, nameAndType.getNameConstant().getString(), nameAndType.getDescriptor().getString());
	}
	
	public MethodDescriptor(String className, String name, String descriptor) {
		this(ClassType.fromDescriptor(className), name, descriptor);
	}
	
	public MethodDescriptor(ClassConstant clazz, NameAndTypeConstant nameAndType) {
		this(clazz.toClassType(), nameAndType);
	}
	
	public MethodDescriptor(ReferenceType clazz, NameAndTypeConstant nameAndType) {
		this(clazz, nameAndType.getNameConstant().getString(), nameAndType.getDescriptor().getString());
	}
	
	public MethodDescriptor(ReferenceType clazz, String name, String descriptor) {
		this(clazz, name, new ExtendedStringReader(descriptor));
	}
	
	public MethodDescriptor(ReferenceType clazz, String name, ExtendedStringReader descriptor) {
		this(clazz, name, Type.parseMethodArguments(descriptor), Type.parseReturnType(descriptor));
	}
	
	
	public MethodDescriptor(ReferenceType clazz, String name, Type returnType, Type... arguments) {
		this(clazz, name, List.of(arguments), returnType);
	}
	
	public MethodDescriptor(ReferenceType clazz, String name, Type returnType) {
		this(clazz, name, Collections.emptyList(), returnType);
	}
	
	public MethodDescriptor(ReferenceType clazz, String name, @Immutable List<Type> arguments, Type returnType) {
		super(clazz, name);
		this.arguments = arguments;
		this.returnType = returnType;
		this.kind = typeForName(name);
	}
	
	
	public MethodDescriptor(ReferenceType clazz, ExtendedDataInputStream in, ConstantPool pool) {
		this(clazz, pool.getUtf8String(in.readUnsignedShort()), pool.getUtf8String(in.readUnsignedShort()));
	}
	
	
	int getVisibleStartIndex(ClassInfo classinfo) {
		return classinfo.getModifiers().isEnum() && this.isConstructorOf(classinfo.getThisType()) ? IMPLICIT_ENUM_ARGUMENTS : 0;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(returnType);
		arguments.forEach(arg -> classinfo.addImport(arg));
	}
	
	
	public int countLocals(Modifiers modifiers) {
		return arguments.stream().mapToInt(argType -> argType.getSize().slotsOccupied()).sum() + (modifiers.isNotStatic() ? 1 : 0);
	}
	
	
	public void write(StringifyOutputStream out, StringifyContext context, Attributes attributes, @Nullable MethodSignatureAttribute signature) {
		
		if(signature != null && signature.parameters != null) {
			out.writesp(signature.parameters, context.classinfo);
		}
		
		switch(kind) {
			case CONSTRUCTOR -> {
				out.print(getDeclaringClass(), context.classinfo);
				this.writeArguments(out, context, attributes, signature);
			}
				
			case STATIC_INITIALIZER -> {
				out.print("static");
			}
				
			case PLAIN -> {
				out.print(signature != null ? signature.returnType : returnType, context.classinfo).printsp().print(getName());
				this.writeArguments(out, context, attributes, signature);
			}
		}
	}
	
	
	private void writeArguments(StringifyOutputStream out, StringifyContext context, Attributes attributes, @Nullable MethodSignatureAttribute signature) {
		
		var classinfo = context.classinfo;
		
		int startIndex = getVisibleStartIndex(context.classinfo);
		
		IntHolder varIndex = new IntHolder((context.modifiers.isNotStatic() ? 1 : 0) + startIndex);
		
		ParameterAnnotationsAttribute
				visibleParameterAnnotations = attributes.getOrDefault("RuntimeVisibleParameterAnnotations", ParameterAnnotationsAttribute.emptyVisible()),
				invisibleParameterAnnotations = attributes.getOrDefault("RuntimeInvisibleParameterAnnotations", ParameterAnnotationsAttribute.emptyInvisible());
		
		IntConsumer writeParameterAnnotations = i -> {
			visibleParameterAnnotations.write(out, classinfo, i);
			invisibleParameterAnnotations.write(out, classinfo, i);
		};
		
		
		Function<Type, String> getVariableName = type -> context.methodScope.getDefinedVariable(varIndex.postInc(type.getSize().slotsOccupied())).getName();
		
		ObjIntConsumer<Type> write;
		
		if(context.modifiers.isVarargs()) {
			int varargsIndex = arguments.size() - 1;
			
			if(arguments.isEmpty() || !arguments.get(varargsIndex).isBasicArrayType())
				throw new IllegalMethodHeaderException("Varargs method must have array as last argument");
			
			write = (type, i) ->
					(i != varargsIndex ? out.printsp(type, classinfo) : out.print(((ArrayType)type).getElementType(), classinfo).print("... "))
					.print(getVariableName.apply(type));
			
		} else {
			write = (type, i) ->
					out.printsp(type, classinfo).print(getVariableName.apply(type));
		}
		
		out.write('(');
		
		Util.forEachExcludingLast(signature != null ? signature.arguments : arguments,
				(type, i) -> {
					writeParameterAnnotations.accept(i);
					write.accept(type, i);
				},
				type -> out.write(", "),
				signature == null ? startIndex : 0, startIndex);
		
		out.write(')');
	}
	
	@Override
	public String toString() {
		return getDeclaringClass().getName() + "." +
				(kind == MethodKind.STATIC_INITIALIZER ? "static {}" :
				(kind == MethodKind.CONSTRUCTOR ? ((ClassType)getDeclaringClass()).getSimpleName() : getName())
						+ "(" + arguments.stream().map(Type::getName).collect(Collectors.joining(", ")) + ")");
	}
	
	
	public boolean equalsIgnoreClass(MethodDescriptor other) {
		return this == other || getName().equals(other.getName()) &&
				returnType.equals(other.returnType) && arguments.equals(other.arguments);
	}
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof MethodDescriptor descriptor && this.equals(descriptor);
	}
	
	public boolean equals(MethodDescriptor other) {
		return this == other || getName().equals(other.getName()) && getDeclaringClass().equals(other.getDeclaringClass()) &&
				returnType.equals(other.returnType) && arguments.equals(other.arguments);
	}
	
	
	public boolean equals(String name, Type returnType) {
		return this.equalsRaw(name, returnType) && argumentsEquals();
	}
	
	public boolean equals(String name, Type returnType, Type arg1) {
		return this.equalsRaw(name, returnType) && argumentsEquals(arg1);
	}
	
	public boolean equals(String name, Type returnType, Type arg1, Type arg2) {
		return this.equalsRaw(name, returnType) && argumentsEquals(arg1, arg2);
	}
	
	public boolean equals(String name, Type returnType, Type arg1, Type arg2, Type arg3) {
		return this.equalsRaw(name, returnType) && argumentsEquals(arg1, arg2, arg3);
	}
	
	public boolean equals(String name, Type returnType, Type... args) {
		return this.equalsRaw(name, returnType) && argumentsEquals(args);
	}
	
	private boolean equalsRaw(String name, Type returnType) {
		return this.getName().equals(name) && this.returnType.equals(returnType);
	}
	
	
	public boolean equals(ClassType clazz, String name, Type returnType) {
		return this.equalsRaw(clazz, name, returnType) && argumentsEquals();
	}
	
	public boolean equals(ClassType clazz, String name, Type returnType, Type arg1) {
		return this.equalsRaw(clazz, name, returnType) && argumentsEquals(arg1);
	}
	
	public boolean equals(ClassType clazz, String name, Type returnType, Type arg1, Type arg2) {
		return this.equalsRaw(clazz, name, returnType) && argumentsEquals(arg1, arg2);
	}
	
	public boolean equals(ClassType clazz, String name, Type returnType, Type arg1, Type arg2, Type arg3) {
		return this.equalsRaw(clazz, name, returnType) && argumentsEquals(arg1, arg2, arg3);
	}
	
	public boolean equals(ClassType clazz, String name, Type returnType, Type... args) {
		return this.equalsRaw(clazz, name, returnType) && argumentsEquals(args);
	}
	
	private boolean equalsRaw(ClassType clazz, String name, Type returnType) {
		return this.getDeclaringClass().equals(clazz) && this.getName().equals(name) && this.returnType.equals(returnType);
	}
	
	
	public boolean argumentsEquals() {
		return arguments.isEmpty();
	}
	
	public boolean argumentsEquals(Type arg1) {
		return arguments.size() == 1 &&
				arguments.get(0).equals(arg1);
	}
	
	public boolean argumentsEquals(Type arg1, Type arg2) {
		return arguments.size() == 2 &&
				arguments.get(0).equals(arg1) &&
				arguments.get(1).equals(arg2);
	}
	
	public boolean argumentsEquals(Type arg1, Type arg2, Type arg3) {
		return arguments.size() == 3 &&
				arguments.get(0).equals(arg1) &&
				arguments.get(1).equals(arg2) &&
				arguments.get(1).equals(arg3);
	}
	
	public boolean argumentsEquals(Type... types) {
		if(arguments.size() != types.length)
			return false;
		
		Iterator<Type> iter = Arrays.stream(types).iterator();
		return arguments.stream().allMatch(arg -> arg.equals(iter.next()));
	}
}
