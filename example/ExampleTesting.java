package example;

import x590.jdecompiler.main.Main;

public class ExampleTesting {
	
	public static void runDecompiler(Class<?> clazz) {
		
		try {
			Main.main(new String[] { "bin/" + clazz.getCanonicalName().replace('.', '/') + ".class" });
			
		} catch(Throwable ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}