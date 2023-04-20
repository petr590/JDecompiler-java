package x590.jdecompiler.example.extending;

import x590.jdecompiler.example.Example;

@Example
public interface SuperinterfaceExample {

	public default void foo() {}

	public default int classMethod2() {
		return 1;
	}
}
