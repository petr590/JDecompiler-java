package example;

import java.io.Serializable;
import java.util.List;

public abstract class Generic<T> extends SuperGeneric<T> implements Serializable, List<SuperGeneric<? extends T>> {
	
	private static final long serialVersionUID = 1L;
	
	public static <S extends String> void main(S[] args) {
		ExampleTesting.runDecompiler(Generic.class);
	}
	
	/** Конструктор с сигнатурой не должен считаться автосгенерированным */
	public Generic(T t) {}
	
	public Generic() {}
	
	public List<?> field;
	
	@SafeVarargs
	public static <U extends String> U foo(List<? extends U> eu, List<? super U> su, U... u) {
		return null;
	}
}
