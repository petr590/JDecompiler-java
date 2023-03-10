package x590.jdecompiler;

import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.exception.IllegalModifiersException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.modifiers.FieldModifiers;
import x590.jdecompiler.operation.invoke.InvokeOperation;

import static x590.jdecompiler.modifiers.Modifiers.*;
import static x590.jdecompiler.MethodDescriptor.IMPLICIT_ENUM_ARGUMENTS;

public class JavaEnumField extends JavaField {
	
	protected JavaEnumField(ExtendedDataInputStream in, ClassInfo classinfo, ConstantPool pool, FieldModifiers modifiers) {
		super(in, classinfo, pool, modifiers);
		
		if(classinfo.getModifiers().isNotEnum()) {
			throw new IllegalModifiersException("Cannot declare enum field in not enum class");
		}
		
		if(modifiers.isNotAll(ACC_PUBLIC | ACC_STATIC | ACC_FINAL)) {
			throw new IllegalModifiersException("Enum field must be public static and final, got " + modifiers);
		}
	}
	
	
	public void checkHasEnumInitializer(ClassInfo classinfo) {
		if(!(getInitializer() instanceof InvokeOperation)) {
			throw new DecompilationException("Enum constant " + getDescriptor() + " must have enum initializer");
		}
	}
	
	public InvokeOperation getEnumInitializer() {
		return (InvokeOperation)getInitializer();
	}
	
	public boolean hasArgumentsInEnumInitializer() {
		return getEnumInitializer().argumentsCount() > IMPLICIT_ENUM_ARGUMENTS;
	}
	
	
	@Override
	public boolean canStringify(ClassInfo classinfo) {
		return false;
	}
	
	@Override
	public void writeNameAndInitializer(StringifyOutputStream out, ClassInfo classinfo) {
		
		var initializer = getEnumInitializer();
		var context = classinfo.getStaticInitializerStringifyContext();
		
		out.write(getDescriptor().getName());
		
		if(initializer.argumentsCount() > IMPLICIT_ENUM_ARGUMENTS) {
			out.print('(').printAll(initializer.getArguments(), IMPLICIT_ENUM_ARGUMENTS, context, ", ").print(')');
		}
	}
	
	public void writeIndent(StringifyOutputStream out, ClassInfo classinfo) {
		if(hasArgumentsInEnumInitializer())
			out.println(',').printIndent();
		else
			out.print(", ");
	}
}
