package x590.jdecompiler.attribute.annotation;

import java.util.Collections;
import java.util.List;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import x590.jdecompiler.Importable;
import x590.jdecompiler.attribute.Attribute;
import x590.jdecompiler.attribute.AttributeNames;
import x590.jdecompiler.attribute.Attributes.Location;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.writable.StringifyWritable;
import x590.util.IntegerUtil;
import x590.util.annotation.Immutable;

// Слишкам сложна ёмаё
public class TypeAnnotationsAttribute extends Attribute {
	
	private final @Immutable List<TypeAnnotation> parametersAnnotations;
	
	public TypeAnnotationsAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool, Location location) {
		this(name, length, in.readImmutableList(() -> new TypeAnnotation(in, pool, location)));
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
				VISIBLE = new EmptyParameterAnnotationsAttribute(AttributeNames.RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS),
				INVISIBLE = new EmptyParameterAnnotationsAttribute(AttributeNames.RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS);
		
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
		
		public TypeAnnotation(ExtendedDataInputStream in, ConstantPool pool, Location location) {
			int tag = in.readUnsignedByte();
			
			this.target = switch(location) {
				case CLASS ->
					switch(tag) {
						case 0x00 -> new TypeParameterTarget(in, pool);      // type parameter declaration of generic class
						case 0x10 -> new SupertypeTarget(in, pool);          // type in extends or implements clause of class declaration
						case 0x11 -> new TypeParameterBoundTarget(in, pool); // type in bound of type parameter declaration of generic class
						
						default -> throw newInvalidTargetTypeException(tag);
					};
				
				case FIELD ->
					switch(tag) {
						case 0x13 -> EmptyTarget.INSTANCE;                   // type in field declaration
						
						default -> throw newInvalidTargetTypeException(tag);
					};
				
				case METHOD ->
					switch(tag) {
						case 0x01 -> new TypeParameterTarget(in, pool);      // type parameter declaration of generic method
						case 0x12 -> new TypeParameterBoundTarget(in, pool); // type in bound of type parameter declaration of generic method
						case 0x14 -> EmptyTarget.INSTANCE;                   // return type of method, or type of newly constructed object
						case 0x15 -> EmptyTarget.INSTANCE;                   // receiver type of method or constructor
						case 0x16 -> new FormalParameterTarget(in, pool);    // type in formal parameter declaration of method, constructor, or lambda
						case 0x17 -> new ThrowsTarget(in, pool);             // type in throws clause of method or constructor
						
						default -> throw newInvalidTargetTypeException(tag);
					};
				
				case CODE_ATTRIBUTE ->
					switch(tag) {
						case 0x40 -> new LocalvarTarget(in, pool);           // type in local variable declaration
						case 0x41 -> new LocalvarTarget(in, pool);           // type in resource variable declaration
						case 0x42 -> new CatchTarget(in, pool);              // type in exception parameter declaration
						case 0x43 -> new OffsetTarget(in, pool);             // type in instanceof expression
						case 0x44 -> new OffsetTarget(in, pool);             // type in new expression
						case 0x45 -> new OffsetTarget(in, pool);             // type in method reference expression using ::new
						case 0x46 -> new OffsetTarget(in, pool);             // type in method reference expression using ::Identifier
						case 0x47 -> new TypeArgumentTarget(in, pool);       // type in cast expression
						case 0x48 -> new TypeArgumentTarget(in, pool);       // type argument for generic constructor in new expression or explicit constructor invocation statement
						case 0x49 -> new TypeArgumentTarget(in, pool);       // type argument for generic method in method invocation expression
						case 0x4A -> new TypeArgumentTarget(in, pool);       // type argument for generic constructor in method reference expression using ::new
						case 0x4B -> new TypeArgumentTarget(in, pool);       // type argument for generic method in method reference expression using ::Identifier
						
						default -> throw newInvalidTargetTypeException(tag);
					};
				
				default ->
					throw new DisassemblingException("Invalid location" +
							" for the TypeAnnotationsAttribute: " + location);
			};
			
			this.path = new AnnotationPath(in, pool);
			
			this.annotation = Annotation.read(in, pool);
		}
		
		private static DisassemblingException newInvalidTargetTypeException(int tag) {
			return new DisassemblingException("Invalid target type in the TypeAnnotationsAttribute: 0x"
					+ IntegerUtil.hex(tag));
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
		
		private final int typeParameterIndex;
		
		public TypeParameterTarget(ExtendedDataInputStream in, ConstantPool pool) {
			this.typeParameterIndex = in.readUnsignedByte();
		}
	}
	
	public static class SupertypeTarget extends Target {
		
		private final int supertypeIndex;
		
		public SupertypeTarget(ExtendedDataInputStream in, ConstantPool pool) {
			this.supertypeIndex = in.readUnsignedShort();
		}
	}
	
	public static class TypeParameterBoundTarget extends Target {
		
		private final int typeParameterIndex, boundIndex;
		
		public TypeParameterBoundTarget(ExtendedDataInputStream in, ConstantPool pool) {
			this.typeParameterIndex = in.readUnsignedByte();
			this.boundIndex = in.readUnsignedByte();
		}
	}
	
	public static class EmptyTarget extends Target {
		
		public static final EmptyTarget INSTANCE = new EmptyTarget();
		
		private EmptyTarget() {}
	}
	
	public static class FormalParameterTarget extends Target {
		
		private final int formalParameterIndex;
		
		public FormalParameterTarget(ExtendedDataInputStream in, ConstantPool pool) {
			this.formalParameterIndex = in.readUnsignedByte();
		}
	}
	
	public static class ThrowsTarget extends Target {
		
		private final int throwsTypeIndex;
		
		public ThrowsTarget(ExtendedDataInputStream in, ConstantPool pool) {
			this.throwsTypeIndex = in.readUnsignedShort();
		}
	}
	
	public static class LocalvarTarget extends Target {
		
		private static class LocalvarEntry {
			private final int start, length, index;
			
			public LocalvarEntry(ExtendedDataInputStream in, ConstantPool pool) {
				this.start = in.readUnsignedShort();
				this.length = in.readUnsignedShort();
				this.index = in.readUnsignedShort();
			}
		}
		
		private final @Immutable List<LocalvarEntry> entries;
		
		public LocalvarTarget(ExtendedDataInputStream in, ConstantPool pool) {
			this.entries = in.readImmutableList(() -> new LocalvarEntry(in, pool));
		}
	}
	
	public static class CatchTarget extends Target {
		
		private final int exceptionTableIndex;
		
		public CatchTarget(ExtendedDataInputStream in, ConstantPool pool) {
			this.exceptionTableIndex = in.readUnsignedShort();
		}
	}
	
	public static class OffsetTarget extends Target {
		
		private final int offset;
		
		public OffsetTarget(ExtendedDataInputStream in, ConstantPool pool) {
			this.offset = in.readUnsignedShort();
		}
	}
	
	public static class TypeArgumentTarget extends Target {
		
		private final int offset, typeArgumentIndex;
		
		public TypeArgumentTarget(ExtendedDataInputStream in, ConstantPool pool) {
			this.offset = in.readUnsignedShort();
			this.typeArgumentIndex = in.readUnsignedByte();
		}
	}
}
