package x590.jdecompiler.attribute.signature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import x590.jdecompiler.attribute.ExceptionsAttribute;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.type.GenericParameterType;
import x590.jdecompiler.type.GenericParameters;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Type;
import x590.util.CollectionUtil;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public final class MethodSignatureAttribute extends SignatureAttribute {
	
	public final @Nullable GenericParameters<GenericParameterType> parameters;
	public final @Immutable List<Type> arguments;
	public final Type returnType;
	public final @Immutable List<ReferenceType> exceptionTypes;
	
	public MethodSignatureAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
		
		ExtendedStringInputStream signatureIn = new ExtendedStringInputStream(pool.getUtf8String(in.readUnsignedShort()));
		
		this.parameters = Type.parseNullableGenericParameters(signatureIn);
		this.arguments = Type.parseMethodArguments(signatureIn);
		this.returnType = Type.parseReturnType(signatureIn);
		
		if(signatureIn.get() == '^') {
			
			List<ReferenceType> throwsTypes = new ArrayList<>();
			
			while(signatureIn.isAvailable() && signatureIn.get() == '^') {
				signatureIn.incPos();
				throwsTypes.add(Type.parseSignatureParameter(signatureIn));
			}
			
			this.exceptionTypes = Collections.unmodifiableList(throwsTypes);
			
		} else {
			this.exceptionTypes = Collections.emptyList();
		}
	}
	
	public boolean hasGenericTypes() {
		return parameters != null || arguments.stream().anyMatch(Type::isGenericType) ||
				returnType.isGenericType() || !exceptionTypes.isEmpty();
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(parameters != null)
			parameters.addImports(classinfo);
		
		returnType.addImports(classinfo);
		classinfo.addImportsFor(arguments);
	}
	
	public void checkTypes(MethodDescriptor descriptor, int skip, @Nullable ExceptionsAttribute excepionsAttr) {
		
		List<Type> descriptorArguments = descriptor.getArguments();
		
		if(!CollectionUtil.collectionsEquals(arguments, descriptorArguments.subList(skip, descriptorArguments.size()), Type::equalsIgnoreSignature)) {
			throw new DecompilationException(
					"Method signature doesn't matches the arguments: (" + argumentsToString(arguments) + ")"
					+ " and (" + argumentsToString(descriptorArguments.subList(skip, descriptorArguments.size())) + ")"
			);
		}
		
		if(!returnType.equalsIgnoreSignature(descriptor.getReturnType())) {
			throw new DecompilationException("Method signature doesn't matches the return type: " + returnType + " and " + descriptor.getReturnType());
		}
		
		if(!exceptionTypes.isEmpty()) {
			if(excepionsAttr == null || !CollectionUtil.collectionsEquals(exceptionTypes, excepionsAttr.getExceptionTypes(), Type::equalsIgnoreSignature)) {
				throw new DecompilationException("Method signature doesn't matches the \"Excepions\" attribute: " + argumentsToString(exceptionTypes) +
						" and " + (excepionsAttr == null ? "null" : argumentsToString(excepionsAttr.getExceptionTypes())));
			}
		}
	}
	
	private static String argumentsToString(List<? extends Type> arguments) {
		return arguments.stream().map(Type::toString).collect(Collectors.joining(", "));
	}
}
