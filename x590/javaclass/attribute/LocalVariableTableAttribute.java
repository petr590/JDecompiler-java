package x590.javaclass.attribute;

import javax.annotation.Nullable;

import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.type.Type;

public class LocalVariableTableAttribute extends Attribute {
	
	private final LocalVariableEntry[] table;
	
	protected LocalVariableTableAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		this.table = in.readArray(LocalVariableEntry[]::new, () -> new LocalVariableEntry(in, pool));
	}
	
	
	public @Nullable LocalVariableEntry findEntry(int slot, int endPos) {
		for(LocalVariableEntry entry : table) {
			if(entry.slot == slot && entry.endPos == endPos)
				return entry;
		}
		
		return null;
	}
	
	
	public static class LocalVariableEntry {
		public final int startPos, endPos, slot;
		public final String name;
		public final Type type;
		
		public LocalVariableEntry(ExtendedDataInputStream in, ConstantPool pool) {
			this.startPos = in.readUnsignedShort();
			this.endPos = startPos + in.readUnsignedShort();
			this.name = pool.getUtf8String(in.readUnsignedShort());
			this.type = Type.parseType(pool.getUtf8String(in.readUnsignedShort()));
			this.slot = in.readUnsignedShort();
		}
	}
}