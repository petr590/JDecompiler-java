package x590.javaclass.attribute;

import java.util.List;
import java.util.stream.Stream;

import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.exception.DecompilationException;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.ExtendedStringReader;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.GenericParameterType;
import x590.javaclass.type.GenericParameters;
import x590.javaclass.type.Type;

public class ClassSignatureAttribute extends SignatureAttribute {
	
	public final GenericParameters<GenericParameterType> parameters;
	public final ClassType superType;
	public final List<ClassType> interfaces;
	
	protected ClassSignatureAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		ExtendedStringReader signatureIn = new ExtendedStringReader(pool.getUtf8String(in.readUnsignedShort()));
		
		this.parameters = Type.parseGenericParameters(signatureIn);
		this.superType = ClassType.readAsType(signatureIn);
		this.interfaces = Stream.generate(() -> signatureIn.isAvailable() ? ClassType.readAsType(signatureIn) : null)
				.takeWhile(type -> type != null).toList();
		
		System.out.println(parameters);
		System.out.println(superType);
		System.out.println(interfaces);
	}
	
	public void checkTypes(ClassType superType, List<ClassType> interfaces) {
		if(!this.superType.baseEquals(superType)) {
			throw new DecompilationException("Class signature doesn't matches the super type: " + this.superType + " and " + superType);
		}
		
		if(this.interfaces.size() == interfaces.size()) {
			var iterator = interfaces.iterator();
			
			if(this.interfaces.stream().allMatch(interfaceType -> interfaceType.baseEquals(iterator.next()))) {
				return;
			}
		}
		
		throw new DecompilationException("Class signature doesn't matches the interfaces: " + this.interfaces + " and " + interfaces);
	}
}