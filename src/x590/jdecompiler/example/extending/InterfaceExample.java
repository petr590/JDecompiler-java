package x590.jdecompiler.example.extending;

import java.io.Serializable;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public interface InterfaceExample extends Serializable {
	
	int VALUE = 10;
	
	void interfaceMethod1();
	
	default void interfaceMethod2() {
		interfaceMethod3();
	}
	
	private void interfaceMethod3() {}
	
	static void main(String[] args) {
		ExampleTesting.runDecompiler(InterfaceExample.class, "--no-print-implicit-modifiers");
	}
}
