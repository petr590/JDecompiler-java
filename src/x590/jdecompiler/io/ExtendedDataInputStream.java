package x590.jdecompiler.io;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;

import x590.util.annotation.Immutable;
import x590.util.io.UncheckedInputStream;

public class ExtendedDataInputStream extends UncheckedInputStream implements DataInput {
	
	private final DataInputStream in;
	
	public ExtendedDataInputStream(DataInputStream in) {
		this.in = in;
	}
	
	public ExtendedDataInputStream(InputStream in) {
		this(in instanceof DataInputStream dataIn ? dataIn : new DataInputStream(in));
	}
	
	
	public DataInputStream getDataInputStream() {
		return in;
	}
	
	@Override
	public int available() {
		try {
			return in.available();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public int read() {
		try {
			return in.readByte();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	
	@Override
	public void readFully(byte[] buffer) {
		try {
			in.readFully(buffer);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
		
	}
	
	@Override
	public void readFully(byte[] buffer, int off, int len) {
		try {
			in.readFully(buffer, off, len);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	
	@Override
	public int skipBytes(int n) {
		try {
			return in.skipBytes(n);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	
	@Override
	public boolean readBoolean() {
		try {
			return in.readBoolean();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public byte readByte() {
		try {
			return in.readByte();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public int readUnsignedByte() {
		try {
			return in.readUnsignedByte();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public short readShort() {
		try {
			return in.readShort();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public int readUnsignedShort() {
		try {
			return in.readUnsignedShort();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public char readChar() {
		try {
			return in.readChar();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public int readInt() {
		try {
			return in.readInt();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public long readLong() {
		try {
			return in.readLong();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public float readFloat() {
		try {
			return in.readFloat();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public double readDouble() {
		try {
			return in.readDouble();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String readLine() {
		try {
			return in.readLine();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public String readUTF() {
		try {
			return in.readUTF();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	public <T> T[] readArray(IntFunction<T[]> arrayCreator, Supplier<T> elementSupplier) {
		return readToArray(arrayCreator.apply(readUnsignedShort()), elementSupplier);
	}
	
	public <T> T[] readToArray(T[] array, Supplier<T> elementSupplier) {
		int length = array.length;
		
		for(int i = 0; i < length; i++)
			array[i] = elementSupplier.get();
		
		return array;
	}
	
	
	public <T> @Immutable List<T> readImmutableList(Supplier<T> elementSupplier) {
		return Collections.unmodifiableList(readArrayList(elementSupplier));
	}
	
	public <T> @Immutable List<T> readImmutableList(int length, Supplier<T> elementSupplier) {
		return Collections.unmodifiableList(readArrayList(length, elementSupplier));
	}

	
	public <T> @Immutable List<T> readImmutableList(ObjIntConsumer<? super List<T>> elementSetter) {
		return Collections.unmodifiableList(readArrayList(elementSetter));
	}
	
	public <T> @Immutable List<T> readImmutableList(int length, ObjIntConsumer<? super List<T>> elementSetter) {
		return Collections.unmodifiableList(readArrayList(length, elementSetter));
	}
	
	
	public <T> ArrayList<T> readArrayList(Supplier<T> elementSupplier) {
		return readArrayList(readUnsignedShort(), elementSupplier);
	}
	
	public <T> ArrayList<T> readArrayList(int length, Supplier<T> elementSupplier) {
		return readArrayList(length, (list, i) -> list.add(elementSupplier.get()));
	}
	
	
	public <T> ArrayList<T> readArrayList(ObjIntConsumer<? super List<T>> elementSetter) {
		return readArrayList(readUnsignedShort(), elementSetter);
	}
	
	public <T> ArrayList<T> readArrayList(int length, ObjIntConsumer<? super List<T>> elementSetter) {
		ArrayList<T> list = new ArrayList<>(length);
		
		for(int i = 0; i < length; i++)
			elementSetter.accept(list, i);
		
		return list;
	}
}
