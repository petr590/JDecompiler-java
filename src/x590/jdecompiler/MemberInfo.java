package x590.jdecompiler;

import java.util.Objects;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.field.FieldInfo;
import x590.jdecompiler.method.MethodInfo;
import x590.jdecompiler.modifiers.ClassEntryModifiers;
import x590.util.annotation.Nullable;

/**
 * Представляет общий шаблон для {@link FieldInfo} и {@link MethodInfo}
 */
public abstract class MemberInfo<D extends Descriptor, M extends ClassEntryModifiers> {
	
	private final D descriptor;
	private final M modifiers;
	private @Nullable Int2ObjectMap<String> enumTable;
	
	public MemberInfo(D descriptor, M modifiers) {
		this.descriptor = descriptor;
		this.modifiers = modifiers;
	}
	
	
	public D getDescriptor() {
		return descriptor;
	}
	
	public M getModifiers() {
		return modifiers;
	}
	
	/**
	 * @return Таблицу enum значений, необходимых для правильной работы {@literal switch},
	 * или {@literal null}, если член класса не содержит таблицу.
	 */
	public @Nullable Int2ObjectMap<String> getEnumTable() {
		return enumTable;
	}
	
	public void setEnumTable(@Nullable Int2ObjectMap<String> enumTable) {
		this.enumTable = enumTable;
	}
	
	
	@Override
	public abstract String toString();
	
	
	@Override
	public int hashCode() {
		return Objects.hash(descriptor, modifiers);
	}
	
	@Override
	public abstract boolean equals(Object obj);
	
	public boolean equals(MemberInfo<D, M> other) {
		return this == other ||
				descriptor.equals(other.descriptor) &&
				modifiers.equals(other.modifiers);
	}
}
