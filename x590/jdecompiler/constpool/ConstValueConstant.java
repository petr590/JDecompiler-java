package x590.jdecompiler.constpool;

import java.util.List;
import java.util.function.Predicate;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.Importable;
import x590.jdecompiler.JavaField;
import x590.jdecompiler.Stringified;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.exception.TypeSizeMismatchException;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;
import x590.util.lazyloading.FunctionalLazyLoadingValue;

public abstract class ConstValueConstant extends Constant implements Stringified, Importable {
	
	public static FunctionalLazyLoadingValue<ClassInfo, JavaField> getConstantLoader(Predicate<? super JavaField> predicate) {
		return new FunctionalLazyLoadingValue<>(
				classinfo -> {
					List<JavaField> constants = classinfo.getConstants().stream().filter(predicate).toList();
					return constants.size() == 1 ? constants.get(0) : null;
				}
			);
	}
	
	
	public abstract Type getType();
	
	public String toStringAs(Type type, ClassInfo classinfo) {
		return this.toString(classinfo);
	}
	
	public abstract String getConstantName();
	
	public abstract Operation toOperation();
	
	public final Operation toOperation(TypeSize size) {
		if(size == this.getType().getSize()) {
			return this.toOperation();
		}
		
		throw new TypeSizeMismatchException(size, this.getType().getSize(), this.getType());
	}
}
