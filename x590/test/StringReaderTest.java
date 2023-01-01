package x590.test;

import x590.javaclass.io.ExtendedStringReader;
import x590.javaclass.type.ClassType;

public class StringReaderTest {
	
	public static void main(String[] args) {
		ExtendedStringReader in = new ExtendedStringReader(
				"Lexample/SuperGeneric<TT;>;Ljava/io/Serializable;Ljava/lang/Comparable<TT;>;"
				//"<a<b<c>d>e>f"
			);
		
		System.out.println(ClassType.read(in));
		System.out.println(in.readAll());
		
		in.close();
	}
}