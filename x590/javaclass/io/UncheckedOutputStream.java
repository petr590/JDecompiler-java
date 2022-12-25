package x590.javaclass.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

public abstract class UncheckedOutputStream extends OutputStream {
	
	@Override
	public abstract void write(int b) throws UncheckedIOException;
	
	protected static  UncheckedIOException getUncheckedException(IOException ex) {
		return new UncheckedIOException(ex);
	}
}