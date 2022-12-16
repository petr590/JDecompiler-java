package example;

public class OverrideTest extends Superclass {
	
	@Deprecated(since = "1.0")
	public int x;
	@Deprecated(since = "1.0")
	public int y;
	@Deprecated(since = "1.0", forRemoval = true)
	public int z;
	
	@Override
	public void foo() {
		super.foo();
	}
	
	@Override
	public void bar() {
		super.bar();
	}
}