package x590.jdecompiler.attribute.signature;

import java.util.List;
import java.util.stream.Stream;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.GenericParameterType;
import x590.jdecompiler.type.GenericParameters;
import x590.jdecompiler.type.Type;
import x590.util.annotation.Nullable;

public final class ClassSignatureAttribute extends SignatureAttribute {
	
	public final @Nullable GenericParameters<GenericParameterType> parameters;
	public final ClassType superType;
	public final List<ClassType> interfaces;
	
	public ClassSignatureAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
		
		ExtendedStringInputStream signatureIn = new ExtendedStringInputStream(pool.getUtf8String(in.readUnsignedShort()));
		
		this.parameters = Type.parseNullableGenericParameters(signatureIn);
		this.superType = ClassType.readAsType(signatureIn);
		this.interfaces = Stream.generate(() -> signatureIn.isAvailable() ? ClassType.readAsType(signatureIn) : null)
				.takeWhile(type -> type != null).toList();
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(parameters != null)
			parameters.addImports(classinfo);
		
		superType.addImports(classinfo);
		classinfo.addImportsFor(interfaces);
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
