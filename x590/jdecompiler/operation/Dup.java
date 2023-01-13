package x590.jdecompiler.operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.type.TypeSize;

public class Dup {
	
	private Dup() {}
	
	
	public static void dup(DecompilationContext context) {
		context.push(context.peekWithSize(TypeSize.WORD));
	}
	
	public static void dupX1(DecompilationContext context) {
		Operation value1 = context.popWithSize(TypeSize.WORD);
		Operation value2 = context.popWithSize(TypeSize.WORD);
		
		context.push(value1);
		context.push(value2);
		context.push(value1);
	}
	
	public static void dupX2(DecompilationContext context) {
		Operation value1 = context.popWithSize(TypeSize.WORD);
		Operation value2 = context.popWithSize(TypeSize.WORD);
		Operation value3 = context.popWithSize(TypeSize.WORD);
		
		context.push(value1);
		context.push(value3);
		context.push(value2);
		context.push(value1);
	}
	
	public static void dup2(DecompilationContext context) {
		Operation value = context.peek();
		
		switch(value.getReturnType().getSize()) {
			case WORD -> {
				context.push(context.peekWithSize(1, TypeSize.WORD));
			}
			
			case LONG -> {}
			
			default -> throw new DecompilationException("Illegal operation type size " + value.getReturnType().getSize());
		}
		
		context.push(value);
	}
	
	
	public static void dup2X1(DecompilationContext context) {
		dup2X(context, 3, "dup2_x1");
	}
	
	
	public static void dup2X2(DecompilationContext context) {
		dup2X(context, 4, "dup2_x2");
	}
	
	
	private static void dup2X(DecompilationContext context, int words, String instructionName) {
		List<Operation> values = new ArrayList<>(words);
		
		int size = 0;
		
		while(size < words) {
			Operation value = context.pop();
			values.add(value);
			size += value.getReturnType().getSize().slotsOccupied();
		}
		
		if(size > words)
			throw new DecompilationException("Illegal stack size for instruction " + instructionName + ": " + values);
		
		Operation value1 = values.get(0);
		
		switch(value1.getReturnType().getSize()) {
			case WORD -> {
				context.push(values.get(1));
			}
			
			case LONG -> {}
			
			default -> throw new DecompilationException("Illegal operation type size " + value1.getReturnType().getSize());
		}
		
		context.push(value1);
		
		Collections.reverse(values);
		context.pushAll(values);
	}
}
