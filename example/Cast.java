package example;

@SuppressWarnings("unused")
public class Cast {
	
	public static void foo(String[] args) {
		
		Object obj = null;
		
		String str = (String)(CharSequence)obj;
	}
}