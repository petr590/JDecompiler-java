package x590.jdecompiler.operation.returning;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.AbstractOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.VoidOperation;
import x590.jdecompiler.type.primitive.PrimitiveType;

public final class VReturnOperation extends AbstractOperation implements VoidOperation {
	
	private static final VReturnOperation INSTANCE = new VReturnOperation();
	
	private VReturnOperation() {}
	
	/** @return Единственный экземпляр класса */
	public static VReturnOperation getInstance() {
		return INSTANCE;
	}
	
	/** Проверяет, что метод переданного {@code context} возвращает {@literal void}.
	 * @throws {@link DecompilationException}, если метод возвращает <b>не {@literal void}</b>.
	 * @return Единственный экземпляр класса */
	public static VReturnOperation getInstance(DecompilationContext context) {
		if(context.getGenericDescriptor().getReturnType() != PrimitiveType.VOID)
			throw new DecompilationException("The method return type (" + context.getGenericDescriptor().getReturnType() + ")" +
					" does not match type of the `return` instruction");
		
		return INSTANCE;
	}
	
	@Override
	public boolean isTerminable() {
		return true;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.write("return");
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other;
	}
}
