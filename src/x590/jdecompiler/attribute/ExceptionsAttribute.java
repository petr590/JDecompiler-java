package x590.jdecompiler.attribute;

import java.util.Collections;
import java.util.List;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.attribute.signature.MethodSignatureAttribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ReferenceType;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public class ExceptionsAttribute extends Attribute {
	
	private final @Immutable List<ReferenceType> exceptionTypes;
	
	ExceptionsAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
		
		int exceptionsLength = in.readUnsignedShort();
		
		if(exceptionsLength == 0)
			throw new DisassemblingException("The \"" + AttributeNames.EXCEPTIONS + "\" attribute cannot be empty");
		
		this.exceptionTypes = in.readImmutableList(exceptionsLength, () -> pool.getClassConstant(in.readUnsignedShort()).toClassType());
	}
	
	private ExceptionsAttribute(String name, int length, List<ReferenceType> exceptionTypes) {
		super(name, length);
		this.exceptionTypes = exceptionTypes;
	}
	
	public @Immutable List<ReferenceType> getExceptionTypes() {
		return exceptionTypes;
	}
	
	public static ExceptionsAttribute empty() {
		return EmptyExceptionsAttribute.INSTANCE;
	}
	
	
	private static class EmptyExceptionsAttribute extends ExceptionsAttribute {
		
		private static final ExceptionsAttribute INSTANCE = new EmptyExceptionsAttribute();
		
		private EmptyExceptionsAttribute() {
			super("Exceptions", 0, Collections.emptyList());
		}
		
		@Override
		public void addImports(ClassInfo classinfo) {}
		
		@Override
		public void write(StringifyOutputStream out, ClassInfo classinfo, @Nullable MethodSignatureAttribute signature) {}
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImportsFor(exceptionTypes);
	}
	
	public void write(StringifyOutputStream out, ClassInfo classinfo, @Nullable MethodSignatureAttribute signature) {
		out.print(" throws ").printAll(signature != null ? signature.throwsTypes : exceptionTypes, classinfo, ", ");
	}
}
