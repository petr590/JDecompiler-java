package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.StringWritable;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;

public final class ModuleConstant extends ConstantWithUtf8String implements StringWritable<ClassInfo> {
	
	public ModuleConstant(ExtendedDataInputStream in) {
		super(in);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.write(value);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.write(0x13);
		out.write(valueIndex);
	}
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof ModuleConstant moduleConstant && this.equals(moduleConstant);
	}
	
	public boolean equals(ModuleConstant other) {
		return value.equals(other.value);
	}
}
