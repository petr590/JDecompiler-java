package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import x590.jdecompiler.JavaSerializable;
import x590.jdecompiler.exception.NoSuchConstantException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.util.annotation.Nullable;

public final class ConstantPool implements JavaSerializable {
	
	private final List<Constant> data;
	private final Map<Object, ICachedConstant<?>> constants = new HashMap<>();
	
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
				
				if(constant instanceof ICachedConstant<?> constableValueConstant) {
					constants.put(constableValueConstant.getValueAsObject(), constableValueConstant);
				}
			}
		}
	}
	
	public static ConstantPool read(ExtendedDataInputStream in) {
		return new ConstantPool(in);
	}
	
	
	@SuppressWarnings("unchecked")
	public <C extends Constant> C get(int index) {
		if(index != 0)
			return (C)data.get(index);
		
		throw new NoSuchConstantException("By index " + index);
	}
	
	@SuppressWarnings("unchecked")
	public <C extends Constant> @Nullable C getNullable(int index) {
		return (C)data.get(index);
	}
	
	public String getUtf8String(int index) {
		return this.<Utf8Constant>get(index).getString();
	}
	
	public @Nullable String getUtf8StringNullable(int index) {
		Utf8Constant utf8Constant = this.<Utf8Constant>getNullable(index);
		return utf8Constant == null ? null : utf8Constant.getString();
	}
	
	public ClassConstant getClassConstant(int index) {
		return this.<ClassConstant>get(index);
	}
	
	
	public int add(Constant constant) {
		int index = data.size();
		data.add(constant);
		return index;
	}
	
	private int findOrAdd(Predicate<Constant> predicate, Supplier<Constant> supplier) {
		for(int i = 0; i < data.size(); i++) {
			if(predicate.test(data.get(i)))
				return i;
		}
				
		return add(supplier.get());
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
	public void serialize(DataOutputStream out) throws IOException {
		int size = data.size();
		out.writeShort(size);
		for(int i = 1; i < size; i++) {
			Constant conatant = data.get(i);
			if(conatant != null)
				conatant.serialize(out);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T, C extends ICachedConstant<T>> C findOrCreateConstant(T value, Function<T, ICachedConstant<T>> creator) {
		
		ICachedConstant<T> constant = (ICachedConstant<T>)constants.get(value);
		
		if(constant != null)
			return (C)constant;
		
		constant = creator.apply(value);
		constants.put(value, constant);
		return (C)constant;
	}
	
	public IntegerConstant findOrCreateConstant(int value) {
		return findOrCreateConstant(value, IntegerConstant::new);
	}
	
	public LongConstant findOrCreateConstant(long value) {
		return findOrCreateConstant(value, LongConstant::new);
	}
	
	public FloatConstant findOrCreateConstant(float value) {
		return findOrCreateConstant(value, FloatConstant::new);
	}
	
	public DoubleConstant findOrCreateConstant(double value) {
		return findOrCreateConstant(value, DoubleConstant::new);
	}
	
	public StringConstant findOrCreateConstant(String value) {
		return findOrCreateConstant(value, string -> new StringConstant(findOrCreateUtf8Constant(string)));
	}
	
	public Utf8Constant findOrCreateUtf8Constant(String value) {
		return findOrCreateConstant(value, Utf8Constant::new);
	}
}
