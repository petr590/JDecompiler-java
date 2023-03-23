package x590.jdecompiler.example;

@Example
public class InstanceofExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(InstanceofExample.class);
	}
	
	public void foo() {
		CharSequence seq = bar();
		System.out.println(seq instanceof String);
	}
	
	public CharSequence bar() {
		return "";
	}
}
