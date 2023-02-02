package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.FieldDescriptor;
import x590.jdecompiler.JavaField;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.operation.constant.DConstOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.util.StringUtil;
import x590.util.annotation.Nullable;

public final class DoubleConstant extends SingleConstableValueConstant<Double> {
	
	private final double value;
	
	protected DoubleConstant(ExtendedDataInputStream in) {
		this(in.readDouble());
	}
	
	protected DoubleConstant(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	
	@Override
	public Double getValueAsObject() {
		return value;
	}
	
	@Override
	public double doubleValue() {
		return value;
	}
	
	@Override
	protected boolean holdsTwo() {
		return true;
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
		return new DConstOperation(this);
	}

	
	private boolean valueEquals(double value) {
		return this.value == value || this.value == -value;
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(canUseConstants()) {
			if(valueEquals(Math.PI) || valueEquals(Math.E)) {
				classinfo.addImport(FPMath.MATH_CLASS);
				
			} else if(!Double.isFinite(value) || valueEquals(Double.MAX_VALUE) || valueEquals(Double.MIN_VALUE) || valueEquals(Double.MIN_NORMAL)) {
				classinfo.addImport(ClassType.DOUBLE);
			}
		}
	}
	
	
	private static final FieldDescriptor
			MAX_VALUE_DESCRIPTOR         = new FieldDescriptor(ClassType.DOUBLE, "MAX_VALUE", PrimitiveType.DOUBLE),
			MIN_VALUE_DESCRIPTOR         = new FieldDescriptor(ClassType.DOUBLE, "MIN_VALUE", PrimitiveType.DOUBLE),
			MIN_NORMAL_DESCRIPTOR        = new FieldDescriptor(ClassType.DOUBLE, "MIN_NORMAL", PrimitiveType.DOUBLE),
			POSITIVE_INFINITY_DESCRIPTOR = new FieldDescriptor(ClassType.DOUBLE, "POSITIVE_INFINITY", PrimitiveType.DOUBLE),
			NEGATIVE_INFINITY_DESCRIPTOR = new FieldDescriptor(ClassType.DOUBLE, "NEGATIVE_INFINITY", PrimitiveType.DOUBLE),
			NaN_DESCRIPTOR               = new FieldDescriptor(ClassType.DOUBLE, "NaN", PrimitiveType.DOUBLE);
	
	private boolean writeConstantIfEquals(StringifyOutputStream out, ClassInfo classinfo, @Nullable FieldDescriptor ownerConstant, double value, FieldDescriptor requiredConstant) {
		return writeConstantIfEquals(out, classinfo, ownerConstant, value, requiredConstant, true);
	}

	private boolean writeConstantIfEquals(StringifyOutputStream out, ClassInfo classinfo, @Nullable FieldDescriptor ownerConstant, double value, FieldDescriptor requiredConstant, boolean canNegate) {
		return writeConstantIfEquals(out, classinfo, ownerConstant, this.value == value, canNegate && this.value == -value, requiredConstant);
	}
	
	@Override
	public void writeValue(StringifyOutputStream out, ClassInfo classinfo, Type type, boolean implicit, @Nullable FieldDescriptor ownerConstant) {
		
		if(!canUseConstants() || 
			!writeConstantIfEquals(out, classinfo, ownerConstant, Double.MAX_VALUE, MAX_VALUE_DESCRIPTOR) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Double.MIN_VALUE, MIN_VALUE_DESCRIPTOR) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Double.MIN_NORMAL, MIN_NORMAL_DESCRIPTOR) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Double.POSITIVE_INFINITY, POSITIVE_INFINITY_DESCRIPTOR, false) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Double.NEGATIVE_INFINITY, NEGATIVE_INFINITY_DESCRIPTOR, false) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Double.isNaN(value), NaN_DESCRIPTOR) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Math.PI, FPMath.PI_DESCRIPTOR) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Math.E, FPMath.E_DESCRIPTOR)
		) {
			out.write(implicit && canImlicitCastToInt() ? StringUtil.toLiteral((int)value) : StringUtil.toLiteral(value));
		}
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(6);
		out.writeDouble(value);
	}
	
	@Override
	public int getPriority() {
		return !canUseConstants() && !Double.isFinite(value) ? Priority.DIVISION : Priority.DEFAULT_PRIORITY;
	}
	
	@Override
	protected boolean canUseConstant(JavaField constant) {
		return Double.compare(constant.getConstantValue().doubleValue(), value) == 0;
	}
	
	@Override
	public boolean canImlicitCastToInt() {
		return (int)value == value;
	}
	
	@Override
	public boolean isOne() {
		return value == 1;
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof DoubleConstant constant && this.equals(constant);
	}
	
	public boolean equals(DoubleConstant other) {
		return this == other || Double.compare(value, other.value) == 0;
	}
}
