package x590.jdecompiler.example;

@Example
public class FieldAccessExample extends SuperclassExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(FieldAccessExample.class);
	}
	
	public boolean getFlag() {
		return flag;
	}
}
