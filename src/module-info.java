module jdecompiler {
	
	requires java.base;
	requires transitive x590.util;
	requires transitive x590.argparser;
	requires transitive it.unimi.dsi.fastutil;
	requires junit;
	
	exports x590.jdecompiler;
	exports x590.jdecompiler.main;
	exports x590.jdecompiler.main.performing;
	
	exports x590.jdecompiler.type;
	exports x590.jdecompiler.clazz;
	exports x590.jdecompiler.field;
	exports x590.jdecompiler.method;
	exports x590.jdecompiler.modifiers;
	exports x590.jdecompiler.constpool;
	
	exports x590.jdecompiler.attribute;
	exports x590.jdecompiler.attribute.annotation;
	exports x590.jdecompiler.attribute.signature;
	
	exports x590.jdecompiler.context;
	exports x590.jdecompiler.variable;
	
	exports x590.jdecompiler.instruction;
	exports x590.jdecompiler.instruction.load;
	exports x590.jdecompiler.instruction.store;
	exports x590.jdecompiler.instruction.constant;
	exports x590.jdecompiler.instruction.field;
	exports x590.jdecompiler.instruction.invoke;
	exports x590.jdecompiler.instruction.operator;
	exports x590.jdecompiler.instruction.dup;
	exports x590.jdecompiler.instruction.array;
	exports x590.jdecompiler.instruction.arrayload;
	exports x590.jdecompiler.instruction.arraystore;
	exports x590.jdecompiler.instruction.returning;
	exports x590.jdecompiler.instruction.scope;
	exports x590.jdecompiler.instruction.cmp;
	
	exports x590.jdecompiler.operation;
	exports x590.jdecompiler.operation.load;
	exports x590.jdecompiler.operation.store;
	exports x590.jdecompiler.operation.constant;
	exports x590.jdecompiler.operation.field;
	exports x590.jdecompiler.operation.invoke;
	exports x590.jdecompiler.operation.operator;
	exports x590.jdecompiler.operation.array;
	exports x590.jdecompiler.operation.arrayload;
	exports x590.jdecompiler.operation.arraystore;
	exports x590.jdecompiler.operation.cmp;
	exports x590.jdecompiler.operation.condition;
	exports x590.jdecompiler.operation.returning;
	exports x590.jdecompiler.scope;
	
	exports x590.jdecompiler.io;
	exports x590.jdecompiler.util;
	exports x590.jdecompiler.exception;
}
