package x590.jdecompiler.example.other;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

import java.io.Serializable;

@Example
public record RecordExample<T>(int x, int y, T obj) implements Serializable {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(RecordExample.class, "-A");
	}
	
	public RecordExample(int x, int y, T obj) {
		this.x = x;
		this.y = -1;
		this.obj = obj;
	}
	
	public RecordExample(T obj) {
		this(0, 0, obj);
	}
	
	public int x() {
		return x;
	}
	
	int foo() {
		return x - 1;
	}
}
