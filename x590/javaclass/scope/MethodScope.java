package x590.javaclass.scope;

import java.util.ArrayList;
import java.util.Collections;

import x590.javaclass.ClassInfo;
import x590.javaclass.MethodDescriptor;
import x590.javaclass.Modifiers;
import x590.javaclass.type.ArrayType;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.javaclass.type.TypeSize;
import x590.javaclass.variable.EmptyVariable;
import x590.javaclass.variable.NamedVariable;
import x590.javaclass.variable.UnnamedVariable;
import x590.javaclass.variable.Variable;
import x590.util.lazyloading.LazyLoadingValue;

import static x590.javaclass.Modifiers.*;

public class MethodScope extends Scope {
	
	private static final LazyLoadingValue<MethodScope> EMPTY_SCOPE = new LazyLoadingValue<>(MethodScope::new);
	
	private MethodScope() {
		super(0, 0, null, Collections.emptyList());
	}
	
	private MethodScope(ClassInfo classinfo, MethodDescriptor descriptor, Modifiers modifiers, int codeLength, int maxLocals) {
		super(0, codeLength, null, new ArrayList<>(maxLocals));
		
		int i = descriptor.arguments.size();
		
		if(modifiers.isNotStatic()) {
			locals.add(new NamedVariable("this", this, classinfo.thisType, true));
			i++;
			
		}
		
		
		EmptyVariable emptyVar = Variable.empty();
		
		// public static void main(String[] args)
		if(modifiers.is(ACC_PUBLIC | ACC_STATIC) && descriptor.equals("main", PrimitiveType.VOID, ArrayType.STRING_ARRAY)) {
			locals.add(new NamedVariable("args", this, ArrayType.STRING_ARRAY, true));
			
		} else {
			
			for(Type argType : descriptor.arguments) {
				locals.add(new UnnamedVariable(this, argType, true));
				
				if(argType.getSize() == TypeSize.EIGHT_BYTES) {
					locals.add(emptyVar);
				}
			}
		}
		
		
		for(; i < maxLocals; i++)
			locals.add(emptyVar);
	}
	
	
	public static MethodScope of(ClassInfo classinfo, MethodDescriptor descriptor, Modifiers modifiers, int codeLength, int maxLocals) {
		return codeLength == 0 && maxLocals == 0 ? EMPTY_SCOPE.get() : new MethodScope(classinfo, descriptor, modifiers, codeLength, maxLocals);
	}
	
	/** Меняет область видимости на public */
	@Override
	public void reduceTypes() {
		super.reduceTypes();
	}
	
	/** Меняет область видимости на public */
	@Override
	public void assignVariablesNames() {
		super.assignVariablesNames();
	}
	
	
	@Override
	protected boolean canOmitCurlyBrackets() {
		return false;
	}
}