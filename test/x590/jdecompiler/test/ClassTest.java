package x590.jdecompiler.test;

public class ClassTest {
	
	public static void main(String[] args) {
		String d = Object.class.descriptorString();
		
		System.out.println(d.substring(1, d.length() - 1));
	}
}