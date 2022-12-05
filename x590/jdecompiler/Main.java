package x590.jdecompiler;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import x590.javaclass.JavaClass;
import x590.javaclass.io.StringifyOutputStream;
import x590.util.Timer;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		JDecompiler.init(new String[] {
				"bin/example/Example2.class",
				
//				"bin/example/annotation/TestAnnotation.class",
//				"bin/example/annotation/InvisibleAnnotation.class",
				
//				"bin/example/If.class",
//				"bin/example/Else.class",
//				"bin/example/Synchronized.class",
//				"bin/example/Annotations.class",
				"--no-omit-curly-brackets"
		});
		
		for(String file : JDecompiler.getInstance().files) {
			// Нужен ли здесь BufferedInputStream ?..
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			int available = in.available();
			
			try {
				Timer timer = Timer.startNewTimer();
				
				JavaClass clazz = new JavaClass(in);
				
				timer.logElapsed("Class reading");
				
				clazz.addImports(clazz.classinfo);
				
				var out = new StringifyOutputStream(System.out);
				clazz.writeTo(out);
				out.flush();
				
			} catch(Exception ex) {
				System.out.println("At pos 0x" + Integer.toHexString(available - in.available()));
				throw ex;
				
			} finally {
				in.close();
			}
		}
		
//		Type t1 = VariableCapacityIntegralType.getInstance(1, 4);
//		Type t2 = VariableCapacityIntegralType.CHAR_OR_INT;
		
//		Type t1 = VariableCapacityIntegralType.getInstance(1, 2);
//		Type t2 = VariableCapacityIntegralType.CHAR_OR_SHORT_OR_INT;
		
//		Type t1 = VariableCapacityIntegralType.SHORT_OR_INT;
//		Type t2 = VariableCapacityIntegralType.getInstance(1, 2);
//		
//		castNarrowest(t1, t2);
//		castNarrowest(t2, t1);
//		castWidest(t1, t2);
//		castWidest(t2, t1);
//
//		castWidest(VariableCapacityIntegralType.ANY_INT_OR_BOOLEAN, VariableCapacityIntegralType.getInstance(1, 1));
	}
	
//	private static void castNarrowest(Type t1, Type t2) {
//		System.out.println(t1 + " as " + t2 + " -> " + t1.castToNarrowest(t2));
//	}
//	
//	private static void castWidest(Type t1, Type t2) {
//		System.out.println(t1 + " as " + t2 + " -> " + t1.castToWidest(t2));
//	}
}