package x590.jdecompiler.attribute;

import java.util.Arrays;
import java.util.List;

import x590.jdecompiler.constpool.ConstValueConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.constpool.MethodHandleConstant;
import x590.jdecompiler.io.ExtendedDataInputStream;

public final class BootstrapMethodsAttribute extends Attribute {
	
	public final List<BootstrapMethod> bootstrapMethods;
	
	
	public BootstrapMethodsAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		this.bootstrapMethods = Arrays.asList(in.readArray(BootstrapMethod[]::new, () -> new BootstrapMethod(in, pool)));
	}
	
	
	public static class BootstrapMethod {
		
		public final MethodHandleConstant methodHandle;
		public final List<ConstValueConstant> arguments;
		
		public BootstrapMethod(ExtendedDataInputStream in, ConstantPool pool) {
			this.methodHandle = pool.get(in.readUnsignedShort());
			this.arguments = Arrays.asList(in.readArray(ConstValueConstant[]::new, () -> pool.get(in.readUnsignedShort())));
		}
	}	
}
