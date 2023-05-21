package x590.jdecompiler.example;

public class AgarIoTaskExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(
				"/home/winch/eclipse-workspace/Olymp/bin/olymp/test/AgarIoTask.class"
//				AgarIoTaskExample.class
		);
	}
	
	// Fixed
	public static int test() {
		@SuppressWarnings("unused")
		int a, b, c, d;
		return d = c = b = a = 0;
	}
	
	public static void test2() {
		for(int a = 10, b = 20; a != b; a++) {
			for(int j = 0; j < 100; j++) {}
		}
	}
}
