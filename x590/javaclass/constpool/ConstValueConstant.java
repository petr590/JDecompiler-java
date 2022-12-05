package x590.javaclass.constpool;

import x590.javaclass.ClassInfo;
import x590.javaclass.Importable;
import x590.javaclass.Stringified;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.Type;

public abstract class ConstValueConstant extends Constant implements Stringified, Importable {
	
	public abstract Type getType();
	
	public String toStringAs(Type type, ClassInfo classinfo) {
		return this.toString(classinfo);
	}
	
	public abstract String getConstantName();
	
	public abstract Operation toOperation();
}