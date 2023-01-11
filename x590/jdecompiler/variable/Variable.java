package x590.jdecompiler.variable;

import java.util.ArrayList;
import java.util.List;

import x590.jdecompiler.attribute.LocalVariableTableAttribute.LocalVariableEntry;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.Types;
import x590.util.annotation.Nullable;

public abstract class Variable extends EmptyableVariable {
	
	protected Type type;
	private Scope enclosingScope;
	
	// Если тип переменной фиксирован, он не должен меняться в принципе
	private final boolean typeFixed;
	
	private String name;
	private final List<Operation> assignedOperations = new ArrayList<>();
	
	/** Наиболее вероятный тип переменной. Если мы, например, инкрементируем переменную, как short, то
	 * наиболее вероятный тип - short. При сведении типа этот тип будет выбран вместо int, если возможно. */
	private Type probableType;
	
	/** {@literal true}, если переменная объявлена */
	private boolean defined;
	
	/** Флаг чтобы избежать бесконечной рекурсии */
	private boolean casting;
	
	public Variable(Scope enclosingScope) {
		this(enclosingScope, Types.ANY_TYPE, false);
	}
	
	public Variable(Scope enclosingScope, boolean typeFixed) {
		this(enclosingScope, Types.ANY_TYPE, typeFixed);
	}
	
	public Variable(Scope enclosingScope, Type type) {
		this(enclosingScope, type, false);
	}
	
	public Variable(Scope enclosingScope, Type type, boolean typeFixed) {
		this.type = type;
		this.enclosingScope = enclosingScope;
		this.typeFixed = typeFixed;
	}
	
	
	public static EmptyVariable empty() {
		return EmptyVariable.INSTANCE;
	}
	
	public static Variable valueOf(@Nullable LocalVariableEntry entry, Scope enclosingScope, Type type, boolean typeFixed) {
		return entry == null ?
				new UnnamedVariable(enclosingScope, type, typeFixed) :
				new NamedVariable(entry.name, enclosingScope, type, typeFixed);
	}
	
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	
	@Override
	public boolean hasName() {
		return name != null;
	}
	
	
	protected void setName(String name) {
		this.name = name;
	}
	
	
	/** Определяет имя переменной. Получает начальное имя из метода {@link #chooseName()}.
	 * Если переменная с таким именем уже есть в scope, то к имени добавляется число. */
	@Override
	public void assignName() {
		if(this.name == null) {
			String name = chooseName();
			
			EmptyableVariable otherVar = enclosingScope.getVariableWithName(name);
			
			
			if(!otherVar.isEmpty()) {
				otherVar.notEmpty().setName(name + '1');
			} else {
				otherVar = enclosingScope.getVariableWithName(name + '1');
			}
			
			if(!otherVar.isEmpty()) {
				String newName;
				int i = 1;
				
				do {
					newName = name + ++i;
				} while(enclosingScope.hasVariableWithName(newName));
				
				name = newName;
				
			}
			
			this.name = name;
		}
	}
	
	
	@Override
	public String getName() {
		return name;
	}
	
	/** Определяет изначальное имя переменной. Используется в {@link #assignName()}. */
	protected abstract String chooseName();
	
	
	public abstract void addName(String name);
	
	
	public boolean isDefined() {
		return defined;
	}
	
	public EmptyableVariable define() {
		defined = true;
		return this;
	}
	
	
	public Type getType() {
		return type;
	}
	
	
	public void addAssignedOperation(Operation operation) {
		assignedOperations.add(operation);
	}
	
	private Type castAssignedOperations(Type type, boolean widest) {
		
		if(casting)
			return type;
		
		casting = true;
		
		for(Operation operation : assignedOperations) {
			if(widest) {
				type = type.castToWidest(operation.getReturnType());
				
				operation.castReturnTypeToNarrowest(type);
			} else {
				type = operation.getReturnTypeAsNarrowest(type);
			}
		}
		
		casting = false;
		
		return type;
	}
	
	private Type castAssignedOperations(Type type, Type newType, boolean widest) {
		
		if(typeFixed || casting)
			return type;
		
		return castAssignedOperations(widest ? type.castToWidest(newType) : type.castToNarrowest(newType), widest);
	}
	
	public void castTypeToNarrowest(Type newType) {
		type = castAssignedOperations(type, newType, false);
	}
	
	public void castTypeToWidest(Type newType) {
		type = castAssignedOperations(type, newType, true);
	}
	
	
	/** Сведение типа переменной. При сведении типа мы определяем конечный тип переменной */
	@Override
	public void reduceType() {
		if(!typeFixed) {
			type = castAssignedOperations(probableType != null && type.isSubtypeOf(probableType) ? probableType : type.reduced(), true);
		}
	}
	
	
	public void setProbableType(Type probableType) {
		this.probableType = probableType;
	}
	
	
	public Scope getEnclosingScope() {
		return enclosingScope;
	}
	
	public void setEnclosingScope(Scope enclosingScope) {
		this.enclosingScope = enclosingScope;
	}
	
	@Override
	public Variable notEmpty() {
		return this;
	}
}