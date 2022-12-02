package x590.javaclass.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.StringifyOutputStream;

public class WhitespaceStringBuilder implements IWhitespaceStringBuilder {
	
	private final List<String> values = new ArrayList<>();
	private boolean printTrailingSpace;
	
	public WhitespaceStringBuilder() {}
	
	public WhitespaceStringBuilder(String str) {
		values.add(str);
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
		if(printTrailingSpace) {
			values.forEach(value -> {
				out.print(value);
				out.print(' ');
			});
			
		} else {
			if(!values.isEmpty()) {
				for(Iterator<String> iter = values.iterator();;) {
					out.print(iter.next());
					
					if(!iter.hasNext())
						return;
					
					out.print(' ');
				}
			}
		}
	}
	
	@Override
	public WhitespaceStringBuilder printTrailingSpace() {
		this.printTrailingSpace = true;
		return this;
	}
	
	
	private static class EmptyWhitespaceStringBuilder implements IWhitespaceStringBuilder {
		
		public static final EmptyWhitespaceStringBuilder INSTANCE = new EmptyWhitespaceStringBuilder();
		
		@Override
		public IWhitespaceStringBuilder append(String str) {
			throw new UnsupportedOperationException("append");
		}
		
		@Override
		public boolean isEmpty() {
			return true;
		}
		
		@Override
		public IWhitespaceStringBuilder printTrailingSpace() {
			// Do nothing
			return this;
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			// Do nothing
		}
	}
}