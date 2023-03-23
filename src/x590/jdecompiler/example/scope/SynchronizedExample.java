package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;

@Example
public class SynchronizedExample {
	
	public static void main(String... args) {
		
		int i = 10;
		
		synchronized(System.out) {
			i += 20;
		}
		
		System.out.println(i);
	}
}
