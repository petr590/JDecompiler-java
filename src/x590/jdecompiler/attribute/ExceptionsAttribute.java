package x590.jdecompiler.attribute;

import java.util.Collections;
import java.util.List;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.attribute.signature.MethodSignatureAttribute;
import x590.jdecompiler.constpool.ClassConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ReferenceType;
import x590.util.Util;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public class ExceptionsAttribute extends Attribute {
	
	private final @Immutable List<ReferenceType> exceptionTypes;
	
	ExceptionsAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		int exceptionsLength = in.readUnsignedShort();
		
		if(exceptionsLength == 0)
			throw new DisassemblingException("The \"" + AttributeNames.EXCEPTIONS + "\" attribute cannot be empty");
		
		this.exceptionTypes = in.readImmutableList(exceptionsLength, () -> pool.<ClassConstant>get(in.readUnsignedShort()).toClassType());
	}
	
	private ExceptionsAttribute(int nameIndex, String name, int length, List<ReferenceType> exceptionTypes) {
		super(nameIndex, name, length);
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
			super(0, "Exceptions", 0, Collections.emptyList());
		}
		
		@Override
		public void addImports(ClassInfo classinfo) {}
		
		@Override
		public void write(StringifyOutputStream out, ClassInfo classinfo, @Nullable MethodSignatureAttribute signature) {}
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		exceptionTypes.forEach(exceptionType -> classinfo.addImport(exceptionType));
	}
	
	public void write(StringifyOutputStream out, ClassInfo classinfo, @Nullable MethodSignatureAttribute signature) {
		out.write(" throws ");
		Util.forEachExcludingLast(signature != null ? signature.throwsTypes : exceptionTypes,
				exceptionType -> out.write(exceptionType, classinfo),
				exceptionType -> out.write(", "));
	}
}
