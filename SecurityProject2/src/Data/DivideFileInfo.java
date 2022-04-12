package Data;

import java.io.Serializable;

public class DivideFileInfo implements Serializable {
	int len;
	byte[] seperateFile;
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public byte[] getSeperateFile() {
		return seperateFile;
	}
	public void setSeperateFile(byte[] seperateFile) {
		this.seperateFile = seperateFile;
	}
	
}
