package x590.test;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

public class FastutilTest {
	
	public static void main(String[] args) {
		
		Int2IntMap m = new Int2IntOpenHashMap();
		m.put(10, 20);
		
		System.out.println(m.get(-1));
	}
}
