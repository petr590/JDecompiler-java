package x590.jdecompiler;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import x590.javaclass.JavaClass;
import x590.javaclass.exception.DisassemblingException;
import x590.javaclass.io.StringifyOutputStream;
import x590.util.Timer;

/**
 * Если кто-то захочет импортировать модуль x590.argparser, x590.util, jsr305
 * или любой другой модуль "как надо" - через jar файл с модулем, пожалуйста, не делайте этого.
 * Пусть они подключаются через симлинки, может это немного сложнее
 * и не является лучшей практикой в программировании, этот способ хотя бы работает.
 * 
 * Может, первый способ сработает на другой машине,
 * но мой eclipse ни в какую не хочет видеть классы.
 * 
 * Для наглядности, вот сколько часов было потрачено в попытках
 * разобраться в том, почему eclipse не видит классы из импортированного
 * jar файла:
 * 
 * int hoursWasted = 3;
 * 
 * Если вы всё же решите это сделать - увеличьте счётчик на соответствующее значение,
 * чтобы кто-то другой (или вы сами в будущем) не пытался сделать это.
 * 
 * @author 0x590
 */

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		if(args.length > 0) {
			JDecompiler.init(args);
		} else {
			JDecompiler.init(new String[] {
					"bin/example/Example2.class",
					
					"bin/example/annotation/TestAnnotation.class",
					"bin/example/annotation/InvisibleAnnotation.class",
					
					"bin/example/If.class",
					"bin/example/Else.class",
					"bin/example/Synchronized.class",
					"bin/example/Annotations.class",
					"bin/example/Cast.class",
					"bin/example/Charset.class",
					"bin/example/Increment1.class",
					"bin/example/Increment2.class",
					"bin/example/StaticFields.class",
					"bin/example/NonStaticFields.class",
					"bin/example/Methods.class",
					"bin/example/MultiDeclaration.class",
					"bin/example/OverrideTest.class",
					"bin/example/Interface.class",
					"bin/example/Enum.class",
					"bin/example/Arrays.class",
					"bin/example/Constants.class",
					"bin/example/Autoboxing.class",
					"bin/example/Autounboxing.class",
					"bin/example/TernaryOperator.class",
					"bin/example/Throws.class",
					
//					"/home/winch/0x590/java/jdk-8-rt/java/lang/Object.class",
					
					"bin/x590/javaclass/Importable.class",
					"bin/x590/javaclass/Version.class",
					
					"bin/x590/util/Pair.class",
					
//					"--no-omit-curly-brackets",
//					"--no-omit-this-class",
//					"--no-brackets-around-bitwise-operands",
			});
		}
		
		
		List<JavaClass> classes = new ArrayList<>(JDecompiler.getInstance().getFiles().size());
		
		for(String file : JDecompiler.getInstance().getFiles()) {
			
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			int available = in.available();
			
			try {
				Timer timer = Timer.startNewTimer();
				
				classes.add(new JavaClass(in));
				
				timer.logElapsed("Class reading");
				
			} catch(DisassemblingException ex) {
				System.out.println("At pos 0x" + Integer.toHexString(available - in.available()));
				ex.printStackTrace();
				
			} catch(Exception ex) {
				ex.printStackTrace();
				
			} finally {
				in.close();
			}
		}
		

		
		var out = new StringifyOutputStream(System.out);
		
		for(JavaClass clazz : classes) {
			
			out.resetIndent();
			out.write("\n\n----------------------------------------------------------------------------------------------------\n\n");
			
			try {
				clazz.decompile();
				clazz.resolveImports();
				clazz.writeTo(out);
				
			} catch(Exception ex) {
				// Если исключение возникло при выводе файла в консоль,
				// надо, чтобы стектрейс начинался с новой строки.
				System.out.println();
				ex.printStackTrace();
			}
		}
		
		out.writeln();
		out.flush();
	}
}
