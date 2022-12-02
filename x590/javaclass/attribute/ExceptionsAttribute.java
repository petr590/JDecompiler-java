package x590.javaclass.attribute;

import x590.javaclass.ClassInfo;
import x590.javaclass.StringWritable;
import x590.javaclass.constpool.ClassConstant;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.exception.DisassemblingException;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ClassType;
import x590.javaclass.util.Util;

public class ExceptionsAttribute extends Attribute implements StringWritable {
	
	private final ClassType[] exceptions;
	
	protected ExceptionsAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		int exceptionsLength = in.readUnsignedShort();
		
		if(exceptionsLength == 0)
			throw new DisassemblingException("Exceptions attribute cannot be empty");
		
		var exceptions = this.exceptions = new ClassType[exceptionsLength];
		
		for(int i = 0; i < exceptionsLength; i++) {
			exceptions[i] = pool.<ClassConstant>get(in.readUnsignedShort()).toClassType();
		}
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.write(" throws ");
		Util.forEachExcludingLast(exceptions,
				exceptionType -> out.write(exceptionType, classinfo),
				exceptionType -> out.write(", "));
	}
}