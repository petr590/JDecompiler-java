package x590.jdecompiler.main.performing;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.function.Function;

import x590.jdecompiler.FileSource;
import x590.jdecompiler.clazz.JavaClass;
import x590.jdecompiler.main.Config;

public abstract class AbstractPerforming<S extends OutputStream> implements Performing<S> {
	
	protected S out;
	
	/** Если {@literal true}, то выводится в System.out, иначе записывается в файлы */
	protected final boolean separateOutputStream;

	/** Если {@literal true}, то выводится в System.out, иначе записывается в файлы */
	protected final FileSource fileSource;
	
	public AbstractPerforming(Config config) {
		this(config, !config.writeToConsole());
	}

	public AbstractPerforming(Config config, boolean separateOutputStream) {
		this.separateOutputStream = separateOutputStream;
		this.fileSource = config.getFileSource();
	}
	
	@Override
	public void setup() throws IOException, UncheckedIOException {
		if(!separateOutputStream)
			this.out = createOutputStream(System.out);
	}
	
	protected abstract S createOutputStream(OutputStream out);
	
	@Override
	public S getOutputStream() {
		return out;
	}
	
	@Override
	public final void write(JavaClass clazz) throws IOException, UncheckedIOException {
		if(separateOutputStream) {
			this.out = createOutputStream(new FileOutputStream(clazz.getSourceFilePath()));
			doWrite(clazz);
			out.close();
			out = null;
		} else {
			doWrite(clazz);
		}
	}
	
	public abstract void doWrite(JavaClass clazz);
	
	@Override
	public void close() throws IOException, UncheckedIOException {
		if(!separateOutputStream && out != null) {
			out.close();
			out = null;
		}
	}
	
	
	public enum PerformingType {
		DECOMPILE(DecompilingPerforming::new),
		DISASSEMBLE(DisassemblingPerforming::new),
		ASSEMBLE(AssemblingPerforming::new);
		
		private final Function<Config, Performing<?>> performingGetter;
		
		PerformingType(Function<Config, Performing<?>> performingGetter) {
			this.performingGetter = performingGetter;
		}
		
		public Performing<?> getPerforming(Config config) {
			return performingGetter.apply(config);
		}
	}
}
