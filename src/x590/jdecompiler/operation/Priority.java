package x590.jdecompiler.operation;

/**
 * Хранит приоритеты операций
 */
public final class Priority {
	
	private Priority() {}
	
	public static final int
			DEFAULT_PRIORITY         = 17,
			POST_INCREMENT           = 16,
			UNARY                    = 15, PRE_INCREMENT = UNARY, BIT_NOT = UNARY, LOGICAL_NOT = UNARY, UNARY_PLUS = UNARY, UNARY_MINUS = UNARY,
			CAST                     = 14,
			BITWISE_OPERAND          = 13,	// Промежуточный приоритет между тем, что надо оборачивать в скобки в битовых операциях,
											// и тем, что не надо. используется в BitwiseOperatorOperation#getVisiblePriority
			MULTIPLE                 = 12, DIVISION = MULTIPLE, REMAINDER = MULTIPLE,
			PLUS                     = 11, MINUS = PLUS,
			SHIFT                    = 10,
			GREATER_LESS_COMPARASION = 9,  INSTANCEOF = GREATER_LESS_COMPARASION,
			EQUALS_COMPARASION       = 8,
			BIT_AND                  = 7,
			BIT_XOR                  = 6,
			BIT_OR                   = 5,
			LOGICAL_AND              = 4,
			LOGICAL_OR               = 3,
			TERNARY_OPERATOR         = 2,
			ASSIGNMENT               = 1,
			LAMBDA                   = 0;
}
