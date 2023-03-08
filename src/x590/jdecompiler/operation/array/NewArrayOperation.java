package x590.jdecompiler.operation.array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.IConstOperation;
import x590.jdecompiler.operation.constant.ZeroConstOperation;
import x590.jdecompiler.operationinstruction.constant.AConstNullOperationInstruction;
import x590.jdecompiler.type.ArrayType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.util.Util;
import x590.util.annotation.Immutable;

public class NewArrayOperation extends Operation {
	
	private final ArrayType arrayType;
	private final @Immutable List<Operation> arrayLengths;
	private final int length;
	private final List<Operation> initializers = new ArrayList<>();
	private boolean varargsInlined;
	
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
		
		if(dimensions > arrayType.getNestingLevel())
			throw new DecompilationException("Instruction newarray (or another derivative of it)" +
					"has too many dimensions (" + dimensions + ") for its array type " + arrayType.toString());
		
		this.arrayType = arrayType;
		
		List<Operation> arrayLengths = new ArrayList<>(dimensions);
		
		for(int i = dimensions; i > 0; i--)
			arrayLengths.add(context.popAsNarrowest(PrimitiveType.INT));
		
		Collections.reverse(arrayLengths);
		
		this.arrayLengths = Collections.unmodifiableList(arrayLengths);
		this.length = arrayLengths.get(0) instanceof IConstOperation iconst ? iconst.getValue() : -1;
	}
	
	
	@Override
	public Type getReturnType() {
		return arrayType;
	}
	
	public int getLength() {
		return length;
	}
	
	private void fillInitializersWithZeros(int toIndex) {
		for(int i = initializers.size(); i < toIndex; i++) {
			initializers.add(arrayType.getElementType().isPrimitive() ? ZeroConstOperation.INSTANCE : AConstNullOperationInstruction.INSTANCE);
		}
	}
	
	public boolean addToInitializer(Operation operation, IConstOperation indexOperation) {
		int index = indexOperation.getValue();
		
		if(index >= 0 && (length == -1 || index < length)) {
			fillInitializersWithZeros(index);
			operation.allowImplicitCast();
			initializers.add(operation);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void inlineVarargs() {
		if(canInitAsList()) {
			varargsInlined = true;
			initializers.forEach(Operation::denyImplicitCast);
		}
	}
	
	
	public boolean canInitAsList() {
		return !initializers.isEmpty() || arrayLengths.size() == 1 && length == 0;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		arrayType.addImports(classinfo);
		arrayLengths.forEach(arrayLength -> arrayLength.addImports(classinfo));
		initializers.forEach(initializer -> initializer.addImports(classinfo));
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(varargsInlined) {
			Util.forEachExcludingLast(initializers, element -> element.writeTo(out, context), element -> out.write(", "));
			return;
		}
		
		if(canInitAsList()) {
			out.print("new ").print(arrayType, context.getClassinfo()).printsp();
		}
		
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
			out.print("new ").print(arrayType.getMemberType(), context.getClassinfo());
			arrayLengths.forEach(arrayLength -> out.print('[').print(arrayLength, context).print(']'));
			for(int i = arrayLengths.size(), nestLevel = arrayType.getNestingLevel(); i < nestLevel; i++)
				out.write("[]");
		}
	}
	
	@Override
	public boolean requiresLocalContext() {
		return initializers.stream().anyMatch(Operation::requiresLocalContext) ||
				arrayLengths.stream().anyMatch(Operation::requiresLocalContext);
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof NewArrayOperation operation &&
				arrayType.equals(operation.arrayType) && arrayLengths.equals(operation.arrayLengths) &&
				initializers.equals(operation.initializers) && varargsInlined == operation.varargsInlined;
	}
}
