package x590.jdecompiler.example;

@Example
public class BridgeMethodExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(BridgeMethodExample.class, "-b");
	}
	
	public class Child extends Parent<Child.Inner> {
		
		@Override
		public String getState() {
			return null;
		}
		
		private static class Inner {}
	}
	
	public class Parent<T> {
		
		public Object getState() {
			return null;
		}
	}
}
