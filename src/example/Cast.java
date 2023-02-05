package example;

@SuppressWarnings("unused")
public class Cast {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Cast.class);
	}
	
	public static void foo() {
		Object obj = null;
		String str = (String)(CharSequence)obj;
	}
}