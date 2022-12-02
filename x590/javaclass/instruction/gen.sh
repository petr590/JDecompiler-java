#!/bin/bash

for i in I L F D A B C S; do
	
	echo -n "package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.${i}AStoreOperation;
import x590.javaclass.operation.Operation;

public class ${i}AStoreInstruction extends ArrayStoreInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ${i}AStoreOperation(context);
	}
}" > "${i}AStoreInstruction.java"
	
done
