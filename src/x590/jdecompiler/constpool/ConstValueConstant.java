package x590.jdecompiler.constpool;

import x590.jdecompiler.Importable;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.exception.TypeSizeMismatchException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;
import x590.jdecompiler.writable.BiStringifyWritable;
import x590.jdecompiler.writable.StringifyWritable;

/**
 * Константа, описывающяя какое-то константное значение - примитив или объект
 */
public abstract class ConstValueConstant extends Constant
		implements StringifyWritable<ClassInfo>, BiStringifyWritable<ClassInfo, Type>, Importable {
	
	public abstract Type getType();
	
	
	public int intValue() {
		throw new IllegalStateException("Constant " + getConstantName() + " cannot be used as int");
	}
	
	public long longValue() {
		throw new IllegalStateException("Constant " + getConstantName() + " cannot be used as long");
	}
	
	public float floatValue() {
		throw new IllegalStateException("Constant " + getConstantName() + " cannot be used as float");
	}
	
	public double doubleValue() {
		throw new IllegalStateException("Constant " + getConstantName() + " cannot be used as double");
	}
	
	
	public abstract Operation toOperation();
	
	public final Operation toOperation(TypeSize size) {
		Type type = getType();
		
		if(size == type.getSize()) {
			return toOperation();
		}
		
		throw new TypeSizeMismatchException(size, type.getSize(), type);
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
