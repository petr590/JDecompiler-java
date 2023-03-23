package x590.jdecompiler.main;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import x590.jdecompiler.JavaClass;
import x590.jdecompiler.main.performing.Performing;

/**
 * Точка входа программы
 */
public final class Main {
	
	private Main() {}
	
	public static void main(String[] args) {
		
		JDecompiler.init(args);
		
		List<JavaClass> classes = new ArrayList<>(JDecompiler.getInstance().getFiles().size());
		
		Performing<?> performing = JDecompiler.getInstance().getPerforming();
		
		for(String file : JDecompiler.getInstance().getFiles()) {
			JavaClass clazz = performing.readSafe(file);
			if(clazz != null)
				classes.add(clazz);
		}
		
		try {
			performing.setup();
		} catch(IOException | UncheckedIOException ex) {
			ex.printStackTrace();
		}
		
		for(JavaClass clazz : classes) {
			
			if(clazz.canStringify()) {
				try {
					performing.perform(clazz);
					performing.write(clazz);
					
				} catch(Exception ex) {
					// Если исключение возникло при выводе файла в консоль,
					// надо, чтобы стектрейс начинался с новой строки.
					System.out.println();
					ex.printStackTrace();
				}
				
			} else {
				if(clazz.getModifiers().isSynthetic()) {
					System.out.println("Ignored synthetic class " + clazz);
				}
			}
		}
		
		try {
			performing.close();
		} catch(IOException | UncheckedIOException ex) {
			ex.printStackTrace();
		}
	}
}
