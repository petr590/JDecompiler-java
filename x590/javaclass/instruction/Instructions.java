package x590.javaclass.instruction;

import x590.javaclass.instruction.scope.MonitorEnterInstruction;
import x590.javaclass.instruction.scope.MonitorExitInstruction;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.TypeSize;

public class Instructions {
	
	public static final Instruction
			ACONST_NULL = AConstNullInstruction.INSTANCE,
			
			ICONST_M1 = new IConstInstruction(-1),
			ICONST_0  = new IConstInstruction(0),
			ICONST_1  = new IConstInstruction(1),
			ICONST_2  = new IConstInstruction(2),
			ICONST_3  = new IConstInstruction(3),
			ICONST_4  = new IConstInstruction(4),
			ICONST_5  = new IConstInstruction(5),
			
			LCONST_0  = new LConstInstruction(0),
			LCONST_1  = new LConstInstruction(1),
			
			FCONST_0  = new FConstInstruction(0),
			FCONST_1  = new FConstInstruction(1),
			FCONST_2  = new FConstInstruction(2),
			
			DCONST_0  = new DConstInstruction(0),
			DCONST_1  = new DConstInstruction(1),
			
			
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
			
			
			POP     = new PopInstruction(TypeSize.FOUR_BYTES),
			POP2    = new PopInstruction(TypeSize.EIGHT_BYTES),
			DUP     = new DupInstruction(),
			DUP_X1  = new DupX1Instruction(),
			DUP_X2  = new DupX2Instruction(),
			DUP2    = new Dup2Instruction(),
			DUP2_X1 = new Dup2X1Instruction(),
			DUP2_X2 = new Dup2X2Instruction(),
			SWAP    = new SwapInstruction(),
			
			
			IADD = new AddOperatorInstruction(PrimitiveType.ANY_INT_OR_BOOLEAN),
			LADD = new AddOperatorInstruction(PrimitiveType.LONG),
			FADD = new AddOperatorInstruction(PrimitiveType.FLOAT),
			DADD = new AddOperatorInstruction(PrimitiveType.DOUBLE),
			ISUB = new SubOperatorInstruction(PrimitiveType.ANY_INT_OR_BOOLEAN),
			LSUB = new SubOperatorInstruction(PrimitiveType.LONG),
			FSUB = new SubOperatorInstruction(PrimitiveType.FLOAT),
			DSUB = new SubOperatorInstruction(PrimitiveType.DOUBLE),
			IMUL = new MulOperatorInstruction(PrimitiveType.ANY_INT_OR_BOOLEAN),
			LMUL = new MulOperatorInstruction(PrimitiveType.LONG),
			FMUL = new MulOperatorInstruction(PrimitiveType.FLOAT),
			DMUL = new MulOperatorInstruction(PrimitiveType.DOUBLE),
			IDIV = new DivOperatorInstruction(PrimitiveType.ANY_INT_OR_BOOLEAN),
			LDIV = new DivOperatorInstruction(PrimitiveType.LONG),
			FDIV = new DivOperatorInstruction(PrimitiveType.FLOAT),
			DDIV = new DivOperatorInstruction(PrimitiveType.DOUBLE),
			IREM = new RemOperatorInstruction(PrimitiveType.ANY_INT_OR_BOOLEAN),
			LREM = new RemOperatorInstruction(PrimitiveType.LONG),
			FREM = new RemOperatorInstruction(PrimitiveType.FLOAT),
			DREM = new RemOperatorInstruction(PrimitiveType.DOUBLE),
			INEG = new NegOperatorInstruction(PrimitiveType.ANY_INT_OR_BOOLEAN),
			LNEG = new NegOperatorInstruction(PrimitiveType.LONG),
			FNEG = new NegOperatorInstruction(PrimitiveType.FLOAT),
			DNEG = new NegOperatorInstruction(PrimitiveType.DOUBLE),
			
			ISHL  = new ShiftLeftOperatorInstruction(PrimitiveType.ANY_INT_OR_BOOLEAN),
			LSHL  = new ShiftLeftOperatorInstruction(PrimitiveType.LONG),
			ISHR  = new ShiftRightOperatorInstruction(PrimitiveType.ANY_INT_OR_BOOLEAN),
			LSHR  = new ShiftRightOperatorInstruction(PrimitiveType.LONG),
			IUSHR = new UShiftRightOperatorInstruction(PrimitiveType.ANY_INT_OR_BOOLEAN),
			LUSHR = new UShiftRightOperatorInstruction(PrimitiveType.LONG),
			IAND  = new AndOperatorInstruction(PrimitiveType.ANY_INT_OR_BOOLEAN),
			LAND  = new AndOperatorInstruction(PrimitiveType.LONG),
			IOR   = new OrOperatorInstruction(PrimitiveType.ANY_INT_OR_BOOLEAN),
			LOR   = new OrOperatorInstruction(PrimitiveType.LONG),
			IXOR  = new XorOperatorInstruction(PrimitiveType.ANY_INT_OR_BOOLEAN),
			LXOR  = new XorOperatorInstruction(PrimitiveType.LONG),
			
			
			I2L = new CastInstruction(PrimitiveType.INT,    PrimitiveType.LONG,   false),
			I2F = new CastInstruction(PrimitiveType.INT,    PrimitiveType.FLOAT,  false),
			I2D = new CastInstruction(PrimitiveType.INT,    PrimitiveType.DOUBLE, false),
			L2I = new CastInstruction(PrimitiveType.LONG,   PrimitiveType.INT,    true),
			L2F = new CastInstruction(PrimitiveType.LONG,   PrimitiveType.FLOAT,  false),
			L2D = new CastInstruction(PrimitiveType.LONG,   PrimitiveType.DOUBLE, false),
			F2I = new CastInstruction(PrimitiveType.FLOAT,  PrimitiveType.INT,    true),
			F2L = new CastInstruction(PrimitiveType.FLOAT,  PrimitiveType.LONG,   true),
			F2D = new CastInstruction(PrimitiveType.FLOAT,  PrimitiveType.DOUBLE, false),
			D2I = new CastInstruction(PrimitiveType.DOUBLE, PrimitiveType.INT,    true),
			D2L = new CastInstruction(PrimitiveType.DOUBLE, PrimitiveType.LONG,   true),
			D2F = new CastInstruction(PrimitiveType.DOUBLE, PrimitiveType.FLOAT,  true),
			I2B = new CastInstruction(PrimitiveType.INT,    PrimitiveType.BYTE,   true),
			I2C = new CastInstruction(PrimitiveType.INT,    PrimitiveType.CHAR,   true),
			I2S = new CastInstruction(PrimitiveType.INT,    PrimitiveType.SHORT,  true),
			
			
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