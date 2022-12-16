package x590.javaclass.variable;

/**
 * Крч, переменная может быть empty, а может и нет.
 */
public abstract class EmptyableVariable {
	
	public abstract boolean isEmpty();
	
	public abstract boolean hasName();
	
	public abstract void assignName();
	
	public abstract String getName();
	
	
	public abstract void reduceType();
	
	
	public abstract Variable notEmpty();
}