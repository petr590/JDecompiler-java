package example;

import java.util.Arrays;

import x590.jdecompiler.main.Main;

public class ExampleTesting {
	
	private static String getClassPath(String dir, Class<?> clazz) {
		return dir + "/" + clazz.getCanonicalName().replace('.', '/') + ".class";
	}
	
	public static void runDecompiler(Class<?> clazz) {
		runDecompiler("bin", clazz);
	}
	
	public static void runDecompiler(String dir, Class<?> clazz) {
		runDecompiler(new String[] { getClassPath(dir, clazz) });
	}
	
	public static void runDecompiler(Class<?> clazz, String... otherArgs) {
		runDecompiler("bin", clazz, otherArgs);
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