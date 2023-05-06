package x590.jdecompiler.operation.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.OperationUtils;
import x590.jdecompiler.operation.constant.IConstOperation;
import x590.jdecompiler.operation.field.GetStaticFieldOperation;
import x590.jdecompiler.operation.invoke.InvokevirtualOperation;
import x590.jdecompiler.type.reference.ArrayType;

public final class IAStoreOperation extends ArrayStoreOperation {
	
	public IAStoreOperation(DecompilationContext context) {
		super(ArrayType.INT_ARRAY, context);
	}
	
	@Override
	public void postDecompilation(DecompilationContext context) {
		var enumTable = getArray().getEnumTable(context);
		
		if(enumTable != null &&
			getValue() instanceof IConstOperation iconst &&
			getIndex() instanceof InvokevirtualOperation invokevirtual &&
			OperationUtils.isDescriptorOrdinal(invokevirtual.getDescriptor()) &&
			invokevirtual.getObject() instanceof GetStaticFieldOperation getstatic) {
			
			var descriptor = getstatic.getDescriptor();
			
			if(descriptor.getType().equals(descriptor.getDeclaringClass())) {
				enumTable.put(iconst.getValue(), descriptor.getName());
				context.getMethod().getMethodInfo().setEnumTable(enumTable);
			}
		}
	}
}
