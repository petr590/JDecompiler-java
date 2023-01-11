package x590.test;

import java.util.HashMap;
import java.util.Map;

public class MapTest {
	
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<>();
		
		System.out.println(map);
		
		System.out.println(map.put("abc", "gg"));
		
		System.out.println(map);
	}
}