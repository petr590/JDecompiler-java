package example;

@SuppressWarnings("unused")
public class Instanceof {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Instanceof.class);
	}
	
	static CharSequence readAll() { return ""; }
	
	static void foo() {
		CharSequence seq;
		bar(-1, (seq = readAll()) instanceof String);
	}
	
	static void bar(int i, boolean b) {}
}