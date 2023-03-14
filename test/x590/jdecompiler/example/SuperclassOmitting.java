package x590.jdecompiler.example;

public class SuperclassOmitting extends Superclass implements Superinterface {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(SuperclassOmitting.class);
	}
	
	@Override
	public void foo() {
		Superinterface.super.foo();
	}
	
	@Override
	public void bar() {
		Superinterface.super.bar();
	}
}
