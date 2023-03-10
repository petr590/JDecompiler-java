package x590.jdecompiler;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.util.annotation.Immutable;

@Immutable
public final class Version {

	// I have not found an official indication of version JDK Beta, JDK 1.0 number,
	// and I'm too lazy to check it
	public static final int
			JDK_BETA = 43, JDK_1_0 = 44, JDK_1_1 = 45, JAVA_1_2 = 46, JAVA_1_3 = 47, JAVA_1_4 = 48,
			JAVA_5   = 49, JAVA_6  = 50, JAVA_7  = 51, JAVA_8   = 52, JAVA_9   = 53, JAVA_10  = 54,
			JAVA_11  = 55, JAVA_12 = 56, JAVA_13 = 57, JAVA_14  = 58, JAVA_15  = 59, JAVA_16  = 60,
			JAVA_17  = 61, JAVA_18 = 62, JAVA_19 = 63;
	
	private static final Int2ObjectMap<String> VERSIONS = new Int2ObjectOpenHashMap<>(21);
	
	static {
		VERSIONS.put(JDK_BETA, "JDK Beta");
		VERSIONS.put(JDK_1_0,  "JDK 1.0");
		VERSIONS.put(JDK_1_1,  "JDK 1.1");
		VERSIONS.put(JAVA_1_2, "Java 1.2");
		VERSIONS.put(JAVA_1_3, "Java 1.3");
		VERSIONS.put(JAVA_1_4, "Java 1.4");
		VERSIONS.put(JAVA_5,   "Java 5");
		VERSIONS.put(JAVA_6,   "Java 6");
		VERSIONS.put(JAVA_7,   "Java 7");
		VERSIONS.put(JAVA_8,   "Java 8");
		VERSIONS.put(JAVA_9,   "Java 9");
		VERSIONS.put(JAVA_10,  "Java 10");
		VERSIONS.put(JAVA_11,  "Java 11");
		VERSIONS.put(JAVA_12,  "Java 12");
		VERSIONS.put(JAVA_13,  "Java 13");
		VERSIONS.put(JAVA_14,  "Java 14");
		VERSIONS.put(JAVA_15,  "Java 15");
		VERSIONS.put(JAVA_16,  "Java 16");
		VERSIONS.put(JAVA_17,  "Java 17");
		VERSIONS.put(JAVA_18,  "Java 18");
		VERSIONS.put(JAVA_19,  "Java 19");
	}
	
	
	public final int minorVersion, majorVersion;
	
	public Version(int minorVersion, int majorVersion) {
		this.minorVersion = minorVersion;
		this.majorVersion = majorVersion;
	}
	
	public static Version read(ExtendedDataInputStream in) {
		return new Version(in.readUnsignedShort(), in.readUnsignedShort());
	}
	
	
	@Override
	public String toString() {
		String versionName = VERSIONS.get(majorVersion);
		return majorVersion + "." + minorVersion + (versionName != null ? " (" + versionName + ")" : "");
	}
}
