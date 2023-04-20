package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class SwitchExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(SwitchExample.class);
	}
	
	public static void foo(int i) {
		switch(i) {
			case -1 -> System.out.println("iconst_m1");
			case  0 -> System.out.println("iconst_0");
			case  1 -> System.out.println("iconst_1");
			case  2 -> System.out.println("iconst_2");
			case  3 -> System.out.println("iconst_3");
			case  4 -> System.out.println("iconst_4");
			case  5 -> System.out.println("iconst_5");
			default	-> System.out.println("iconst " + i);
		}
	}
	
	public static void bar(int i) {
		switch(i) {
			case -1: System.out.println("iconst_m1"); break;
			case  0: System.out.println("iconst_0"); break;
			case  1: System.out.println("iconst_1"); break;
			case  2: System.out.println("iconst_2"); break;
			case  3: System.out.println("iconst_3"); break;
			case  4: System.out.println("iconst_4"); break;
			case  5: System.out.println("iconst_5"); break;
			default: System.out.println("iconst " + i); break;
		}
	}
	
	public static String baz(int i, int j) {
		switch(i) {
			case -1 -> { return "iconst_m1"; }
			case  0 -> { return "iconst_0"; }
			case  1 -> { return "iconst_1"; }
			case  2 -> { return "iconst_2"; }
			case  3 -> { return "iconst_3"; }
			case  4 -> { return "iconst_4"; }
			case  5 -> { return "iconst_5"; }
			default -> {
				switch(j) {
					case 0: return "iconst " + (0);
					case 1: return "iconst " + (1);
					default: return "iconst " + i;
				}
			}
		}
	}
	
	public static void switchInLoopWithBreak(int i, int j, int k) {
		while(j != 0) {
			switch(j) {
				case 0:
					if(i <= -1)
						break;
			}
			
			System.out.println(i++ + " " + j--);
		}
	}
}
