package x590.javaclass.attribute;

import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import x590.javaclass.MethodDescriptor;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.exception.DecompilationException;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.ExtendedStringReader;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.GenericParameterType;
import x590.javaclass.type.GenericParameters;
import x590.javaclass.type.Type;

public class MethodSignatureAttribute extends SignatureAttribute {
	
	public final @Nullable GenericParameters<GenericParameterType> parameters;
	public final List<Type> arguments;
	public final Type returnType;
	
	protected MethodSignatureAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		ExtendedStringReader signatureIn = new ExtendedStringReader(pool.getUtf8String(in.readUnsignedShort()));
		
		this.parameters = Type.parseGenericParameters(signatureIn);
		this.arguments = Type.parseMethodArguments(signatureIn);
		this.returnType = Type.parseReturnType(signatureIn);
	}
	
	public void checkTypes(MethodDescriptor descriptor) {
		if(arguments.size() == descriptor.arguments.size()) {
			var iterator = descriptor.arguments.iterator();
			
			if(arguments.stream().allMatch(argType -> argType.baseEquals(iterator.next()))) {
				if(!returnType.baseEquals(descriptor.returnType)) {
					throw new DecompilationException("Method signature doesn't matches the return type: " + returnType + " and " + descriptor.returnType);
				}
				
				return;
			}
		}
		
		throw new DecompilationException("Method signature doesn't matches the arguments: " + arguments + " and " + descriptor.arguments);
	}
}
