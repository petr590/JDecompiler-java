package x590.jdecompiler.example;

public class SuperclassOmitting extends Superclass {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(SuperclassOmitting.class);
	}
	
	@Override
	public void foo() {
		super.foo();
	}
	
	@Override
	public void bar() {
		super.bar();
	}
}
