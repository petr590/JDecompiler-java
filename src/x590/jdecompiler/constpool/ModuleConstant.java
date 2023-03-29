package x590.jdecompiler.constpool;

import x590.jdecompiler.StringifyWritable;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;
import x590.jdecompiler.io.StringifyOutputStream;

public final class ModuleConstant extends ConstantWithUtf8String implements StringifyWritable<ClassInfo> {
	
	public ModuleConstant(ExtendedDataInputStream in) {
		super(in);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.write(value);
	}
	
	@Override
	public String getConstantName() {
		return "Module";
	}
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {
		out.write(TAG_MODULE);
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
