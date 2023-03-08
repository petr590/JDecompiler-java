package x590.jdecompiler.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import x590.jdecompiler.JavaClass;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.util.Logger;
import x590.util.Timer;

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
	
	
	public static void main(String[] args) throws IOException {
		
		if(args.length > 0) {
			JDecompiler.init(args);
		} else {
			JDecompiler.init(findClassNamesAsStreamInPackage("example")
					.map(className -> "bin/" + className.replace('.', '/') + ".class").toArray(String[]::new));
			
//			JDecompiler.init(new String[] {
//					"bin/example/Example2.class",
					
//					"bin/example/annotation/TestAnnotation.class",
//					"bin/example/annotation/InvisibleAnnotation.class",
					
//					"bin/example/If.class",
//					"bin/example/Else.class",
//					"bin/example/Synchronized.class",
//					"bin/example/Annotations.class",
//					"bin/example/Cast.class",
//					"bin/example/Charset.class",
					
//					"bin/example/Increment1.class",
//					"bin/example/Increment2.class",
//					"bin/example/StaticFieldsIncrement.class",
//					"bin/example/NonStaticFieldsIncrement.class",
					
//					"bin/example/LoopIncrement.class",
//					"bin/example/StaticFieldsLoopIncrement.class",
//					"bin/example/NonStaticFieldsLoopIncrement.class",
					
//					"bin/example/Methods.class",
//					"bin/example/MultiDeclaration.class",
//					"bin/example/OverrideTest.class",
//					"bin/example/Interface.class",
//					"bin/example/Enum.class",
//					"bin/example/Arrays.class",
//					"bin/example/Constants.class",
//					"bin/example/Autoboxing.class",
//					"bin/example/Autounboxing.class",
//					"bin/example/TernaryOperator.class",
//					"bin/example/Throws.class",

//					"vbin/example/Enum.class",
//					"vbin/example/ConcatStringsInvokedynamic.class",
//					"/home/winch/0x590/java/jdk-8-rt/java/lang/Object.class",
					
//					"bin/x590/jdecompiler/Importable.class",
//					"bin/x590/jdecompiler/Version.class",
					
//					"/home/winch/eclipse-workspace/Util/bin/x590/util/Pair.class"
					
//					"--no-omit-curly-brackets",
//					"--no-omit-this-class",
//					"--no-brackets-around-bitwise-operands",
					
//					"bin/x590/jdecompiler/modifiers/Modifiers.class",
//					"bin/module-info.class",
//					"bin/example/package-info.class", "--omit-single-import",
//					"bin/module-info.class"
//					"bin/example/EmptyClass.class",
//			});
		}
		
		
		List<JavaClass> classes = new ArrayList<>(JDecompiler.getInstance().getFiles().size());
		
		for(String file : JDecompiler.getInstance().getFiles()) {
			
			DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(Paths.get(file))));
			int wasAvailable = in.available();
			
			try {
				Timer timer = Timer.startNewTimer();
				
				JavaClass javaClass = JavaClass.read(in);
				
				timer.logElapsed("Class reading");
				
				classes.add(javaClass);
				
			} catch(DisassemblingException ex) {
				Logger.warningFormatted("At pos 0x" + Integer.toHexString(wasAvailable - in.available()));
				ex.printStackTrace();
				
			} catch(Exception ex) {
				ex.printStackTrace();
				
			} finally {
				in.close();
			}
		}
		
		
		
		var out = new StringifyOutputStream(System.out);
		var performance = JDecompiler.getInstance().getPerformance();
		
		for(JavaClass clazz : classes) {
			
			if(clazz.canStringify()) {
				out.resetIndent().write("\n\n----------------------------------------------------------------------------------------------------\n\n");
				
				try {
					performance.perform(clazz);
					performance.write(clazz, out);
					
				} catch(Exception ex) {
					// Если исключение возникло при выводе файла в консоль,
					// надо, чтобы стектрейс начинался с новой строки.
					System.out.println();
					ex.printStackTrace();
				}
			}
		}
		
		out.writeln();
		out.flush();
	}
}