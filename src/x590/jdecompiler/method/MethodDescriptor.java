package x590.jdecompiler.method;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;

import x590.jdecompiler.Descriptor;
import x590.jdecompiler.Importable;
import x590.jdecompiler.attribute.AttributeType;
import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.attribute.annotation.ParameterAnnotationsAttribute;
import x590.jdecompiler.attribute.signature.MethodSignatureAttribute;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ClassConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.constpool.NameAndTypeConstant;
import x590.jdecompiler.constpool.ReferenceConstant;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.IllegalMethodHeaderException;
import x590.jdecompiler.exception.InvalidMethodDescriptorException;
import x590.jdecompiler.io.DisassemblingOutputStream;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.modifiers.ClassEntryModifiers;
import x590.jdecompiler.type.ArrayType;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Type;
import x590.util.IntHolder;
import x590.util.LoopUtil;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public final class MethodDescriptor extends Descriptor implements Importable {
	
	public static final int
			IMPLICIT_ENUM_ARGUMENTS = 2,
			IMPLICIT_NONSTATIC_NESTED_CLASS_ARGUMENTS = 1;
	
	private static final Map<ReferenceType, Map<String, Map<Type, Map<List<Type>, MethodDescriptor>>>> INSTANCES = new HashMap<>();
	
	private final @Immutable List<Type> arguments;
	private final Type returnType;
	
	private enum MethodKind {
		CONSTRUCTOR, STATIC_INITIALIZER, PLAIN
	}
	
	private final MethodKind kind;
	
	private MethodKind kindForName(String name) {
		MethodKind kind = switch(name) {
			case "<init>"   -> MethodKind.CONSTRUCTOR;
			case "<clinit>" -> MethodKind.STATIC_INITIALIZER;
			default         -> MethodKind.PLAIN;
		};
		
		if(kind != MethodKind.PLAIN) {
			
			if(returnType != PrimitiveType.VOID) {
				throw new InvalidMethodDescriptorException("Method " + this.toString() + " must return void");
			}
			
			if(!getDeclaringClass().isClassType()) {
				throw new InvalidMethodDescriptorException("Class " + getDeclaringClass() +
						" cannot have " + this.toString() + " method");
			}
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
	
	
	public boolean isPlain() {
		return kind == MethodKind.PLAIN;
	}
	
	public boolean isConstructor() {
		return kind == MethodKind.CONSTRUCTOR;
	}
	
	public boolean isStaticInitializer() {
		return kind == MethodKind.STATIC_INITIALIZER;
	}
	
	public boolean isConstructorOf(ReferenceType declaringClass) {
		return this.isConstructor() && getDeclaringClass().equals(declaringClass);
	}
	
	public boolean isStaticInitializerOf(ReferenceType declaringClass) {
		return this.isStaticInitializer() && getDeclaringClass().equals(declaringClass);
	}
	
	
	public static MethodDescriptor from(ReferenceConstant referenceConstant) {
		return from(referenceConstant.getClassConstant().toReferenceType(), referenceConstant.getNameAndType());
	}
	
	public static MethodDescriptor from(String className, NameAndTypeConstant nameAndType) {
		return from(className, nameAndType.getNameConstant().getString(), nameAndType.getDescriptor().getString());
	}
	
	public static MethodDescriptor from(String className, String name, String descriptor) {
		return from(ClassType.fromDescriptor(className), name, descriptor);
	}
	
	
	public static MethodDescriptor from(ClassConstant declaringClass, NameAndTypeConstant nameAndType) {
		return from(declaringClass.toClassType(), nameAndType);
	}
	
	public static MethodDescriptor from(ReferenceType declaringClass, NameAndTypeConstant nameAndType) {
		return from(declaringClass, nameAndType.getNameConstant().getString(), nameAndType.getDescriptor().getString());
	}
	
	public static MethodDescriptor from(ReferenceType declaringClass, ExtendedDataInputStream in, ConstantPool pool) {
		return from(declaringClass, pool.getUtf8String(in.readUnsignedShort()), pool.getUtf8String(in.readUnsignedShort()));
	}
	
	public static MethodDescriptor from(ReferenceType declaringClass, String name, String descriptor) {
		return from(declaringClass, name, new ExtendedStringInputStream(descriptor));
	}
	
	public static MethodDescriptor from(ReferenceType declaringClass, String name, ExtendedStringInputStream descriptor) {
		// При парсинге дескриптора сначала идёт список аргументов, а потом возвращаемый тип
		List<Type> arguments = Type.parseMethodArguments(descriptor);
		return of(Type.parseReturnType(descriptor), declaringClass, name, arguments);
	}

	
	public static MethodDescriptor of(Type returnType, ReferenceType declaringClass, String name) {
		return of(returnType, declaringClass, name, Collections.emptyList());
	}
	
	public static MethodDescriptor of(Type returnType, ReferenceType declaringClass, String name, Type... arguments) {
		return of(returnType, declaringClass, name, List.of(arguments));
	}
	
	public static MethodDescriptor of(Type returnType, ReferenceType declaringClass, String name, @Immutable List<Type> arguments) {
		return INSTANCES
				.computeIfAbsent(declaringClass, key -> new HashMap<>())
				.computeIfAbsent(name, key -> new HashMap<>())
				.computeIfAbsent(returnType, key -> new HashMap<>())
				.computeIfAbsent(arguments, key -> new MethodDescriptor(returnType, declaringClass, name, arguments));
	}
	
	public static MethodDescriptor constructor(ReferenceType declaringClass, @Immutable List<Type> arguments) {
		return INSTANCES
				.computeIfAbsent(declaringClass, key -> new HashMap<>())
				.computeIfAbsent("<init>", key -> new HashMap<>())
				.computeIfAbsent(PrimitiveType.VOID, key -> new HashMap<>())
				.computeIfAbsent(arguments, key -> new MethodDescriptor(PrimitiveType.VOID, declaringClass, "<init>", MethodKind.CONSTRUCTOR, arguments));
	}
	
	private MethodDescriptor(Type returnType, ReferenceType declaringClass, String name, @Immutable List<Type> arguments) {
		super(declaringClass, name);
		this.arguments = arguments;
		this.returnType = returnType;
		this.kind = kindForName(name);
	}
	
	private MethodDescriptor(Type returnType, ReferenceType declaringClass, String name, MethodKind kind, @Immutable List<Type> arguments) {
		super(declaringClass, name);
		this.arguments = arguments;
		this.returnType = returnType;
		this.kind = kind;
	}
	
	
	public static MethodDescriptor fromReflectMethod(ReferenceType thisType, Method method) {
		return of(
				Type.fromClass(method.getReturnType()),
				thisType, method.getName(),
				Arrays.stream(method.getParameterTypes()).map(Type::fromClass).toList()
		);
	}
	
	public static MethodDescriptor fromReflectConstructor(ReferenceType thisType, Constructor<?> constructor) {
		return constructor(thisType, Arrays.stream(constructor.getParameterTypes()).map(Type::fromClass).toList());
	}
	
	
	int getVisibleStartIndex(ClassInfo classinfo) {
		return isEnumConstructor(classinfo) ? IMPLICIT_ENUM_ARGUMENTS :
				isImplicitNonstaticNestedClassConstructor(classinfo) ? IMPLICIT_NONSTATIC_NESTED_CLASS_ARGUMENTS : 0;
	}
	
	private boolean isEnumConstructor(ClassInfo classinfo) {
		return classinfo.getModifiers().isEnum() && this.isConstructorOf(classinfo.getThisType());
	}
	
	private boolean isImplicitNonstaticNestedClassConstructor(ClassInfo classinfo) {
		return classinfo.getThisType().isNested() && this.isConstructorOf(classinfo.getThisType()) && classinfo.getModifiers().isNotStatic();
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(returnType);
		classinfo.addImportsFor(arguments);
	}
	
	
	public int countLocals(ClassEntryModifiers modifiers) {
		return arguments.stream().mapToInt(argType -> argType.getSize().slotsOccupied()).sum()
				+ (modifiers.isNotStatic() ? 1 : 0);
	}
	
	
	public void write(StringifyOutputStream out, StringifyContext context, Attributes attributes, @Nullable MethodSignatureAttribute signature) {
		
		if(signature != null && signature.parameters != null) {
			out.printsp(signature.parameters, context.getClassinfo());
		}
		
		switch(kind) {
			case CONSTRUCTOR -> {
				out.print(getDeclaringClass(), context.getClassinfo());
				writeArguments(out, context, attributes, signature);
			}
			
			case STATIC_INITIALIZER -> {
				out.write("static");
			}
			
			case PLAIN -> {
				out.print(signature != null ? signature.returnType : returnType, context.getClassinfo()).printsp().print(getName());
				writeArguments(out, context, attributes, signature);
			}
		}
	}
	
	public void writeAsLambda(StringifyOutputStream out, StringifyContext context, Attributes attributes, @Nullable MethodSignatureAttribute signature, int captured) {
		writeArguments(out, context, attributes, signature, captured, true);
	}
	
	
	private void writeArguments(StringifyOutputStream out, StringifyContext context, Attributes attributes, @Nullable MethodSignatureAttribute signature) {
		writeArguments(out, context, attributes, signature, getVisibleStartIndex(context.getClassinfo()), false);
	}
	
	private void writeArguments(StringifyOutputStream out, StringifyContext context, Attributes attributes, @Nullable MethodSignatureAttribute signature, int startIndex, boolean asLambda) {
		
		var classinfo = context.getClassinfo();
		
		IntHolder varIndex = new IntHolder((context.getMethodModifiers().isNotStatic() ? 1 : 0) + startIndex);
		
		ParameterAnnotationsAttribute
				visibleParameterAnnotations = attributes.getOrDefaultEmpty(AttributeType.RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS),
				invisibleParameterAnnotations = attributes.getOrDefaultEmpty(AttributeType.RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS);
		
		boolean canOmitTypes = asLambda && visibleParameterAnnotations.isEmpty() && invisibleParameterAnnotations.isEmpty();
		
		IntConsumer writeParameterAnnotations = canOmitTypes ?
				slot -> {} :
				slot -> {
					visibleParameterAnnotations.write(out, classinfo, slot);
					invisibleParameterAnnotations.write(out, classinfo, slot);
				};
		
		
		Function<Type, String> getVariableName = type -> context.getMethodScope().getDefinedVariable(varIndex.postInc(type.getSize().slotsOccupied())).getName();
		
		ObjIntConsumer<Type> write;
		
		if(context.getMethodModifiers().isVarargs()) {
			int varargsIndex = arguments.size() - 1;
			
			if(arguments.isEmpty() || !arguments.get(varargsIndex).isArrayType())
				throw new IllegalMethodHeaderException("Varargs method must have array as last argument");
			
			write = canOmitTypes ?
					(type, slot) -> out.write(getVariableName.apply(type)) :
					(type, slot) ->
						(slot != varargsIndex ? out.printsp(type, classinfo) : out.print(((ArrayType)type).getElementType(), classinfo).printsp("..."))
							.write(getVariableName.apply(type));
			
		} else {
			write = canOmitTypes ?
					(type, slot) -> out.write(getVariableName.apply(type)) :
					(type, slot) -> out.printsp(type, classinfo).write(getVariableName.apply(type));
		}
		
		
		boolean canOmitBrackets = asLambda && canOmitTypes && arguments.size() - startIndex == 1;
		
		if(!canOmitBrackets)
			out.write('(');
		
		LoopUtil.forEachExcludingLast(signature != null ? signature.arguments : arguments,
				(type, slot) -> {
					writeParameterAnnotations.accept(slot);
					write.accept(type, slot);
				},
				type -> out.write(", "),
				signature == null ? startIndex : 0, startIndex);
		
		if(!canOmitBrackets)
			out.write(')');
	}
	
	@Override
	public String toString() {
		return returnType.getName() + " " +
				getDeclaringClass().getName() + "." +
				(kind == MethodKind.STATIC_INITIALIZER ?
						"static {}" :
						(kind == MethodKind.CONSTRUCTOR ? ((ClassType)getDeclaringClass()).getSimpleName() : getName())
								+ arguments.stream().map(Type::getName).collect(Collectors.joining(", ", "(", ")")));
	}
	
	
	public boolean equalsIgnoreClass(MethodDescriptor other) {
		return this == other ||
				getName().equals(other.getName()) &&
				returnType.equals(other.returnType) &&
				arguments.equals(other.arguments);
	}
	
	public boolean equalsIgnoreClassAndReturnType(MethodDescriptor other) {
		return this == other ||
				getName().equals(other.getName()) &&
				arguments.equals(other.arguments);
	}
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof MethodDescriptor descriptor && this.equals(descriptor);
	}
	
	public boolean equals(MethodDescriptor other) {
		return this == other ||
				getName().equals(other.getName()) &&
				getDeclaringClass().equals(other.getDeclaringClass()) &&
				returnType.equals(other.returnType) && arguments.equals(other.arguments);
	}
	
	
	public boolean equalsIgnoreClass(Type returnType, String name) {
		return this.equalsRawIgnoreClass(returnType, name) && argumentsEquals();
	}
	
	public boolean equalsIgnoreClass(Type returnType, String name, Type arg1) {
		return this.equalsRawIgnoreClass(returnType, name) && argumentsEquals(arg1);
	}
	
	public boolean equalsIgnoreClass(Type returnType, String name, Type arg1, Type arg2) {
		return this.equalsRawIgnoreClass(returnType, name) && argumentsEquals(arg1, arg2);
	}
	
	public boolean equalsIgnoreClass(Type returnType, String name, Type arg1, Type arg2, Type arg3) {
		return this.equalsRawIgnoreClass(returnType, name) && argumentsEquals(arg1, arg2, arg3);
	}
	
	public boolean equalsIgnoreClass(Type returnType, String name, Type... args) {
		return this.equalsRawIgnoreClass(returnType, name) && argumentsEquals(args);
	}
	
	public boolean equalsIgnoreClass(Type returnType, String name, int argumentsCount) {
		return this.equalsRawIgnoreClass(returnType, name) && arguments.size() == argumentsCount;
	}
	
	private boolean equalsRawIgnoreClass(Type returnType, String name) {
		return this.returnType.equals(returnType) && getName().equals(name);
	}
	
	
	public boolean equals(Type returnType, ClassType declaringClass, String name) {
		return this.equalsRaw(returnType, declaringClass, name) && argumentsEquals();
	}
	
	public boolean equals(Type returnType, ClassType declaringClass, String name, Type arg1) {
		return this.equalsRaw(returnType, declaringClass, name) && argumentsEquals(arg1);
	}
	
	public boolean equals(Type returnType, ClassType declaringClass, String name, Type arg1, Type arg2) {
		return this.equalsRaw(returnType, declaringClass, name) && argumentsEquals(arg1, arg2);
	}
	
	public boolean equals(Type returnType, ClassType declaringClass, String name, Type arg1, Type arg2, Type arg3) {
		return this.equalsRaw(returnType, declaringClass, name) && argumentsEquals(arg1, arg2, arg3);
	}
	
	public boolean equals(Type returnType, ClassType declaringClass, String name, Type... args) {
		return this.equalsRaw(returnType, declaringClass, name) && argumentsEquals(args);
	}
	
	public boolean equals(Type returnType, ClassType declaringClass, String name, int argumentsCount) {
		return this.equalsRaw(returnType, declaringClass, name) && arguments.size() == argumentsCount;
	}
	
	private boolean equalsRaw(Type returnType, ClassType declaringClass, String name) {
		return this.returnType.equals(returnType) && this.getDeclaringClass().equals(declaringClass) && getName().equals(name);
	}

	
	public boolean argumentsEmpty() {
		return arguments.isEmpty();
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
	
	
	@Override
	public void writeDisassembled(DisassemblingOutputStream out, ClassInfo classinfo) {
		
	}
}
