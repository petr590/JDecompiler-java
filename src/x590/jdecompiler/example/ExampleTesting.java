package x590.jdecompiler.example;

import static java.io.File.separatorChar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import x590.jdecompiler.clazz.JavaClass;
import x590.jdecompiler.FileSource;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.Config;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.main.Main;
import x590.jdecompiler.main.performing.AbstractPerforming.PerformingType;
import x590.util.Logger;

public class ExampleTesting {
	
	public static final String
			// IntelliJ IDEA
			DEFAULT_DIR = "out/production/jdecompiler",
			VANILLA_DIR = "out/vanilla-example/jdecompiler";

			// Eclipse IDE
//			DEFAULT_DIR = "bin",
//			VANILLA_DIR = "vbin";
	
	private static final String[] EMPTY_ARGS = {};

	
	public static String getClassPath(Class<?> clazz) {
		return getClassPath(DEFAULT_DIR, clazz);
	}
	
	public static String getClassPath(String className) {
		return getClassPath(DEFAULT_DIR, className);
	}
	
	public static String getClassPath(String dir, Class<?> clazz) {
		return getClassPath(dir, clazz.getName());
	}
	
	public static String getClassPath(String dir, String className) {
		return dir + separatorChar + className.replace('.', separatorChar) + ".class";
	}
	
	private static Function<Class<?>, String> classToClassPath(String dir) {
		return clazz -> getClassPath(dir, clazz);
	}
	
	
	private static Example getExampleAnnotation(Class<?> clazz) {
		Example exampleAnnotation = clazz.getDeclaredAnnotation(Example.class);
		
		if(exampleAnnotation == null) {
			throw new IllegalArgumentException("Class " + clazz.getCanonicalName() + " is not annotated with @Example");
		}
		
		return exampleAnnotation;
	}
	
	public static void runDecompilerForExampleClass(Class<?> clazz) {
		Example exampleAnnotation = getExampleAnnotation(clazz);
		
		runDecompiler(exampleAnnotation.directory(), exampleAnnotation.classes(), exampleAnnotation.args());
	}
	
	public static void runDecompilerForExampleClasses(Class<?>... classes) {
		
		List<String> args = new ArrayList<>(classes.length);
		
		Logger.debug("classes: (" + classes.length + ") " + Arrays.toString(classes));
		
		int skipped = 0;
		
		for(Class<?> clazz : classes) {
			Example exampleAnnotation = clazz.getDeclaredAnnotation(Example.class);
			
			if(exampleAnnotation != null) {
				
				if(exampleAnnotation.source() == FileSource.JDK) {
					skipped += 1;
					Logger.debug("Class " + clazz.getName() + " provides JDK example");
					continue;
//					args.add("-jdk");
				}
				
				String dir = exampleAnnotation.directory();
				
				Class<?>[] classesToDecompile = exampleAnnotation.classes();
				
				
				if(classesToDecompile.length == 0) {
					args.add(getClassPath(dir, clazz));
					
				} else {
					for(Class<?> decompilingClass : classesToDecompile) {
						args.add(getClassPath(dir, decompilingClass));
					}
				}
				
				args.addAll(Arrays.asList(exampleAnnotation.args()));
				
			} else {
				skipped++;
				Logger.debug("Class " + clazz.getName() + " has no @Example annotation");
			}
		}
		
		Logger.debug(skipped + " classes skipped");
		Logger.debug("args: (" + args.size() + ") " + args);
		
		runDecompiler(args.toArray(String[]::new));
	}
	
	
	public static void runDecompilerForJdk(Class<?> clazz) {
		runDecompilerForJdk(new Class[] { clazz }, EMPTY_ARGS);
	}
	
	public static void runDecompilerForJdk(Class<?>... classes) {
		runDecompilerForJdk(classes, EMPTY_ARGS);
	}
	
	public static void runDecompilerForJdk(Class<?> clazz, String... otherArgs) {
		runDecompilerForJdk(new Class[] { clazz }, otherArgs);
	}
	
	public static void runDecompilerForJdk(Class<?>[] classes, String... otherArgs) {
//		JDecompiler.init(PerformingType.DECOMPILE, Config.newBuilder().withArguments(otherArgs).build());
//		JDecompiler.setDebug(true);
//
//		List<JavaClass> javaClasses = new ArrayList<>(classes.length);
//
//		for(Class<?> clazz : classes) {
//			URL resource = clazz.getResource(getClassSimpleName(clazz) + ".class");
//
//			if(resource != null) {
//
//				try(InputStream in = resource.openStream()) {
//					javaClasses.add(JavaClass.read(in));
//
//				} catch(IOException | DisassemblingException | DecompilationException ex) {
//					ex.printStackTrace();
//				}
//
//			} else {
//				System.err.println("Cannot find resource for class " + clazz.getName());
//			}
//		}
//
//		var out = new StringifyOutputStream(System.out);
//
//		for(JavaClass javaClass : javaClasses) {
//			javaClass.decompile();
//		}
//
//		for(JavaClass javaClass : javaClasses) {
//			javaClass.afterDecompilation();
//			javaClass.resolveImports();
//
//			if(javaClass.canStringify()) {
//				out.printsp("Stringifying").println(javaClass.getThisType().getName());
//				javaClass.writeTo(out);
//			} else {
//				out.printsp("Cannot stringify").println(javaClass.getThisType().getName());
//			}
//		}
//
//		out.flush();
		
		runDecompiler(
				Stream.concat(
					Stream.of("-jdk"),
					Stream.concat(
						Arrays.stream(classes).map(Class::getName),
						Arrays.stream(otherArgs)
					)
				).toArray(String[]::new)
		);
	}
	
	
	private static String getClassSimpleName(Class<?> clazz) {
		String simpleName = clazz.getSimpleName();
		
		if(!simpleName.isEmpty())
			return simpleName;
		
		String name = clazz.getName();
		String packageName = clazz.getPackageName();
		return name.startsWith(packageName) ? name.substring(packageName.length() + 1) : name;
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
		runDecompiler(getClassPath(dir, clazz));
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
