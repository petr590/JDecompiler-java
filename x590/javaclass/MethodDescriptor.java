package x590.javaclass;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import x590.javaclass.constpool.ClassConstant;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.constpool.NameAndTypeConstant;
import x590.javaclass.constpool.ReferenceConstant;
import x590.javaclass.exception.IllegalMethodDescriptorException;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.ExtendedStringReader;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.ReferenceType;
import x590.javaclass.type.Type;
import x590.javaclass.util.Util;

public class MethodDescriptor extends Descriptor implements StringWritableAndImportable {
	public final ClassType clazz;
	public final String name;
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
		
		if(type != MethodType.PLAIN && returnType != PrimitiveType.VOID)
			throw new IllegalMethodDescriptorException("Method " + this.toString() + " must return void");
		
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
	
	public boolean argumentsEmpty() {
		return this.arguments.isEmpty();
	}
	
	
	public MethodDescriptor(ReferenceConstant referenceConstant) {
		this(referenceConstant.getClassConstant().toClassType(), referenceConstant.getNameAndType());
	}
	
	public MethodDescriptor(String className, NameAndTypeConstant nameAndType) {
		this(className, nameAndType.getName().getValue(), nameAndType.getDescriptor().getValue());
	}
	
	public MethodDescriptor(String className, String name, String descriptor) {
		this(ClassType.valueOf(className), name, descriptor);
	}
	
	public MethodDescriptor(ClassConstant clazz, NameAndTypeConstant nameAndType) {
		this(clazz.toClassType(), nameAndType);
	}
	
	public MethodDescriptor(ClassType clazz, NameAndTypeConstant nameAndType) {
		this(clazz, nameAndType.getName().getValue(), nameAndType.getDescriptor().getValue());
	}
	
	public MethodDescriptor(ClassType clazz, String name, String descriptor) {
		this(clazz, name, new ExtendedStringReader(descriptor));
	}
	
	public MethodDescriptor(ClassType clazz, String name, ExtendedStringReader descriptor) {
		this(clazz, name, Type.parseMethodArguments(descriptor), Type.parseReturnType(descriptor));
	}
	
	
	public MethodDescriptor(ClassType clazz, String name, Type returnType, Type... arguments) {
		this(clazz, name, List.of(arguments), returnType);
	}
	
	public MethodDescriptor(ClassType clazz, String name, Type returnType) {
		this(clazz, name, Collections.emptyList(), returnType);
	}
	
	public MethodDescriptor(ClassType clazz, String name, List<Type> arguments, Type returnType) {
		this.clazz = clazz;
		this.name = name;
		this.arguments = arguments;
		this.returnType = returnType;
		this.type = typeForName(name);
	}
	
	
	public MethodDescriptor(ClassType clazz, ExtendedDataInputStream in, ConstantPool pool) {
		this(clazz, pool.getUtf8String(in.readUnsignedShort()), pool.getUtf8String(in.readUnsignedShort()));
	}
	
	
//	public String toString(StringifyContext context, Attributes attributes) {
//		boolean isNonStatic = !(context.modifiers && Modifiers.ACC_STATIC);
//		
//		MethodSignature signature = attributes.has<MethodSignatureAttribute>() ? attributes.get<MethodSignatureAttribute>()->signature : null;
//		
//		String in = (signature == null || signature.genericParameters.isEmpty() ?
//				"" : "<" + join<GenericParameter>(signature.genericParameters,
//					[&context] (GenericParameter parameter) { return parameter.toString(context.classinfo); }) + "> ") +
//				(isConstructor() ? context.classinfo.thisType.simpleName :
//				(signature == null ? returnType : signature.returnType)->toString(context.classinfo) + ' ' + name);
//		
//		
//		int offset = isNonStatic ? 1 : 0;
//		
//		var getVarName = (Type type, size_t i) -> {
//			return context.getCurrentScope().getNameFor(
//					context.methodScope.getVariable(i + (type.getSize() == TypeSize.EIGHT_BYTES ? offset++ : offset), false));
//		};
//		
//		List<Type> arguments = signature != null ? signature.arguments : this.arguments;
//		
//		ParameterAnnotationsAttribute parameterAnnotations = attributes.get<ParameterAnnotationsAttribute>();
//		IntFunction<String> parameterAnnotationToString = parameterAnnotations != null ?
//			i -> parameterAnnotations.parameterAnnotationsToString(i, context.classinfo) :
//			i -> "";
//		
//		
//		function<String(Type*, size_t)> concater;
//		
//		if((context.modifiers & ACC_VARARGS) != 0) {
//			size_t varargsIndex = arguments.size() - 1;
//			
//			if(arguments.isEmpty() || !(arguments.back().isArrayType())) {
//				throw new IllegalMethodHeaderException("Varargs method " + this.toString() + " must have last argument as array");
//			}
//			
//			concater = [&context, parameterAnnotationToString, getVarName, varargsIndex] (Type type, size_t i) {
//				return parameterAnnotationToString(i) + (i == varargsIndex ?
//					safe_cast<ArrayType>(type)->elementType.toString(context.classinfo) +
//					"... " + getVarName(type, i) : variableDeclarationToString(type, context.classinfo, getVarName(type, i)));
//			};
//		
//		} else {
//			concater = [&context, parameterAnnotationToString, getVarName] (Type type, size_t i) {
//				return parameterAnnotationToString(i) + variableDeclarationToString(type, context.classinfo, getVarName(type, i));
//			};
//		}
//		
//		
//		return in + '(' + join<Type>(arguments, concater) + ')';
//	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImportIfReferenceType(returnType);
		arguments.forEach(arg -> classinfo.addImportIfReferenceType(arg));
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		
		switch(type) {
			case CONSTRUCTOR -> {
				out.print(clazz, classinfo);
				this.writeArguments(out, classinfo);
			}
				
			case STATIC_INITIALIZER -> {
				out.print("static");
			}
				
			case PLAIN -> {
				out.print(returnType, classinfo).printsp().print(name);
				this.writeArguments(out, classinfo);
			}
		}
	}
	
	
	private void writeArguments(StringifyOutputStream out, ClassInfo classinfo) {
		out.write('(');
		Util.forEachExcludingLast(arguments, arg -> out.write(arg, classinfo), () -> out.write(", "));
		out.write(')');
	}
	
	public String toString() {
		return clazz.getName() + "." +
				(type == MethodType.STATIC_INITIALIZER ? "static {}" :
				(type == MethodType.CONSTRUCTOR ? clazz.getSimpleName() : name)
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
		return  this == other || this.equalsRaw(other);
	}
	
	private boolean equalsRaw(MethodDescriptor other) {
		return  this.name.equals(other.name) && this.clazz.equals(other.clazz) &&
				this.returnType.equals(other.returnType) &&
				this.arguments.equals(other.arguments);
	}
	
	
	public boolean argumentsEquals() {
		return arguments.isEmpty();
	}
	
	public boolean argumentsEquals(Type type1) {
		return arguments.size() == 1 &&
				arguments.get(0).equals(type1);
	}
	
	public boolean argumentsEquals(Type type1, Type type2) {
		return arguments.size() == 2 &&
				arguments.get(0).equals(type1) &&
				arguments.get(1).equals(type2);
	}
	
	public boolean argumentsEquals(Type type1, Type type2, Type type3) {
		return arguments.size() == 3 &&
				arguments.get(0).equals(type1) &&
				arguments.get(1).equals(type2) &&
				arguments.get(1).equals(type3);
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
