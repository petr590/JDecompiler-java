package x590.jdecompiler.instruction;

import x590.jdecompiler.instruction.scope.MonitorEnterInstruction;
import x590.jdecompiler.instruction.scope.MonitorExitInstruction;
import x590.jdecompiler.type.TypeSize;
import x590.jdecompiler.instruction.arrayload.*;
import x590.jdecompiler.instruction.arraystore.*;
import x590.jdecompiler.instruction.cmp.*;
import x590.jdecompiler.instruction.constant.*;
import x590.jdecompiler.instruction.dup.*;
import x590.jdecompiler.instruction.load.*;
import x590.jdecompiler.instruction.operator.*;
import x590.jdecompiler.instruction.returning.*;
import x590.jdecompiler.instruction.store.*;

import static x590.jdecompiler.type.PrimitiveType.*;

public class Instructions {
	
	public static final Instruction
			ACONST_NULL = AConstNullInstruction.INSTANCE,
			
			ICONST_M1 = new IPushInstruction(-1),
			ICONST_0  = new IPushInstruction(0),
			ICONST_1  = new IPushInstruction(1),
			ICONST_2  = new IPushInstruction(2),
			ICONST_3  = new IPushInstruction(3),
			ICONST_4  = new IPushInstruction(4),
			ICONST_5  = new IPushInstruction(5),
			
			LCONST_0  = new LPushInstruction(0),
			LCONST_1  = new LPushInstruction(1),
			
			FCONST_0  = new FPushInstruction(0),
			FCONST_1  = new FPushInstruction(1),
			FCONST_2  = new FPushInstruction(2),
			
			DCONST_0  = new DPushInstruction(0),
			DCONST_1  = new DPushInstruction(1),
			
			
			ILOAD_0 = new ILoadInstruction(0),
			ILOAD_1 = new ILoadInstruction(1),
			ILOAD_2 = new ILoadInstruction(2),
			ILOAD_3 = new ILoadInstruction(3),
			
			LLOAD_0 = new LLoadInstruction(0),
			LLOAD_1 = new LLoadInstruction(1),
			LLOAD_2 = new LLoadInstruction(2),
			LLOAD_3 = new LLoadInstruction(3),
			
			FLOAD_0 = new FLoadInstruction(0),
			FLOAD_1 = new FLoadInstruction(1),
			FLOAD_2 = new FLoadInstruction(2),
			FLOAD_3 = new FLoadInstruction(3),
			
			DLOAD_0 = new DLoadInstruction(0),
			DLOAD_1 = new DLoadInstruction(1),
			DLOAD_2 = new DLoadInstruction(2),
			DLOAD_3 = new DLoadInstruction(3),
			
			ALOAD_0 = new ALoadInstruction(0),
			ALOAD_1 = new ALoadInstruction(1),
			ALOAD_2 = new ALoadInstruction(2),
			ALOAD_3 = new ALoadInstruction(3),
			
			
			IALOAD = new IALoadInstruction(),
			LALOAD = new LALoadInstruction(),
			FALOAD = new FALoadInstruction(),
			DALOAD = new DALoadInstruction(),
			AALOAD = new AALoadInstruction(),
			BALOAD = new BALoadInstruction(),
			CALOAD = new CALoadInstruction(),
			SALOAD = new SALoadInstruction(),
			
			
			ISTORE_0 = new IStoreInstruction(0),
			ISTORE_1 = new IStoreInstruction(1),
			ISTORE_2 = new IStoreInstruction(2),
			ISTORE_3 = new IStoreInstruction(3),
			
			LSTORE_0 = new LStoreInstruction(0),
			LSTORE_1 = new LStoreInstruction(1),
			LSTORE_2 = new LStoreInstruction(2),
			LSTORE_3 = new LStoreInstruction(3),
			
			FSTORE_0 = new FStoreInstruction(0),
			FSTORE_1 = new FStoreInstruction(1),
			FSTORE_2 = new FStoreInstruction(2),
			FSTORE_3 = new FStoreInstruction(3),
			
			DSTORE_0 = new DStoreInstruction(0),
			DSTORE_1 = new DStoreInstruction(1),
			DSTORE_2 = new DStoreInstruction(2),
			DSTORE_3 = new DStoreInstruction(3),
			
			ASTORE_0 = new AStoreInstruction(0),
			ASTORE_1 = new AStoreInstruction(1),
			ASTORE_2 = new AStoreInstruction(2),
			ASTORE_3 = new AStoreInstruction(3),
			
			IASTORE = new IAStoreInstruction(),
			LASTORE = new LAStoreInstruction(),
			FASTORE = new FAStoreInstruction(),
			DASTORE = new DAStoreInstruction(),
			AASTORE = new AAStoreInstruction(),
			BASTORE = new BAStoreInstruction(),
			CASTORE = new CAStoreInstruction(),
			SASTORE = new SAStoreInstruction(),
			
			
			POP     = new PopInstruction(TypeSize.WORD),
			POP2    = new PopInstruction(TypeSize.LONG),
			DUP     = new DupInstruction(),
			DUP_X1  = new DupX1Instruction(),
			DUP_X2  = new DupX2Instruction(),
			DUP2    = new Dup2Instruction(),
			DUP2_X1 = new Dup2X1Instruction(),
			DUP2_X2 = new Dup2X2Instruction(),
			SWAP    = new SwapInstruction(),
			
			
			IADD = new AddOperatorInstruction(BYTE_SHORT_INT_CHAR),
			LADD = new AddOperatorInstruction(LONG),
			FADD = new AddOperatorInstruction(FLOAT),
			DADD = new AddOperatorInstruction(DOUBLE),
			ISUB = new SubOperatorInstruction(BYTE_SHORT_INT_CHAR),
			LSUB = new SubOperatorInstruction(LONG),
			FSUB = new SubOperatorInstruction(FLOAT),
			DSUB = new SubOperatorInstruction(DOUBLE),
			IMUL = new MulOperatorInstruction(BYTE_SHORT_INT_CHAR),
			LMUL = new MulOperatorInstruction(LONG),
			FMUL = new MulOperatorInstruction(FLOAT),
			DMUL = new MulOperatorInstruction(DOUBLE),
			IDIV = new DivOperatorInstruction(BYTE_SHORT_INT_CHAR),
			LDIV = new DivOperatorInstruction(LONG),
			FDIV = new DivOperatorInstruction(FLOAT),
			DDIV = new DivOperatorInstruction(DOUBLE),
			IREM = new RemOperatorInstruction(BYTE_SHORT_INT_CHAR),
			LREM = new RemOperatorInstruction(LONG),
			FREM = new RemOperatorInstruction(FLOAT),
			DREM = new RemOperatorInstruction(DOUBLE),
			INEG = new NegOperatorInstruction(BYTE_SHORT_INT_CHAR),
			LNEG = new NegOperatorInstruction(LONG),
			FNEG = new NegOperatorInstruction(FLOAT),
			DNEG = new NegOperatorInstruction(DOUBLE),
			
			ISHL  = new ShiftLeftOperatorInstruction(BYTE_SHORT_INT_CHAR),
			LSHL  = new ShiftLeftOperatorInstruction(LONG),
			ISHR  = new ShiftRightOperatorInstruction(BYTE_SHORT_INT_CHAR),
			LSHR  = new ShiftRightOperatorInstruction(LONG),
			IUSHR = new UShiftRightOperatorInstruction(BYTE_SHORT_INT_CHAR),
			LUSHR = new UShiftRightOperatorInstruction(LONG),
			IAND  = new AndOperatorInstruction(BYTE_SHORT_INT_CHAR_BOOLEAN),
			LAND  = new AndOperatorInstruction(LONG),
			IOR   = new OrOperatorInstruction(BYTE_SHORT_INT_CHAR_BOOLEAN),
			LOR   = new OrOperatorInstruction(LONG),
			IXOR  = new XorOperatorInstruction(BYTE_SHORT_INT_CHAR_BOOLEAN),
			LXOR  = new XorOperatorInstruction(LONG),
			
			
			I2L = new CastInstruction(INT,    LONG,   true),
			I2F = new CastInstruction(INT,    FLOAT,  true),
			I2D = new CastInstruction(INT,    DOUBLE, true),
			L2I = new CastInstruction(LONG,   INT,    false),
			L2F = new CastInstruction(LONG,   FLOAT,  true),
			L2D = new CastInstruction(LONG,   DOUBLE, true),
			F2I = new CastInstruction(FLOAT,  INT,    false),
			F2L = new CastInstruction(FLOAT,  LONG,   false),
			F2D = new CastInstruction(FLOAT,  DOUBLE, true),
			D2I = new CastInstruction(DOUBLE, INT,    false),
			D2L = new CastInstruction(DOUBLE, LONG,   false),
			D2F = new CastInstruction(DOUBLE, FLOAT,  false),
			I2B = new CastInstruction(INT,    BYTE,   false),
			I2C = new CastInstruction(INT,    CHAR,   false),
			I2S = new CastInstruction(INT,    SHORT,  false),
			
			
			LCMP = new LCmpInstruction(),
			FCMP = new FCmpInstruction(),
			DCMP = new DCmpInstruction(),
			
			
			IRETURN = new IReturnInstruction(),
			LRETURN = new LReturnInstruction(),
			FRETURN = new FReturnInstruction(),
			DRETURN = new DReturnInstruction(),
			ARETURN = new AReturnInstruction(),
			VRETURN = new VReturnInstruction(),
			
			ARRAYLENGTH = new ArrayLengthInstruction(),
			ATHROW = new AThrowInstruction(),
			
			MONITORENTER = new MonitorEnterInstruction(),
			MONITOREXIT = new MonitorExitInstruction();
}
