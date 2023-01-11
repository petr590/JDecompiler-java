package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.DConstOperation;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.util.StringUtil;

public class DoubleConstant extends ConstValueConstant {
	
	private final double value;
	
	protected DoubleConstant(ExtendedDataInputStream in) {
		this.value = in.readDouble();
	}
	
	public double getValue() {
		return value;
	}
	
	@Override
	protected boolean holdsTwo() {
		return true;
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return StringUtil.toLiteral(value);
	}
	
	@Override
	public Type getType() {
		return PrimitiveType.DOUBLE;
	}
	
	@Override
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
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof DoubleConstant constant && this.equals(constant);
	}
	
	public boolean equals(DoubleConstant other) {
		return this == other || this.value == other.value;
	}
}
