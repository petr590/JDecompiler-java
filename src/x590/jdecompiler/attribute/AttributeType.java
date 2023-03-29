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

public final class AttributeType<A extends Attribute> implements AttributeReader<A> {
	
	private static final Map<Location, Map<String, AttributeType<?>>> ATTRIBUTE_TYPES = new HashMap<>();
	
	private static final Set<Location> CLASS_FIELD_OR_METHOD_LOCATION = EnumSet.of(Location.CLASS, Location.FIELD, Location.METHOD);
	private static final Set<Location> CLASS_FIELD_METHOD_OR_CODE_LOCATION = EnumSet.of(Location.CLASS, Location.FIELD, Location.METHOD, Location.CODE_ATTRIBUTE);
	
	
	public static final AttributeType<UnknownAttribute> UNKNOWN = new AttributeType<>("", (name, length, in, pool) -> new UnknownAttribute(name, length, in));
	
	// Class
	public static final AttributeType<SourceFileAttribute>          SOURCE_FILE          = new AttributeType<>(Location.CLASS, AttributeNames.SOURCE_FILE,          SourceFileAttribute::new);
	public static final AttributeType<ClassSignatureAttribute>      CLASS_SIGNATURE      = new AttributeType<>(Location.CLASS, AttributeNames.SIGNATURE,            ClassSignatureAttribute::new);
	public static final AttributeType<InnerClassesAttribute>        INNER_CLASSES        = new AttributeType<>(Location.CLASS, AttributeNames.INNER_CLASSES,        InnerClassesAttribute::new);
//	public static final AttributeType<EnclosingMethodAttribute>     ENCLOSING_METHOD     = new AttributeType<>(Location.CLASS, AttributeNames.ENCLOSING_METHOD,     EnclosingMethodAttribute::new);
	public static final AttributeType<BootstrapMethodsAttribute>    BOOTSTRAP_METHODS    = new AttributeType<>(Location.CLASS, AttributeNames.BOOTSTRAP_METHODS,    BootstrapMethodsAttribute::new);
	public static final AttributeType<ModuleAttribute>              MODULE               = new AttributeType<>(Location.CLASS, AttributeNames.MODULE,               ModuleAttribute::new);
	public static final AttributeType<PermittedSubclassesAttribute> PERMITTED_SUBCLASSES = new AttributeType<>(Location.CLASS, AttributeNames.PERMITTED_SUBCLASSES, PermittedSubclassesAttribute::new);
	
	// Field
	public static final AttributeType<ConstantValueAttribute>  CONSTANT_VALUE  = new AttributeType<>(Location.FIELD, AttributeNames.CONSTANT_VALUE, ConstantValueAttribute::new);
	public static final AttributeType<FieldSignatureAttribute> FIELD_SIGNATURE = new AttributeType<>(Location.FIELD, AttributeNames.SIGNATURE,      FieldSignatureAttribute::new);
	
	// Method
	public static final AttributeType<CodeAttribute>                 CODE               = new AttributeType<>(Location.METHOD, AttributeNames.CODE,               CodeAttribute::new);
	public static final AttributeType<ExceptionsAttribute>           EXCEPTIONS         = new AttributeType<>(Location.METHOD, AttributeNames.EXCEPTIONS,         ExceptionsAttribute::new);
	public static final AttributeType<MethodSignatureAttribute>      METHOD_SIGNATURE   = new AttributeType<>(Location.METHOD, AttributeNames.SIGNATURE,          MethodSignatureAttribute::new);
	public static final AttributeType<AnnotationDefaultAttribute>    ANNOTATION_DEFAULT = new AttributeType<>(Location.METHOD, AttributeNames.ANNOTATION_DEFAULT, AnnotationDefaultAttribute::new);
	public static final AttributeType<ParameterAnnotationsAttribute> RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS   = new AttributeType<>(Location.METHOD, AttributeNames.RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS,   ParameterAnnotationsAttribute::new);
	public static final AttributeType<ParameterAnnotationsAttribute> RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS = new AttributeType<>(Location.METHOD, AttributeNames.RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS, ParameterAnnotationsAttribute::new);
	
	// Code
	public static final AttributeType<LocalVariableTableAttribute>     LOCAL_VARIABLE_TABLE      = new AttributeType<>(Location.CODE_ATTRIBUTE, AttributeNames.LOCAL_VARIABLE_TABLE,      LocalVariableTableAttribute::new);
//	public static final AttributeType<LocalVariableTypeTableAttribute> LOCAL_VARIABLE_TYPE_TABLE = new AttributeType<>(Location.CODE_ATTRIBUTE, AttributeNames.LOCAL_VARIABLE_TYPE_TABLE, LocalVariableTypeTableAttribute::new);
	
	// Class, field, method
	public static final AttributeType<SyntheticAttribute>  SYNTHETIC  = new AttributeType<>(CLASS_FIELD_OR_METHOD_LOCATION, AttributeNames.SYNTHETIC,  AttributeReader.convert(SyntheticAttribute::get));
	public static final AttributeType<DeprecatedAttribute> DEPRECATED = new AttributeType<>(CLASS_FIELD_OR_METHOD_LOCATION, AttributeNames.DEPRECATED, AttributeReader.convert(DeprecatedAttribute::get));
	public static final AttributeType<AnnotationsAttribute> RUNTIME_VISIBLE_ANNOTATIONS   = new AttributeType<>(CLASS_FIELD_OR_METHOD_LOCATION, AttributeNames.RUNTIME_VISIBLE_ANNOTATIONS,   AnnotationsAttribute::new);
	public static final AttributeType<AnnotationsAttribute> RUNTIME_INVISIBLE_ANNOTATIONS = new AttributeType<>(CLASS_FIELD_OR_METHOD_LOCATION, AttributeNames.RUNTIME_INVISIBLE_ANNOTATIONS, AnnotationsAttribute::new);
	
	// Class, field, method, code
	public static final AttributeType<TypeAnnotationsAttribute> RUNTIME_VISIBLE_TYPE_ANNOTATIONS   = new AttributeType<>(CLASS_FIELD_METHOD_OR_CODE_LOCATION, AttributeNames.RUNTIME_VISIBLE_TYPE_ANNOTATIONS,   TypeAnnotationsAttribute::new);
	public static final AttributeType<TypeAnnotationsAttribute> RUNTIME_INVISIBLE_TYPE_ANNOTATIONS = new AttributeType<>(CLASS_FIELD_METHOD_OR_CODE_LOCATION, AttributeNames.RUNTIME_INVISIBLE_TYPE_ANNOTATIONS, TypeAnnotationsAttribute::new);
	
	
	
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
	
	protected AttributeType(Location location, String name, AttributeReaderIgnoringLocation<A> reader) {
		this(location, name, (AttributeReader<A>)reader);
	}
	
	protected AttributeType(Location location, String name, AttributeReader<A> reader) {
		this(name, reader);
		putToMap(location);
	}
	
	protected AttributeType(Set<Location> locations, String name, AttributeReaderIgnoringLocation<A> reader) {
		this(locations, name, (AttributeReader<A>)reader);
	}
	
	protected AttributeType(Set<Location> locations, String name, AttributeReader<A> reader) {
		this(name, reader);
		
		for(Location location : locations)
			putToMap(location);
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
