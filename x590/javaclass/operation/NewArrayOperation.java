package x590.javaclass.operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.exception.DecompilationException;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ArrayType;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.javaclass.util.Util;

public class NewArrayOperation extends Operation {
	
	protected ArrayType arrayType;
	private final Operation[] lengths;
	protected final List<Operation> initializer = new ArrayList<>();
	
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
		this.lengths = new Operation[arrayType.nestingLevel];
		
		if(dimensions > arrayType.nestingLevel)
			throw new DecompilationException("Instruction newarray (or another derivative of it)" +
					"has too many dimensions (" + dimensions + ") for its array type " + arrayType.toString());
		
		for(int i = dimensions; i > 0; )
			lengths[--i] = context.stack.popAsNarrowest(PrimitiveType.INT);
	}
	
	
	@Override
	public Type getReturnType() {
		return arrayType;
	}
	
	protected boolean canInitAsList() {
		return !initializer.isEmpty() || ((lengths.length == 1 || (lengths.length > 1 && lengths[1] == null)) &&
				lengths[0] instanceof IConstOperation && ((IConstOperation)lengths[0]).value == 0);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(canInitAsList())
			out.print("new ").print(arrayType, context.classinfo).printsp();
		
		writeAsArrayInitializer(out, context);
	}
	
	@Override
	public void writeAsArrayInitializer(StringifyOutputStream out, StringifyContext context) {
		
		if(canInitAsList()) {
			boolean useSpaces = arrayType.nestingLevel == 1 && !initializer.isEmpty();
			out.write(useSpaces ? "{ " : "{");
			Util.forEachExcludingLast(initializer, element -> element.writeAsArrayInitializer(out, context), () -> out.write(", "));
			out.write(useSpaces ? " }" : "}");
			
		} else {
			out.print("new ").print(arrayType.memberType, context.classinfo);
			
			Util.forEach(lengths, length -> {
					out.write('[');
					
					if(length != null)
						out.print(length, context);

					out.write(']');
			});
		}
	}
	
	@Override
	public boolean requiresLocalContext() {
		return initializer.stream().anyMatch(Operation::requiresLocalContext) ||
				Arrays.stream(lengths).anyMatch(length -> length != null && length.requiresLocalContext());
	}
}