package x590.jdecompiler.constpool;

import java.util.HashMap;
import java.util.Map;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.FieldDescriptor;
import x590.jdecompiler.JavaField;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.IConstOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.UncertainIntegralType;
import x590.jdecompiler.util.StringUtil;
import x590.util.annotation.Nullable;
import x590.util.lazyloading.FunctionalLazyLoadingValue;

public final class IntegerConstant extends ConstableValueConstant<Integer> {
	
	private final int value;
	
	private final Type type;
	
	private static Type getTypeFor(int value) {
		if((value & 0x1) == value)
			return PrimitiveType.BYTE_SHORT_INT_CHAR_BOOLEAN;
		
		int minCapacity =
				(byte)value == value ? 1 :
				(short)value == value ? 2 : 4;
		
		return UncertainIntegralType.getInstance(minCapacity, 4, false, (char)value == value);
	}
	
	protected IntegerConstant(ExtendedDataInputStream in) {
		this(in.readInt());
	}
	
	protected IntegerConstant(int value) {
		this.value = value;
		this.type = getTypeFor(value);
	}

	public int getValue() {
		return value;
	}
	
	@Override
	public Integer getValueAsObject() {
		return value;
	}
	
	@Override
	public int intValue() {
		return value;
	}
	
	@Override
	public Type getType() {
		return type;
	}
	
	@Override
	protected Type getWidestType() {
		return PrimitiveType.INT;
	}
	
	@Override
	public String getConstantName() {
		return "Integer";
	}
	
	@Override
	public Operation toOperation() {
		return new IConstOperation(this);
	}
	
	
	private final Map<Type, FunctionalLazyLoadingValue<ClassInfo, JavaField>> constantLoaders = new HashMap<>(5, 1);
	
	@Override
	protected @Nullable FunctionalLazyLoadingValue<ClassInfo, JavaField> getConstantLoader(Type type) {
		if(constantLoaders.containsKey(type))
			return constantLoaders.get(type);
		
		var constantLoader = newConstantLoader(type);
		constantLoaders.put(type, constantLoader);
		return constantLoader;
	}
	
	
	private boolean valueEquals(int value) {
		return this.value == value || this.value == -value;
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(canUseConstants() && (valueEquals(Integer.MAX_VALUE) || valueEquals(Integer.MIN_VALUE))) {
			classinfo.addImport(ClassType.INTEGER);
		}
	}
	
	
	private static final FieldDescriptor
			MAX_VALUE_DESCRIPTOR = new FieldDescriptor(ClassType.INTEGER, "MAX_VALUE", PrimitiveType.INT),
			MIN_VALUE_DESCRIPTOR = new FieldDescriptor(ClassType.INTEGER, "MIN_VALUE", PrimitiveType.INT);
	
	private boolean writeConstantIfEquals(StringifyOutputStream out, ClassInfo classinfo, @Nullable FieldDescriptor ownerConstant, int value, FieldDescriptor requiredConstant) {
		return writeConstantIfEquals(out, classinfo, ownerConstant, this.value == value, this.value == -value, requiredConstant);
	}
	
	@Override
	public void writeValue(StringifyOutputStream out, ClassInfo classinfo, Type type, boolean implicit, @Nullable FieldDescriptor ownerConstant) {
		
		if(!canUseConstants() ||
			!writeConstantIfEquals(out, classinfo, ownerConstant, Integer.MAX_VALUE, MAX_VALUE_DESCRIPTOR) &&
			!writeConstantIfEquals(out, classinfo, ownerConstant, Integer.MIN_VALUE, MIN_VALUE_DESCRIPTOR)
		) {
			out.write(
					type.isSubtypeOf(PrimitiveType.BOOLEAN) ? StringUtil.toLiteral(value != 0) :
					type.isSubtypeOf(PrimitiveType.BYTE) ? StringUtil.toLiteral((byte)value) :
					type.isSubtypeOf(PrimitiveType.CHAR) ? StringUtil.toLiteral((char)value) :
					type.isSubtypeOf(PrimitiveType.SHORT) ? StringUtil.toLiteral((short)value) :
						StringUtil.toLiteral(value)
			);
		}
	}
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {
		out.writeByte(TAG_INTEGER);
		out.writeInt(value);
	}
	
	@Override
	protected boolean canUseConstant(JavaField constant) {
		return constant.getConstantValue().intValue() == value;
	}
	
	@Override
	public boolean isOne() {
		return value == 1;
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof IntegerConstant constant && this.equals(constant);
	}
	
	public boolean equals(IntegerConstant other) {
		return this == other || this.value == other.value;
	}
	
	@Override
	public String toString() {
		return "IntegerConstant {" + value + "}";
	}
}
