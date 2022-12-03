package x590.javaclass.scope;

import java.util.ArrayList;
import java.util.List;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.jdecompiler.JDecompiler;

public class Scope extends Operation {
	
	private List<Operation> code = new ArrayList<>();
	private List<Scope> scopes = new ArrayList<>();
	
	public final int startIndex, endIndex;
	
	
	public Scope(int startIndex, int endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	public Scope(DecompilationContext context, int endIndex) {
		this.startIndex = context.currentIndex();
		this.endIndex = endIndex;
	}
	
	
	public boolean isEmpty() {
		return code.isEmpty();
	}
	
	public List<Operation> getCode() {
		return code;
	}
	
	public Operation getOperation(int index) {
		return code.get(startIndex + index);
	}
	
	public void addOperation(DecompilationContext context, Operation operation) {
		code.add(operation);
		if(operation instanceof Scope)
			scopes.add((Scope)operation);
	}
	
	
	public void deleteRemovedOperations() {
		code = code.stream().filter(operation -> !operation.isRemoved() && !operation.canOmit()).toList();
		scopes.forEach(Scope::deleteRemovedOperations);
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		writeHeader(out, context);
		
		if(code.isEmpty()) {
			if(canOmitCurlyBrackets())
				out.write(';');
			else
				out.write(" {}");
				
		} else {
			boolean canOmitCurlyBraces = canOmitCurlyBrackets();
			
			if(!canOmitCurlyBraces)
				out.write(" {");
			
			out.increaseIndent();
			code.forEach(operation -> operation.printFront(out, context).print(operation, context).print(operation.getBackSeparator(context)));
			out.reduceIndent();
			
			if(!canOmitCurlyBraces)
				out.println().printIndent().print('}');
		}
	}
	
	/** 
	 * Можно ли записать точку с запятой вместо фигурных скобок
	 * когда скоп пустой или в нём только одна операция
	 */
	protected boolean canOmitCurlyBrackets() {
		return code.size() <= 1 && JDecompiler.getInstance().canOmitCurlyBrackets();
	}
	
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {}
	
	@Override
	public String getBackSeparator(StringifyContext context) {
		return "";
	}
	
	
	@Override
	public Type getReturnType() {
		return PrimitiveType.VOID;
	}
	
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " {" + startIndex + ", " + endIndex + "}";
	}
}