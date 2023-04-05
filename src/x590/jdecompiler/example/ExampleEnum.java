package x590.jdecompiler.example;

@Example
public enum ExampleEnum {
	A(0, "A"), B(1), C, D, E(-1, "BD");
	
	private final int value;
	private final String name;
	
	private ExampleEnum() {
		this(-1);
	}
	
	private ExampleEnum(int value) {
		this(value, "unknown");
	}
	
	private ExampleEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ExampleEnum.class, "-A");
	}
}
