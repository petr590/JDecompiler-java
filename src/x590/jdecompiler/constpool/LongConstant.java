package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.FieldDescriptor;
import x590.jdecompiler.JavaField;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.LConstOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.util.StringUtil;
import x590.util.annotation.Nullable;

public final class LongConstant extends SingleConstableValueConstant<Long> {
	
	private final long value;
	
	protected LongConstant(ExtendedDataInputStream in) {
		this(in.readLong());
	}
	
	protected LongConstant(long value) {
		this.value = value;
	}
	
	public long getValue() {
		return value;
	}
	
	@Override
	public Long getValueAsObject() {
		return value;
	}
	
	@Override
	public long longValue() {
		return value;
	}
	
	@Override
	protected boolean holdsTwo() {
		return true;
	}
	
	@Override
	public Type getType() {
		return PrimitiveType.LONG;
	}
	
	@Override
	public String getConstantName() {
		return "Long";
	}
	
	@Override
	public Operation toOperation() {
		return new LConstOperation(this);
	}
	
	
	private boolean valueEquals(long value) {
		return this.value == value || this.value == -value;
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(canUseConstants() && (valueEquals(Long.MAX_VALUE) || valueEquals(Long.MIN_VALUE))) {
			classinfo.addImport(ClassType.LONG);
		}
	}
	
	
	private static final FieldDescriptor
			MAX_VALUE_DESCRIPTOR = new FieldDescriptor(ClassType.LONG, "MAX_VALUE", PrimitiveType.LONG),
			MIN_VALUE_DESCRIPTOR = new FieldDescriptor(ClassType.LONG, "MIN_VALUE", PrimitiveType.LONG);
	
	private boolean writeConstantIfEquals(StringifyOutputStream out, ClassInfo classinfo, @Nullable FieldDescriptor ownerConstant, long value, FieldDescriptor requiredConstant) {
		return writeConstantIfEquals(out, classinfo, ownerConstant, this.value == value, this.value == -value, requiredConstant);
	}
	
	@Override
	public void writeValue(StringifyOutputStream out, ClassInfo classinfo, Type type, boolean implicit, @Nullable FieldDescriptor ownerConstant) {
		
		if(!canUseConstants() ||
			!writeConstantIfEquals(out, classinfo, ownerConstant, Long.MAX_VALUE, MAX_VALUE_DESCRIPTOR) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Long.MIN_VALUE, MIN_VALUE_DESCRIPTOR)
		) {
			out.write(implicit && canImlicitCastToInt() ? StringUtil.toLiteral((int)value) : StringUtil.toLiteral(value));
		}
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(5);
		out.writeLong(value);
	}
	
	@Override
	protected boolean canUseConstant(JavaField constant) {
		return constant.getConstantValue().longValue() == value;
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
		return this == other || other instanceof LongConstant constant && this.equals(constant);
	}
	
	public boolean equals(LongConstant other) {
		return this == other || value == other.value;
	}
}
