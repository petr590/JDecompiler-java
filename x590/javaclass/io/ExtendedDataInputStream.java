package x590.javaclass.io;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class ExtendedDataInputStream extends UncheckedInputStream implements DataInput {
	
	private final DataInputStream in;
	
	public ExtendedDataInputStream(DataInputStream in) {
		this.in = in;
	}
	
	public ExtendedDataInputStream(InputStream in) {
		this(in instanceof DataInputStream dataIn ? dataIn : new DataInputStream(in));
	}


	@Override
	public int available() {
		try {
			return in.available();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}

	@Override
	public int read() {
		try {
			return in.readByte();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	
	@Override
	public void readFully(byte[] buffer) {
		try {
			in.readFully(buffer);
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
		
	}
	
	@Override
	public void readFully(byte[] buffer, int off, int len) {
		try {
			in.readFully(buffer, off, len);
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	
	@Override
	public int skipBytes(int n) {
		try {
			return in.skipBytes(n);
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	
	@Override
	public boolean readBoolean() {
		try {
			return in.readBoolean();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	@Override
	public byte readByte() {
		try {
			return in.readByte();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	@Override
	public int readUnsignedByte() {
		try {
			return in.readUnsignedByte();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	@Override
	public short readShort() {
		try {
			return in.readShort();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	@Override
	public int readUnsignedShort() {
		try {
			return in.readUnsignedShort();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	@Override
	public char readChar() {
		try {
			return in.readChar();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	@Override
	public int readInt() {
		try {
			return in.readInt();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	@Override
	public long readLong() {
		try {
			return in.readLong();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	@Override
	public float readFloat() {
		try {
			return in.readFloat();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	@Override
	public double readDouble() {
		try {
			return in.readDouble();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String readLine() {
		try {
			return in.readLine();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	@Override
	public String readUTF() {
		try {
			return in.readUTF();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	public <T> T[] readArray(IntFunction<T[]> arrayCreator, Supplier<T> elementSupplier) {
		int length = readUnsignedShort();
		T[] array = arrayCreator.apply(length);
		
		for(int i = 0; i < length; i++)
			array[i] = elementSupplier.get();
		
		return array;
	}
}