package example;

import java.util.Arrays;
import java.util.stream.Stream;

import x590.jdecompiler.main.Main;

public class ExampleTesting {
	
	public static final String DEFAULT_DIR = "bin";
	
	public static String getClassPath(String dir, Class<?> clazz) {
		return dir + "/" + clazz.getName().replace('.', '/') + ".class";
	}
	
	public static String getClassPath(Class<?> clazz) {
		return getClassPath(DEFAULT_DIR, clazz);
	}
	
	public static void runDecompiler(Class<?> clazz) {
		runDecompiler(DEFAULT_DIR, clazz);
	}
	
	public static void runDecompiler(Class<?>... classes) {
		runDecompiler(DEFAULT_DIR, classes);
	}
	
	public static void runDecompiler(Class<?>[] classes, String... otherArgs) {
		runDecompiler(DEFAULT_DIR, classes, otherArgs);
	}
	
	public static void runDecompiler(Class<?> clazz, String... otherArgs) {
		runDecompiler(DEFAULT_DIR, clazz, otherArgs);
	}
	
	public static void runDecompiler(String dir, Class<?> clazz) {
		runDecompiler(new String[] { getClassPath(dir, clazz) });
	}
	
	// class0 нужен для избежания неоднозначности в вызове метода
	public static void runDecompiler(String dir, Class<?> class0, Class<?>... classes) {
		runDecompiler(Stream.concat(Stream.of(class0), Arrays.stream(classes)).map(clazz -> getClassPath(dir, clazz)).toArray(String[]::new));
	}
	
	public static void runDecompiler(String dir, Class<?>[] classes, String... otherArgs) {
		int classesLength = classes.length;
		
		String[] args = new String[classesLength + otherArgs.length];
		Arrays.setAll(args, index -> index < classesLength ? getClassPath(dir, classes[index]) : otherArgs[index - classesLength]);
		runDecompiler(args);
	}
	
	public static void runDecompiler(String dir, Class<?> clazz, String... otherArgs) {
		String[] args = new String[otherArgs.length + 1];
		Arrays.setAll(args, index -> index == 0 ? getClassPath(dir, clazz) : otherArgs[index - 1]);
		runDecompiler(args);
	}
	
	public static void runDecompiler(String... args) {
		
		try {
			Main.main(args);
			
		} catch(Throwable ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}