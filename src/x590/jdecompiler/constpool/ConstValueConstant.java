package x590.jdecompiler.constpool;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.Importable;
import x590.jdecompiler.StringifyWritable;
import x590.jdecompiler.BiStringifyWritable;
import x590.jdecompiler.exception.TypeSizeMismatchException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;

/**
 * Константа, описывающяя какое-то константное значение - примитив или объект
 */
public abstract class ConstValueConstant extends Constant implements StringifyWritable<ClassInfo>, BiStringifyWritable<ClassInfo, Type>, Importable {
	
	public abstract Type getType();
	
	
	public int intValue() {
		throw new IllegalStateException("Constant " + getConstantName() + " cannot be used as int");
	}
	
	public long longValue() {
		return intValue();
	}
	
	public float floatValue() {
		return longValue();
	}
	
	public double doubleValue() {
		return floatValue();
	}
	
	
	public abstract Operation toOperation();
	
	public final Operation toOperation(TypeSize size) {
		if(size == this.getType().getSize()) {
			return this.toOperation();
		}
		
		throw new TypeSizeMismatchException(size, this.getType().getSize(), this.getType());
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo, Type type) {
		writeTo(out, classinfo);
	}
	
	
	public boolean isOne() {
		return false;
	}
	
	public int getPriority() {
		return Priority.DEFAULT_PRIORITY;
	}
}
