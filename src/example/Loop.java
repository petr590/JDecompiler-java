package example;

public class Loop {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Loop.class);
	}
	
	public static void doWhileLoop(int i) {
		System.out.println("before");
		
		do {
			System.out.println(i++);
		} while(i < 10);
		
		System.out.println("after");
	}
	
	public static void whileLoop(int i) {
		System.out.println("before");
		
		while(i < 10) {
			System.out.println(i++);
		}
		
		System.out.println("after");
	}
	
	public static void forLoop() {
		System.out.println("before");
		
		for(int i = 0; i < 10; i++) {
			System.out.println(i);
		}
		
		System.out.println("after");
	}
	
	public static void whileLoopWithAndCondition(int i, int j) {
		while(i < 10 && j > 10) {
			System.out.println(i++ + " " + j--);
		}
	}
	
	public static void whileLoopWithOrCondition(int i, int j) {
		while(i < 10 || j > 10) {
			System.out.println(i++ + " " + j--);
		}
	}
	
	public static void whileLoopWithDifficultCondition(int i, int j, int k) {
		while(i > -1 && i < 10 && (j != 0 || k != 0)) {
			System.out.println(i++ + " " + j--);
		}
	}
}