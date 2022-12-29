package x590.javaclass.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public abstract class UncheckedInputStream extends InputStream {
	
	@Override
	public abstract int read() throws UncheckedIOException;
	
	protected static UncheckedIOException getUncheckedException(IOException ex) {
		return new UncheckedIOException(ex);
	}
}
