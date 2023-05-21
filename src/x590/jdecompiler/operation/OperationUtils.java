package x590.jdecompiler.operation;

import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.clazz.IClassInfo;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.field.FieldInfo;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.method.MethodInfo;
import x590.jdecompiler.operation.array.NewArrayOperation;
import x590.jdecompiler.operation.arrayload.IALoadOperation;
import x590.jdecompiler.operation.invoke.InvokevirtualOperation;
import x590.jdecompiler.operation.other.PopOperation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ClassType;
import x590.util.annotation.Nullable;

public final class OperationUtils {
	
	private OperationUtils() {}
	
	
	public static void removeIfAutogeneratedCheckForNull(DecompilationContext context, Operation object) {
		if(!JDecompiler.getConfig().showAutogenerated()) {
			var prevOperation = context.currentScope().getLastOperation();
			
			if( prevOperation != null &&
				prevOperation instanceof PopOperation pop &&
				pop.getOperand() instanceof InvokevirtualOperation invokevirtual &&
				invokevirtual.getDescriptor().equals(ClassType.CLASS, ClassType.OBJECT, "getClass") &&
				invokevirtual.getObject() == object) {
				
				pop.remove();
			}
		}
	}
	
	
	public static void tryInlineVarargs(DecompilationContext context, MethodDescriptor descriptor, Deque<Operation> arguments,
			IClassInfo classinfo, MethodInfo methodInfo) {
		
		if(methodInfo.getModifiers().isVarargs()) {
			
			List<Type> argTypes = descriptor.getArguments();
			
			if(!argTypes.isEmpty() && argTypes.get(argTypes.size() - 1).isArrayType()) {
				
				Operation lastOperation = arguments.getLast();
				
				if(lastOperation instanceof NewArrayOperation varargsArray && varargsArray.canInitAsList()) {
					var name = descriptor.getName();
					var argumentsCount = arguments.size() - 1 + varargsArray.getLength();
					
					if(!classinfo.hasMethodByDescriptor(
							methodDescriptor ->
									!methodDescriptor.equals(descriptor) && methodDescriptor.getName().equals(name) &&
									methodDescriptor.getArguments().size() == argumentsCount)
					) {
//							varargsArray.inlineVarargs();
						
						arguments.removeLast();
						varargsArray.remove();
						
						List<Operation> initializers = varargsArray.getInitializers();
						initializers.forEach(Operation::denyImplicitCast);
						
						arguments.addAll(initializers);
					}
				}
				
			} else {
				context.warning("Varargs method " + descriptor + " must have an array as the last argument");
			}
		}
	}
	
	
	public static @Nullable Int2ObjectMap<String> getEnumTable(DecompilationContext context, FieldDescriptor descriptor) {
		var fieldClassinfo = context.getClassinfo().findIClassInfo(descriptor.getDeclaringClass());
		
		return fieldClassinfo.isPresent() ?
				fieldClassinfo.get().findFieldInfo(descriptor).map(FieldInfo::getEnumTable).orElse(null) :
				null;
	}
	
	public static @Nullable Int2ObjectMap<String> getEnumTable(DecompilationContext context, MethodDescriptor descriptor) {
		var methodClassinfo = context.getClassinfo().findIClassInfo(descriptor.getDeclaringClass());
		
		return methodClassinfo.isPresent() ?
				methodClassinfo.get().findMethodInfo(descriptor).map(MethodInfo::getEnumTable).orElse(null) :
				null;
	}
	
	
	public static @Nullable Operation getEnumValueInSwitch(DecompilationContext context, Operation value, Consumer<Int2ObjectMap<String>> enumTableSetter) {
		
		if(value instanceof IALoadOperation iaload) {
			
			var enumTable = iaload.getArray().getEnumTable(context);
			
			if(enumTable != null &&
				iaload.getIndex() instanceof InvokevirtualOperation invokevirtual &&
				isDescriptorOrdinal(invokevirtual.getDescriptor())) {
				
				enumTableSetter.accept(enumTable);
				
				return invokevirtual.getObject();
				
			}
		}
		
		return null;
	}
	
	
	public static boolean isDescriptorOrdinal(MethodDescriptor descriptor) {
		return descriptor.equalsIgnoreClass(PrimitiveType.INT, "ordinal");
	}
	
	
	public static @Nullable Int2ObjectMap<String> initEnumTable(DecompilationContext context, FieldDescriptor descriptor,
			@Nullable Int2ObjectMap<String> enumTable, Consumer<@Nullable Int2ObjectMap<String>> enumTableSetter) {
		
		if(enumTable != FieldInfo.UNDEFINED_ENUM_TABLE)
			return enumTable;
		
		enumTable = OperationUtils.getEnumTable(context, descriptor);
		enumTableSetter.accept(enumTable);
		return enumTable;
	}
	
	
	public static @Nullable Int2ObjectMap<String> initEnumTable(DecompilationContext context, MethodDescriptor descriptor,
			@Nullable Int2ObjectMap<String> enumTable, Consumer<@Nullable Int2ObjectMap<String>> enumTableSetter) {
		
		if(enumTable != FieldInfo.UNDEFINED_ENUM_TABLE)
			return enumTable;
		
		enumTable = OperationUtils.getEnumTable(context, descriptor);
		enumTableSetter.accept(enumTable);
		return enumTable;
	}
}
