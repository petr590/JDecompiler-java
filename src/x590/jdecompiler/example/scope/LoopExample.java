package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example(directory = ExampleTesting.VANILLA_DIR)
public class LoopExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ExampleTesting.VANILLA_DIR, LoopExample.class);
	}
	
	public static void doWhileLoop(int i) {
		do {
			System.out.println(i++);
		} while(i < 10);
	}
	
	public static void infiniteLoop(int i) {
		while(true) {
			System.out.println(i);
		}
	}
	
	public static void emptyInfiniteLoop(int i) {
		while(true);
	}
	
	public static void whileLoop(int i) {
		while(i < 10) {
			System.out.println(i++);
		}
	}
	
	public static void forLoop1() {
		for(int i = 0; i < 10; i++) {
			System.out.println(i);
		}
	}
	
	public static void forLoop2() {
		int i = 0;
		
		for(float j = 1; i < 18; i++, j++) {
			System.out.println(i + j);
		}
	}
	
	public static void forLoop3(int i, double j) {
		for(i = 0, j = 10; i < 10; i++, j++) {
			System.out.println(i + j);
		}
	}
	
	public static void forLoop4() {
		for(int i = 0, j = 1;;) {
			System.out.println(i + j);
		}
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
	
	public static void whileLoopWithDifficultCondition1(int i, int j, int k) {
		while(i > -1 && i < 10 && (j != 0 || k != 0)) {
			System.out.println(i++ + " " + j--);
		}
	}
	
	public static void whileLoopWithDifficultCondition2(int i, int j, int k) {
		// Всё, что здесь написано, не имеет смысла, оно нужно просто для тестирования
		while(i > -1 && i < 10 && (j != 0 || k != 0 || (i == 999 && j < 0)) && (j == -1 || k != 47)) {
			System.out.println(i++ + " " + j--);
		}
	}
	
	public static void whileLoopWithBreak(int i, int j, int k) {
		while(j != 0) {
			if(i <= -1)
				break;
			
			System.out.println(i++ + " " + j--);
		}
	}
	
	public static void whileLoopWithContinue(int i, int j, int k) {
		while(j != 0) {
			if(i <= -1)
				continue;
			
			if(j <= 0)
				continue;
			
			if(k <= 1)
				continue;
			
			System.out.println(i++ + " " + j--);
		}
	}
}
