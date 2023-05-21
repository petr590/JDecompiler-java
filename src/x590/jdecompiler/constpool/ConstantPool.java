package x590.jdecompiler.constpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import it.unimi.dsi.fastutil.doubles.Double2ObjectArrayMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectArrayMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import x590.jdecompiler.JavaSerializable;
import x590.jdecompiler.exception.NoSuchConstantException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;
import x590.util.annotation.Nullable;

public final class ConstantPool implements JavaSerializable {
	
	private static final Int2ObjectMap<IntegerConstant> INTEGER_CONSTANTS = new Int2ObjectArrayMap<>();
	private static final Long2ObjectMap<LongConstant> LONG_CONSTANTS = new Long2ObjectArrayMap<>();
	private static final Float2ObjectMap<FloatConstant> FLOAT_CONSTANTS = new Float2ObjectArrayMap<>();
	private static final Double2ObjectMap<DoubleConstant> DOUBLE_CONSTANTS = new Double2ObjectArrayMap<>();
	
	private static final Map<String, Utf8Constant> UTF8_CONSTANTS = new HashMap<>();
	private static final Map<String, StringConstant> STRING_CONSTANTS = new HashMap<>();
	
	private final List<Constant> data;
	
	private ConstantPool(ExtendedDataInputStream in) {
		var length = in.readUnsignedShort();
		var data = this.data = new ArrayList<>(length);
		data.add(null); // 0-й элемент всегда null
		
		for(int i = 1; i < length; ) {
			Constant constant = Constant.readConstant(in);
			data.add(constant);
			
			if(constant.holdsTwo()) {
				data.add(null);
				i += 2;
			} else {
				i++;
			}
		}
		
		for(Constant constant : data) {
			if(constant != null) {
				constant.init(this);
			}
		}
	}
	
	public static ConstantPool read(ExtendedDataInputStream in) {
		return new ConstantPool(in);
	}
	
	
	public <C extends Constant> C get(int index) {
		if(index != 0)
			return getNullable(index);
		
		throw new NoSuchConstantException("By index " + index);
	}
	
	@SuppressWarnings("unchecked")
	public <C extends Constant> @Nullable C getNullable(int index) {
		try {
			return (C)data.get(index);
		} catch(IndexOutOfBoundsException ex) {
			throw new NoSuchConstantException("By index " + index);
		}
	}
	
	public String getUtf8String(int index) {
		return this.<Utf8Constant>get(index).getString();
	}
	
	public @Nullable String getNullableUtf8String(int index) {
		Utf8Constant utf8Constant = getNullable(index);
		return utf8Constant == null ? null : utf8Constant.getString();
	}
	
	public ClassConstant getClassConstant(int index) {
		return get(index);
	}
	
	public @Nullable ClassConstant getNullableClassConstant(int index) {
		return getNullable(index);
	}
	
	
	public int add(Constant constant) {
		int index = data.size();
		data.add(constant);
		return index;
	}
	
	private int findOrAdd(Predicate<Constant> equalsPredicate, Supplier<Constant> newConstantSupplier) {
		for(int i = 0; i < data.size(); i++) {
			if(equalsPredicate.test(data.get(i)))
				return i;
		}
				
		return add(newConstantSupplier.get());
	}
	
	public int findOrAddUtf8(String value) {
		return findOrAdd(constant -> constant instanceof Utf8Constant utf8 &&
				utf8.getString().equals(value),
				() -> new Utf8Constant(value));
	}
	
	public int findOrAddClass(int nameIndex) {
		return findOrAdd(constant -> constant instanceof ClassConstant clazz &&
				clazz.getNameIndex() == nameIndex,
				() -> new ClassConstant(nameIndex, this));
	}
	
	public int findOrAddNameAndType(int nameIndex, int descriptorIndex) {
		return findOrAdd(constant -> constant instanceof NameAndTypeConstant nameAndType &&
				nameAndType.getNameIndex() == nameIndex && nameAndType.getDescriptorIndex() == descriptorIndex,
				() -> new NameAndTypeConstant(nameIndex, descriptorIndex, this));
	}
	
	private int findOrAddReferenceConstant(int classIndex, int nameAndTypeIndex, Predicate<Constant> predicate, Supplier<Constant> supplier) {
		return findOrAdd(constant -> predicate.test(constant) && constant instanceof ReferenceConstant ref &&
				ref.getClassIndex() == classIndex && ref.getNameAndTypeIndex() == nameAndTypeIndex,
				supplier);
	}
	
	public int findOrAddFieldref(int classIndex, int nameAndTypeIndex) {
		return findOrAddReferenceConstant(classIndex, nameAndTypeIndex,
				constant -> constant instanceof FieldrefConstant,
				() -> new FieldrefConstant(classIndex, nameAndTypeIndex, this));
	}
	
	public int findOrAddMethodref(int classIndex, int nameAndTypeIndex) {
		return findOrAddReferenceConstant(classIndex, nameAndTypeIndex,
				constant -> constant instanceof MethodrefConstant,
				() -> new MethodrefConstant(classIndex, nameAndTypeIndex, this));
	}
	
	public int findOrAddInterfaceMethodref(int classIndex, int nameAndTypeIndex) {
		return findOrAddReferenceConstant(classIndex, nameAndTypeIndex,
				constant -> constant instanceof InterfaceMethodrefConstant,
				() -> new InterfaceMethodrefConstant(classIndex, nameAndTypeIndex, this));
	}
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {
		int size = data.size();
		out.writeShort(size);
		
		for(int i = 1; i < size; i++) {
			Constant conatant = data.get(i);
			if(conatant != null)
				conatant.serialize(out);
		}
	}
	
	public static IntegerConstant findOrCreateConstant(int value) {
		return INTEGER_CONSTANTS.computeIfAbsent(value, IntegerConstant::new);
	}
	
	public static LongConstant findOrCreateConstant(long value) {
		return LONG_CONSTANTS.computeIfAbsent(value, LongConstant::new);
	}
	
	public static FloatConstant findOrCreateConstant(float value) {
		return FLOAT_CONSTANTS.computeIfAbsent(value, FloatConstant::new);
	}
	
	public static DoubleConstant findOrCreateConstant(double value) {
		return DOUBLE_CONSTANTS.computeIfAbsent(value, DoubleConstant::new);
	}
	
	public static IntegerConstant findOrCreateConstant(boolean value) {
		return findOrCreateConstant(value ? 1 : 0);
	}
	
	public static Utf8Constant findOrCreateUtf8Constant(String value) {
		return UTF8_CONSTANTS.computeIfAbsent(value, Utf8Constant::new);
	}
	
	public static StringConstant findOrCreateConstant(String value) {
		return STRING_CONSTANTS.computeIfAbsent(value, string -> new StringConstant(findOrCreateUtf8Constant(string)));
	}
}
