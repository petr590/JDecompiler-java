package x590.jdecompiler.constpool;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.field.JavaField;
import x590.jdecompiler.type.Type;
import x590.util.lazyloading.FunctionalLazyLoadingValue;

/**
 * Константа только с одним типом.
 * Такими константами являются все, кроме {@link IntegerConstant}
 */
public abstract class SingleConstableValueConstant<T> extends ConstableValueConstant<T> {
	
	private FunctionalLazyLoadingValue<ClassInfo, JavaField> constantLoader;
	
	@Override
	protected FunctionalLazyLoadingValue<ClassInfo, JavaField> getConstantLoader(Type type) {
		if(constantLoader != null)
			return constantLoader;
		
		return constantLoader = newConstantLoader(type);
	}
}
