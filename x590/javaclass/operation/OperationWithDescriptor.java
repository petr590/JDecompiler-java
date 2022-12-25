package x590.javaclass.operation;

import x590.javaclass.Descriptor;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.operation.load.ALoadOperation;
import x590.jdecompiler.JDecompiler;

public abstract class OperationWithDescriptor<D extends Descriptor> extends Operation {
	
	public final D descriptor;
	
	public OperationWithDescriptor(D descriptor) {
		this.descriptor = descriptor;
	}
	
	protected boolean canOmitClass(StringifyContext context) {
		return JDecompiler.getInstance().canOmitThisClass() && descriptor.clazz.equals(context.classinfo.thisType);
	}
	
	protected boolean canOmitObject(StringifyContext context, Operation object) {
		return JDecompiler.getInstance().canOmitThisClass() && context.modifiers.isNotStatic() &&
				object instanceof ALoadOperation aload && aload.getIndex() == 0;
	}
}