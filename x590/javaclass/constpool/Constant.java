package x590.javaclass.constpool;

import x590.javaclass.JavaSerializable;
import x590.javaclass.exception.DisassemblingException;
import x590.javaclass.io.ExtendedDataInputStream;

public abstract class Constant implements JavaSerializable {

	protected Constant() {}

	protected static Constant readConstant(ExtendedDataInputStream in) {
		int tag = in.readUnsignedByte();
		switch(tag) {
			case 1:  return new Utf8Constant(in);
			case 3:  return new IntegerConstant(in);
			case 4:  return new FloatConstant(in);
			case 5:  return new LongConstant(in);
			case 6:  return new DoubleConstant(in);
			case 7:  return new ClassConstant(in);
			case 8:  return new StringConstant(in);
			case 9:  return new FieldrefConstant(in);
			case 10: return new MethodrefConstant(in);
			case 11: return new InterfaceMethodrefConstant(in);
			case 12: return new NameAndTypeConstant(in);
			case 15: return new MethodHandleConstant(in);
			case 16: return new MethodTypeConstant(in);
			case 18: return new InvokeDynamicConstant(in);
			default: throw new DisassemblingException("Unknown tag " + tag);
		}
	}
	
	void init(ConstantPool pool) {}
	
	protected boolean holdsTwo() {
		return false;
	}
}
