package x590.jdecompiler.main.performing;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import x590.jdecompiler.JavaClass;
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
		DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(Paths.get(file))));
		int wasAvailable = in.available();
		
		try {
			Timer timer = Timer.startNewTimer();
			
			JavaClass javaClass = JavaClass.read(in);
			
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
	
	@Override
	public void doWrite(JavaClass clazz) {
		if(!separateOutputStream)
			out.resetIndent().print("\n\n----------------------------------------------------------------------------------------------------\n\n");
	}
}
