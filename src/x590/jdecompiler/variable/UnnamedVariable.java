package x590.jdecompiler.variable;

import java.util.HashSet;
import java.util.Set;

import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.Type;
import x590.util.annotation.Nullable;

public class UnnamedVariable extends AbstractVariable {
	
	private enum Kind {
		PLAIN, INDEX
	}
	
	private final Set<String> names = new HashSet<>();
	private Kind kind = Kind.PLAIN;
	
	public UnnamedVariable(Scope enclosingScope) {
		super(enclosingScope);
	}
	
	public UnnamedVariable(Scope enclosingScope, Type type) {
		super(enclosingScope, type);
	}
	
	public UnnamedVariable(Scope enclosingScope, boolean typeFixed) {
		super(enclosingScope, typeFixed);
	}
	
	public UnnamedVariable(Scope enclosingScope, Type type, boolean typeFixed) {
		super(enclosingScope, type, typeFixed);
	}
	
	@Override
	protected String chooseName() {
		return switch(kind) {
			case PLAIN -> names.isEmpty() ? getNameByType(type) : removeKeyword(names.iterator().next());
			case INDEX -> "i";
		};
	}
	
	@Override
	protected String nextName(String baseName, int index) {
		return switch(kind) {
			case PLAIN -> super.nextName(baseName, index);
			case INDEX -> {
				int num = --index / 18;
				yield Character.toString((char)('i' + index - num * 18)) +
					(num == 0 ? "" : Integer.toString(num + 1));
			}
		};
	}
	
	
	private static String getNameByType(Type type) {
		String name = quickGetNameByType(type);
		return name == null ? removeKeyword(type.getNameForVariable()) : name;
	}
	
	
	private static String removeKeyword(String name) {
		switch(name) {
			case "boolean":		return "bool";
			case "byte":		return "b";
			case "char":		return "c";
			case "short":		return "s";
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
			case "else":		return "els";
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
			default:			return name;
		}
	}
	
	
	private static @Nullable String quickGetNameByType(Type type) {
		if(type instanceof ClassType classType) {
			switch(classType.getSimpleName()) {
				case "Object":		 return "obj";
				case "Class":		 return "clazz";
				case "Byte":		 return "b";
				case "Character":	 return "c";
				case "Short":		 return "s";
				case "Integer":		 return "n";
				case "Long":		 return "l";
				case "Float":		 return "f";
				case "Double":		 return "d";
				case "Boolean":		 return "bool";
				case "String":		 return "str";
				case "Enum":		 return "en";
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
	public void addPossibleName(@Nullable String name) {
		if(name != null)
			names.add(name);
	}
	
	@Override
	public void makeAnIndex() {
		kind = Kind.INDEX;
	}
	
	
	@Override
	public String toString() {
		return String.format("UnnamedVariable #%x { type = %s, names = %s, enclosingScope = %s }",
				hashCode(), type, names, getEnclosingScope());
	}
}
