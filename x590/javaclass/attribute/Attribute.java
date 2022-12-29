package x590.javaclass.attribute;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.Importable;
import x590.javaclass.JavaSerializable;
import x590.javaclass.attribute.annotation.AnnotationDefaultAttribute;
import x590.javaclass.attribute.annotation.AnnotationsAttribute;
import x590.javaclass.attribute.annotation.ParameterAnnotationsAttribute;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.exception.DisassemblingException;
import x590.javaclass.io.ExtendedDataInputStream;

public class Attribute implements JavaSerializable, Importable {
	
	public final int nameIndex;
	public final String name;
	public final int length;
	
	protected Attribute(int nameIndex, String name, int length) {
		this.nameIndex = nameIndex;
		this.name = name;
		this.length = length;
	}
	
	
	public static Attribute read(ExtendedDataInputStream in, ConstantPool pool) {
		int nameIndex = in.readUnsignedShort();
		String name = pool.getUtf8String(nameIndex);
		int length = in.readInt();
		int pos = in.available() - length;
		
		Attribute attribute = switch(name) {
			case "ConstantValue" -> new ConstantValueAttribute(nameIndex, name, length, in, pool);
			case "Code"			 -> new CodeAttribute(nameIndex, name, length, in, pool);
			case "Exceptions"	 -> new ExceptionsAttribute(nameIndex, name, length, in, pool);
			
			case "RuntimeVisibleAnnotations", "RuntimeInvisibleAnnotations" ->
				new AnnotationsAttribute(nameIndex, name, length, in, pool);
			
			case "RuntimeVisibleParameterAnnotations", "RuntimeInvisibleParameterAnnotations" ->
				new ParameterAnnotationsAttribute(nameIndex, name, length, in, pool);
			
			case "AnnotationDefault"  -> new AnnotationDefaultAttribute(nameIndex, name, length, in, pool);
			case "BootstrapMethods"	  -> new BootstrapMethodsAttribute(nameIndex, name, length, in, pool);
			case "LocalVariableTable" -> new LocalVariableTableAttribute(nameIndex, name, length, in, pool);
			default -> new UnknownAttribute(nameIndex, name, length, in);
		};
		
		if(pos == in.available())
			return attribute;
		else
			throw new DisassemblingException("Attribute \"" + name + "\" was disassembled wrong: position difference " + (pos - in.available()));
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeShort(nameIndex);
		out.writeInt(length);
	}
}
