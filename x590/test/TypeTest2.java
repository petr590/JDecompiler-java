package x590.test;

import static x590.javaclass.type.PrimitiveType.*;

import java.util.ArrayList;
import java.util.List;

import x590.javaclass.type.Type;
import x590.javaclass.type.VariableCapacityIntegralType;

public class TypeTest2 {
	
	public static void main(String[] args) {
		test(List.of(BYTE, SHORT, INT, CHAR));
		
		System.out.println();
		
		test(List.of(
				VariableCapacityIntegralType.getInstance(1, 1),
				VariableCapacityIntegralType.getInstance(2, 2),
				VariableCapacityIntegralType.getInstance(4, 4),
				VariableCapacityIntegralType.getInstance(1, 1, VariableCapacityIntegralType.INCLUDE_CHAR)
		));
	}
	
	public static void test(List<Type> types1) {
		
		List<Type> types2 = new ArrayList<>();
		
		types2.add(BYTE);
		types2.add(SHORT);
		types2.add(CHAR);
		types2.add(INT);
		
		for(int i = 0; i < 3; i++) {
			types2.addAll(
					List.of(
							VariableCapacityIntegralType.getInstance(1, 1, i),
							VariableCapacityIntegralType.getInstance(1, 2, i),
							VariableCapacityIntegralType.getInstance(1, 4, i),
							VariableCapacityIntegralType.getInstance(2, 2, i),
							VariableCapacityIntegralType.getInstance(2, 4, i),
							VariableCapacityIntegralType.getInstance(4, 4, i)
					)
			);
		}
		
		for(Type t1 : types1)
			for(Type t2 : types2)
				System.out.println(String.format("%-30s", t1.toString()) + " -> " + String.format("%-30s", t2.toString()) + " = " + t1.castToNarrowestNoexcept(t2));
		
	}
}