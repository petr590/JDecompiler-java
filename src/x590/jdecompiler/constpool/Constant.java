package x590.jdecompiler.constpool;

import x590.jdecompiler.JavaSerializable;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;

public abstract class Constant implements JavaSerializable {
	
	protected Constant() {}
	
	protected static Constant readConstant(ExtendedDataInputStream in) {
		int tag = in.readUnsignedByte();
		switch(tag) {
			case 0x1:  return new Utf8Constant(in);
			case 0x3:  return new IntegerConstant(in);
			case 0x4:  return new FloatConstant(in);
			case 0x5:  return new LongConstant(in);
			case 0x6:  return new DoubleConstant(in);
			case 0x7:  return new ClassConstant(in);
			case 0x8:  return new StringConstant(in);
			case 0x9:  return new FieldrefConstant(in);
			case 0xA:  return new MethodrefConstant(in);
			case 0xB:  return new InterfaceMethodrefConstant(in);
			case 0xC:  return new NameAndTypeConstant(in);
			case 0xF:  return new MethodHandleConstant(in);
			case 0x10: return new MethodTypeConstant(in);
			case 0x12: return new InvokeDynamicConstant(in);
			default:   throw new DisassemblingException("Unknown tag " + tag);
		}
	}
	
	void init(ConstantPool pool) {}
	
	/** LongConstant и DoubleConstant, они исторически занимают две позиции в пуле */
	protected boolean holdsTwo() {
		return false;
	}
	
	@Override
	public abstract boolean equals(Object other);
}
