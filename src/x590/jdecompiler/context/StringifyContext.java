package x590.jdecompiler.context;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.JavaMethod;
import x590.util.Logger;

public class StringifyContext extends DecompilationAndStringifyContext {
	
	public StringifyContext(Context otherContext, ClassInfo classinfo, JavaMethod method) {
		super(otherContext, classinfo, method);
	}
	
	@Override
	public void warning(String message) {
		Logger.warning("Stringify warning " + message);
	}
}