package example;

public class IfElse {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(IfElse.class);
	}
	
	public static void foo(Object obj) {
		System.out.println("Blablabla");
		
		if(obj == null)
			System.out.println("Null");
		else
			System.out.println("Not null");
		
		System.out.println("Blablabla");
	}
	
	public static void bar(int x) {
		if(x == 10) {
			if(x == 20) {
				if(x == 30) {
					if(x == 40) {
						System.out.print("1");
					}
				}
			}
			
		} else {
			System.out.print("0");
		}
	}
}
