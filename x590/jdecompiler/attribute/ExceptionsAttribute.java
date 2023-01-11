package x590.jdecompiler.attribute;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.StringWritable;
import x590.jdecompiler.constpool.ClassConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ClassType;
import x590.util.ArrayUtil;

public class ExceptionsAttribute extends Attribute implements StringWritable {
	
	private final ClassType[] exceptionTypes;
	
	protected ExceptionsAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		int exceptionsLength = in.readUnsignedShort();
		
		if(exceptionsLength == 0)
			throw new DisassemblingException("Exceptions attribute cannot be empty");
		
		this.exceptionTypes = in.readToArray(new ClassType[exceptionsLength], () -> pool.<ClassConstant>get(in.readUnsignedShort()).toClassType());
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.write(" throws ");
		ArrayUtil.forEachExcludingLast(exceptionTypes,
				exceptionType -> out.write(exceptionType, classinfo),
				exceptionType -> out.write(", "));
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		ArrayUtil.forEach(exceptionTypes, exceptionType -> classinfo.addImport(exceptionType));
	}
}
