package x590.jdecompiler.example.nested;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
@SuppressWarnings("unused")
public class NestMembersAccessExample {
	
	private int nonstaticField;
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ExampleTesting.VANILLA_DIR, NestMembersAccessExample.class
//				, "-s"
		);
	}
	
	
	public static int getNonstaticInnerField(StaticClass inner) {
		return inner.nonstaticField;
	}
	
	public static void setNonstaticInnerField(StaticClass inner, int x) {
		inner.nonstaticField = x;
	}

	public static void incNonstaticInnerField(StaticClass inner, int x) {
		inner.nonstaticField++;
	}

	public static int getStaticInnerField() {
		return StaticClass.staticField;
	}

	public static void setNonstaticInnerField(int x) {
		StaticClass.staticField = x;
	}


	public static class StaticClass {

		private static int staticField;
		private int nonstaticField;
		
		public static int getNonstaticOuterField(NestMembersAccessExample outer) {
			return outer.nonstaticField;
		}
		
		public static void setNonstaticOuterField(NestMembersAccessExample outer, int x) {
			outer.nonstaticField = x;
		}
	}
	
	public class NonstaticClass {
		
		public int getNonstaticOuterField() {
			return nonstaticField;
		}
		
		public int setNonstaticOuterField(int x) {
			return NestMembersAccessExample.this.nonstaticField = x;
		}

		public void incNonstaticOuterField(int x) {
			NestMembersAccessExample.this.nonstaticField += x;
		}
	}
}
