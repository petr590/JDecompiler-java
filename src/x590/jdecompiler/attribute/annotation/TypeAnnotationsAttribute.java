package x590.jdecompiler.attribute.annotation;

import java.util.Collections;
import java.util.List;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.Importable;
import x590.jdecompiler.StringifyWritable;
import x590.jdecompiler.attribute.Attribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.util.IntegerUtil;
import x590.util.annotation.Immutable;

// Слишком сложно
public class TypeAnnotationsAttribute extends Attribute {
	
	private final @Immutable List<TypeAnnotation> parametersAnnotations;
	
	public TypeAnnotationsAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		this(name, length, in.readImmutableList(() -> new TypeAnnotation(in, pool)));
	}
	
	public TypeAnnotationsAttribute(String name, int length, @Immutable List<TypeAnnotation> parametersAnnotations) {
		super(name, length);
		this.parametersAnnotations = parametersAnnotations;
	}
	
	
	public static TypeAnnotationsAttribute emptyVisible() {
		return EmptyParameterAnnotationsAttribute.VISIBLE;
	}
	
	public static TypeAnnotationsAttribute emptyInvisible() {
		return EmptyParameterAnnotationsAttribute.INVISIBLE;
	}
	
	
	public boolean isEmpty() {
		return false;
	}
	
	
	public static final class EmptyParameterAnnotationsAttribute extends TypeAnnotationsAttribute {
		
		public static final TypeAnnotationsAttribute
				VISIBLE = new EmptyParameterAnnotationsAttribute("RuntimeVisibleParameterAnnotations"),
				INVISIBLE = new EmptyParameterAnnotationsAttribute("RuntimeInvisibleParameterAnnotations");
		
		public EmptyParameterAnnotationsAttribute(String name) {
			super(name, 0, Collections.emptyList());
		}
		
		@Override
		public boolean isEmpty() {
			return true;
		}
		
		@Override
		public void write(StringifyOutputStream out, ClassInfo classinfo, int index) {}
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImportsFor(parametersAnnotations);
	}
	
	public void write(StringifyOutputStream out, ClassInfo classinfo, int index) {
		if(index < parametersAnnotations.size() && parametersAnnotations.get(index) != null)
			parametersAnnotations.get(index).writeTo(out, classinfo);
	}
	
	
	public static class TypeAnnotation implements StringifyWritable<ClassInfo>, Importable {
		
		private final Target target;
		private final AnnotationPath path;
		private final Annotation annotation;
		
		public TypeAnnotation(ExtendedDataInputStream in, ConstantPool pool) {
			int tag = in.readUnsignedByte();
			
			this.target = switch(tag) {
				case 0x00, 0x01 -> new TypeParameterTarget(in, pool);
				case 0x10       -> new SupertypeTarget(in, pool);
				// TODO
				default ->
					throw new DisassemblingException("Invalid target type in the TypeAnnotationsAttribute: 0x"
							+ IntegerUtil.hex(tag));
			};
			
			this.path = new AnnotationPath(in, pool);
			
			this.annotation = new Annotation(in, pool);
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			
		}
	}
	
	
	public static class AnnotationPath {
		
		private static final int
				ARRAY_TYPE = 0x0,
				NESTED_TYPE = 0x1,
				GENERIC_BOUND = 0x2,
				PARAMETERIZED_TYPE = 0x3;
		
		private static final int INDEX_OFFSET = 8;
		
		
		private final @Immutable IntList path;
		
		public AnnotationPath(ExtendedDataInputStream in, ConstantPool pool) {
			int lenght = in.readUnsignedByte();
			
			IntList path = new IntArrayList(lenght);
			
			for(int i = 0; i < lenght; i++) {
				int kind = in.readUnsignedByte(),
					index = in.readUnsignedByte();
				
				if(kind < 0 || kind > 3)
					throw new DisassemblingException("Invalid kind of AnnotationPath: " + kind);
				
				if(kind != PARAMETERIZED_TYPE && index != 0)
					throw new DisassemblingException("Invalid argument index " + index + " for kind " + kind);
				
				path.add(kind | index << INDEX_OFFSET);
			}
			
			this.path = IntLists.unmodifiable(path);
		}
	}
	
	
	public static abstract class Target {
		
	}
	
	public static class TypeParameterTarget extends Target {
		
		private final int index;
		
		public TypeParameterTarget(ExtendedDataInputStream in, ConstantPool pool) {
			this.index = in.readUnsignedByte();
		}
	}
	
	public static class SupertypeTarget extends Target {
		
		private final int index;
		
		public SupertypeTarget(ExtendedDataInputStream in, ConstantPool pool) {
			this.index = in.readUnsignedShort();
		}
	}
}
