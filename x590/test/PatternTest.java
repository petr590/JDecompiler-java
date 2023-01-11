package x590.test;

import java.util.regex.Pattern;

public class PatternTest {
	
	private static final Pattern METHOD_NAME_PATTERN = Pattern.compile("(is|get|set|equals).*");
	
	public static void main(String[] args) {
		System.out.println(METHOD_NAME_PATTERN.matcher("set").matches());
	}
}