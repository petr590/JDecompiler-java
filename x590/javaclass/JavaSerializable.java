package x590.javaclass;

import java.io.DataOutputStream;
import java.io.IOException;

public interface JavaSerializable {
	
	public void serialize(DataOutputStream out) throws IOException;
}