package Data;

import java.io.Serializable;

public class FileData implements Serializable {
	String file;
	String filename;
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

}
