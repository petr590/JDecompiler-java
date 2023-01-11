package x590.jdecompiler.attribute;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.context.DisassemblerContext;

public class EmptyCodeAttribute extends CodeAttribute {
	
	public static final EmptyCodeAttribute INSTANCE = new EmptyCodeAttribute();
	
	private EmptyCodeAttribute() {
		super(0, "Code", 0, 0, 0, DisassemblerContext.EMPTY_DATA, ExceptionTable.empty(), Attributes.empty());
	}
	
	@Override
	public boolean isEmpty() {
		return true;
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {}
}
