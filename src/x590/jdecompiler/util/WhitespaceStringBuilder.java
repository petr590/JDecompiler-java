package x590.jdecompiler.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;

public class WhitespaceStringBuilder extends AbstractWhitespaceStringBuilder {
	
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
		return EmptyWhitespaceStringBuilder.NO_PRINT_TRAILING_SPACE_INSTANCE;
	}
	
	public static IWhitespaceStringBuilder empty(boolean printTrailingSpace) {
		return printTrailingSpace ?
				EmptyWhitespaceStringBuilder.PRINT_TRAILING_SPACE_INSTANCE :
				EmptyWhitespaceStringBuilder.NO_PRINT_TRAILING_SPACE_INSTANCE;
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
		return values.stream().collect(Collectors.joining(" "));
	}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		if(printTrailingSpace)
			out.printEach(values);
		else
			out.printAll(values);
	}
	
	
	private static class EmptyWhitespaceStringBuilder extends AbstractWhitespaceStringBuilder {
		
		public static final EmptyWhitespaceStringBuilder
				PRINT_TRAILING_SPACE_INSTANCE = new EmptyWhitespaceStringBuilder(true),
				NO_PRINT_TRAILING_SPACE_INSTANCE = new EmptyWhitespaceStringBuilder(false);
		
		
		private EmptyWhitespaceStringBuilder(boolean printTrailingSpace) {
			super(printTrailingSpace);
		}
		
		@Override
		public EmptyWhitespaceStringBuilder printTrailingSpace() {
			return PRINT_TRAILING_SPACE_INSTANCE;
		}
		
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
		public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {}
	}
}
