package x590.javaclass;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import x590.javaclass.attribute.Attributes;
import x590.javaclass.attribute.MethodSignatureAttribute;
import x590.javaclass.attribute.annotation.ParameterAnnotationsAttribute;
import x590.javaclass.constpool.ClassConstant;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.constpool.NameAndTypeConstant;
import x590.javaclass.constpool.ReferenceConstant;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.exception.InvalidMethodDescriptorException;
import x590.javaclass.exception.IllegalMethodHeaderException;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.ExtendedStringReader;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ArrayType;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.ReferenceType;
import x590.javaclass.type.Type;
import x590.javaclass.util.Util;
import x590.util.IntHolder;

public class MethodDescriptor extends Descriptor implements Importable {
	
	public final List<Type> arguments;
	public final Type returnType;
	
	private enum MethodType {
		CONSTRUCTOR, STATIC_INITIALIZER, PLAIN
	};
	
	private final MethodType type;
	
	private MethodType typeForName(String name) {
		MethodType type = switch(name) {
			case "<init>"   -> MethodType.CONSTRUCTOR;
			case "<clinit>" -> MethodType.STATIC_INITIALIZER;
			default         -> MethodType.PLAIN;
		};
		
		if(type != MethodType.PLAIN) {
			
			if(returnType != PrimitiveType.VOID)
				throw new InvalidMethodDescriptorException("Method " + this.toString() + " must return void");
			
			if(!clazz.isBasicClassType())
				throw new InvalidMethodDescriptorException("Class " + clazz + " cannot have " + this.toString() + " method");
		}
		
		return type;
	}
	
	public boolean isConstructor() {
		return type == MethodType.CONSTRUCTOR;
	}
	
	public boolean isStaticInitializer() {
		return type == MethodType.STATIC_INITIALIZER;
	}
	
	public boolean isConstructorOf(ReferenceType clazz) {
		return this.isConstructor() && this.clazz == clazz;
	}
	
	public boolean isStaticInitializerOf(ReferenceType clazz) {
		return this.isStaticInitializer() && this.clazz == clazz;
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
	
	public MethodDescriptor(ReferenceType clazz, String name, List<Type> arguments, Type returnType) {
		super(clazz, name);
		this.arguments = arguments;
		this.returnType = returnType;
		this.type = typeForName(name);
	}
	
	
	public MethodDescriptor(ReferenceType clazz, ExtendedDataInputStream in, ConstantPool pool) {
		this(clazz, pool.getUtf8String(in.readUnsignedShort()), pool.getUtf8String(in.readUnsignedShort()));
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(returnType);
		arguments.forEach(arg -> classinfo.addImport(arg));
	}
	
	
	public int countLocals(Modifiers modifiers) {
		return arguments.stream().mapToInt(argType -> argType.getSize().slotsOccupied()).sum() + (modifiers.isNotStatic() ? 1 : 0);
	}
	
	
	public void write(StringifyOutputStream out, StringifyContext context, Attributes attributes) {
		
		MethodSignatureAttribute signature = attributes.get("Signature");
		
		if(signature != null) {
			signature.checkTypes(this);
			
			if(signature.parameters != null) {
				out.writesp(signature.parameters, context.classinfo);
			}
		}
		
		switch(type) {
			case CONSTRUCTOR -> {
				out.print(clazz, context.classinfo);
				this.writeArguments(out, context, attributes, signature);
			}
				
			case STATIC_INITIALIZER -> {
				out.print("static");
			}
				
			case PLAIN -> {
				out.print(signature != null ? signature.returnType : returnType, context.classinfo).printsp().print(name);
				this.writeArguments(out, context, attributes, signature);
			}
		}
	}
	
	
	private void writeArguments(StringifyOutputStream out, StringifyContext context, Attributes attributes, @Nullable MethodSignatureAttribute signature) {
		out.write('(');
		
		ClassInfo classinfo = context.classinfo;
		
		IntHolder varIndex = new IntHolder(context.modifiers.isNotStatic() ? 1 : 0);
		
		
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
					(i != varargsIndex ? out.printsp(type, classinfo) : out.print(((ArrayType)type).elementType, classinfo).print("... "))
					.print(getVariableName.apply(type));
			
		} else {
			write = (type, i) ->
					out.printsp(type, classinfo).print(getVariableName.apply(type));
		}
		
		
		Util.forEachExcludingLast(signature != null ? signature.arguments : arguments,
				(type, i) -> {
					writeParameterAnnotations.accept(i);
					write.accept(type, i);
				},
				() -> out.write(", "));
		
		out.write(')');
	}
	
	
	private void writeArguments(StringifyOutputStream out, ClassInfo classinfo) {
		out.write('(');
		Util.forEachExcludingLast(arguments, arg -> out.write(arg, classinfo), () -> out.write(", "));
		out.write(')');
	}
	
	@Override
	public String toString() {
		return clazz.getName() + "." +
				(type == MethodType.STATIC_INITIALIZER ? "static {}" :
				(type == MethodType.CONSTRUCTOR ? ((ClassType)clazz).getSimpleName() : name)
						+ "(" + arguments.stream().map(Type::getName).collect(Collectors.joining(", ")) + ")");
	}
	
	
	public boolean equalsIgnoreClass(MethodDescriptor other) {
		return  this == other || (this.name == other.name &&
				this.returnType == other.returnType &&
				this.arguments.equals(other.arguments));
	}
	
	@Override
	public boolean equals(Object obj) {
		return this == obj ||
				obj instanceof MethodDescriptor other && this.equalsRaw(other);
	}
	
	public boolean equals(MethodDescriptor other) {
		return this == other || this.equalsRaw(other);
	}
	
	private boolean equalsRaw(MethodDescriptor other) {
		return  this.name.equals(other.name) && this.clazz.equals(other.clazz) &&
				this.returnType.equals(other.returnType) &&
				this.arguments.equals(other.arguments);
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
		return this.name.equals(name) && this.returnType.equals(returnType);
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
		return this.clazz.equals(clazz) && this.name.equals(name) && this.returnType.equals(returnType);
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
		
		Iterator<Type> iter = arguments.iterator();
		for(int i = 0, length = types.length; i < length; i++) {
			if(!iter.next().equals(types[i]))
				return false;
		}
		
		return true;
	}
}
