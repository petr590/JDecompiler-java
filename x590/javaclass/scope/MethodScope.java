package x590.javaclass.scope;

import x590.util.function.LazyLoadingValue;

public class MethodScope extends Scope {
	
	private static final LazyLoadingValue<MethodScope> EMPTY_VALUE = new LazyLoadingValue<>(() -> new MethodScope(0));
	
	public MethodScope(int codeLength) {
		super(0, codeLength);
	}
	
	
	@Override
	protected boolean canOmitBody() {
		return false;
	}
	
	
	public static MethodScope empty() {
		return EMPTY_VALUE.get();
	}
}