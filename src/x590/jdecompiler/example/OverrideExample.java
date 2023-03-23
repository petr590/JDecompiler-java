package x590.jdecompiler.example;

@Example
public abstract class OverrideExample extends SuperclassExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(OverrideExample.class, "-x=never");
	}
	
	@Override
	public abstract void foo();
	
	@Override
	public abstract int bar();
	
	@Override
	public String toString() {
		return "OverrideExample";
	}
	
	@Override
	public int hashCode() {
		return 0x10;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof OverrideExample;
	}
}
