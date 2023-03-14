package x590.jdecompiler.io;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import x590.jdecompiler.JavaSerializable;
import x590.jdecompiler.exception.WriteException;
import x590.util.annotation.Nullable;
import x590.util.io.UncheckedOutputStream;

public class ExtendedDataOutputStream extends UncheckedOutputStream implements DataOutput {
	
	private final DataOutputStream out;
	
	public ExtendedDataOutputStream(OutputStream out) {
		this(out instanceof DataOutputStream dataOut ? dataOut : new DataOutputStream(out));
	}
	
	public ExtendedDataOutputStream(DataOutputStream out) {
		this.out = out;
	}
	
	public DataOutputStream getDataOutputStream() {
		return out;
	}
	
	@Override
	public void write(int b) {
		try {
			out.write(b);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public void write(byte[] bytes) {
		write(bytes, 0, bytes.length);
	}
	
	@Override
	public void write(byte[] bytes, int offset, int length) {
		for(int i = offset, end = offset + length; i < end; i++) {
			write(bytes[i]);
		}
	}
	
	@Override
	public void writeBoolean(boolean value) {
		try {
			out.writeBoolean(value);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public void writeByte(int value) {
		try {
			out.writeByte(value);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public void writeShort(int value) {
		try {
			out.writeShort(value);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public void writeChar(int value) {
		try {
			out.writeChar(value);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public void writeDouble(double value) {
		try {
			out.writeDouble(value);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public void writeFloat(float value) {
		try {
			out.writeFloat(value);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public void writeInt(int value) {
		try {
			out.writeInt(value);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public void writeLong(long value) {
		try {
			out.writeLong(value);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public void writeBytes(String bytes) {
		try {
			out.writeBytes(bytes);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public void writeChars(String chars) {
		try {
			out.writeChars(chars);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public void writeUTF(String str) {
		try {
			out.writeUTF(str);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	public void write(JavaSerializable serializable) {
		serializable.serialize(this);
	}
	
	public void writeIfNotNull(@Nullable JavaSerializable serializable) {
		if(serializable != null)
			serializable.serialize(this);
	}
	
	
	
	public void writeIntSize(int size) {
		writeInt(size);
	}
	
	public void writeShortSize(int size) {
		if(size > 0xFFFF || size < 0)
			throw new WriteException("Size exceeds the limit");
		writeShort(size);
	}
	
	public void writeByteSize(int size) {
		if(size > 0xFF || size < 0)
			throw new WriteException("Size exceeds the limit");
		writeByte(size);
	}
	
	
	public void writeByteArrayIntSized(byte[] data) {
		writeIntSize(data.length);
		write(data);
	}
	
	public void writeByteArrayShortSized(byte[] data) {
		writeShortSize(data.length);
		write(data);
	}
	
	public void writeByteArrayByteSized(byte[] data) {
		writeByteSize(data.length);
		write(data);
	}
	
	
	public void writeAll(Collection<? extends JavaSerializable> serializables) {
		writeAllShortSized(serializables, serializables.size());
	}
	
	public void writeAllIntSized(Collection<? extends JavaSerializable> serializables, int size) {
		writeIntSize(size);
		writeAllNoSized(serializables);
	}
	
	public void writeAllShortSized(Collection<? extends JavaSerializable> serializables, int size) {
		writeShortSize(size);
		writeAllNoSized(serializables);
	}
	
	public void writeAllByteSized(Collection<? extends JavaSerializable> serializables, int size) {
		writeByteSize(size);
		writeAllNoSized(serializables);
	}
	
	public void writeAllNoSized(Collection<? extends JavaSerializable> serializables) {
		for(JavaSerializable serializable : serializables)
			write(serializable);
	}
}
