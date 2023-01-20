package example;

public class Example2 {
	
	public static final void getVoid() {
		return;
	}
	
	public static final boolean getBool() {
		return true;
	}
	
	public static byte getByte() {
		return 0x7F;
	}
	
	public static short getShort() {
		return 0x7FFF;
	}
	
	public static char getChar() {
		return 0xFFFF;
	}
	
	public static int getInt() {
		return 0x7FFFFFFF;
	}
	
	public static long getLong() {
		return 0x7FFFFFFFFFFFFFFFL;
	}
	
	public static float getFloat() {
		return 999;
	}
	
	public static double getDouble() {
		return 999;
	}
	
	public static Object getObject() {
		return new Object();
	}
}