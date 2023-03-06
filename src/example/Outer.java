package example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Outer {
	
	public static final Map<Outer, Inner> someMap = new HashMap<>();
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Outer.class, Inner.class, Inner.Inner2.class);
	}
	
	public static class Inner {
		public static final int CONSTANT = -1;
		
		public static final List<Outer> someList = new ArrayList<>();
		
		public static class Inner2 {
			public static final int CONSTANT = -2;
		}
	}
}
