package x590.jdecompiler.constpool;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.FieldDescriptor;
import x590.jdecompiler.JavaField;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.type.CastStatus;
import x590.jdecompiler.type.Type;
import x590.util.annotation.Nullable;
import x590.util.lazyloading.FunctionalLazyLoadingValue;

/**
 * Константа, которая встраивается компилятором.
 * Это все примитивы и String
 */
public abstract class ConstableValueConstant<T> extends ConstValueConstant implements ICachedConstant<T> {
	
	protected FunctionalLazyLoadingValue<ClassInfo, @Nullable JavaField> newConstantLoader(Type type) {
		return new FunctionalLazyLoadingValue<>(
				classinfo -> {
					var groupedConstants = classinfo.getConstants().stream()
							.collect(Collectors.groupingBy(field -> constantUsageStatus(field, type)));
					
					Optional<Integer> foundMinStatus = groupedConstants.keySet().stream().min(Integer::compare);
					
					if(foundMinStatus.isEmpty())
						return null;
					
					Integer minStatus = foundMinStatus.get();
					
					if(minStatus >= CastStatus.NONE)
						return null;
					
					
					List<JavaField> constants = groupedConstants.get(minStatus);
					
					return constants.size() == 1 ? constants.get(0) : null;
				}
			);
	}
	
	
	protected abstract @Nullable FunctionalLazyLoadingValue<ClassInfo, JavaField> getConstantLoader(Type type);
	
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
		
		var constantLoader = getConstantLoader(type);
		
		if(constantLoader != null && constantLoader.isNonNullValue(classinfo)) {
			writeField(out, classinfo, constantLoader.getRequired().getDescriptor());
			
		} else {
			this.writeValue(out, classinfo, type, implicit, null);
		}
	}
	
	
	protected static boolean canUseConstants() {
		return JDecompiler.getInstance().constantsUsagePolicy().isNotNever();
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
