package x590.jdecompiler.main.performing;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import x590.jdecompiler.clazz.JavaClass;
import x590.jdecompiler.exception.ParseException;
import x590.jdecompiler.io.ExtendedDataOutputStream;
import x590.util.annotation.Nullable;

public class AssemblingPerforming extends AbstractPerforming<ExtendedDataOutputStream> {
	
	public AssemblingPerforming() {
		super(true);
	}
	
	@Override
	protected ExtendedDataOutputStream createOutputStream(OutputStream out) {
		return new ExtendedDataOutputStream(out);
	}
	
	@Override
	public @Nullable JavaClass read(String file) throws IOException, UncheckedIOException {
		Reader in = Files.newBufferedReader(Paths.get(file));
		
		try {
			return JavaClass.parse(in);
			
		} catch(ParseException ex) {
			ex.printStackTrace();
			
		} finally {
			in.close();
		}
		
		return null;
	}
	
	@Override
	public void perform(JavaClass clazz) {}
	
	@Override
	public void doWrite(JavaClass clazz) {
		clazz.serialize(out);
	}
}
