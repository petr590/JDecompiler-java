package x590.jdecompiler.constpool;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.field.JavaField;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.operation.constant.FConstOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.util.StringUtil;
import x590.util.annotation.Nullable;

public final class FloatConstant extends SingleConstableValueConstant<Float> {
	
	private final float value;
	
	protected FloatConstant(ExtendedDataInputStream in) {
		this(in.readFloat());
	}
	
	protected FloatConstant(float value) {
		this.value = value;
	}
	
	public float getValue() {
		return value;
	}
	
	@Override
	public Float getValueAsObject() {
		return value;
	}
	
	@Override
	public float floatValue() {
		return value;
	}
	
	@Override
	public Type getType() {
		return PrimitiveType.FLOAT;
	}
	
	@Override
	public String getConstantName() {
		return "Float";
	}
	
	@Override
	public Operation toOperation() {
		return new FConstOperation(this);
	}
	
	
	private boolean valueEquals(float value) {
		return this.value == value || this.value == -value;
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(canUseConstants()) {
			if(valueEquals(FLOAT_PI) || valueEquals(FLOAT_E)) {
				classinfo.addImport(FPMath.MATH_CLASS);
				
			} else if(!Float.isFinite(value) || valueEquals(Float.MAX_VALUE) || valueEquals(Float.MIN_VALUE) || valueEquals(Float.MIN_NORMAL)) {
				classinfo.addImport(ClassType.FLOAT);
			}
		}
	}
	
	
	private static final float
			FLOAT_PI = (float)Math.PI,
			FLOAT_E = (float)Math.E;
	
	private static final FieldDescriptor
			MAX_VALUE_DESCRIPTOR         = new FieldDescriptor(ClassType.FLOAT, "MAX_VALUE", PrimitiveType.FLOAT),
			MIN_VALUE_DESCRIPTOR         = new FieldDescriptor(ClassType.FLOAT, "MIN_VALUE", PrimitiveType.FLOAT),
			MIN_NORMAL_DESCRIPTOR        = new FieldDescriptor(ClassType.FLOAT, "MIN_NORMAL", PrimitiveType.FLOAT),
			POSITIVE_INFINITY_DESCRIPTOR = new FieldDescriptor(ClassType.FLOAT, "POSITIVE_INFINITY", PrimitiveType.FLOAT),
			NEGATIVE_INFINITY_DESCRIPTOR = new FieldDescriptor(ClassType.FLOAT, "NEGATIVE_INFINITY", PrimitiveType.FLOAT),
			NaN_DESCRIPTOR               = new FieldDescriptor(ClassType.FLOAT, "NaN", PrimitiveType.FLOAT);
	
	private boolean writeConstantIfEquals(StringifyOutputStream out, ClassInfo classinfo, @Nullable FieldDescriptor ownerConstant, float value, FieldDescriptor requiredConstant) {
		return writeConstantIfEquals(out, classinfo, ownerConstant, value, requiredConstant, true);
	}

	private boolean writeConstantIfEquals(StringifyOutputStream out, ClassInfo classinfo, @Nullable FieldDescriptor ownerConstant, float value, FieldDescriptor requiredConstant, boolean canNegate) {
		return writeConstantIfEquals(out, classinfo, ownerConstant, this.value == value, canNegate && this.value == -value, requiredConstant);
	}
	
	@Override
	public void writeValue(StringifyOutputStream out, ClassInfo classinfo, Type type, boolean implicit, @Nullable FieldDescriptor ownerConstant) {
		
		if(!canUseConstants() ||
			!writeConstantIfEquals(out, classinfo, ownerConstant, Float.MAX_VALUE, MAX_VALUE_DESCRIPTOR) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Float.MIN_VALUE, MIN_VALUE_DESCRIPTOR) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Float.MIN_NORMAL, MIN_NORMAL_DESCRIPTOR) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Float.POSITIVE_INFINITY, POSITIVE_INFINITY_DESCRIPTOR, false) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Float.NEGATIVE_INFINITY, NEGATIVE_INFINITY_DESCRIPTOR, false) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Float.isNaN(value), NaN_DESCRIPTOR) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, value == FLOAT_PI, value == -FLOAT_PI, FPMath.PI_DESCRIPTOR, "(float)") &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, value == FLOAT_E, value == -FLOAT_E, FPMath.E_DESCRIPTOR, "(float)")
		) {
			out.write(implicit && canImlicitCastToInt() ? StringUtil.toLiteral((int)value) : StringUtil.toLiteral(value));
		}
	}
	
	@Override
	public String toString() {
		return String.format("FloatConstant { %f }", value);
	}
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {
		out.writeByte(TAG_FLOAT);
		out.writeFloat(value);
	}
	
	@Override
	public int getPriority() {
		return !canUseConstants() ? !Float.isFinite(value) ? Priority.DIVISION : Priority.DEFAULT_PRIORITY :
				valueEquals(FLOAT_PI) || valueEquals(FLOAT_E) ? Priority.CAST : Priority.DEFAULT_PRIORITY;
	}

	@Override
	protected boolean canUseConstant(JavaField constant) {
		return Float.compare(constant.getConstantValue().floatValue(), value) == 0;
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
		return this == other || other instanceof FloatConstant constant && this.equals(constant);
	}
	
	public boolean equals(FloatConstant other) {
		return this == other || Float.compare(value, other.value) == 0;
	}
}
