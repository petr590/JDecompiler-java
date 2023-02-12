package x590.jdecompiler.attribute;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.Importable;
import x590.jdecompiler.JavaSerializable;
import x590.jdecompiler.attribute.Attributes.Location;
import x590.jdecompiler.attribute.annotation.AnnotationDefaultAttribute;
import x590.jdecompiler.attribute.annotation.AnnotationsAttribute;
import x590.jdecompiler.attribute.annotation.ParameterAnnotationsAttribute;
import x590.jdecompiler.attribute.signature.ClassSignatureAttribute;
import x590.jdecompiler.attribute.signature.FieldSignatureAttribute;
import x590.jdecompiler.attribute.signature.MethodSignatureAttribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;

import static x590.jdecompiler.attribute.AttributeNames.*;

public abstract class Attribute implements JavaSerializable, Importable {
	
	private final int nameIndex;
	private final String name;
	private final int length;
	
	protected Attribute(int nameIndex, String name, int length) {
		this.nameIndex = nameIndex;
		this.name = name;
		this.length = length;
	}
	
	
	public static Attribute read(ExtendedDataInputStream in, ConstantPool pool, Location location) {
		int nameIndex = in.readUnsignedShort();
		String name = pool.getUtf8String(nameIndex);
		int length = in.readInt();
		int pos = in.available() - length;
		
		
		Attribute attribute = switch(name) {
			case CONSTANT_VALUE     -> new ConstantValueAttribute(nameIndex, name, length, in, pool);
			case DEPRECATED         -> DeprecatedAttribute.get(nameIndex, name, length);
			case SYNTHETIC          -> SyntheticAttribute.get(nameIndex, name, length);
			case EXCEPTIONS         -> new ExceptionsAttribute(nameIndex, name, length, in, pool);
			case ANNOTATION_DEFAULT -> new AnnotationDefaultAttribute(nameIndex, name, length, in, pool);
			case MODULE             -> new ModuleAttribute(nameIndex, name, length, in, pool);
			
			case SIGNATURE -> switch(location) {
				case CLASS -> new ClassSignatureAttribute(nameIndex, name, length, in, pool);
				case FIELD -> new FieldSignatureAttribute(nameIndex, name, length, in, pool);
				case METHOD -> new MethodSignatureAttribute(nameIndex, name, length, in, pool);
				default -> new UnknownAttribute(nameIndex, name, length, in);
			};
			
			case CODE                 -> new CodeAttribute(nameIndex, name, length, in, pool);
			case BOOTSTRAP_METHODS    -> new BootstrapMethodsAttribute(nameIndex, name, length, in, pool);
			case LOCAL_VARIABLE_TABLE -> new LocalVariableTableAttribute(nameIndex, name, length, in, pool);
			
			case RUNTIME_VISIBLE_ANNOTATIONS, RUNTIME_INVISIBLE_ANNOTATIONS ->
				new AnnotationsAttribute(nameIndex, name, length, in, pool);
			
			case RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS, RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS ->
				new ParameterAnnotationsAttribute(nameIndex, name, length, in, pool);
			
			default -> new UnknownAttribute(nameIndex, name, length, in);
		};
		
		if(pos == in.available())
			return attribute;
		else
			throw new DisassemblingException("Attribute \"" + name + "\" was disassembled wrong: position difference " + (pos - in.available()));
	}
	
	public String getName() {
		return name;
	}
	
	public int getLength() {
		return length;
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeShort(nameIndex);
		out.writeInt(length);
	}
}
