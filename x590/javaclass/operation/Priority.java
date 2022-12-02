package x590.javaclass.operation;

public class Priority {
	
	public static final int
			DEFAULT_PRIORITY         = 16,
			POST_INCREMENT           = 15,
			UNARY                    = 14, PRE_INCREMENT = UNARY, BIT_NOT = UNARY, LOGICAL_NOT = UNARY, UNARY_PLUS = UNARY, UNARY_MINUS = UNARY,
			CAST                     = 13,
			MUL_DIV_REM              = 12, MULTIPLE = MUL_DIV_REM, DIVISION = MUL_DIV_REM, REMAINDER = MUL_DIV_REM,
			PLUS_MINUS               = 11, PLUS = PLUS_MINUS, MINUS = PLUS_MINUS,
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