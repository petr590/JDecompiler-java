package x590.jdecompiler.operation.invoke;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.ReturnableOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;

public final class RecordSyntheticOperation extends ReturnableOperation {
	
	public static final RecordSyntheticOperation
			TO_STRING = new RecordSyntheticOperation(ClassType.STRING),
			HASH_CODE = new RecordSyntheticOperation(PrimitiveType.INT),
			EQUALS    = new RecordSyntheticOperation(PrimitiveType.BOOLEAN);
	
	private RecordSyntheticOperation(Type returnType) {
		super(returnType);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		throw new UnsupportedOperationException("Unexpected RecordSyntheticOperation in the code");
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other;
	}
}
