package x590.jdecompiler.constpool;

import x590.jdecompiler.JavaSerializable;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;

/**
 * Описывает константу в пуле констант
 */
public abstract class Constant implements JavaSerializable {
	
	protected Constant() {}
	
	protected static Constant readConstant(ExtendedDataInputStream in) {
		int tag = in.readUnsignedByte();
		return switch(tag) {
			case 0x1  -> new Utf8Constant(in);
			case 0x3  -> new IntegerConstant(in);
			case 0x4  -> new FloatConstant(in);
			case 0x5  -> new LongConstant(in);
			case 0x6  -> new DoubleConstant(in);
			case 0x7  -> new ClassConstant(in);
			case 0x8  -> new StringConstant(in);
			case 0x9  -> new FieldrefConstant(in);
			case 0xA  -> new MethodrefConstant(in);
			case 0xB  -> new InterfaceMethodrefConstant(in);
			case 0xC  -> new NameAndTypeConstant(in);
			case 0xF  -> new MethodHandleConstant(in);
			case 0x10 -> new MethodTypeConstant(in);
			case 0x12 -> new InvokeDynamicConstant(in);
			case 0x13 -> new ModuleConstant(in);
			case 0x14 -> new PackageConstant(in);
			default ->
				throw new DisassemblingException("Unknown tag " + tag);
		};
	}
	
	void init(ConstantPool pool) {}
	
	/** LongConstant и DoubleConstant, они исторически занимают две позиции в пуле */
	protected boolean holdsTwo() {
		return false;
	}
	
	public abstract String getConstantName();
	
	@Override
	public abstract boolean equals(Object other);
	
	@Override
	public abstract String toString();
}
