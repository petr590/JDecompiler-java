package x590.jdecompiler.example;

@Example
public abstract class OverrideExample extends SuperclassExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(new Class[] { SuperclassOfSuperclassExample.class, OverrideExample.class }, "-x=never");
	}
	
	public OverrideExample(int x) {}
	
	static {
		System.out.println();
	}
	
	@Override
	public abstract void foo(int x);
	
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
