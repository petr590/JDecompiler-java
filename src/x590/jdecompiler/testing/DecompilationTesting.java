package x590.jdecompiler.testing;

import static java.io.File.separator;
import static java.io.File.separatorChar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import x590.jdecompiler.example.ExampleTesting;

public class DecompilationTesting {
	
	@Test
	public void testAllExamples() {
		ExampleTesting.runDecompiler(findClassNamesAsStreamInPackage(ExampleTesting.class.getPackageName())
				.map(className -> "bin" + separator + className.replace('.', separatorChar) + ".class").toArray(String[]::new));
	}
	
	
	public static Set<Class<?>> findAllClassesInPackage(String packageName) {
		return findClassesAsStreamInPackage(packageName).collect(Collectors.toSet());
	}
	
	public static Set<String> findAllClassNamesInPackage(String packageName) {
		return findClassNamesAsStreamInPackage(packageName).collect(Collectors.toSet());
	}
	
	public static Stream<Class<?>> findClassesAsStreamInPackage(String packageName) {
		return findClassNamesAsStreamInPackage(packageName).map(DecompilationTesting::findClass);
	}
	
	public static Stream<String> findClassNamesAsStreamInPackage(String packageName) {
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replace('.', '/'));
		assert stream != null;
		
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
}
