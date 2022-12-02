package x590.javaclass.operation;

import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.util.Util;

public class IConstOperation extends ConstOperation {
	
	protected final int value;
	
	public IConstOperation(int value) {
		super(PrimitiveType.ANY_INT_OR_BOOLEAN);
		this.value = value;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(
				returnType.isSubtypeOf(PrimitiveType.BYTE)    ? Util.toLiteral((byte)value) :
				returnType.isSubtypeOf(PrimitiveType.SHORT)   ? Util.toLiteral((short)value) :
				returnType.isSubtypeOf(PrimitiveType.CHAR)    ? Util.toLiteral((char)value) :
				returnType.isSubtypeOf(PrimitiveType.BOOLEAN) ? Util.toLiteral(value == 0 ? true : false) :
					Util.toLiteral(value)
		);
	}
}