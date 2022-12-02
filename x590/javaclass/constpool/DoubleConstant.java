package x590.javaclass.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.operation.DConstOperation;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.javaclass.util.Util;

public class DoubleConstant extends ConstValueConstant {
	
	public final double value;

	protected DoubleConstant(ExtendedDataInputStream in) {
		this.value = in.readDouble();
	}
	
	@Override
	protected boolean holdsTwo() {
		return true;
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return Util.toLiteral(value);
	}
	
	@Override
	public Type getType() {
		return PrimitiveType.DOUBLE;
	}
	
	
	public String getConstantName() {
		return "Double";
	}
	
	@Override
	public Operation toOperation() {
		return new DConstOperation(value);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(6);
		out.writeDouble(value);
	}
}