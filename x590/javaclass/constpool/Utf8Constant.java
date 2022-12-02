package x590.javaclass.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.exception.DisassemblingException;
import x590.javaclass.io.ExtendedDataInputStream;

public class Utf8Constant extends Constant {

	private final String value;
	
	protected Utf8Constant(ExtendedDataInputStream in) {
		this.value = decodeUtf8(in);
	}
	
	public Utf8Constant(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	/** Используйте {@link #getValue()} вместо этого метода */
	@Override
	@Deprecated
	public String toString() {
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
					b2 = in.readByte(),
					b3 = in.readByte(),
					b4 = in.readByte(),
					b5 = in.readByte();

				if(ch == 0xED && i + 5 < length &&
						(b1 & 0xF0) == 0xA0 && (b2 & 0xC0) == 0x80 && (b3 & 0xFF) == 0xED
					 && (b4 & 0xF0) == 0xB0 && (b5 & 0xC0) == 0x80) {
					
					int c = 0x10000 | (b1 & 0xF) << 16 |
							(b2 & 0x3F) << 10 | (b4 & 0xF) << 6 | (b5 & 0x3F);

					if((c & 0xFFFF0000) != 0)
						result.append((char)(c >>> 16));
					result.append((char)c);

					i += 5;
					continue;
				}

				ch = (ch & 0xF) << 12 | (b1 & 0x3F) << 6 | (b2 & 0x3F);

				i += 2;
			}

			result.append((char)ch);
		}
		
		if(i > length)
			throw new DisassemblingException("String decoding failed");
		
		return result.toString();
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(1);
		byte[] bytes = value.getBytes();
		out.writeShort(bytes.length);
		out.write(bytes);
	}
}