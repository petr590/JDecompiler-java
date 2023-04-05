package x590.jdecompiler.example;

import java.io.Serializable;

@Example
public record RecordExample<T>(int x, int y, T obj) implements Serializable {

	public static void main(String[] args) {
		ExampleTesting.runDecompiler(RecordExample.class, "-A");
	}
	
	public RecordExample(int x, int y, T obj) {
		this.x = x;
		this.y = x;
		this.obj = obj;
	}
	
	public RecordExample(float x, float y, T obj) {
		this((int)x, (int)y, obj);
	}
	
	public int x() {
		return x - 1;
	}
}
