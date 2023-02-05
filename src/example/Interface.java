package example;

import java.io.Serializable;

public interface Interface extends Serializable {
	
	void foo1();
	
	default void foo2() {
		foo3();
	}
	
	private void foo3() {}
	
	static void main(String[] args) {
		ExampleTesting.runDecompiler(Interface.class, "--no-print-implicit-modifiers");
	}
}