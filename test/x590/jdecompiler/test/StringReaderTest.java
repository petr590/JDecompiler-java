package x590.jdecompiler.test;

import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.type.ClassType;

public class StringReaderTest {
	
	public static void main(String[] args) {
		ExtendedStringInputStream in = new ExtendedStringInputStream(
				"Lexample/SuperGeneric<TT;>;Ljava/io/Serializable;Ljava/lang/Comparable<TT;>;"
				//"<a<b<c>d>e>f"
			);
		
		System.out.println(ClassType.read(in));
		System.out.println(in.readAll());
		
		in.close();
	}
}