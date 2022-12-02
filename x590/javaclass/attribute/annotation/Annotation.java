package x590.javaclass.attribute.annotation;

import x590.javaclass.ClassInfo;
import x590.javaclass.Stringified;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.type.ClassType;
import x590.javaclass.util.Util;

public class Annotation implements Stringified {
	
	private final ClassType type;
	private final Element[] elements;
	
	protected Annotation(ExtendedDataInputStream in, ConstantPool pool) {
		this.type = ClassType.valueOf(pool.getUtf8String(in.readUnsignedShort()));
		
		int length = in.readUnsignedShort();
		var elements = this.elements = new Element[length];
		
		for(int i = 0; i < length; i++)
			elements[i] = new Element(in, pool);
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		StringBuilder str = new StringBuilder("@").append(type.toString(classinfo));
		
		 if(elements.length > 0) {
			str.append('(');
			Util.forEachExcludingLast(elements,
					element -> str.append(element.toString(classinfo)),
					element -> str.append(", "));
			str.append(')');
		 }
		 
		 return str.toString();
	}
}
