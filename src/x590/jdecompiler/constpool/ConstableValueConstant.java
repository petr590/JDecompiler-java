package x590.jdecompiler.constpool;

import java.lang.constant.Constable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import x590.jdecompiler.attribute.AttributeType;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.field.JavaField;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.Config;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.type.CastStatus;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ClassType;
import x590.util.annotation.Nullable;

/**
 * Константа, которая встраивается компилятором.
 * Это все примитивы и String
 */
public abstract class ConstableValueConstant<T extends Constable>
		extends ConstValueConstant implements ICachedConstant<T> {
	
	protected @Nullable JavaField findConstantField(ClassInfo classinfo, Type type) {
		return findConstant(type, classinfo, this::findConstantInEnclosingAndNestedClasses);
	}
	
	
	private @Nullable JavaField findConstant(Type type, ClassType classType, BiFunction<Type, ClassInfo, JavaField> nextFinder) {
		return ClassInfo.findClassInfo(classType)
				.map(innerClassinfo -> findConstant(type, innerClassinfo, nextFinder))
				.orElse(null);
	}
	
	private @Nullable JavaField findConstant(Type type, ClassInfo classinfo, BiFunction<Type, ClassInfo, JavaField> nextFinder) {
		var groupedConstants = classinfo.getConstants().stream()
				.collect(Collectors.groupingBy(field -> constantUsageStatus(field, type)));
		
		Optional<Integer> foundMinStatus = groupedConstants.keySet().stream().min(Integer::compare);
		
		if(foundMinStatus.isEmpty())
			return nextFinder.apply(type, classinfo);
		
		Integer minStatus = foundMinStatus.get();
		
		if(CastStatus.isNone(minStatus))
			return nextFinder.apply(type, classinfo);
		
		
		List<JavaField> constants = groupedConstants.get(minStatus);
		
		return constants.size() == 1 ? constants.get(0) : nextFinder.apply(type, classinfo);
	}
	
	
	private @Nullable JavaField findConstantInEnclosingAndNestedClasses(Type type, ClassInfo classinfo) {
		var constant = findConstantInEnclosingClasses(type, classinfo);
		return constant != null ? constant : findConstantInNestedClasses(type, classinfo);
	}
	
	
	private @Nullable JavaField findConstantInEnclosingClasses(Type type, ClassInfo classinfo) {
		
		for(ClassType enclosingClass = classinfo.getThisType().getEnclosingClass();
				enclosingClass != null; enclosingClass = enclosingClass.getEnclosingClass()) {
			
			JavaField constant = findConstant(type, enclosingClass, this::findConstantInEnclosingClasses);
			if(constant != null)
				return constant;
		}
		
		return null;
	}
	
	private @Nullable JavaField findConstantInNestedClasses(Type type, ClassInfo classinfo) {
		
		for(var iter = classinfo.getAttributes().getOrDefaultEmpty(AttributeType.INNER_CLASSES)
					.getEntryStreamWithOuterType(classinfo.getThisType()).iterator();
				iter.hasNext(); ) {
			
			JavaField constant = findConstant(type, iter.next().getInnerType(), this::findConstantInNestedClasses);
			if(constant != null)
				return constant;
		}
		
		return null;
	}
	
	protected Type getWidestType() {
		return getType();
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		writeTo(out, classinfo, getWidestType());
	}
	
	@Override
	public final void writeTo(StringifyOutputStream out, ClassInfo classinfo, Type type) {
		writeTo(out, classinfo, type, false);
	}
	
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo, Type type, boolean implicit) {
		
		var constantField = findConstantField(classinfo, type);
		
		if(constantField != null) {
			writeField(out, classinfo, constantField.getDescriptor());
			
		} else {
			this.writeValue(out, classinfo, type, implicit, null);
		}
	}
	
	
	protected static boolean canUseConstants() {
		return JDecompiler.getConfig().constantsUsagePolicy() != Config.UsagePolicy.NEVER;
	}
	
	protected boolean writeConstantIfEquals(StringifyOutputStream out, ClassInfo classinfo,
			@Nullable FieldDescriptor ownerConstant, boolean equals, FieldDescriptor requiredConstant, String additionalText) {
		
		return writeConstantIfEquals(out, classinfo, ownerConstant, equals, false, requiredConstant, additionalText);
	}
	
	protected boolean writeConstantIfEquals(StringifyOutputStream out, ClassInfo classinfo,
			@Nullable FieldDescriptor ownerConstant, boolean equals, FieldDescriptor requiredConstant) {
		
		return writeConstantIfEquals(out, classinfo, ownerConstant, equals, false, requiredConstant, "");
	}
	
	protected boolean writeConstantIfEquals(StringifyOutputStream out, ClassInfo classinfo,
			@Nullable FieldDescriptor ownerConstant, boolean equals, boolean equalsNegative, FieldDescriptor requiredConstant) {
		
		return writeConstantIfEquals(out, classinfo, ownerConstant, equals, equalsNegative, requiredConstant, "");
	}
	
	protected boolean writeConstantIfEquals(StringifyOutputStream out, ClassInfo classinfo,
			@Nullable FieldDescriptor ownerConstant, boolean equals, boolean equalsNegative, FieldDescriptor requiredConstant, String additionalText) {
		
		if((equals || equalsNegative) && (ownerConstant == null || !ownerConstant.equals(requiredConstant))) {
			out.write(additionalText);
			
			if(equalsNegative && !equals)
				out.write('-');
				
			writeField(out, classinfo, requiredConstant);
			return true;
		}
		
		return false;
	}
	
	protected void writeField(StringifyOutputStream out, ClassInfo classinfo, FieldDescriptor descriptor) {
		if(!classinfo.canOmitClass(descriptor))
			out.print(descriptor.getDeclaringClass(), classinfo).print('.');
		
		out.write(descriptor.getName());
	}
	
	public abstract void writeValue(StringifyOutputStream out, ClassInfo classinfo, Type type, boolean implicit, @Nullable FieldDescriptor ownerConstant);
	
	
	protected final int constantUsageStatus(JavaField constant, Type type) {
		int status = constant.getDescriptor().getType().implicitCastStatus(type);
		return !CastStatus.isNone(status) && canUseConstant(constant) ? status : CastStatus.NONE;
	}
	
	protected boolean canUseConstant(JavaField constant) {
		return false;
	}
	
	public boolean canImlicitCastToInt() {
		return false;
	}
}
