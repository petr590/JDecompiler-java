package x590.jdecompiler.main;

import x590.jdecompiler.JavaClass;
import x590.jdecompiler.io.ExtendedOutputStream;

public interface Performing {
	
	public ExtendedOutputStream<?> getOutputStream();
	
	public void closeOutputStream();
	
	public void perform(JavaClass clazz);
	
	public void write(JavaClass clazz);
}
