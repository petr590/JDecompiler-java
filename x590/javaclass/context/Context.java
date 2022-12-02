package x590.javaclass.context;

import x590.javaclass.constpool.ConstantPool;

public abstract class Context {
	
	public final ConstantPool pool;
	protected int index;
	protected int[] indexMap, posMap;
	
	
	public Context(Context otherContext) {
		this.pool = otherContext.pool;
		this.indexMap = otherContext.indexMap;
		this.posMap = otherContext.posMap;
	}
	
	public Context(ConstantPool pool) {
		this.pool = pool;
	}
	
	public Context(ConstantPool pool, int[] indexMap, int[] posMap) {
		this(pool);
		this.indexMap = indexMap;
		this.posMap = posMap;
	}
	
	
	public int getIndex() {
		return index;
	}
	
	public int posToIndex(int pos) {
		return indexMap[pos];
	}
	
	public int indexToPos(int index) {
		return posMap[index];
	}
	
	
	public abstract void warning(String message);
}