package x590.jdecompiler.constpool;

import java.lang.constant.Constable;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.field.JavaField;
import x590.jdecompiler.type.Type;
import x590.util.annotation.Nullable;

/**
 * Константа только с одним возможным типом.
 * Такими константами являются все, кроме {@link IntegerConstant}
 */
public abstract class SingleConstableValueConstant<T extends Constable> extends ConstableValueConstant<T> {
	
	private @Nullable JavaField constantField;
	private boolean constantSearchPerformed;
	
	@Override
	protected JavaField findConstantField(ClassInfo classinfo, Type type) {
		if(constantSearchPerformed)
			return constantField;
		
		constantSearchPerformed = true;
		
		return constantField = super.findConstantField(classinfo, type);
	}
}
