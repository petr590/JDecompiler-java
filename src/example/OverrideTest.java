package example;

public abstract class OverrideTest extends Superclass {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(OverrideTest.class, "-x=never");
	}
	
	@Override
	public abstract void foo();
	
	@Override
	public abstract void bar();
	
	@Override
	public String toString() {
		return "OverrideTest";
	}
	
	@Override
	public int hashCode() {
		return 0x10;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof OverrideTest;
	}
}