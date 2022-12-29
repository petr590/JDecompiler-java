package x590.javaclass.constpool;

import x590.javaclass.io.ExtendedDataInputStream;

public abstract class ReferenceConstant extends Constant {
	
	public final int classIndex, nameAndTypeIndex;
	private ClassConstant clazz;
	private NameAndTypeConstant nameAndType;
	
	protected ReferenceConstant(ExtendedDataInputStream in) {
		this.classIndex = in.readUnsignedShort();
		this.nameAndTypeIndex = in.readUnsignedShort();
	}
	
	public ReferenceConstant(int classIndex, int nameAndTypeIndex, ConstantPool pool) {
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
		init(pool);
	}
	
	@Override
	void init(ConstantPool pool) {
		clazz = pool.get(classIndex);
		nameAndType = pool.get(nameAndTypeIndex);
	}
	
	public ClassConstant getClassConstant() {
		return clazz;
	}
	
	public NameAndTypeConstant getNameAndType() {
		return nameAndType;
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof ReferenceConstant constant && this.equals(constant);
	}
	
	public boolean equals(ReferenceConstant other) {
		return this == other || this.clazz.equals(other.clazz) && this.nameAndType.equals(other.nameAndType);
	}
}