package x590.jdecompiler.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import x590.jdecompiler.JavaClass;
import x590.jdecompiler.example.ExampleTesting;
import x590.jdecompiler.example.scope.TryCatch;
import x590.jdecompiler.main.performing.Performing;

/**
 * Точка входа программы
 */
public final class Main {
	
	private Main() {}
	
	public static Set<Class<?>> findAllClassesInPackage(String packageName) {
		return findClassesAsStreamInPackage(packageName).collect(Collectors.toSet());
	}
	
	public static Set<String> findAllClassNamesInPackage(String packageName) {
		return findClassNamesAsStreamInPackage(packageName).collect(Collectors.toSet());
	}
	
	public static Stream<Class<?>> findClassesAsStreamInPackage(String packageName) {
		return findClassNamesAsStreamInPackage(packageName).map(Main::findClass);
	}
	
	public static Stream<String> findClassNamesAsStreamInPackage(String packageName) {
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replace('.', '/'));
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		Map<Boolean, List<String>> partitioned = reader.lines()
				.collect(Collectors.partitioningBy(line -> line.endsWith(".class")));
		
		return partitioned.get(Boolean.FALSE).stream()
				.map(subPackageName -> findClassNamesAsStreamInPackage(packageName + '.' + subPackageName))
				.reduce(partitioned.get(Boolean.TRUE).stream().map(className -> packageName + '.' + className.substring(0, className.lastIndexOf('.'))),
						Stream::concat);
	}
	
	private static Class<?> findClass(String name) {
		try {
			return Class.forName(name);
		} catch(ClassNotFoundException ex) {
			System.err.println("Failed to load class \"" + name + "\"");
		}
		
		return null;
	}
	
	
	public static void main(String[] args) {
		
		if(args.length > 0) {
			JDecompiler.init(args);
		} else {
//			JDecompiler.init(findClassNamesAsStreamInPackage("example")
//					.map(className -> "bin/" + className.replace('.', '/') + ".class").toArray(String[]::new));
			
			ExampleTesting.runDecompiler(TryCatch.class, "-oc" /* Вывод в консоль, а не в файл */);
//			ExampleTesting.runDecompiler(ElseIf.class, "-oc");
			
//			ExampleTesting.runDecompiler("-a", "Test.asm");
			
			return;
		}
		
		
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
