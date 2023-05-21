package x590.jdecompiler.attribute;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.type.Type;
import x590.util.annotation.Immutable;

public final class LocalVariableTableAttribute extends Attribute {
	
	private static final LocalVariableTableAttribute
			EMPTY_TABLE = new LocalVariableTableAttribute(AttributeNames.LOCAL_VARIABLE_TABLE),
			EMPTY_TYPE_TABLE = new LocalVariableTableAttribute(AttributeNames.LOCAL_VARIABLE_TYPE_TABLE);
	
	private final @Immutable List<LocalVariableEntry> table;
	
	private LocalVariableTableAttribute(String name) {
		super(name, 0);
		this.table = Collections.emptyList();
	}
	
	protected LocalVariableTableAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
		
		this.table = in.readImmutableList(() -> new LocalVariableEntry(in, pool));
	}
	
	
	public static LocalVariableTableAttribute emptyTable() {
		return EMPTY_TABLE;
	}
	
	public static LocalVariableTableAttribute emptyTypeTable() {
		return EMPTY_TYPE_TABLE;
	}
	
	
	public boolean isEmpty() {
		return table.isEmpty();
	}
	
	public Optional<LocalVariableEntry> findEntry(int slot, int endPos) {
		return table.stream().filter(entry -> entry.slot == slot && entry.endPos == endPos).findAny();
	}
	
	
	public static final class LocalVariableEntry {
		private final int startPos, endPos, slot;
		private final String name;
		private final Type type;
		
		public LocalVariableEntry(ExtendedDataInputStream in, ConstantPool pool) {
			this.startPos = in.readUnsignedShort();
			this.endPos = startPos + in.readUnsignedShort();
			this.name = pool.getUtf8String(in.readUnsignedShort());
			this.type = Type.parseType(pool.getUtf8String(in.readUnsignedShort()));
			this.slot = in.readUnsignedShort();
		}
		
		public int startPos() {
			return startPos;
		}
		
		public int endPos() {
			return endPos;
		}
		
		public int slot() {
			return slot;
		}
		
		public String getName() {
			return name;
		}
		
		public Type getType() {
			return type;
		}
	}
}
