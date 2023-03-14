package x590.jdecompiler.constpool;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.StringifyWritable;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;
import x590.jdecompiler.io.StringifyOutputStream;

public final class PackageConstant extends ConstantWithUtf8String implements StringifyWritable<ClassInfo> {
	
	public PackageConstant(ExtendedDataInputStream in) {
		super(in);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.write(value.replace('/', '.'));
	}
	
	@Override
	public String getConstantName() {
		return "Package";
	}
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {
		out.write(TAG_PACKAGE);
		out.write(valueIndex);
	}
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof PackageConstant packageConstant && this.equals(packageConstant);
	}
	
	public boolean equals(PackageConstant other) {
		return value.equals(other.value);
	}
}
