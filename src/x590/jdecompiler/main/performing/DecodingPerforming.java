package x590.jdecompiler.main.performing;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import x590.jdecompiler.clazz.JavaClass;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.util.Logger;
import x590.util.Timer;
import x590.util.annotation.Nullable;

public abstract class DecodingPerforming<S extends ExtendedOutputStream<S>> extends AbstractPerforming<S> {
	
	public DecodingPerforming(boolean separateOutputStream) {
		super(separateOutputStream);
	}
	
	@Override
	public @Nullable JavaClass read(String file) throws IOException, UncheckedIOException {
		Path path = Paths.get(file);
		
		DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(path)));
		int wasAvailable = in.available();
		
		try {
			Timer timer = Timer.startNewTimer();
			
			JavaClass javaClass = JavaClass.read(in, path.getParent().toString());
			
			timer.logElapsed("Class reading");
			
			return javaClass;
			
		} catch(DisassemblingException ex) {
			Logger.warningFormatted("At pos 0x" + Integer.toHexString(wasAvailable - in.available()));
			ex.printStackTrace();
			
		} finally {
			in.close();
		}
		
		return null;
	}
	
	protected void writeSeparator() {
		if(!separateOutputStream) {
			out.resetIndent().print("\n\n----------------------------------------------------------------------------------------------------\n\n");
		}
	}
}
