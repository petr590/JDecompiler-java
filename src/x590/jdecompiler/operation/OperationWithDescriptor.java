package x590.jdecompiler.operation;

import java.util.Optional;

import x590.jdecompiler.Descriptor;
import x590.jdecompiler.MemberInfo;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.clazz.IClassInfo;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.main.JDecompiler;
import x590.util.annotation.Nullable;

public abstract class OperationWithDescriptor<D extends Descriptor<D>> extends AbstractOperation {
	
	private final D descriptor;
	private D genericDescriptor;
	
	public OperationWithDescriptor(D descriptor) {
		this.descriptor = descriptor;
		this.genericDescriptor = descriptor;
	}
	
	/** Доступен только для прямых подклассов */
	protected void setGenericDescriptor(D genericDescriptor) {
		this.genericDescriptor = genericDescriptor; 
	}
	
	/** Инициализация generic дескриптора
	 * Метод должен вызываться для всех подклассов в конструкторе после инициализации объекта.
	 * В него передаётся объект или {@literal null} для вызовов статических методов. */
	protected void initGenericDescriptor(DecompilationContext context, @Nullable Operation object) {
		var iclassinfo = ClassInfo.findIClassInfo(descriptor.getDeclaringClass(), context.pool);
		
		if(iclassinfo.isPresent()) {
			var foundMethodInfo = findMemberInfo(iclassinfo.get(), descriptor);
			
			if(foundMethodInfo.isPresent()) {
				setGenericDescriptor(foundMethodInfo.get().getGenericDescriptor(object, context.pool));
			}
		}
	}
	
	protected abstract Optional<? extends MemberInfo<D, ?>> findMemberInfo(IClassInfo classinfo, D descriptor);
	
	
	public D getDescriptor() {
		return descriptor;
	}
	
	public D getGenericDescriptor() {
		return genericDescriptor;
	}
	
	protected boolean canOmitClass(StringifyContext context) {
		return context.getClassinfo().canOmitClass(descriptor);
	}
	
	protected boolean canOmitObject(StringifyContext context, Operation object) {
		return JDecompiler.getConfig().canOmitThisAndClass() && object.isThisObject(context.getMethodModifiers());
	}
	
	protected boolean equals(OperationWithDescriptor<D> other) {
		return descriptor.equals(other.descriptor);
	}
}
