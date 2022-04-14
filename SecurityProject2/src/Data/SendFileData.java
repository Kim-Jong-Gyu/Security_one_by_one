package Data;

import java.io.Serializable;

public class SendFileData implements Serializable {
	FileData encryptedMessage;
	byte[] signatureData;
	public FileData getEncryptedMessage() {
		return encryptedMessage;
	}
	public void setEncryptedMessage(FileData encryptedMessage) {
		this.encryptedMessage = encryptedMessage;
	}
	public byte[] getSignatureData() {
		return signatureData;
	}
	public void setSignatureData(byte[] signatureData) {
		this.signatureData = signatureData;
	}
	
}
