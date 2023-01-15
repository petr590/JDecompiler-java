package example;

public enum Enum {
	A(0, "A"), B(1), C, D(-1, "BD");
	
	private final int value;
	private final String name;
	
	private Enum() {
		this(-1);
	}
	
	private Enum(int value) {
		this(value, "unknown");
	}
	
	private Enum(int value, String name) {
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
		ExampleTesting.runDecompiler(Enum.class);
	}
}