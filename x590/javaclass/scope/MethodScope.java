package x590.javaclass.scope;

import java.util.ArrayList;
import java.util.Collections;

import x590.javaclass.ClassInfo;
import x590.javaclass.MethodDescriptor;
import x590.javaclass.Modifiers;
import x590.javaclass.attribute.CodeAttribute;
import x590.javaclass.attribute.LocalVariableTableAttribute;
import x590.javaclass.type.ArrayType;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.javaclass.type.TypeSize;
import x590.javaclass.variable.EmptyVariable;
import x590.javaclass.variable.NamedVariable;
import x590.javaclass.variable.UnnamedVariable;
import x590.javaclass.variable.Variable;
import x590.util.function.ObjIntFunction;
import x590.util.lazyloading.LazyLoadingValue;

import static x590.javaclass.Modifiers.*;

public class MethodScope extends Scope {
	
	private static final LazyLoadingValue<MethodScope> EMPTY_SCOPE = new LazyLoadingValue<>(MethodScope::new);
	
	private MethodScope() {
		super(0, 0, null, Collections.emptyList());
	}
	
	private MethodScope(ClassInfo classinfo, MethodDescriptor descriptor, Modifiers modifiers, CodeAttribute codeAttribute, int endIndex, int maxLocals) {
		super(0, endIndex, null, new ArrayList<>(maxLocals));
		
		int i = 0;
		
		if(modifiers.isNotStatic()) {
			locals.add(new NamedVariable("this", this, classinfo.thisType, true).define());
			i++;
		}
		
		
		LocalVariableTableAttribute localVariableTable = codeAttribute.attributes.get("LocalVariableTable");
		
		
		EmptyVariable emptyVar = Variable.empty();
		
		// public static void main(String[] args)
		if(localVariableTable == null && modifiers.is(ACC_PUBLIC | ACC_STATIC) && descriptor.equals("main", PrimitiveType.VOID, ArrayType.STRING_ARRAY)) {
			locals.add(new NamedVariable("args", this, ArrayType.STRING_ARRAY, true).define());
			
		} else {
			
			int codeLength = codeAttribute.code.length;
			
			ObjIntFunction<Type, Variable> variableCreator = localVariableTable != null ?
					(argType, index) -> Variable.valueOf(localVariableTable.findEntry(index, codeLength), this, argType, true) :
					(argType, index) -> new UnnamedVariable(this, argType, true);
			
			
			for(Type argType : descriptor.arguments) {
				locals.add(variableCreator.apply(argType, i).define());
				
				if(argType.getSize() == TypeSize.EIGHT_BYTES) {
					locals.add(emptyVar);
				}
				
				i++;
			}
		}
		
		
		for(; i < maxLocals; i++)
			locals.add(emptyVar);
	}
	
	
	public static MethodScope of(ClassInfo classinfo, MethodDescriptor descriptor, Modifiers modifiers, CodeAttribute codeAttribute, int endIndex, int maxLocals) {
		return endIndex == 0 && maxLocals == 0 ? EMPTY_SCOPE.get() : new MethodScope(classinfo, descriptor, modifiers, codeAttribute, endIndex, maxLocals);
	}
	
	/** Меняет область видимости на public */
	@Override
	public void reduceTypes() {
		super.reduceTypes();
	}
	
	/** Меняет область видимости на public */
	@Override
	public void defineVariables() {
		super.defineVariables();
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