package x590.jdecompiler.variable;

import java.util.ArrayList;
import java.util.List;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.type.CastingKind;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.Types;
import x590.util.annotation.Nullable;

public abstract class AbstractVariable implements Variable {
	
	protected Type type;
	private Scope enclosingScope;
	private @Nullable Int2ObjectMap<String> enumMap;
	
	// Если тип переменной фиксирован, он не должен меняться в принципе
	private final boolean typeFixed;
	
	private @Nullable String name;
	private final List<Operation> assignedOperations = new ArrayList<>();
	
	/** Наиболее вероятный тип переменной. Если мы, например, инкрементируем переменную, как short, то
	 * наиболее вероятный тип - short. При сведении типа этот тип будет выбран вместо int, если возможно. */
	private Type probableType;
	
	/** {@literal true}, если переменная объявлена */
	private boolean defined;
	
	/** Флаг чтобы избежать бесконечной рекурсии */
	private boolean casting;
	
	public AbstractVariable(Scope enclosingScope) {
		this(enclosingScope, Types.ANY_TYPE, false);
	}
	
	public AbstractVariable(Scope enclosingScope, boolean typeFixed) {
		this(enclosingScope, Types.ANY_TYPE, typeFixed);
	}
	
	public AbstractVariable(Scope enclosingScope, Type type) {
		this(enclosingScope, type, false);
	}
	
	public AbstractVariable(Scope enclosingScope, Type type, boolean typeFixed) {
		this.type = type;
		this.enclosingScope = enclosingScope;
		this.typeFixed = typeFixed;
	}
	
	
	@Override
	public boolean hasName() {
		return name != null;
	}
	
	@Override
	public void setName(String name) {
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
				
				int lastIndex = name.length() - 1;
				
				if(lastIndex > 0 && name.charAt(lastIndex) == '1' && !Character.isDigit(name.charAt(lastIndex - 1)) &&
						type.getNameForVariable().equals(name.substring(0, lastIndex))) {
					
					name = type.getNameForVariable();
					
				} else {
					otherVar.nonEmpty().setName(nextName(name, 1));
				}
				
			} else {
				otherVar = enclosingScope.getVariableWithName(nextName(name, 1));
			}
			
			if(!otherVar.isEmpty()) {
				String newName;
				int i = 1;
				
				do {
					newName = nextName(name, ++i);
				} while(enclosingScope.hasVariableWithName(newName));
				
				name = newName;
			}
			
			this.name = name;
		}
	}
	
	
	@Override
	public @Nullable String getName() {
		return name;
	}
	
	/** Определяет изначальное имя переменной. Используется в {@link #assignName()}. */
	protected abstract String chooseName();
	
	/** Определяет имя переменной, когда мы пытаемся получить ещё не использованное имя. */
	protected String nextName(String baseName, int index) {
		return Character.isDigit(baseName.charAt(baseName.length() - 1)) ?
				baseName + '_' + index :
				baseName + index;
	}
	
	
	@Override
	public boolean isDefined() {
		return defined;
	}
	
	@Override
	public void define() {
		defined = true;
	}
	
	
	@Override
	public Type getType() {
		return type;
	}
	
	
	@Override
	public void reduceType() {
		if(!typeFixed) {
			type = castAssignedOperations(probableType != null && type.canCastToNarrowest(probableType) ? probableType : type.reduced());
		}
	}
	
	
	@Override
	public void setProbableType(Type probableType) {
		this.probableType = probableType;
	}
	
	
	@Override
	public void addAssignedOperation(Operation operation) {
		assignedOperations.add(operation);
	}
	
	private Type castAssignedOperations(Type type) {
		
		if(casting)
			return type;
		
		casting = true;
		
		for(Operation operation : assignedOperations) {
			type = type.castToWidest(operation.getReturnType());
			operation.castReturnTypeToNarrowest(type);
		}
		
		casting = false;
		
		return type;
	}
	
	private Type castAssignedOperations(Type type, Type newType, CastingKind kind) {
		
		if(typeFixed)
			return type;
		
		return castAssignedOperations(type.castTo(newType, kind));
	}
	
	@Override
	public void castTypeToNarrowest(Type newType) {
		if(newType != type)
			type = castAssignedOperations(type, newType, CastingKind.NARROWEST);
	}
	
	@Override
	public void castTypeToWidest(Type newType) {
		if(newType != type)
			type = castAssignedOperations(type, newType, CastingKind.WIDEST);
	}
	
	@Override
	public void castTypeTo(Type newType, CastingKind kind) {
		if(newType != type)
			type = castAssignedOperations(type, newType, kind);
	}
	
	
	@Override
	public Scope getEnclosingScope() {
		return enclosingScope;
	}
	
	@Override
	public void setEnclosingScope(Scope enclosingScope) {
		this.enclosingScope = enclosingScope;
	}
	
	@Override
	public @Nullable Int2ObjectMap<String> getEnumTable() {
		return enumMap;
	}
	
	@Override
	public void setEnumTable(@Nullable Int2ObjectMap<String> enumTable) {
		this.enumMap = enumTable;
	}
}
