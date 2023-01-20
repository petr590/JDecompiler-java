package example;

public class Omit {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Omit.class);
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