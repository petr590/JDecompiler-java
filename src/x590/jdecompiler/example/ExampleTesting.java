package x590.jdecompiler.example;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.main.Main;

public class ExampleTesting {
	
	public static final String
			DEFAULT_DIR = "bin",
			VANILLA_DIR = "/home/winch/0x590/java/jdk-8-rt";
	
	
	
	public static String getClassPath(String dir, Class<?> clazz) {
		return dir + "/" + clazz.getName().replace('.', '/') + ".class";
	}
	
	public static String getClassPath(Class<?> clazz) {
		return getClassPath(DEFAULT_DIR, clazz);
	}
	
	private static Function<Class<?>, String> classToClassPath(String dir) {
		return clazz -> getClassPath(dir, clazz);
	}
	
	
	public static void runDecompilerVanilla(Class<?> clazz) {
		runDecompiler(VANILLA_DIR, clazz);
	}
	
	public static void runDecompilerVanilla(Class<?>... classes) {
		runDecompiler(VANILLA_DIR, classes);
	}
	
	public static void runDecompilerVanilla(Class<?> clazz, String... otherArgs) {
		runDecompiler(VANILLA_DIR, clazz, otherArgs);
	}
	
	public static void runDecompilerVanilla(Class<?>[] classes, String... otherArgs) {
		runDecompiler(VANILLA_DIR, classes, otherArgs);
	}
	
	
	public static void runDecompiler(Class<?> clazz) {
		runDecompiler(DEFAULT_DIR, clazz);
	}
	
	public static void runDecompiler(Class<?>... classes) {
		runDecompiler(DEFAULT_DIR, classes);
	}
	
	public static void runDecompiler(Class<?> clazz, String... otherArgs) {
		runDecompiler(DEFAULT_DIR, clazz, otherArgs);
	}
	
	public static void runDecompiler(Class<?>[] classes, String... otherArgs) {
		runDecompiler(DEFAULT_DIR, classes, otherArgs);
	}
	
	public static void runDecompiler(String dir, Class<?> clazz) {
		runDecompiler(new String[] { getClassPath(dir, clazz) });
	}
	
	// class0 нужен для избежания неоднозначности при вызове перегруженного метода
	public static void runDecompiler(String dir, Class<?> class0, Class<?>... classes) {
		runDecompiler(
				Stream.concat(Stream.of(class0), Arrays.stream(classes))
						.map(classToClassPath(dir)).toArray(String[]::new)
		);
	}
	
	public static void runDecompiler(String dir, Class<?> clazz, String... otherArgs) {
		runDecompiler(
				Stream.concat(
					Stream.of(getClassPath(dir, clazz)),
					Arrays.stream(otherArgs)
				).toArray(String[]::new)
		);
	}
	
	public static void runDecompiler(String dir, Class<?>[] classes, String... otherArgs) {
		runDecompiler(
				Stream.concat(
					Arrays.stream(classes).map(clazz -> getClassPath(dir, clazz)),
					Arrays.stream(otherArgs)
				).toArray(String[]::new)
		);
	}
	
	public static void runDecompiler(String... args) {
		runDecompiler(true, args);
	}
	
	public static void runDecompiler(boolean isDebug, String... args) {
		
		JDecompiler.setDebug(isDebug);
		
		try {
			Main.main(args);
			
		} catch(Throwable ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
