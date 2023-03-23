package x590.jdecompiler.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Example
public class OuterExample {
	
	public static final Map<OuterExample, Inner> someMap = new HashMap<>();
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(OuterExample.class, Inner.class, Inner.Inner2.class);
	}
	
	public static class Inner {
		public static final int CONSTANT = -1;
		
		public static final List<OuterExample> someList = new ArrayList<>();
		
		public static class Inner2 {
			public static final int CONSTANT = -2;
		}
	}
}
