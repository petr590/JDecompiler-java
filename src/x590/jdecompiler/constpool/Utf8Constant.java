package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;

public final class Utf8Constant extends Constant implements ICachedConstant<String> {
	
	private final String value;
	
	protected Utf8Constant(ExtendedDataInputStream in) {
		this.value = decodeUtf8(in);
	}
	
	public Utf8Constant(String value) {
		this.value = value;
	}
	
	public String getString() {
		return value;
	}
	
	@Override
	public String getValueAsObject() {
		return value;
	}
	
	private static String decodeUtf8(ExtendedDataInputStream in) {
		int length = in.readUnsignedShort();
		StringBuilder result = new StringBuilder();
		
		int i = 0;
		
		for(; i < length; i++) {
			int ch = in.readUnsignedByte();
			
			if((ch & 0xE0) == 0xC0) {
				ch = (ch & 0x1F) << 6 | (in.readByte() & 0x3F);
				i++;
				
			} else if((ch & 0xF0) == 0xE0) {
				
				int b1 = in.readByte(),
					b2 = in.readByte();
				
				if(ch == 0xED && i + 5 < length &&
						(b1 & 0xF0) == 0xA0 && (b2 & 0xC0) == 0x80) {
					
					int b3 = in.readByte(),
						b4 = in.readByte(),
						b5 = in.readByte();
					
					if((b3 & 0xFF) == 0xED
						 && (b4 & 0xF0) == 0xB0 && (b5 & 0xC0) == 0x80) {
						
						int c = 0x10000 | (b1 & 0xF) << 16 |
								(b2 & 0x3F) << 10 | (b4 & 0xF) << 6 | (b5 & 0x3F);
						
						if((c & 0xFFFF0000) != 0)
							result.append((char)(c >>> 16));
						result.append((char)c);
						
						i += 5;
						continue;
					}
				}
				
				ch = (ch & 0xF) << 12 | (b1 & 0x3F) << 6 | (b2 & 0x3F);
				
				i += 2;
			}
			
			result.append((char)ch);
		}
		
		assert i == length;
		
		if(i > length)
			throw new DisassemblingException("String decoding failed");
		
		return result.toString();
	}
	
	@Override
	public String getConstantName() {
		return "Utf8";
	}
	
	@Override
	public String toString() {
		return String.format("Utf8Constant \"%s\"", value);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(0x1);
		byte[] bytes = value.getBytes();
		out.writeShort(bytes.length);
		out.write(bytes);
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof Utf8Constant constant && this.equals(constant);
	}
	
	public boolean equals(Utf8Constant other) {
		return this == other || this.value.equals(other.value);
	}
}
