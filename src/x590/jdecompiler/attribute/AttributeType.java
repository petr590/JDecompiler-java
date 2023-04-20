package x590.jdecompiler.attribute;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import x590.jdecompiler.attribute.Attributes.Location;
import x590.jdecompiler.attribute.annotation.AnnotationDefaultAttribute;
import x590.jdecompiler.attribute.annotation.AnnotationsAttribute;
import x590.jdecompiler.attribute.annotation.ParameterAnnotationsAttribute;
import x590.jdecompiler.attribute.annotation.TypeAnnotationsAttribute;
import x590.jdecompiler.attribute.signature.ClassSignatureAttribute;
import x590.jdecompiler.attribute.signature.FieldSignatureAttribute;
import x590.jdecompiler.attribute.signature.MethodSignatureAttribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;

public class AttributeType<A extends Attribute> implements AttributeReader<A> {
	
	private static final Map<Location, Map<String, AttributeType<?>>> ATTRIBUTE_TYPES = new HashMap<>();
	
	private static final Set<Location> CLASS_FIELD_OR_METHOD_LOCATION = EnumSet.of(Location.CLASS, Location.FIELD, Location.METHOD);
	private static final Set<Location> CLASS_FIELD_METHOD_OR_CODE_LOCATION = EnumSet.of(Location.CLASS, Location.FIELD, Location.METHOD, Location.CODE_ATTRIBUTE);
	
	
	public static final AttributeType<UnknownAttribute> UNKNOWN = new AttributeType<>("", (name, length, in, pool) -> new UnknownAttribute(name, length, in));
	
	// Class
	public static final AttributeType<SourceFileAttribute>            SOURCE_FILE          = create(Location.CLASS, AttributeNames.SOURCE_FILE,          SourceFileAttribute::new);
	public static final AttributeType<ClassSignatureAttribute>        CLASS_SIGNATURE      = create(Location.CLASS, AttributeNames.SIGNATURE,            ClassSignatureAttribute::new);
	public static final EmptyableAttributeType<InnerClassesAttribute> INNER_CLASSES        = create(Location.CLASS, AttributeNames.INNER_CLASSES,        InnerClassesAttribute::new, InnerClassesAttribute.empty());
//	public static final AttributeType<EnclosingMethodAttribute>       ENCLOSING_METHOD     = create(Location.CLASS, AttributeNames.ENCLOSING_METHOD,     EnclosingMethodAttribute::new);
	public static final AttributeType<BootstrapMethodsAttribute>      BOOTSTRAP_METHODS    = create(Location.CLASS, AttributeNames.BOOTSTRAP_METHODS,    BootstrapMethodsAttribute::new);
	public static final AttributeType<ModuleAttribute>                MODULE               = create(Location.CLASS, AttributeNames.MODULE,               ModuleAttribute::new);
	public static final AttributeType<PermittedSubclassesAttribute>   PERMITTED_SUBCLASSES = create(Location.CLASS, AttributeNames.PERMITTED_SUBCLASSES, PermittedSubclassesAttribute::new);
	
	// Field
	public static final AttributeType<ConstantValueAttribute>  CONSTANT_VALUE  = create(Location.FIELD, AttributeNames.CONSTANT_VALUE, ConstantValueAttribute::new);
	public static final AttributeType<FieldSignatureAttribute> FIELD_SIGNATURE = create(Location.FIELD, AttributeNames.SIGNATURE,      FieldSignatureAttribute::new);
	
	// Method
	public static final EmptyableAttributeType<CodeAttribute>       CODE               = create(Location.METHOD, AttributeNames.CODE,                CodeAttribute::new,        CodeAttribute.empty());
	public static final EmptyableAttributeType<ExceptionsAttribute> EXCEPTIONS         = create(Location.METHOD, AttributeNames.EXCEPTIONS,          ExceptionsAttribute::new,  ExceptionsAttribute.empty());
	public static final AttributeType<MethodSignatureAttribute>     METHOD_SIGNATURE   = create(Location.METHOD, AttributeNames.SIGNATURE,           MethodSignatureAttribute::new);
	public static final AttributeType<AnnotationDefaultAttribute>   ANNOTATION_DEFAULT = create(Location.METHOD, AttributeNames.ANNOTATION_DEFAULT,  AnnotationDefaultAttribute::new);
	
	public static final EmptyableAttributeType<ParameterAnnotationsAttribute> RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS   = create(Location.METHOD, AttributeNames.RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS,   ParameterAnnotationsAttribute::new, ParameterAnnotationsAttribute.emptyVisible());
	public static final EmptyableAttributeType<ParameterAnnotationsAttribute> RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS = create(Location.METHOD, AttributeNames.RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS, ParameterAnnotationsAttribute::new, ParameterAnnotationsAttribute.emptyInvisible());
	
	// Code
	public static final AttributeType<LocalVariableTableAttribute>     LOCAL_VARIABLE_TABLE      = create(Location.CODE_ATTRIBUTE, AttributeNames.LOCAL_VARIABLE_TABLE,      LocalVariableTableAttribute::new);
//	public static final AttributeType<LocalVariableTypeTableAttribute> LOCAL_VARIABLE_TYPE_TABLE = create(Location.CODE_ATTRIBUTE, AttributeNames.LOCAL_VARIABLE_TYPE_TABLE, LocalVariableTypeTableAttribute::new);
	
	// Class, field, method
	public static final AttributeType<SyntheticAttribute>  SYNTHETIC  = create(CLASS_FIELD_OR_METHOD_LOCATION, AttributeNames.SYNTHETIC,  AttributeReader.convert(SyntheticAttribute::get));
	public static final AttributeType<DeprecatedAttribute> DEPRECATED = create(CLASS_FIELD_OR_METHOD_LOCATION, AttributeNames.DEPRECATED, AttributeReader.convert(DeprecatedAttribute::get));
	
	public static final EmptyableAttributeType<AnnotationsAttribute> RUNTIME_VISIBLE_ANNOTATIONS   = create(CLASS_FIELD_OR_METHOD_LOCATION, AttributeNames.RUNTIME_VISIBLE_ANNOTATIONS,   AnnotationsAttribute::new, AnnotationsAttribute.emptyVisible());
	public static final EmptyableAttributeType<AnnotationsAttribute> RUNTIME_INVISIBLE_ANNOTATIONS = create(CLASS_FIELD_OR_METHOD_LOCATION, AttributeNames.RUNTIME_INVISIBLE_ANNOTATIONS, AnnotationsAttribute::new, AnnotationsAttribute.emptyInvisible());
	
	// Class, field, method, code
	public static final AttributeType<TypeAnnotationsAttribute> RUNTIME_VISIBLE_TYPE_ANNOTATIONS   = create(CLASS_FIELD_METHOD_OR_CODE_LOCATION, AttributeNames.RUNTIME_VISIBLE_TYPE_ANNOTATIONS,   TypeAnnotationsAttribute::new);
	public static final AttributeType<TypeAnnotationsAttribute> RUNTIME_INVISIBLE_TYPE_ANNOTATIONS = create(CLASS_FIELD_METHOD_OR_CODE_LOCATION, AttributeNames.RUNTIME_INVISIBLE_TYPE_ANNOTATIONS, TypeAnnotationsAttribute::new);
	
	
	
	private final String name;
	private final AttributeReader<A> reader;
	
	private void putToMap(Location location) {
		ATTRIBUTE_TYPES.computeIfAbsent(location, loc -> new HashMap<>()).put(name, this);
	}
	
	private AttributeType(String name, AttributeReaderIgnoringLocation<A> reader) {
		this(name, (AttributeReader<A>)reader);
	}
	
	private AttributeType(String name, AttributeReader<A> reader) {
		this.name = name;
		this.reader = reader;
	}
	
	protected AttributeType(Location location, String name, AttributeReader<A> reader) {
		this(name, reader);
		putToMap(location);
	}
	
	protected AttributeType(Set<Location> locations, String name, AttributeReader<A> reader) {
		this(name, reader);
		
		for(Location location : locations)
			putToMap(location);
	}
	
	
	public static <A extends Attribute> AttributeType<A> create(Location location, String name, AttributeReader<A> reader) {
		return new AttributeType<>(location, name, reader);
	}
	
	public static <A extends Attribute> AttributeType<A> create(Location location, String name, AttributeReaderIgnoringLocation<A> reader) {
		return new AttributeType<>(location, name, reader);
	}
	
	public static <A extends Attribute> AttributeType<A> create(Set<Location> locations, String name, AttributeReader<A> reader) {
		return new AttributeType<>(locations, name, reader);
	}
	
	public static <A extends Attribute> AttributeType<A> create(Set<Location> locations, String name, AttributeReaderIgnoringLocation<A> reader) {
		return new AttributeType<>(locations, name, reader);
	}
	
	public static <A extends Attribute> EmptyableAttributeType<A> create(Location location, String name, AttributeReader<A> reader, A emptyAttribute) {
		return new EmptyableAttributeType<>(location, name, reader, emptyAttribute);
	}
	
	public static <A extends Attribute> EmptyableAttributeType<A> create(Location location, String name, AttributeReaderIgnoringLocation<A> reader, A emptyAttribute) {
		return new EmptyableAttributeType<>(location, name, reader, emptyAttribute);
	}
	
	public static <A extends Attribute> EmptyableAttributeType<A> create(Set<Location> locations, String name, AttributeReader<A> reader, A emptyAttribute) {
		return new EmptyableAttributeType<>(locations, name, reader, emptyAttribute);
	}
	
	public static <A extends Attribute> EmptyableAttributeType<A> create(Set<Location> locations, String name, AttributeReaderIgnoringLocation<A> reader, A emptyAttribute) {
		return new EmptyableAttributeType<>(locations, name, reader, emptyAttribute);
	}
	
	
	public String getName() {
		return name;
	}
	
	@Override
	public A readAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool, Location location) {
		return reader.readAttribute(name, length, in, pool, location);
	}
	
	
	public static AttributeType<?> getAttributeType(Location location, String name) {
		return ATTRIBUTE_TYPES.getOrDefault(location, Collections.emptyMap()).getOrDefault(name, UNKNOWN);
	}
}
