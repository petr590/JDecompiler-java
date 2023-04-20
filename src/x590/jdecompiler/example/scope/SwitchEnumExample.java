package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class SwitchEnumExample {
	
	private enum IConst {
		ICONST_M1,
		ICONST_0,
		ICONST_1,
		ICONST_2,
		ICONST_3,
		ICONST_4,
		ICONST_5,
		ICONST,
	}
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(
//				ExampleTesting.VANILLA_DIR,
				SwitchEnumExample.class
//				"vbin/x590/jdecompiler/example/scope/SwitchEnumExample$1.class",
//				"-A"
		);
	}
	
	public static void foo(IConst i) {
		switch(i) {
			case ICONST_M1 -> System.out.println("iconst_m1");
			case ICONST_0  -> System.out.println("iconst_0");
			case ICONST_1  -> System.out.println("iconst_1");
			case ICONST_2  -> System.out.println("iconst_2");
			case ICONST_3  -> System.out.println("iconst_3");
			case ICONST_4  -> System.out.println("iconst_4");
			case ICONST_5  -> System.out.println("iconst_5");
			case ICONST    -> System.out.println("iconst " + i);
		}
	}
	
	public static void bar(IConst i) {
		switch(i) {
			case ICONST_M1: System.out.println("iconst_m1"); break;
			case ICONST_0:  System.out.println("iconst_0"); break;
			case ICONST_1:  System.out.println("iconst_1"); break;
			case ICONST_2:  System.out.println("iconst_2"); break;
			case ICONST_3:  System.out.println("iconst_3"); break;
			case ICONST_4:  System.out.println("iconst_4"); break;
			case ICONST_5:  System.out.println("iconst_5"); break;
			case ICONST:    System.out.println("iconst " + i); break;
		}
		
		System.out.println("gg");
	}
	
	public static String baz(IConst i, IConst j) {
		switch(i) {
			case ICONST_M1 -> { return "iconst_m1"; }
			case ICONST_0  -> { return "iconst_0"; }
			case ICONST_1  -> { return "iconst_1"; }
			case ICONST_2  -> { return "iconst_2"; }
			case ICONST_3  -> { return "iconst_3"; }
			case ICONST_4  -> { return "iconst_4"; }
			case ICONST_5  -> { return "iconst_5"; }
			default        -> {
				switch(j) {
					case ICONST_0: return "iconst " + (0);
					case ICONST_1: return "iconst " + (1);
					default: return "iconst " + i;
				}
			}
		}
	}
}
