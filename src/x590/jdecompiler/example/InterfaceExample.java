package x590.jdecompiler.example;

import java.io.Serializable;

@Example
public interface InterfaceExample extends Serializable {
	
	void foo1();
	
	default void foo2() {
		foo3();
	}
	
	private void foo3() {}
	
	static void main(String[] args) {
		ExampleTesting.runDecompiler(InterfaceExample.class, "--no-print-implicit-modifiers");
	}
}
