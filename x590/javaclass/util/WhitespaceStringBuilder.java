package x590.javaclass.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.StringifyOutputStream;

public class WhitespaceStringBuilder extends AbstractWhitespaceStringBuilder<WhitespaceStringBuilder> {
	
	private final List<String> values = new ArrayList<>();
	
	public WhitespaceStringBuilder() {}
	
	public WhitespaceStringBuilder(String str) {
		values.add(str);
	}
	
	public WhitespaceStringBuilder(boolean printTrailingSpace) {
		super(printTrailingSpace);
	}
	
	public WhitespaceStringBuilder(String str, boolean printTrailingSpace) {
		this(str);
		this.printTrailingSpace = printTrailingSpace;
	}
	
	
	public static IWhitespaceStringBuilder empty() {
		return EmptyWhitespaceStringBuilder.INSTANCE;
	}
	
	
	@Override
	public WhitespaceStringBuilder append(String str) {
		values.add(str);
		return this;
	}
	
	@Override
	public boolean isEmpty() {
		return values.isEmpty();
	}
	
	@Override
	public String toString() {
		return values.stream().collect(Collectors.joining(" ")).toString();
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		if(printTrailingSpace)
			values.forEach(value -> out.print(value).printsp());
		else
			Util.forEachExcludingLast(values, value -> out.print(value), () -> out.printsp());
	}
	
	
	private static class EmptyWhitespaceStringBuilder extends AbstractWhitespaceStringBuilder<EmptyWhitespaceStringBuilder> {
		
		public static final EmptyWhitespaceStringBuilder INSTANCE = new EmptyWhitespaceStringBuilder();
		
		
		private boolean printTrailingSpace;
		
		@Override
		public IWhitespaceStringBuilder append(String str) {
			return new WhitespaceStringBuilder(str, printTrailingSpace);
		}
		
		@Override
		public boolean isEmpty() {
			return true;
		}
		
		@Override
		public String toString() {
			return "";
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			// Do nothing
		}
	}
}