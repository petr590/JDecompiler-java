package x590.jdecompiler.example;

@Example
public class OmitExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(OmitExample.class);
	}
	
	public boolean isEmpty() {
		return true;
	}
	
	@SuppressWarnings("unused")
	public void foo() {
		if(this.isEmpty() && !this.equals(null)) {
			Class<?> clazz = this.getClass();
		}
	}
}
