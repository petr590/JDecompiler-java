package x590.jdecompiler.operation.invoke;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BooleanSupplier;

import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.operation.constant.StringConstOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.Type;

public final class ConcatStringsOperation extends InvokeOperation {

	private final LinkedList<Operation> operands;
	
	// Должно вызываться после сведения типов, поэтому реализовано через функцию
	private BooleanSupplier canOmitEmptyStringFunc = () -> false;
	
	public ConcatStringsOperation(DecompilationContext context, MethodDescriptor descriptor,
			LinkedList<Operation> operands) {
		
		super(context, descriptor);
		this.operands = operands;
	}
	
	public ConcatStringsOperation(DecompilationContext context, MethodDescriptor concater,
				StringConstOperation pattern, List<Operation> staticArguments) {
		
		super(context, concater);
		
		String patternStr = pattern.getValue();
		
		var arg = arguments.iterator();
		var staticArg = staticArguments.iterator();
		
		StringBuilder str = new StringBuilder();
		
		LinkedList<Operation> operands = new LinkedList<>();
		this.operands = operands;
		
		for(int i = 0, length = patternStr.length(); i < length; i++) {
			char ch = patternStr.charAt(i);
			
			if(ch == '\1' || ch == '\2') {
				if(!str.isEmpty()) {
					operands.add(new StringConstOperation(context, str.toString()));
					str.setLength(0);
				}
				
				switch(ch) {
					case '\1': operands.add(arg.next()); break;
					case '\2': operands.add(staticArg.next()); break;
					default: throw new IllegalStateException("WTF??"); // 🙃️
				}
				
			} else {
				str.append(ch);
			}
		}
		
		if(!str.isEmpty())
			operands.add(new StringConstOperation(context, str.toString()));
	}
	
	
	public Operation getFirstOperand() {
		return operands.getFirst();
	}
	
	public void removeFirstOperand() {
		operands.removeFirst();
	}
	
	public void setCanOmitEmptyStringFunc(BooleanSupplier canOmitEmptyStringFunc) {
		this.canOmitEmptyStringFunc = canOmitEmptyStringFunc;
	}
	
	
	private void writeEmptyStringIfNecessary(StringifyOutputStream out) {
		if((operands.size() == 1 && !canOmitEmptyStringFunc.getAsBoolean() || operands.size() > 1 && !operands.get(1).getReturnType().equals(ClassType.STRING))
				&& !operands.getFirst().getReturnType().equals(ClassType.STRING)) {
			
			out.write("\"\" + ");
		}
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		writeEmptyStringIfNecessary(out);
		
		out.printAllUsingFunction(operands, operation -> out.printPrioritied(this, operation, context, Associativity.RIGHT), " + ");
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
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof ConcatStringsOperation operation &&
				super.equals(operation) && operands.equals(operation.operands);
	}
}
