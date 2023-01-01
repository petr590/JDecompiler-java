package example;

import java.io.Serializable;
import java.util.List;

public abstract class Generic<T, S extends T> extends SuperGeneric<S> implements Serializable, List<T> {
	
	private static final long serialVersionUID = 1L;
	
	public static <V> void main(String[] args) {
		ExampleTesting.runDecompiler(Generic.class);
	}
	
	public <U extends S> void foo(List<? extends U> eu, List<? super U> su) {}
}