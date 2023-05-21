package x590.jdecompiler.example;

@Example
public class EmptyEnumExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(EmptyEnumExample.class);
	}
	
	public enum ExampleEnum1 {}
	
	public enum ExampleEnum2 {
		;
		
		int f;
	}
	
	public enum ExampleEnum3 {
		;
		
		void foo() {}
	}
	
	public enum ExampleEnum4 {
		;
		
		enum Inner {}
	}
	
	public enum ExampleEnum5 {
		A, B;
		
		int f;
	}
	
	public enum ExampleEnum6 {
		A, B;
		
		void foo() {}
	}
	
	public enum ExampleEnum7 {
		A, B;
		
		static class Inner {}
	}
	
	public enum ExampleEnum8 {
		A, B
	}
}
