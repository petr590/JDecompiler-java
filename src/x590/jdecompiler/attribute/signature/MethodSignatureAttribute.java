package x590.jdecompiler.attribute.signature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.attribute.ExceptionsAttribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.type.GenericParameterType;
import x590.jdecompiler.type.GenericParameters;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Type;
import x590.util.LoopUtil;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public final class MethodSignatureAttribute extends SignatureAttribute {
	
	public final @Nullable GenericParameters<GenericParameterType> parameters;
	public final @Immutable List<Type> arguments;
	public final Type returnType;
	public final @Immutable List<ReferenceType> throwsTypes;
	
	public MethodSignatureAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
		
		ExtendedStringInputStream signatureIn = new ExtendedStringInputStream(pool.getUtf8String(in.readUnsignedShort()));
		
		this.parameters = Type.parseNullableGenericParameters(signatureIn);
		this.arguments = Type.parseMethodArguments(signatureIn);
		this.returnType = Type.parseReturnType(signatureIn);
		
		if(signatureIn.get() == '^') {
			signatureIn.incPos();
			
			List<ReferenceType> throwsTypes = new ArrayList<>();
			
			while(signatureIn.isAvailable()) {
				throwsTypes.add(Type.parseSignatureParameter(signatureIn));
			}
			
			this.throwsTypes = Collections.unmodifiableList(throwsTypes);
			
		} else {
			this.throwsTypes = Collections.emptyList();
		}
	}
	
	public boolean hasGenericTypes() {
		return parameters != null || arguments.stream().anyMatch(Type::isGenericType) ||
				returnType.isGenericType() || !throwsTypes.isEmpty();
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(parameters != null)
			parameters.addImports(classinfo);
		
		returnType.addImports(classinfo);
		classinfo.addImportsFor(arguments);
	}
	
	public void checkTypes(MethodDescriptor descriptor, int skip, @Nullable ExceptionsAttribute excepionsAttr) {
		var iterator = descriptor.getArguments().iterator();
		
		for(int i = 0; i < skip && iterator.hasNext(); i++)
			iterator.next();
		
		if(!LoopUtil.iteratorsEquals(arguments.iterator(), iterator, Type::baseEquals)) {
			throw new DecompilationException("Method signature doesn't matches the arguments: (" + argumentsToString(arguments.stream()) + ") and (" + argumentsToString(descriptor.getArguments().stream().skip(skip)) + ")");
		}
		
		if(!returnType.baseEquals(descriptor.getReturnType())) {
			throw new DecompilationException("Method signature doesn't matches the return type: " + returnType + " and " + descriptor.getReturnType());
		}
		
		if(!throwsTypes.isEmpty()) {
			if(excepionsAttr == null || !LoopUtil.collectionsEquals(throwsTypes, excepionsAttr.getExceptionTypes(), Type::baseEquals)) {
				throw new DecompilationException("Method signature doesn't matches the \"Excepions\" attribute: " + argumentsToString(throwsTypes) +
						" and " + (excepionsAttr == null ? "null" : argumentsToString(excepionsAttr.getExceptionTypes())));
			}
		}
	}
	
	private static String argumentsToString(List<? extends Type> arguments) {
		return argumentsToString(arguments.stream());
	}
	
	private static String argumentsToString(Stream<? extends Type> arguments) {
		return arguments.map(Type::toString).collect(Collectors.joining(", "));
	}
}
