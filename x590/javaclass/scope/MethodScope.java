package x590.javaclass.scope;

import java.util.ArrayList;
import java.util.Collections;

import x590.javaclass.ClassInfo;
import x590.javaclass.MethodDescriptor;
import x590.javaclass.Modifiers;
import x590.javaclass.NamedVariable;
import x590.javaclass.UnnamedVariable;
import x590.javaclass.type.Type;
import x590.util.function.LazyLoadingValue;

public class MethodScope extends Scope {
	
	private static final LazyLoadingValue<MethodScope> EMPTY_SCOPE = new LazyLoadingValue<>(() -> new MethodScope());
	
	private MethodScope() {
		super(0, 0, Collections.emptyList());
	}
	
	
	private MethodScope(ClassInfo classinfo, MethodDescriptor descriptor, int modifiers, int codeLength, int maxLocals) {
		super(0, codeLength, new ArrayList<>(maxLocals));
		
		int i = descriptor.arguments.size();
		
		if(!Modifiers.isStatic(modifiers)) {
			locals.add(new NamedVariable("this", classinfo.thisType));
			i++;
		}
		
		for(Type argType : descriptor.arguments) {
			locals.add(new UnnamedVariable(argType));
		}
		
		for(; i < maxLocals; i++)
			locals.add(null);
	}
	
	
	public static MethodScope of(ClassInfo classinfo, MethodDescriptor descriptor, int modifiers, int codeLength, int maxLocals) {
		return codeLength == 0 ? EMPTY_SCOPE.get() : new MethodScope(classinfo, descriptor, modifiers, codeLength, maxLocals);
	}
	
	
	@Override
	protected boolean canOmitCurlyBrackets() {
		return false;
	}
	
	
	public static MethodScope empty() {
		return EMPTY_SCOPE.get();
	}
}