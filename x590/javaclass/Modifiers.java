package x590.javaclass;

public class Modifiers {
	
	public static final int
			ACC_VISIBLE      = 0x0000, // class, field, method
			ACC_PUBLIC       = 0x0001, // class, field, method
			ACC_PRIVATE      = 0x0002, // nested class, field, method
			ACC_PROTECTED    = 0x0004, // nested class, field, method
			ACC_STATIC       = 0x0008, // nested class, field, method
			ACC_FINAL        = 0x0010, // class, field, method
			ACC_SYNCHRONIZED = 0x0020, // method, block
			ACC_SUPER        = 0x0020, // class (deprecated)
			ACC_VOLATILE     = 0x0040, // field
			ACC_TRANSIENT    = 0x0080, // field
			ACC_BRIDGE       = 0x0040, // method
			ACC_VARARGS      = 0x0080, // method
			ACC_NATIVE       = 0x0100, // method
			ACC_INTERFACE    = 0x0200, // class
			ACC_ABSTRACT     = 0x0400, // class, method
			ACC_STRICT       = 0x0800, // class, non-abstract method
			ACC_SYNTHETIC    = 0x1000, // class, field, method
			ACC_ANNOTATION   = 0x2000, // class
			ACC_ENUM         = 0x4000, // class, field
			
			ACC_ACCESS_FLAGS = ACC_VISIBLE | ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED,
			ACC_SYNTHETIC_OR_BRIDGE = ACC_SYNTHETIC | ACC_BRIDGE;
	
	public static boolean isStatic(int modifiers) {
		return (modifiers & ACC_STATIC) != 0;
	}
}