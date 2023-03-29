package x590.jdecompiler.main;

import java.util.Objects;
import java.util.function.Consumer;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import x590.argparser.ArgsNamespace;
import x590.argparser.Flag;
import x590.argparser.StandartArgParser;
import x590.argparser.option.EnumOption;
import x590.argparser.option.StringOption;
import x590.jdecompiler.main.performing.AbstractPerforming.PerformingType;
import x590.util.ObjectHolder;

public class Config {
	
	public enum UsagePolicy {
		ALWAYS, AUTO, NEVER;
		
		
		public boolean isAlways() {
			return this == ALWAYS;
		}
		
		public boolean isAuto() {
			return this == AUTO;
		}
		
		public boolean isNever() {
			return this == NEVER;
		}
		
		
		public boolean isNotAlways() {
			return this != ALWAYS;
		}
		
		public boolean isNotAuto() {
			return this != AUTO;
		}
		
		public boolean isNotNever() {
			return this != NEVER;
		}
	}
	
	
	private boolean writeToConsole = JDecompiler.isDebug();
	
	private boolean showAutogenerated;
	private boolean showSynthetic;
	private boolean showBridge;
	
	private String indent = "    ";
	
	private UsagePolicy constantsUsagePolicy = UsagePolicy.ALWAYS;
	private UsagePolicy hexNumbersUsagePolicy = UsagePolicy.AUTO;
	
	private char longSuffix = 'L';
	private char floatSuffix = 'F';
	private char doubleSuffix = 'D';
	
	private boolean printDoubleSuffix;
	private boolean printTrailingZero = true;
	
	private boolean escapeUnicodeChars;
	private boolean printClassVersion = true;
	private boolean printImplicitModifiers = true;
	
	private boolean multilineStringAllowed = true;
	private boolean shortArrayInitAllowed = true;
	
	private boolean canOmitCurlyBrackets = true;
	private boolean canOmitThisAndClass = true;
	private boolean canOmitSingleImport;
	
	private boolean useOverrideAnnotation = true;
	private boolean useCStyleArray;

	private boolean printBracketsAroundBitwiseOperands = true;
	private boolean decompileStringBuilderAsConcatenation = true;
	
	private boolean canSearchNestedClasses = true;
	
	
	static ArgsNamespace parseArguments(String[] args, ObjectHolder<PerformingType> performingTypeHolder, Builder builder) {
		return new StandartArgParser("JDecompiler", Version.VERSION).localize()
				
				.add(new StringOption("files")        .oneOrMoreTimes()                                                    .help("Files to be processed"))
				.add(new Flag("-ds", "--disassemble") .onParse(() -> performingTypeHolder.set(PerformingType.DISASSEMBLE)) .help("Disassemble java class"))
				.add(new Flag("-as", "--assemble")    .onParse(() -> performingTypeHolder.set(PerformingType.ASSEMBLE))    .help("Assemble java class"))
				.add(new Flag("-oc", "--console")     .onParse(builder::writeToConsole)                                    .help("Write to console"))
				
				.add(new Flag("-a", "--autogenerated")     .onParse(builder::showAutogenerated)    .help("Show synthetic fields, methods and classes generated by compiler"))
				.add(new Flag("-s", "--synthetic")         .onParse(builder::showSynthetic)        .help("Show bridge methods generated by compiler"))
				.add(new Flag("-b", "--bridge")            .onParse(builder::showBridge)           .help("Show methods autogenerated by compiler (such as Enum.valueOf(String) or constructor by default)"))
				.add(new Flag("-A", "--all-autogen")       .onParse(builder::showAllAutogenerated) .help("Show synthetic, bridge and autogenerated fields, methods and classes"))
				
				.add(new StringOption("-i", "--indent") .onParse(      builder::setIndent)      .help("Set indent (by default four spaces)"))
				.add(new Flag("-t", "--tab")            .onParse(() -> builder.setIndent("\t")) .help("Use tab as indent"))
				
				.add(new EnumOption<>(UsagePolicy.class, "-c", "--constants")
						.onParse(builder::constantsUsagePolicy)
						.implicitValue(UsagePolicy.ALWAYS)
						.help("Use constants: always - always use, auto - use only general constants (Integer.MAX_VALUE, Math.PI, etc.), never - never use"))
				
				.add(new EnumOption<>(UsagePolicy.class, "-x", "--hex")
						.onParse(builder::hexNumbersUsagePolicy)
						.implicitValue(UsagePolicy.ALWAYS)
						.help("Use hex numbers: always - always use, min - only for values like 0x7F, 0x80 and 0xFF, never - never use"))
				
				
				.add(new Flag("--low-literals")     .onParse(   (builder::useLowerSuffixes))   .help("Print lower letter literals for long, float and double values"))
				.add(new Flag("--double-suffix")    .onParse(   (builder::printDoubleSuffix))  .help("Print suffix for double values"))
				.add(new Flag("--no-trailing-zero") .onParse(not(builder::printTrailingZero))  .help("No print trailing zero for float and double values"))
				.add(new Flag("--esc-utf")          .onParse(   (builder::escapeUnicodeChars)) .help("Escape multibyte unicode characters"))
				
				.add(new Flag("-V", "--no-version")            .onParse(not(builder::printClassVersion))      .help("Don't print version for each class"))
				.add(new Flag("--no-print-implicit-modifiers") .onParse(not(builder::printImplicitModifiers)) .help("Don't print implied modifiers (`public` for interfaces or `private` for enum constructor)"))
				.add(new Flag("-M", "--no-multiline-string")   .onParse(not(builder::multilineStringAllowed)) .help("Don't print multiline strings"))
				.add(new Flag("--no-short-arr-init")           .onParse(not(builder::shortArrayInitAllowed))  .help("Don't use short array initialization (like int[] arr = {})"))
				.add(new Flag("--no-omit-curly-brackets")      .onParse(not(builder::canOmitCurlyBrackets))   .help("No omit curly brackets if scope contains zero or one expression"))
				.add(new Flag("--no-omit-this-and-class")      .onParse(not(builder::canOmitThisAndClass))    .help("No omit `this` keyword and this class in fields and methods access"))
				.add(new Flag("--omit-single-import")          .onParse(   (builder::canOmitSingleImport))    .help("Omit import if class uses only one time"))
				.add(new Flag("--no-use-override")             .onParse(not(builder::canOmitCurlyBrackets))   .help("Don't use the \"java.lang.Override\" annotation"))
				.add(new Flag("--c-style-array")               .onParse(   (builder::useCStyleArray))         .help("Use C-style array declaration"))
				
				.add(new Flag("--no-brackets-around-bitwise-operands")          .onParse(not(builder::printBracketsAroundBitwiseOperands)))
				.add(new Flag("--no-decompile-string-builder-as-concatenation") .onParse(not(builder::decompileStringBuilderAsConcatenation)))
				.add(new Flag("--no-search-nested-classes")                     .onParse(not(builder::canSearchNestedClasses)) .help("Do not search for nested classes in the directory from which the outer class is loaded"))
				
				.parse(args);
	}
	
	private static Consumer<Boolean> not(BooleanConsumer consumer) {
		return value -> consumer.accept(!value);
	}
	
	public boolean writeToConsole() {
		return writeToConsole;
	}
	
	
	public String getIndent() {
		return indent;
	}
	
	
	public boolean showAutogenerated() {
		return showAutogenerated;
	}
	
	
	public boolean showSynthetic() {
		return showSynthetic;
	}
	
	
	public boolean showBridge() {
		return showBridge;
	}
	
	
	public UsagePolicy constantsUsagePolicy() {
		return constantsUsagePolicy;
	}
	
	public UsagePolicy hexNumbersUsagePolicy() {
		return hexNumbersUsagePolicy;
	}
	
	
	public char getLongSuffix() {
		return longSuffix;
	}
	
	public char getFloatSuffix() {
		return floatSuffix;
	}
	
	public char getDoubleSuffix() {
		return doubleSuffix;
	}
	
	public boolean printDoubleSuffix() {
		return printDoubleSuffix;
	}
	
	public boolean printTrailingZero() {
		return printTrailingZero;
	}
	
	
	public boolean escapeUnicodeChars() {
		return escapeUnicodeChars;
	}
	
	
	public boolean printClassVersion() {
		return printClassVersion;
	}
	
	public boolean printImplicitModifiers() {
		return printImplicitModifiers;
	}
	
	
	public boolean multilineStringAllowed() {
		return multilineStringAllowed;
	}
	
	public boolean shortArrayInitAllowed() {
		return shortArrayInitAllowed;
	}
	
	
	public boolean canOmitCurlyBrackets() {
		return canOmitCurlyBrackets;
	}
	
	public boolean canOmitThisAndClass() {
		return canOmitThisAndClass;
	}
	
	public boolean canOmitSingleImport() {
		return canOmitSingleImport;
	}
	
	
	public boolean useOverrideAnnotation() {
		return useOverrideAnnotation;
	}
	
	public boolean useCStyleArray() {
		return useCStyleArray;
	}
	
	
	public boolean printBracketsAroundBitwiseOperands() {
		return printBracketsAroundBitwiseOperands;
	}
	
	public boolean decompileStringBuilderAsConcatenation() {
		return decompileStringBuilderAsConcatenation;
	}
	
	
	public boolean canSearchNestedClasses() {
		return canSearchNestedClasses;
	}
	
	
	private Config() {}
	
	/** Возвращает новый конфиг со значениями по умолчанию */
	public static Config newDefaultConfig() {
		return new Config();
	}
	
	/** Создаёт новый {@link Builder} и возвращает его */
	public static Builder newBuilder() {
		return new Builder(new Config());
	}
	
	
	public static class Builder {
		
		private Config config;
		
		private Builder(Config config) {
			this.config = config;
		}
		
		/** Возвращает построенный объект, после чего использоапние билдера становится невозможным */
		public Config build() {
			Config config = this.config;
			this.config = null;
			
			return config;
		}
		
		
		public Builder writeToConsole(boolean writeToConsole) {
			config.writeToConsole = writeToConsole;
			return this;
		}
		
		
		public Builder setIndent(String indent) {
			config.indent = Objects.requireNonNull(indent);
			return this;
		}
		
		
		public Builder showAutogenerated(boolean showAutogenerated) {
			config.showAutogenerated = showAutogenerated;
			return this;
		}
		
		public Builder showSynthetic(boolean showSynthetic) {
			config.showSynthetic = showSynthetic;
			return this;
		}
		
		public Builder showBridge(boolean showBridge) {
			config.showBridge = showBridge;
			return this;
		}
		
		public Builder showAllAutogenerated(boolean show) {
			var config = this.config;
			config.showAutogenerated = config.showSynthetic = config.showBridge = show;
			
			return this;
		}
		
		
		public Builder constantsUsagePolicy(UsagePolicy constantsUsagePolicy) {
			config.constantsUsagePolicy = Objects.requireNonNull(constantsUsagePolicy);
			return this;
		}
		
		public Builder hexNumbersUsagePolicy(UsagePolicy hexNumbersUsagePolicy) {
			config.hexNumbersUsagePolicy = Objects.requireNonNull(hexNumbersUsagePolicy);
			return this;
		}
		
		
		public Builder useLowerSuffixes(boolean useLowerSuffixes) {
			var config = this.config;
			
			if(useLowerSuffixes) {
				config.longSuffix = 'l';
				config.floatSuffix = 'f';
				config.doubleSuffix = 'd';
			} else {
				config.longSuffix = 'L';
				config.floatSuffix = 'F';
				config.doubleSuffix = 'D';
			}
			
			return this;
		}
		
		public Builder printDoubleSuffix(boolean printDoubleSuffix) {
			config.printDoubleSuffix = printDoubleSuffix;
			return this;
		}
		
		public Builder printTrailingZero(boolean printTrailingZero) {
			config.printTrailingZero = printTrailingZero;
			return this;
		}
		
		
		public Builder escapeUnicodeChars(boolean escapeUnicodeChars) {
			config.escapeUnicodeChars = escapeUnicodeChars;
			return this;
		}
		
		
		public Builder printClassVersion(boolean printClassVersion) {
			config.printClassVersion = printClassVersion;
			return this;
		}
		
		public Builder printImplicitModifiers(boolean printImplicitModifiers) {
			config.printImplicitModifiers = printImplicitModifiers;
			return this;
		}
		
		
		public Builder multilineStringAllowed(boolean multilineStringAllowed) {
			config.multilineStringAllowed = multilineStringAllowed;
			return this;
		}
		
		public Builder shortArrayInitAllowed(boolean shortArrayInitAllowed) {
			config.shortArrayInitAllowed = shortArrayInitAllowed;
			return this;
		}
		
		
		public Builder canOmitCurlyBrackets(boolean canOmitCurlyBrackets) {
			config.canOmitCurlyBrackets = canOmitCurlyBrackets;
			return this;
		}
		
		public Builder canOmitThisAndClass(boolean canOmitThisAndClass) {
			config.canOmitThisAndClass = canOmitThisAndClass;
			return this;
		}
		
		
		public Builder canOmitSingleImport(boolean canOmitSingleImport) {
			config.canOmitSingleImport = canOmitSingleImport;
			return this;
		}
		
		
		public Builder useOverrideAnnotation(boolean useOverrideAnnotation) {
			config.useOverrideAnnotation = useOverrideAnnotation;
			return this;
		}
		
		public Builder useCStyleArray(boolean useCStyleArray) {
			config.useCStyleArray = useCStyleArray;
			return this;
		}
		
		
		public Builder printBracketsAroundBitwiseOperands(boolean printBracketsAroundBitwiseOperands) {
			config.printBracketsAroundBitwiseOperands = printBracketsAroundBitwiseOperands;
			return this;
		}
		
		public Builder decompileStringBuilderAsConcatenation(boolean decompileStringBuilderAsConcatenation) {
			config.decompileStringBuilderAsConcatenation = decompileStringBuilderAsConcatenation;
			return this;
		}
		
		public Builder canSearchNestedClasses(boolean canSearchNestedClasses) {
			config.canSearchNestedClasses = canSearchNestedClasses;
			return this;
		}
	}
}
