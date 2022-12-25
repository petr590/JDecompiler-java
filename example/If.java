package example;

public class If {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(If.class);
	}
	
	public static void foo(Object obj) {
		System.out.println("Blablabla");
		
		if(obj == null)
			System.out.println("Null");
		
		System.out.println("Blablabla");
	}
}