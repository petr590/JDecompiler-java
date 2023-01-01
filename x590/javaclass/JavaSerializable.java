package x590.javaclass;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.util.annotation.RemoveIfNotUsed;

@RemoveIfNotUsed
public interface JavaSerializable {
	
	public void serialize(DataOutputStream out) throws IOException;
}