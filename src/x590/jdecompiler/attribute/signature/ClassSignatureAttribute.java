package x590.jdecompiler.attribute.signature;

import java.util.List;
import java.util.stream.Stream;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.generic.GenericDeclarationType;
import x590.jdecompiler.type.reference.generic.GenericParameters;
import x590.util.annotation.Immutable;

public final class ClassSignatureAttribute extends SignatureAttribute {
	
	private final GenericParameters<GenericDeclarationType> parameters;
	private final ClassType superType;
	private final @Immutable List<ClassType> interfaces;
	
	public ClassSignatureAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
		
		ExtendedStringInputStream signatureIn = new ExtendedStringInputStream(pool.getUtf8String(in.readUnsignedShort()));
		
		this.parameters = Type.parseEmptyableGenericParameters(signatureIn);
		this.superType = ClassType.readAsType(signatureIn);
		this.interfaces = Stream.generate(() -> signatureIn.isAvailable() ? ClassType.readAsType(signatureIn) : null)
				.takeWhile(type -> type != null).toList();
	}
	
	public GenericParameters<GenericDeclarationType> getParameters() {
		return parameters;
	}
	
	public ClassType getSuperType() {
		return superType;
	}
	
	public @Immutable List<ClassType> getInterfaces() {
		return interfaces;
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		parameters.addImports(classinfo);
		superType.addImports(classinfo);
		classinfo.addImportsFor(interfaces);
	}
	
	public void checkTypes(ClassType superType, List<ClassType> interfaces) {
		if(!this.superType.equalsIgnoreSignature(superType)) {
			throw new DecompilationException("Class signature doesn't matches the super type: " + this.superType + " and " + superType);
		}
		
		if(this.interfaces.size() == interfaces.size()) {
			var iterator = interfaces.iterator();
			
			if(this.interfaces.stream().allMatch(interfaceType -> interfaceType.equalsIgnoreSignature(iterator.next()))) {
				return;
			}
		}
		
		throw new DecompilationException("Class signature doesn't matches the interfaces: " + this.interfaces + " and " + interfaces);
	}
}
