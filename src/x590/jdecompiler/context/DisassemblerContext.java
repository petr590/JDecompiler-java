package x590.jdecompiler.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import x590.jdecompiler.exception.InstructionFormatException;
import x590.jdecompiler.exception.InvalidOpcodeException;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.instruction.*;
import x590.jdecompiler.instruction.array.*;
import x590.jdecompiler.instruction.constant.*;
import x590.jdecompiler.instruction.field.*;
import x590.jdecompiler.instruction.invoke.*;
import x590.jdecompiler.instruction.load.*;
import x590.jdecompiler.instruction.scope.*;
import x590.jdecompiler.instruction.store.*;
import x590.jdecompiler.type.TypeSize;
import x590.util.IntegerUtil;
import x590.util.Logger;
import x590.util.annotation.Nullable;

public class DisassemblerContext extends Context {
	
	public static final byte[] EMPTY_DATA = {};
	
	private final byte[] bytes;
	
	private int pos;
	private final List<Instruction> instructions;
	
	private DisassemblerContext(ConstantPool pool, byte[] bytes) {
		super(pool, bytes.length);
		this.bytes = bytes;
		
		var length = bytes.length;
		
		if(length == 0) {
			this.instructions = Collections.emptyList();
			return;
		}
		
		List<Instruction> instructions = new ArrayList<>(length);
		
		while(pos < length) {
			indexMap[pos] = index;
			posMap[index] = pos;
			
			Instruction instruction = readInstruction();
			
			if(instruction != null) {
				instructions.add(instruction);
				index++;
			}
			
			pos++;
		}
		
		this.instructions = Collections.unmodifiableList(instructions);
	}
	
	
	public static DisassemblerContext disassemble(ConstantPool pool, byte[] bytes) {
		return new DisassemblerContext(pool, bytes);
	}
	
	public static DisassemblerContext empty(ConstantPool pool) {
		return new DisassemblerContext(pool, EMPTY_DATA);
	}
	
	
	public List<Instruction> getInstructions() {
		return instructions;
	}
	
	
	private int readByte() {
		return bytes[pos += 1];
	}
	
	private int readShort() {
		return (short)((bytes[pos + 1] & 0xFF) << 8 | bytes[pos += 2] & 0xFF);
	}
	
	private int readInt() {
		return  (bytes[pos + 1] & 0xFF) << 24 | (bytes[pos + 2] & 0xFF) << 16 |
				(bytes[pos + 3] & 0xFF) << 8  | bytes[pos += 4] & 0xFF;
	}
	
	private int readUnsignedByte() {
		return readByte() & 0xFF;
	}
	
	private int readUnsignedShort() {
		return readShort() & 0xFFFF;
	}
	
	private void skip(int skip) {
		pos += skip;
	}
	
	
	private @Nullable Instruction readInstruction() {
		switch(bytes[pos] & 0xFF) {
			case 0x00: return null;
			case 0x01: return Instructions.ACONST_NULL;
			case 0x02: return Instructions.ICONST_M1;
			case 0x03: return Instructions.ICONST_0;
			case 0x04: return Instructions.ICONST_1;
			case 0x05: return Instructions.ICONST_2;
			case 0x06: return Instructions.ICONST_3;
			case 0x07: return Instructions.ICONST_4;
			case 0x08: return Instructions.ICONST_5;
			case 0x09: return Instructions.LCONST_0;
			case 0x0A: return Instructions.LCONST_1;
			case 0x0B: return Instructions.FCONST_0;
			case 0x0C: return Instructions.FCONST_1;
			case 0x0D: return Instructions.FCONST_2;
			case 0x0E: return Instructions.DCONST_0;
			case 0x0F: return Instructions.DCONST_1;
			case 0x10: return new IPushInstruction(readByte());
			case 0x11: return new IPushInstruction(readShort());
			case 0x12: return new LdcInstruction(TypeSize.WORD, this, readUnsignedByte());
			case 0x13: return new LdcInstruction(TypeSize.WORD, this, readUnsignedShort());
			case 0x14: return new LdcInstruction(TypeSize.LONG, this, readUnsignedShort());
			
			case 0x15: return new ILoadInstruction(readUnsignedByte());
			case 0x16: return new LLoadInstruction(readUnsignedByte());
			case 0x17: return new FLoadInstruction(readUnsignedByte());
			case 0x18: return new DLoadInstruction(readUnsignedByte());
			case 0x19: return new ALoadInstruction(readUnsignedByte());
			case 0x1A: return Instructions.ILOAD_0;
			case 0x1B: return Instructions.ILOAD_1;
			case 0x1C: return Instructions.ILOAD_2;
			case 0x1D: return Instructions.ILOAD_3;
			case 0x1E: return Instructions.LLOAD_0;
			case 0x1F: return Instructions.LLOAD_1;
			case 0x20: return Instructions.LLOAD_2;
			case 0x21: return Instructions.LLOAD_3;
			case 0x22: return Instructions.FLOAD_0;
			case 0x23: return Instructions.FLOAD_1;
			case 0x24: return Instructions.FLOAD_2;
			case 0x25: return Instructions.FLOAD_3;
			case 0x26: return Instructions.DLOAD_0;
			case 0x27: return Instructions.DLOAD_1;
			case 0x28: return Instructions.DLOAD_2;
			case 0x29: return Instructions.DLOAD_3;
			case 0x2A: return Instructions.ALOAD_0;
			case 0x2B: return Instructions.ALOAD_1;
			case 0x2C: return Instructions.ALOAD_2;
			case 0x2D: return Instructions.ALOAD_3;
			
			case 0x2E: return Instructions.IALOAD;
			case 0x2F: return Instructions.LALOAD;
			case 0x30: return Instructions.FALOAD;
			case 0x31: return Instructions.DALOAD;
			case 0x32: return Instructions.AALOAD;
			case 0x33: return Instructions.BALOAD;
			case 0x34: return Instructions.CALOAD;
			case 0x35: return Instructions.SALOAD;
			
			case 0x36: return new IStoreInstruction(readUnsignedByte());
			case 0x37: return new LStoreInstruction(readUnsignedByte());
			case 0x38: return new FStoreInstruction(readUnsignedByte());
			case 0x39: return new DStoreInstruction(readUnsignedByte());
			case 0x3A: return new AStoreInstruction(readUnsignedByte());
			case 0x3B: return Instructions.ISTORE_0;
			case 0x3C: return Instructions.ISTORE_1;
			case 0x3D: return Instructions.ISTORE_2;
			case 0x3E: return Instructions.ISTORE_3;
			case 0x3F: return Instructions.LSTORE_0;
			case 0x40: return Instructions.LSTORE_1;
			case 0x41: return Instructions.LSTORE_2;
			case 0x42: return Instructions.LSTORE_3;
			case 0x43: return Instructions.FSTORE_0;
			case 0x44: return Instructions.FSTORE_1;
			case 0x45: return Instructions.FSTORE_2;
			case 0x46: return Instructions.FSTORE_3;
			case 0x47: return Instructions.DSTORE_0;
			case 0x48: return Instructions.DSTORE_1;
			case 0x49: return Instructions.DSTORE_2;
			case 0x4A: return Instructions.DSTORE_3;
			case 0x4B: return Instructions.ASTORE_0;
			case 0x4C: return Instructions.ASTORE_1;
			case 0x4D: return Instructions.ASTORE_2;
			case 0x4E: return Instructions.ASTORE_3;
			
			case 0x4F: return Instructions.IASTORE;
			case 0x50: return Instructions.LASTORE;
			case 0x51: return Instructions.FASTORE;
			case 0x52: return Instructions.DASTORE;
			case 0x53: return Instructions.AASTORE;
			case 0x54: return Instructions.BASTORE;
			case 0x55: return Instructions.CASTORE;
			case 0x56: return Instructions.SASTORE;
			
			case 0x57: return Instructions.POP;
			case 0x58: return Instructions.POP2;
			case 0x59: return Instructions.DUP;
			case 0x5A: return Instructions.DUP_X1;
			case 0x5B: return Instructions.DUP_X2;
			case 0x5C: return Instructions.DUP2;
			case 0x5D: return Instructions.DUP2_X1;
			case 0x5E: return Instructions.DUP2_X2;
			case 0x5F: return Instructions.SWAP;
			
			case 0x60: return Instructions.IADD;
			case 0x61: return Instructions.LADD;
			case 0x62: return Instructions.FADD;
			case 0x63: return Instructions.DADD;
			case 0x64: return Instructions.ISUB;
			case 0x65: return Instructions.LSUB;
			case 0x66: return Instructions.FSUB;
			case 0x67: return Instructions.DSUB;
			case 0x68: return Instructions.IMUL;
			case 0x69: return Instructions.LMUL;
			case 0x6A: return Instructions.FMUL;
			case 0x6B: return Instructions.DMUL;
			case 0x6C: return Instructions.IDIV;
			case 0x6D: return Instructions.LDIV;
			case 0x6E: return Instructions.FDIV;
			case 0x6F: return Instructions.DDIV;
			case 0x70: return Instructions.IREM;
			case 0x71: return Instructions.LREM;
			case 0x72: return Instructions.FREM;
			case 0x73: return Instructions.DREM;
			case 0x74: return Instructions.INEG;
			case 0x75: return Instructions.LNEG;
			case 0x76: return Instructions.FNEG;
			case 0x77: return Instructions.DNEG;
			
			case 0x78: return Instructions.ISHL;
			case 0x79: return Instructions.LSHL;
			case 0x7A: return Instructions.ISHR;
			case 0x7B: return Instructions.LSHR;
			case 0x7C: return Instructions.IUSHR;
			case 0x7D: return Instructions.LUSHR;
			case 0x7E: return Instructions.IAND;
			case 0x7F: return Instructions.LAND;
			case 0x80: return Instructions.IOR;
			case 0x81: return Instructions.LOR;
			case 0x82: return Instructions.IXOR;
			case 0x83: return Instructions.LXOR;
			
			case 0x84: return new IIncInstruction(readUnsignedByte(), readByte());
			
			case 0x85: return Instructions.I2L;
			case 0x86: return Instructions.I2F;
			case 0x87: return Instructions.I2D;
			case 0x88: return Instructions.L2I;
			case 0x89: return Instructions.L2F;
			case 0x8A: return Instructions.L2D;
			case 0x8B: return Instructions.F2I;
			case 0x8C: return Instructions.F2L;
			case 0x8D: return Instructions.F2D;
			case 0x8E: return Instructions.D2I;
			case 0x8F: return Instructions.D2L;
			case 0x90: return Instructions.D2F;
			case 0x91: return Instructions.I2B;
			case 0x92: return Instructions.I2C;
			case 0x93: return Instructions.I2S;
			
			case 0x94:            return Instructions.LCMP;
			case 0x95: case 0x96: return Instructions.FCMP;
			case 0x97: case 0x98: return Instructions.DCMP;
			
			case 0x99: return new IfEqInstruction(this, 	readShort());
			case 0x9A: return new IfNotEqInstruction(this, 	readShort());
			case 0x9B: return new IfLtInstruction(this, 	readShort());
			case 0x9C: return new IfGeInstruction(this, 	readShort());
			case 0x9D: return new IfGtInstruction(this, 	readShort());
			case 0x9E: return new IfLeInstruction(this, 	readShort());
			case 0x9F: return new IfIEqInstruction(this, 	readShort());
			case 0xA0: return new IfINotEqInstruction(this, readShort());
			case 0xA1: return new IfILtInstruction(this, 	readShort());
			case 0xA2: return new IfIGeInstruction(this, 	readShort());
			case 0xA3: return new IfIGtInstruction(this, 	readShort());
			case 0xA4: return new IfILeInstruction(this, 	readShort());
			case 0xA5: return new IfAEqInstruction(this, 	readShort());
			case 0xA6: return new IfANotEqInstruction(this, readShort());
			case 0xA7: return new GotoInstruction(this, 	readShort());
			
			/*case 0xA8: return jsr(readShort());
			case 0xA9: return ret(readUnsignedByte());*/
			
			case 0xAA: {
				skip(3 - (pos & 0x3)); // alignment by 4 bytes
				
				int defaultOffset = readInt(),
					low = readInt(),
					high = readInt();
				
				if(high < low)
					throw new InstructionFormatException("tableswitch: high < low (low = " + low + ", high = " + high + ")");
				
				Int2IntMap offsetTable = new Int2IntOpenHashMap(high - low);
				
				for(int value = low; value <= high; ++value) {
					offsetTable.put(value, readInt());
				}
				
				return new SwitchInstruction(this, defaultOffset, offsetTable);
			}
			
			case 0xAB: {
				skip(3 - (pos & 0x3)); // alignment by 4 bytes
				
				int defaultOffset = readInt();
				int cases = readInt();
				
				Int2IntMap offsetTable = new Int2IntOpenHashMap(cases);
				
				for(; cases != 0; --cases) {
					offsetTable.put(readInt(), readInt());
				}
				
				return new SwitchInstruction(this, defaultOffset, offsetTable);
			}
			
			case 0xAC: return Instructions.IRETURN;
			case 0xAD: return Instructions.LRETURN;
			case 0xAE: return Instructions.FRETURN;
			case 0xAF: return Instructions.DRETURN;
			case 0xB0: return Instructions.ARETURN;
			case 0xB1: return Instructions.VRETURN;
			
			case 0xB2: return new GetStaticFieldInstruction(readUnsignedShort());
			case 0xB3: return new PutStaticFieldInstruction(readUnsignedShort());
			case 0xB4: return new GetInstanceFieldInstruction(readUnsignedShort());
			case 0xB5: return new PutInstanceFieldInstruction(readUnsignedShort());
			
			case 0xB6: return new InvokevirtualInstruction(readUnsignedShort());
			case 0xB7: return new InvokespecialInstruction(readUnsignedShort());
			case 0xB8: return new InvokestaticInstruction(readUnsignedShort());
			case 0xB9: return new InvokeinterfaceInstruction(this, readUnsignedShort(), readUnsignedByte(), readUnsignedByte());
			case 0xBA: return new InvokedynamicInstruction(this, readUnsignedShort(), readUnsignedShort());
			
			case 0xBB: return new NewInstruction(readUnsignedShort());
			case 0xBC: return new NewArrayInstruction(readUnsignedByte());
			case 0xBD: return new ANewArrayInstruction(readUnsignedShort());
			case 0xBE: return Instructions.ARRAYLENGTH;
			
			case 0xBF: return Instructions.ATHROW;
			case 0xC0: return new CheckCastInstruction(readUnsignedShort());
			case 0xC1: return new InstanceofInstruction(readUnsignedShort());
			
			case 0xC2: return Instructions.MONITORENTER;
			case 0xC3: return Instructions.MONITOREXIT;
			
			case 0xC4: switch(readUnsignedByte()) {
				case 0x15: return new ILoadInstruction(readUnsignedShort());
				case 0x16: return new LLoadInstruction(readUnsignedShort());
				case 0x17: return new FLoadInstruction(readUnsignedShort());
				case 0x18: return new DLoadInstruction(readUnsignedShort());
				case 0x19: return new ALoadInstruction(readUnsignedShort());
				case 0x36: return new IStoreInstruction(readUnsignedShort());
				case 0x37: return new LStoreInstruction(readUnsignedShort());
				case 0x38: return new FStoreInstruction(readUnsignedShort());
				case 0x39: return new DStoreInstruction(readUnsignedShort());
				case 0x3A: return new AStoreInstruction(readUnsignedShort());
				case 0x84: return new IIncInstruction(readUnsignedShort(), readShort());
//				case 0xA9: return ret(readUnsignedShort());
				default:
					throw new InvalidOpcodeException("Illegal wide opcode 0x" + IntegerUtil.hex(bytes[pos]));
			}
			
			case 0xC5: return new MultiANewArrayInstruction(readUnsignedShort(), readUnsignedByte());
			
			case 0xC6: return new IfNullInstruction(this, readShort());
			case 0xC7: return new IfNonNullInstruction(this, readShort());
			case 0xC8: return new GotoInstruction(this, readInt());
			/*case 0xC9: return jsr_w(readInt());
			case 0xCA: return breakpoint;
			case 0xFE: return impdep1;
			case 0xFF: return impdep2;*/
			default:
				throw new InvalidOpcodeException(IntegerUtil.hex2WithPrefix(bytes[pos]));
		}
	}
	
	
	@Override
	public void warning(String message) {
		Logger.warning("Disassembling warning " + message);
	}
}