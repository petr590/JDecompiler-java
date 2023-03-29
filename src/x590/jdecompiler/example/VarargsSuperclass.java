package x590.jdecompiler.example;

import java.lang.reflect.Method;

public class VarargsSuperclass {
	
	static void foo() {}
	
	static void foo(double arg1) {}
	
	static void foo(double arg1, double arg2) {}
	
	static void foo(double arg1, double arg2, double arg3) {}
	
	static void foo(double... args) {}
	
	static void foo2(String name, Method... methods) {}
	
	static void foo3(String name, Method[] methods) {}
}
