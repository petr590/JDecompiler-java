package x590.jdecompiler.main.performing;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import x590.jdecompiler.clazz.JavaClass;

public abstract class AbstractPerforming<S extends OutputStream> implements Performing<S> {
	
	protected S out;
	
	/** Если {@literal true}, то выводится в System.out, иначе записывается в файлы */
	protected final boolean separateOutputStream;
	
	public AbstractPerforming(boolean separateOutputStream) {
		this.separateOutputStream = separateOutputStream;
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
	
	
	public static enum PerformingType {
		DECOMPILE(DecompilingPerforming::new),
		DISASSEMBLE(DisassemblingPerforming::new),
		ASSEMBLE(AssemblingPerforming::new);
		
		private final Boolean2ObjectFunction<Performing<?>> performingGetter;
		
		private PerformingType(Boolean2ObjectFunction<Performing<?>> performingGetter) {
			this.performingGetter = performingGetter;
		}
		
		private PerformingType(Supplier<Performing<?>> performingGetter) {
			this.performingGetter = separateOutputStream -> performingGetter.get();
		}
		
		public Performing<?> getPerforming(boolean separateOutputStream) {
			return performingGetter.get(separateOutputStream);
		}
	}
}
