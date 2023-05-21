package x590.jdecompiler.example.generic;

import java.io.Serializable;
import java.util.List;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public abstract class GenericExample<T> extends SuperGenericExample<T>
		implements Serializable, List<SuperGenericExample<? extends T>> {
	
	private static final long serialVersionUID = 1L;
	
	public static <S extends CharSequence> void main(String[] args) {
		ExampleTesting.runDecompiler(GenericExample.class);
	}
	
	public List<?> field;
	
	@SafeVarargs
	public static <U extends Object & CharSequence & Serializable> U foo(List<? extends U> eu, List<? super U> su, U... u) {
		return eu.get(0);
	}
	
	
	public static abstract class StringGenericExample extends GenericExample<String> {
		private static final long serialVersionUID = 1L;
	}
}
