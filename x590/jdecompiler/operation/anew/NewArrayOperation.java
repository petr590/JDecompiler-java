package x590.jdecompiler.operation.anew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.AConstNullOperation;
import x590.jdecompiler.operation.constant.IConstOperation;
import x590.jdecompiler.operation.constant.ZeroConstOperation;
import x590.jdecompiler.type.ArrayType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.util.ArrayUtil;
import x590.util.Util;

public class NewArrayOperation extends Operation {
	
	private final ArrayType arrayType;
	private final Operation[] lengths;
	private final int length;
	private final List<Operation> initializers = new ArrayList<>();
	
	public NewArrayOperation(DecompilationContext context, int index) {
		this(context, context.pool.getClassConstant(index).toArrayType());
	}
	
	public NewArrayOperation(DecompilationContext context, int index, int dimensions) {
		this(context, context.pool.getClassConstant(index).toArrayType(), dimensions);
	}
	
	public NewArrayOperation(DecompilationContext context, ArrayType arrayType) {
		this(context, arrayType, 1);
	}
	
	public NewArrayOperation(DecompilationContext context, ArrayType arrayType, int dimensions) {
		
		this.arrayType = arrayType;
		this.lengths = new Operation[arrayType.getNestingLevel()];
		
		if(dimensions > arrayType.getNestingLevel())
			throw new DecompilationException("Instruction newarray (or another derivative of it)" +
					"has too many dimensions (" + dimensions + ") for its array type " + arrayType.toString());
		
		for(int i = dimensions; i > 0; )
			lengths[--i] = context.popAsNarrowest(PrimitiveType.INT);
		
		this.length = lengths[0] instanceof IConstOperation iconst ? iconst.getValue() : -1;
	}
	
	
	@Override
	public Type getReturnType() {
		return arrayType;
	}
	
	private void fillInitializersWithZeros(int toIndex) {
		for(int i = initializers.size(); i < toIndex; i++) {
			initializers.add(arrayType.getElementType().isPrimitive() ? ZeroConstOperation.INSTANCE : AConstNullOperation.INSTANCE);
		}
	}
	
	public boolean addToInitializer(Operation operation, IConstOperation indexOperation) {
		int index = indexOperation.getValue();
		
		if(index >= 0 && (length == -1 || index < length)) {
			fillInitializersWithZeros(index);
			initializers.add(operation);
			return true;
		}
		
		return false;
	}
	
	
	private boolean canInitAsList() {
		return !initializers.isEmpty() || ((lengths.length == 1 || (lengths.length > 1 && lengths[1] == null)) && length == 0);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(canInitAsList())
			out.print("new ").print(arrayType, context.classinfo).printsp();
		
		writeAsArrayInitializer(out, context);
	}
	
	@Override
	public void writeAsArrayInitializer(StringifyOutputStream out, StringifyContext context) {
		
		if(!initializers.isEmpty() && length != -1)
			fillInitializersWithZeros(length);
		
		if(canInitAsList()) {
			boolean useSpaces = arrayType.getNestingLevel() == 1 && !initializers.isEmpty();
			out.write(useSpaces ? "{ " : "{");
			Util.forEachExcludingLast(initializers, element -> element.writeAsArrayInitializer(out, context), element -> out.write(", "));
			out.write(useSpaces ? " }" : "}");
			
		} else {
			out.print("new ").print(arrayType.getMemberType(), context.classinfo);
			
			ArrayUtil.forEach(lengths, length -> {
					out.write('[');
					
					if(length != null)
						out.print(length, context);
					
					out.write(']');
			});
		}
	}
	
	@Override
	public boolean requiresLocalContext() {
		return initializers.stream().anyMatch(Operation::requiresLocalContext) ||
				Arrays.stream(lengths).anyMatch(length -> length != null && length.requiresLocalContext());
	}
}
