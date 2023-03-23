package x590.jdecompiler.example;

@Example
public interface SuperinterfaceExample {

	public default void foo() {}

	public default int bar() {
		return 1;
	}
}
