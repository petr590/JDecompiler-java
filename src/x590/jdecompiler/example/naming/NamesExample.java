package x590.jdecompiler.example.naming;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
@SuppressWarnings("unused")
public class NamesExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(NamesExample.class);
	}
	
	
	private final int cost;

	public NamesExample(int cost) {
		this.cost = cost;
	}
	
	
	public int getCost() {
		return cost;
	}
	
	public boolean isFree() {
		return cost == 0;
	}
	
	public void printCost() {
		int cost = this.cost;
		System.out.println(cost);
	}
	
	
	private static void foo() {
		NamesExample example = new NamesExample(10);
		
		int cost = example.getCost();
		boolean isFree = example.isFree();
	}
}
