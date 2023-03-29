package x590.jdecompiler.main;

import java.util.Collections;
import java.util.List;

import x590.argparser.ArgsNamespace;
import x590.jdecompiler.main.performing.Performing;
import x590.jdecompiler.main.Config.Builder;
import x590.jdecompiler.main.performing.AbstractPerforming.PerformingType;
import x590.util.ObjectHolder;
import x590.util.annotation.Immutable;

public class JDecompiler {
	
	private static boolean isDebug;
	
	public static void setDebug(boolean isDebug) {
		JDecompiler.isDebug = isDebug;
	}
	
	public static boolean isDebug() {
		return isDebug;
	}
	
	private static JDecompiler INSTANCE;
	
	public static JDecompiler getInstance() {
		if(INSTANCE != null)
			return INSTANCE;
		
		throw new IllegalStateException("JDecompiler yet not initialized");
	}
	
	public static Config getConfig() {
		return getInstance().config;
	}
	
	
	private final @Immutable List<String> files;
	
	private final PerformingType performingType;
	
	private final Config config;
	
	
	public static void init(String[] args) {
		if(INSTANCE != null)
			throw new IllegalStateException("JDecompiler already initialized");
		
		INSTANCE = new JDecompiler(args);
	}
	
	public static void init(@Immutable List<String> files, PerformingType performingType, Config config) {
		if(INSTANCE != null)
			throw new IllegalStateException("JDecompiler already initialized");
		
		INSTANCE = new JDecompiler(files, performingType, config);
	}
	
	public static void init(PerformingType performingType, Config config) {
		init(Collections.emptyList(), performingType, config);
	}
	
	
	private JDecompiler(String[] args) {
		
		ObjectHolder<PerformingType> performingTypeHolder = new ObjectHolder<>(PerformingType.DECOMPILE);
		
		Builder builder = Config.newBuilder();
		
		ArgsNamespace arguments = Config.parseArguments(args, performingTypeHolder, builder);

		this.files = Collections.unmodifiableList(arguments.getAll("files"));
		this.performingType = performingTypeHolder.get();
		this.config = builder.build();
	}
	
	private JDecompiler(@Immutable List<String> files, PerformingType performingType, Config config) {
		this.files = files;
		this.performingType = performingType;
		this.config = config;
	}
	
	
	public @Immutable List<String> getFiles() {
		return files;
	}
	
	
	public PerformingType getPerformingType() {
		return performingType;
	}
	
	
	public Performing<?> getPerforming() {
		return performingType.getPerforming(!config.writeToConsole());
	}
}
