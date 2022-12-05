package x590.javaclass;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import x590.javaclass.type.ClassType;
import x590.javaclass.type.Type;

public class UnnamedVariable extends Variable {
	
	private final Set<String> names = new HashSet<>();
	
	public UnnamedVariable() {}
	
	public UnnamedVariable(Type type) {
		super(type);
	}
	
	@Override
	public String getName() {
		return names.isEmpty() ? getNameByType(type) : names.iterator().next();
	}
	
	
	private static String getNameByType(Type type) {
		String name = getRawNameByType(type);
		
		if(name == null) {
			
			name = type.getNameForVariable();
			
			switch(name) {
				case "boolean":		return "bool";
				case "byte":		return "b";
				case "char":		return "ch";
				case "short":		return "sh";
				case "int":			return "n";
				case "long":		return "l";
				case "float":		return "f";
				case "double":		return "d";
				case "void":		return "v";
				case "public":		return "pub";
				case "protected":	return "prot";
				case "private":		return "priv";
				case "static":		return "stat";
				case "final":		return "f";
				case "abstract":	return "abs";
				case "transient":	return "trans";
				case "volatile":	return "vol";
				case "native":		return "nat";
				case "synchronized":return "sync";
				case "class":		return "clazz";
				case "interface":	return "interf";
				case "enum":		return "en";
				case "this":		return "ts";
				case "super":		return "sup";
				case "extends":		return "ext";
				case "implements":	return "impl";
				case "import":		return "imp";
				case "package":		return "pack";
				case "instanceof":	return "inst";
				case "new":			return "nw";
				case "if":			return "cond";
				case "else":		return "el";
				case "while":		return "whl";
				case "do":			return "d";
				case "for":			return "f";
				case "switch":		return "sw";
				case "case":		return "cs";
				case "default":		return "def";
				case "break":		return "brk";
				case "continue":	return "cont";
				case "return":		return "ret";
				case "try":			return "tr";
				case "catch":		return "ctch";
				case "finally":		return "fn";
				case "throw":		return "thr";
				case "throws":		return "thrs";
				case "assert":		return "assrt";
				case "true":		return "tr";
				case "false":		return "fls";
				case "null":		return "nul";
				case "strictfp":	return "strict";
				case "const":		return "cnst";
				case "goto":		return "gt";
			}
		}
		
		return name;
	}
	
	private static @Nullable String getRawNameByType(Type type) {
		if(type.isClassType()) {
			switch(((ClassType)type).getSimpleName()) {
				case "Object":		 return "obj";
				case "Class":		 return "clazz";
				case "Byte":		 return "b";
				case "Character":	 return "ch";
				case "Short":		 return "sh";
				case "Integer":		 return "num";
				case "Long":		 return "l";
				case "Float":		 return "f";
				case "Double":		 return "d";
				case "Boolean":		 return "bool";
				case "String":		 return "str";
				case "StringBuilder":return "strBuilder";
				case "StringBuffer": return "strBuffer";
				case "BigInteger":	 return "bigint";
				case "BigDemical":	 return "bigdem";
				case "Void":		 return "v";
			}
		}
		
		return null;
	}
	
	@Override
	public void addName(String name) {
		names.add(name);
	}
}