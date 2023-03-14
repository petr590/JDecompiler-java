package x590.jdecompiler.attribute;

import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.io.ExtendedDataOutputStream;

public final class EmptyCodeAttribute extends CodeAttribute {
	
	public static final EmptyCodeAttribute INSTANCE = new EmptyCodeAttribute();
	
	private EmptyCodeAttribute() {
		super(AttributeNames.CODE, 0, 0, 0, DisassemblerContext.EMPTY_DATA, ExceptionTable.empty(), Attributes.empty());
	}
	
	@Override
	public boolean isEmpty() {
		return true;
	}
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {}
}
