package x590.jdecompiler.example.generic;

import java.io.Serializable;
import java.util.List;

import x590.jdecompiler.example.ExampleTesting;

public abstract class Generic<T> extends SuperGeneric<T> implements Serializable, List<SuperGeneric<? extends T>> {
	
	private static final long serialVersionUID = 1L;
	
	public static <S extends CharSequence> void main(String[] args) {
		ExampleTesting.runDecompiler(Generic.class);
	}
	
	public List<?> field;
	
	@SafeVarargs
	public static <U extends Object & CharSequence & Serializable> U foo(List<? extends U> eu, List<? super U> su, U... u) {
		return null;
	}
}
