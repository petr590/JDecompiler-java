package x590.javaclass.attribute;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.io.ExtendedDataInputStream;

public class CodeAttribute extends Attribute {

	public final int maxStack, maxLocals; 
	public final byte[] code, otherData; 
	
	protected CodeAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		maxStack = in.readUnsignedShort();
		maxLocals = in.readUnsignedShort();
		
		code = new byte[in.readInt()];
		in.readFully(code);
		
		otherData = new byte[length - code.length - 8];
		in.readFully(otherData);
	}

	protected CodeAttribute(int nameIndex, String name, int length, int maxStack, int maxLocals, byte[] code, byte[] otherData) {
		super(nameIndex, name, length);
		
		this.maxStack = maxStack;
		this.maxLocals = maxLocals;
		this.code = code;
		this.otherData = otherData;
	}
	
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		
		out.writeShort(maxStack);
		out.writeShort(maxLocals);
		
		out.writeInt(code.length);
		out.write(code);
		
		out.write(otherData);
	}
}
