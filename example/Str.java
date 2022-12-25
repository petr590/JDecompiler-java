package example;

public class Str {
	
	public static void main(String[] args) {
		
		String  str1 = "a",
				str2 = "abbb",
				str3;
		
		str3 = str2.substring(0, 2).intern();
		
		System.out.println(str1 == str3);
	}
}