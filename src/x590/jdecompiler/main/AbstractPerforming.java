package x590.jdecompiler.main;

import java.io.OutputStream;
import java.util.function.Function;

import x590.jdecompiler.JavaClass;
import x590.jdecompiler.io.DisassemblingOutputStream;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.io.StringifyOutputStream;

public abstract class AbstractPerforming<S extends ExtendedOutputStream<S>> implements Performing {
	
	protected final S out;
	
	public AbstractPerforming(S out) {
		this.out = out;
	}
	
	@Override
	public S getOutputStream() {
		return out;
	}
	
	@Override
	public void closeOutputStream() {
		out.close();
	}
	
	
	public static class DecompilingPerforming extends AbstractPerforming<StringifyOutputStream> {
		
		public DecompilingPerforming(OutputStream out) {
			this(new StringifyOutputStream(out));
		}
		
		public DecompilingPerforming(StringifyOutputStream out) {
			super(out);
		}
		
		@Override
		public void perform(JavaClass clazz) {
			clazz.decompile();
			clazz.resolveImports();
		}
		
		@Override
		public void write(JavaClass clazz) {
			clazz.writeTo(out);
		}
	}
	
	
	public static class DisassemblingPerforming extends AbstractPerforming<DisassemblingOutputStream> {
		
		public DisassemblingPerforming(OutputStream out) {
			this(new DisassemblingOutputStream(out));
		}
		
		public DisassemblingPerforming(DisassemblingOutputStream out) {
			super(out);
		}
		
		@Override
		public void perform(JavaClass clazz) {}
		
		@Override
		public void write(JavaClass clazz) {
			clazz.writeDisassembled(out);
		}
	}


	public static enum PerformingType {
		DECOMPILE(DecompilingPerforming::new),
		DISASSEMBLE(DisassemblingPerforming::new);
		
		private final Function<OutputStream, Performing> performingGetter;
		
		private PerformingType(Function<OutputStream, Performing> performingGetter) {
			this.performingGetter = performingGetter;
		}
		
		public Performing getPerforming(OutputStream out) {
			return performingGetter.apply(out);
		}
	}
}
