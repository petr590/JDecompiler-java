package x590.jdecompiler.scope;

import java.util.List;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.constpool.IntegerConstant;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.operation.BreakOperation;
import x590.jdecompiler.operation.Operation;
import x590.util.annotation.Nullable;

public class CaseScope extends Scope {
	
	private final List<IntegerConstant> cases;
	private @Nullable List<String> enumCases;
	private final boolean useDefaultCase, isLast;
	private boolean canUseNewSwitch;
	private final BreakOperation breakOperation;
	
	public CaseScope(int startIndex, int endIndex, Scope superScope, List<IntegerConstant> cases, boolean useDefaultCase, boolean isLast, List<Operation> operations) {
		super(startIndex, endIndex, superScope);
		
		this.cases = cases;
		this.useDefaultCase = useDefaultCase;
		this.isLast = isLast;
		
		addOperations(operations, startIndex);
		
		this.breakOperation =
				getLastOperation() instanceof BreakOperation breakOperation &&
				breakOperation.getScope() == superScope ?
						breakOperation : null;
	}
	
	boolean usesDefaultCase() {
		return useDefaultCase;
	}
	
	void useNewSwitch() {
		canUseNewSwitch = true;
		
		var breakOperation = this.breakOperation;
		
		if(breakOperation != null)
			breakOperation.remove();
	}
	
	boolean canUseNewSwitch() {
		return  (!useDefaultCase || cases.isEmpty()) &&
				(isLast || breakOperation != null || this.isTerminable());
	}
	
	
	@Override
	public void setEnumTable(@Nullable Int2ObjectMap<String> enumTable) {
		this.enumCases = cases.stream()
				.map(caseValue -> enumTable.computeIfAbsent(caseValue.getValue(),
						key -> "/* Enum constant at index " + key + " in index table not recognized */"))
				.toList();
	}
	
	
	@Override
	public void writeHeader(StringifyOutputStream out, StringifyContext context) {
		
		var cases = this.cases;
		
		if(canUseNewSwitch) {
			if(!cases.isEmpty()) {
				out.write("case ");
				writeCases(out, context);
			}
			
			if(useDefaultCase) {
				out.write("default");
			}
			
			out.write(" ->");
			
		} else {
			
			if(!cases.isEmpty()) {
				out.write("case ");
				writeCases(out, context);
				
				if(useDefaultCase) {
					out.write(": ");
				}
			}
			
			if(useDefaultCase) {
				out.write("default");
			}
			
			out.write(':');
		}
	}
	
	private void writeCases(StringifyOutputStream out, StringifyContext context) {
		if(enumCases != null) {
			out.printAll(enumCases, ", ");
		} else {
			out.printAll(cases, context.getClassinfo(), ", ");
		}
	}
	
	@Override
	protected boolean canOmitCurlyBrackets() {
		if(JDecompiler.getConfig().canOmitCurlyBrackets()) {
			
			if(!canUseNewSwitch)
				return true;
			
			if(getOperationsCount() == 1) {
				var operation = getOperationAt(0);
				return !operation.isScope() && !operation.isTerminable();
			}
		}
		
		return false;
	}
}
