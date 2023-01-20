package x590.jdecompiler;

/**
 * Стадия декомпиляции. Используется для контроля правильного использования {@link JavaClass}
 */
enum DecompilationStage {
	DISASSEMBLED     ("Cannot disassemble class", "class yet not disassembled", "class already disassembled"),
	DECOMPILED       ("Cannot decompile class",   "class yet not decompiled",   "class already decompiled"),
	IMPORTS_RESOLVED ("Cannot resolve imports",   "imports yet not resolved",   "imports already resolved"),
	WRITTEN          ("Cannot write class",       "class yet not written",      "class already written");
	
	private final String exceptionMessage, earlyReason, repeatedReason;
	private DecompilationStage next;
	
	
	static {
		DecompilationStage[] values = values();
		
		int end = values.length - 1;
		
		for(int i = 0; i < end; )
			values[i].next = values[++i];
		
		values[end].next = values[end];
	}
	
	
	private DecompilationStage(String exceptionMessage, String earlyReason, String repeatedReason) {
		this.exceptionMessage = exceptionMessage;
		this.earlyReason = earlyReason;
		this.repeatedReason = repeatedReason;
	}
	
	String getExceptionMessage() {
		return exceptionMessage;
	}
	
	String getEarlyReason() {
		return earlyReason;
	}
	
	String getRepeatedReason() {
		return repeatedReason;
	}
	
	DecompilationStage next() {
		return next;
	}
}
