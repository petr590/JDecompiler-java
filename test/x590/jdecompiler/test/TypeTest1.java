package x590.jdecompiler.test;

import java.util.ArrayList;
import java.util.List;

import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.UncertainIntegralType;

import static x590.jdecompiler.type.PrimitiveType.*;

public class TypeTest1 {
	
	public static void main(String[] args) {
		
		List<Type> types = new ArrayList<>();
		
		types.add(BYTE);
		types.add(SHORT);
		types.add(CHAR);
		types.add(INT);
		
		for(int i = 0; i < 3; i++) {
			types.addAll(
					List.of(
							UncertainIntegralType.getInstance(1, 1, i),
							UncertainIntegralType.getInstance(1, 2, i),
							UncertainIntegralType.getInstance(1, 4, i),
							UncertainIntegralType.getInstance(2, 2, i),
							UncertainIntegralType.getInstance(2, 4, i),
							UncertainIntegralType.getInstance(4, 4, i)
					)
			);
		}
		
		for(Type t1 : types)
			for(Type t2 : types)
				System.out.println(String.format("%-30s", t1.toString()) + " -> " + String.format("%-30s", t2.toString()) + " = " + t1.castToWidestNoexcept(t2));
		
	}
}