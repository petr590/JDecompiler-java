package example;

public class Methods extends Superclass {
	
	{
		foo();
		bar();
	}
	
	static {
		gg();
	}
	
	@Override
	public void foo() {
		super.foo();
	}
	
	@Override
	public void bar() {
		super.bar();
	}
	
	public static void gg() {}
}