package x590.jdecompiler.constpool;

import java.lang.constant.Constable;

interface ICachedConstant<T extends Constable> {
	T getValueAsObject();
}
