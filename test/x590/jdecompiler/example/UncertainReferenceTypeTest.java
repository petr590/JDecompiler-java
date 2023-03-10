package x590.jdecompiler.example;

import java.util.ArrayList;
import java.util.List;

public class UncertainReferenceTypeTest {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(UncertainReferenceTypeTest.class);
	}
	
	public static void foo() {
		List<Object> l1 = new ArrayList<>();
		l1.add(l1);
	}
}