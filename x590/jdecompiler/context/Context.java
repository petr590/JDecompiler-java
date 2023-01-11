package x590.jdecompiler.context;

import java.util.Arrays;

import x590.jdecompiler.constpool.ConstantPool;

public abstract class Context {
	
	public final ConstantPool pool;
	int index;
	final int[] indexMap, posMap;
	
	
	public Context(Context otherContext) {
		this.pool = otherContext.pool;
		this.indexMap = otherContext.indexMap;
		this.posMap = otherContext.posMap;
	}
	
	public Context(ConstantPool pool, int mapLength) {
		this(pool, new int[mapLength], new int[mapLength]);
	}
	
	public Context(ConstantPool pool, int[] indexMap, int[] posMap) {
		this.pool = pool;
		this.indexMap = indexMap;
		this.posMap = posMap;
	}
	
	
	public int currentIndex() {
		return index;
	}
	
	// pos смещается, когда мы читаем аргументы инструкции,
	// поэтому возвращаем позицию по индексу из массива
	public int currentPos() {
		return posMap[index];
	}
	
	
	public int posToIndex(int pos) {
		assert pos >= 0 && pos < indexMap.length && (pos == 0 || indexMap[pos] != 0) :
			"Illegal pos " + pos + ". Index map: " + Arrays.toString(indexMap);
		
		return indexMap[pos];
	}
	
	public int indexToPos(int index) {
		return posMap[index];
	}
	
	
	public abstract void warning(String message);
}