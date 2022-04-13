package Data;

import java.io.Serializable;

public class EncryptedAESInfo implements Serializable {
	byte[] EncryptedAESkey;
	byte[] EncryptedIv;
	public byte[] getEncryptedAESkey() {
		return EncryptedAESkey;
	}
	public void setEncryptedAESkey(byte[] encryptedAESkey) {
		EncryptedAESkey = encryptedAESkey;
	}
	public byte[] getEncryptedIv() {
		return EncryptedIv;
	}
	public void setEncryptedIv(byte[] encryptedIv) {
		EncryptedIv = encryptedIv;
	}
	
}