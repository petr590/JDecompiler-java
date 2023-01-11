package x590.jdecompiler.operation.invoke;

import java.util.LinkedList;
import java.util.List;

import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.operation.constant.EmptyStringConstOperation;
import x590.jdecompiler.operation.constant.StringConstOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.Type;
import x590.util.Util;

public final class ConcatStringsOperation extends InvokeOperation {
	
	protected final StringConstOperation pattern;
	
	protected final List<Operation> operands;
	
	public ConcatStringsOperation(DecompilationContext context, MethodDescriptor concater,
				StringConstOperation pattern, List<Operation> staticArguments) {
		
		super(context, concater, true);
		
		this.pattern = pattern;
		
		String patternStr = pattern.getValue();
		
		var arg = arguments.descendingIterator();
		var staticArg = staticArguments.iterator();
		
		StringBuilder str = new StringBuilder();
		
		this.operands = new LinkedList<>();
		
		for(int i = 0, length = patternStr.length(); i < length; i++) {
			char ch = patternStr.charAt(i);
			
			if(ch == '\1' || ch == '\2') {
				if(!str.isEmpty()) {
					operands.add(new StringConstOperation(str.toString()));
					str.setLength(0);
				}
				
				switch(ch) {
					case '\1': operands.add(arg.next()); break;
					case '\2': operands.add(staticArg.next()); break;
					default: throw new RuntimeException("WTF??"); // 🙃️
				}
			
			} else {
				str.append(ch);
			}
		}
		
		if(!str.isEmpty())
			operands.add(new StringConstOperation(str.toString()));
		
		insertEmptyStringIfNecessary();
	}
	
	protected void insertEmptyStringIfNecessary() {
		if((operands.size() == 1 && !operands.get(0).getReturnType().equals(ClassType.STRING)) ||
			(operands.size() > 1 && !operands.get(0).getReturnType().equals(ClassType.STRING) &&
					!operands.get(1).getReturnType().equals(ClassType.STRING))) {
			
			operands.add(0, EmptyStringConstOperation.INSTANCE);
		}
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		Util.forEachExcludingLast(operands,
				operation -> out.writePrioritied(this, operation, context, Associativity.RIGHT),
				operation -> out.write(" + "));
	}
	
	@Override
	protected String getInstructionName() {
		return "invokedynamic";
	}
	
	@Override
	public Type getReturnType() {
		return ClassType.STRING;
	}
	
	@Override
	public int getPriority() {
		return Priority.PLUS;
	}
}
