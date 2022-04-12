package Data;

import java.io.Serializable;

public class EncryptedFileInfo implements Serializable{
	
	String filename;
	DivideFileInfo[] encryptedFile;
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public DivideFileInfo[] getEncryptedFile() {
		return encryptedFile;
	}
	public void setEncryptedFile(DivideFileInfo[] encryptedFile) {
		this.encryptedFile = encryptedFile;
	}
}
