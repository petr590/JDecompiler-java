package x590.javaclass.scope;

import java.util.ArrayList;
import java.util.Collections;

import x590.javaclass.ClassInfo;
import x590.javaclass.MethodDescriptor;
import x590.javaclass.Modifiers;
import x590.javaclass.type.Type;
import x590.javaclass.type.TypeSize;
import x590.javaclass.variable.EmptyVariable;
import x590.javaclass.variable.NamedVariable;
import x590.javaclass.variable.UnnamedVariable;
import x590.javaclass.variable.Variable;
import x590.util.lazyloading.LazyLoadingValue;

public class MethodScope extends Scope {
	
	private static final LazyLoadingValue<MethodScope> EMPTY_SCOPE = new LazyLoadingValue<>(() -> new MethodScope());
	
	private MethodScope() {
		super(0, 0, Collections.emptyList());
	}
	
	
	private MethodScope(ClassInfo classinfo, MethodDescriptor descriptor, int modifiers, int codeLength, int maxLocals) {
		super(0, codeLength, new ArrayList<>(maxLocals));
		
		int i = descriptor.arguments.size();
		
		if(Modifiers.isNonStatic(modifiers)) {
			locals.add(new NamedVariable("this", this, classinfo.thisType, true));
			i++;
		}
		
		
		EmptyVariable emptyVar = Variable.empty();
		
		for(Type argType : descriptor.arguments) {
			locals.add(new UnnamedVariable(this, argType, true));
			
			if(argType.getSize() == TypeSize.EIGHT_BYTES) {
				locals.add(emptyVar);
			}
		}
		
		
		for(; i < maxLocals; i++)
			locals.add(emptyVar);
	}
	
	
	public static MethodScope of(ClassInfo classinfo, MethodDescriptor descriptor, int modifiers, int codeLength, int maxLocals) {
		return codeLength == 0 ? EMPTY_SCOPE.get() : new MethodScope(classinfo, descriptor, modifiers, codeLength, maxLocals);
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
	
	
	public static MethodScope empty() {
		return EMPTY_SCOPE.get();
	}
}