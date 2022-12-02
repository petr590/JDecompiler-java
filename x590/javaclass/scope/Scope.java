package x590.javaclass.scope;

import java.util.ArrayList;
import java.util.List;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;

public class Scope extends Operation {
	
	private List<Operation> code = new ArrayList<>();
	private List<Scope> scopes = new ArrayList<>();
	
	public final int startIndex, endIndex;
	
	
	public Scope(int startIndex, int endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	public Scope(DecompilationContext context, int endIndex) {
		this.startIndex = context.getIndex();
		this.endIndex = endIndex;
	}
	
	
	public boolean isEmpty() {
		return getCode().isEmpty();
	}
	
	public List<Operation> getCode() {
		return code;
	}
	
	public Operation getOperation(int index) {
		return getCode().get(startIndex + index);
	}
	
	public void addOperation(Operation operation) {
		getCode().add(operation);
	}
	
	
	public void addScope(Scope scope) {
		scopes.add(scope);
	}
	
	
	public void deleteRemovedOperations() {
		code = code.stream().filter(operation -> !operation.isRemoved() && !operation.canOmit()).toList();
		scopes.forEach(Scope::deleteRemovedOperations);
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		writeHeader(out, context);
		
		if(getCode().isEmpty()) {
			if(canOmitBody())
				out.write(';');
			else
				out.write(" {}");
				
		} else {
			out.print(" {").increaseIndent();
			code.forEach(operation -> out.println().printIndent().print(operation, context).print(operation.getBackSeparator(context)));
			out.reduceIndent().println().printIndent().print('}');
		}
	}
	
	/** 
	 * Можно ли записать точку с запятой вместо тела скопа когда он пустой
	 */
	protected boolean canOmitBody() {
		return true;
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
}