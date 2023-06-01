package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;
import x590.util.Logger;

@Example
public class AssertExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(AssertExample.class);
	}

	// Выдаёт ошибку компиляции в некоторых компиляторах
//	public static boolean $assertionsDisabled, $assertionsDisabled_0, $assertionsDisabled_1;
	
	public void test(Object obj) {
		
//		if(!$assertionsDisabled && obj == null) {
//			throw new AssertionError();
//		}
		
		assert obj != null;
		assert obj.getClass() == Object.class : "Not pure Object";
		assert false : false;
	}
}
