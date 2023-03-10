package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.ExampleTesting;

public class Switch {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Switch.class);
	}
	
	public static void foo(int i) {
		switch(i) {
			case -1 -> System.out.println("iconst_m1");
			case  0 -> System.out.println("iconst_0");
			case  1 -> System.out.println("iconst_1");
			case  2 -> System.out.println("iconst_2");
			case  3 -> System.out.println("iconst_3");
			case  4 -> System.out.println("iconst_4");
			case  5, 6 -> System.out.println("iconst_5");
			default	-> System.out.println("iconst " + i);
			case 99 -> System.out.println("iconst_99");
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
			case  5: case 6: default: System.out.println("iconst_5"); break;
			case 99: System.out.println("iconst_99"); break;
//			default: System.out.println("iconst " + i); break;
		}
	}
}