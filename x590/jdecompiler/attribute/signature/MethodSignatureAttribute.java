package x590.jdecompiler.attribute.signature;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedStringReader;
import x590.jdecompiler.type.GenericParameterType;
import x590.jdecompiler.type.GenericParameters;
import x590.jdecompiler.type.Type;
import x590.util.annotation.Nullable;

public class MethodSignatureAttribute extends SignatureAttribute {
	
	public final @Nullable GenericParameters<GenericParameterType> parameters;
	public final List<Type> arguments;
	public final Type returnType;
	
	public MethodSignatureAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		ExtendedStringReader signatureIn = new ExtendedStringReader(pool.getUtf8String(in.readUnsignedShort()));
		
		this.parameters = Type.parseNullableGenericParameters(signatureIn);
		this.arguments = Type.parseMethodArguments(signatureIn);
		this.returnType = Type.parseReturnType(signatureIn);
	}
	
	public boolean hasGenericTypes() {
		return arguments.stream().anyMatch(Type::isGenericType) || returnType.isGenericType();
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(parameters != null)
			parameters.addImports(classinfo);
		
		returnType.addImports(classinfo);
		arguments.forEach(argument -> argument.addImports(classinfo));
	}
	
	public void checkTypes(MethodDescriptor descriptor, int skip) {
		var iterator = descriptor.arguments.iterator();
		
		for(int i = 0; i < skip && iterator.hasNext(); i++)
			iterator.next();
		
		for(Type argument : arguments) {
			if(!iterator.hasNext() || !argument.baseEquals(iterator.next())) {
				throw new DecompilationException("Method signature doesn't matches the arguments: (" + argumentsToString(arguments.stream()) + ") and (" + argumentsToString(descriptor.arguments.stream().skip(skip)) + ")");
			}
		}
		
		if(!returnType.baseEquals(descriptor.returnType)) {
			throw new DecompilationException("Method signature doesn't matches the return type: " + returnType + " and " + descriptor.returnType);
		}
	}
	
	private static String argumentsToString(Stream<Type> arguments) {
		return arguments.map(Type::toString).collect(Collectors.joining(", "));
	}
}
