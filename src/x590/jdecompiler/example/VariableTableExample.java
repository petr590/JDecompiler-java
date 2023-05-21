package x590.jdecompiler.example;

@Example
public class VariableTableExample {
	
	public static void main(String[] arguments) {
		ExampleTesting.runDecompiler(VariableTableExample.class);
	}
	
	public <T> T genericExample(T value) {
		return value;
	}
	
	public void example1() {
		int x = 5;
		System.out.println(x);
	}
}
