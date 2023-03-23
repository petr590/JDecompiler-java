package x590.jdecompiler.testing;

import static java.io.File.separator;
import static java.io.File.separatorChar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

import x590.jdecompiler.example.ExampleTesting;
import x590.util.Logger;
import x590.util.Timer;

public class DecompilationCorrectingTesting {
	
	@Test
	public void testIfDecompilationCorrect() {
		
		File tmpDir = null;
		
		try {
			tmpDir = Files.createTempDirectory("jdecompilerTest").toFile();
			
			PrintStream nullPrintStream = new PrintStream(OutputStream.nullOutputStream());
			
			Logger.setOutputAndErrorStream(nullPrintStream, nullPrintStream);
			
			testDecompilation(tmpDir, DecompilationTesting.findAllClassNamesInPackage(ExampleTesting.class.getPackageName()));
			
			System.out.println(DecompilationTesting.findAllClassNamesInPackage(ExampleTesting.class.getPackageName()));
			
		} catch(IOException | InterruptedException ex) {
			ex.printStackTrace();
			
		} finally {
			if(tmpDir != null)
				tmpDir.delete();
		}
	}
	
	private static void testDecompilation(File outputDir, Collection<String> classNames) throws IOException, InterruptedException {
		
		String  binDirectory1 = outputDir.getAbsolutePath() + separator + "bin1",
				binDirectory2 = outputDir.getAbsolutePath() + separator + "bin2";
		
		List<String> classPaths = classNames.stream()
				.filter(className -> className.endsWith("Example"))
				.map(className -> className.replace('.', separatorChar)).toList();
		
		List<String> javacArguments1 =
				Stream.concat(
						Stream.of("javac", "-g:none", "-d", binDirectory1, "-cp", "bin"),
						classPaths.stream()
								.filter(classPath -> classPath.indexOf('$') == -1)
								.map(classPath -> "src" + separator + classPath + ".java")
				).toList();

		List<String> javacArguments2 =
				Stream.concat(
						Stream.of("javac", "-g:none", "-d", binDirectory2, "-cp", "bin"),
						classPaths.stream()
								.filter(classPath -> classPath.indexOf('$') == -1)
								.map(classPath -> binDirectory1 + separator + classPath + ".java")
				).toList();
		
		String[] classFiles1 = classPaths.stream().map(classPath -> binDirectory1 + separator + classPath + ".class").toArray(String[]::new);
		String[] classFiles2 = classPaths.stream().map(classPath -> binDirectory2 + separator + classPath + ".class").toArray(String[]::new);
		
		
		System.out.println("Compiling first...");
		Timer timer = Timer.startNewTimer();
		
		int code = new ProcessBuilder(javacArguments1).inheritIO().start().waitFor();
		if(code != 0) {
			throw new RuntimeException("Return code = " + code);
		}
		
		timer.logElapsed("Compilation");
		
		
		System.out.println("Decompiling...");
		timer.restart();
		
		ExampleTesting.runDecompiler(false, classFiles1);
		
		timer.logElapsed("Decompiling");
		
		
		System.out.println("Compiling second...");
		timer.restart();
		
		code = new ProcessBuilder(javacArguments2).inheritIO().start().waitFor();
		if(code != 0) {
			throw new RuntimeException("Return code = " + code);
		}
		
		timer.logElapsed("Compilation");
		
		
		System.out.println("Testing...");
		timer.restart();
		
		for(int i = 0, length = classFiles1.length; i < length; i++) {
			
			if(!filesSame(new File(classFiles1[i]), new File(classFiles2[i]))) {
				System.err.println("Class files " + classFiles1[i] + " and " + classFiles2[i] + " are not matches");
			} else {
				System.out.println("Tested class " + classPaths.get(i));
			}
		}
		
		timer.logElapsed("Tests");
	}
	
	
	private static boolean filesSame(File file1, File file2) throws IOException {
		if(file1.length() != file2.length()) {
			return false;
		}
		
		try(
			InputStream in1 = new BufferedInputStream(new FileInputStream(file1));
			InputStream in2 = new BufferedInputStream(new FileInputStream(file2));
		) {
			
			int value1;
			
			do {
				value1 = in1.read();
				if(value1 != in2.read()) {
					return false;
				}
				
			} while(value1 >= 0);
			
			return true;
		}
	}
}
