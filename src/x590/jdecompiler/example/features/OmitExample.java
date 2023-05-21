package x590.jdecompiler.example.features;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class OmitExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(OmitExample.class);
	}
	
	public boolean isEmpty() {
		return true;
	}
	
	// this должен быть опущен для всех вызовов, кроме equals
	@SuppressWarnings("unused")
	public void foo() {
		if(this.isEmpty() && !this.equals(null)) {
			Class<?> clazz = this.getClass();
		}
	}
}
