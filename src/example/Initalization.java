package example;

public class Initalization {
	
	public static final int CONST;
	
	static {
//		if(Math.random() > 0.5)
//			CONST = 1;
//		else
//			CONST = 0;
		
		CONST = Math.random() > 0.5 ? 1 : 0;
	}
	
	
	public final int value = CONST;
	
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Initalization.class);
	}
}