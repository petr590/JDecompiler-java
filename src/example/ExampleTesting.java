package example;

import java.util.Arrays;

import x590.jdecompiler.main.Main;

public class ExampleTesting {
	
	private static String getClassPath(Class<?> clazz) {
		return "bin/" + clazz.getCanonicalName().replace('.', '/') + ".class";
	}
	
	public static void runDecompiler(Class<?> clazz) {
		runDecompiler(new String[] { getClassPath(clazz) });
	}
	
	public static void runDecompiler(Class<?> clazz, String... otherArgs) {
		String[] args = new String[otherArgs.length + 1];
		Arrays.setAll(args, index -> index == 0 ? getClassPath(clazz) : otherArgs[index - 1]);
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