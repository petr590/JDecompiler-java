package example;

public class Synchronized {
	
	public static void main(String... args) {
		
		int i = 10;
		
		synchronized(System.out) {
			i += 20;
		}
		
		System.out.println(i);
	}
}