package x590.jdecompiler;

import x590.jdecompiler.clazz.JavaClass;
import x590.util.holder.ObjectHolder;

/**
 * Стадия декомпиляции. Используется для контроля правильного использования {@link JavaClass}
 */
public enum DecompilationStage {
	DISASSEMBLED        ("Cannot disassemble class", "class yet not disassembled", "class already disassembled"),
	DECOMPILED          ("Cannot decompile class",   "class yet not decompiled",   "class already decompiled"),
	AFTER_DECOMPILATION ("Cannot run afterDecompilation", "afterDecompilation has not been run yet", "afterDecompilation has already been run"),
	IMPORTS_RESOLVED    ("Cannot resolve imports in class",   "imports yet not resolved",   "imports already resolved"),
	WRITTEN             ("Cannot write class",       "class yet not written",      "class already written");
	
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
	
	public void check(JavaClass javaClass, DecompilationStage requiredStage, DecompilationStage nextStage) {
		if(this != requiredStage) {
			throw new IllegalStateException(nextStage.exceptionMessage + ' ' +
					javaClass.getThisType().getName() + ": " +
					(this.ordinal() < nextStage.ordinal() ? next.earlyReason : nextStage.repeatedReason));
		}
	}
	
	
	public static class DecompilationStageHolder extends ObjectHolder<DecompilationStage> {
		
		public DecompilationStageHolder(DecompilationStage stage) {
			super(stage);
		}

		public void checkStage(JavaClass javaClass, DecompilationStage requiredStage, DecompilationStage nextStage) {
			get().check(javaClass, requiredStage, nextStage);
		}

		public void nextStage(JavaClass javaClass, DecompilationStage requiredStage, DecompilationStage nextStage) {
			get().check(javaClass, requiredStage, nextStage);
			set(nextStage);
		}
	}
}
