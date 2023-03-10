package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.StringifyWritable;
import x590.jdecompiler.io.ExtendedDataInputStream;
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
	public void serialize(DataOutputStream out) throws IOException {
		out.write(0x14);
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
