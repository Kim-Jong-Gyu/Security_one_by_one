package Client;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import Data.InputData;
import Data.SendFileData;
import GUI.ClientFrame;
import GUI.ServerFrame;

public class ClientChat {
	public static Socket socket;
	ClientFrame ui;
	public static ObjectInputStream ois = null;
	public static ObjectOutputStream oos = null;
	public static PublicKey server_pub;

	public static boolean connectStatus;// 클라이언트 접속 여부 저장
	public static boolean stopSignal;// 쓰레드 종료 신호 저장
	public static InputData Command;
	public static String filename;
	public static byte[] file;

	public ClientChat() {
		startService();
	}

	public void startService() {
		ui = new ClientFrame();
		ClientFrame.mod_client.setSelected(true);
		ClientFrame.mod_server.setEnabled(false);
		try {
			ClientFrame.user_info.append("Attempting to connect to the server....\n");

			// socket 객체를 생성하여 IP 주소와 포트번호 전달->서버 접속시도
			socket = new Socket("localhost", 6666);
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());

			ClientFrame.user_info.append("Server connection complete\n");
			ClientFrame.user_info.append("Please Key generation and send key for using service with client\n");

			connectStatus = true;
			if (connectStatus == true)
				ClientFrame.connectionCheck.setText("Server : Connection");
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						while (true) {
							receiveData();
						}
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SignatureException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ClientChat();
	}

	public void receiveData() throws ClassNotFoundException, IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException {
		try {
			Command = (InputData) ois.readObject();
			if (Command.getCommand().equals("RECIEVE_PUBLIC")) {
				receivePublic((PublicKey) Command.getObj());
			} else if (Command.getCommand().equals("RECIEVE_FILE")) {
					receiveFile((SendFileData) Command.getObj());
			} else if (Command.getCommand().equals("RECIEVE_MESSAGE")) {
				receiveMessage((String) Command.getObj());
			}
		} catch (EOFException e) {
			ClientFrame.user_info.append(Command.getCommand() + "failed!\n");

		}
	}

	public void receiveMessage(String data) {
		ClientFrame.chatTextArea.append("server : " + DecryptAES(data) + "\n");
	}
	
	public void receiveFile(SendFileData data) throws NoSuchAlgorithmException, InvalidKeyException, IOException, SignatureException {
	    Signature sig2 = Signature.getInstance("SHA512WithRSA");
		sig2.initVerify(server_pub);
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
	    byte[] fileDataByte = null;
	    ObjectOutputStream ois = new ObjectOutputStream(boas);
	    ois.writeObject(data.getEncryptedMessage());
	    fileDataByte = boas.toByteArray();
	    //Encrypted data used by hash function
	    boas.close();
	    ois.close();
	    sig2.update(fileDataByte);
		if(sig2.verify(data.getSignatureData())) {
			filename = DecryptAES(data.getEncryptedMessage().getFilename());
			String strfile = DecryptAES(data.getEncryptedMessage().getFile());
			file = strfile.getBytes("UTF-8");
			ClientFrame.File_Info.append("server : " + filename + "\n");
		}else {
			ClientFrame.File_Info.append("server : " + filename +"is not verifying" +"\n");
		}
	}

	public void receivePublic(PublicKey pk) {
		server_pub = pk;
		byte[] server_pubk = server_pub.getEncoded();
		ClientFrame.otherKeyPair_info.append("\n Server Public Key : ");
		for (byte b : server_pubk)
			ClientFrame.otherKeyPair_info.append(String.format("%02X ", b));
	}

	public String DecryptAES(String ciphertext) {
		String result = null;

		try {

			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, ClientFrame.skey, new IvParameterSpec(ClientFrame.iv.getBytes("UTF-8")));

			byte[] decrypted = Base64.getDecoder().decode(ciphertext.getBytes("UTF-8"));
			result = new String(c.doFinal(decrypted), "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
