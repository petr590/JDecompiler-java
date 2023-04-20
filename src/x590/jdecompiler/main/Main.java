package x590.jdecompiler.main;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import x590.jdecompiler.clazz.JavaClass;
import x590.jdecompiler.main.performing.Performing;

/**
 * Точка входа программы
 */
public final class Main {
	
	private Main() {}
	
	public static void main(String[] args) {
		
		JDecompiler.init(args);
		
		JDecompiler jdecompiler = JDecompiler.getInstance();
		
		List<JavaClass> classes = new ArrayList<>(jdecompiler.getFiles().size());
		
		Performing<?> performing = jdecompiler.getPerforming();
		
		for(String file : jdecompiler.getFiles()) {
			JavaClass clazz = performing.readSafe(file);
			if(clazz != null)
				classes.add(clazz);
		}
		
		try {
			performing.setup();
		} catch(IOException | UncheckedIOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		
		for(JavaClass clazz : classes) {
			
			if(clazz.canStringify()) {
				try {
					performing.perform(clazz);
					performing.write(clazz);
					
				} catch(Exception ex) {
					// Если исключение возникло при выводе файла в консоль,
					// надо, чтобы стектрейс начинался с новой строки.
					System.err.println();
					ex.printStackTrace();
				}
				
			} else {
				if(clazz.getModifiers().isSynthetic()) {
					System.out.println("Ignored " + clazz);
				}
			}
		}
		
//		for(var entry : ClassType.classTypes().entrySet())
//			System.out.printf("%-40s %s\n", entry.getKey(), entry.getValue());
//		
//		for(var classType : ClassType.allClassTypes())
//			System.out.println(classType);
		
		try {
			performing.close();
		} catch(IOException | UncheckedIOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
