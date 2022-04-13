package Server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Data.EncryptedAESInfo;
import Data.InputData;
import GUI.ServerFrame;

public class ServerChat {
	private ServerSocket serverSocket;
	public static Socket socket;
	public static PublicKey client_pub;
	ServerFrame ui;
	public static ObjectInputStream ois = null;
	public static ObjectOutputStream oos = null;

	// between server and client about security
	public static boolean securityConnection;
	public static boolean connectStatus;// 클라이언트 접속 여부 저장
	public static SecretKey skey;
	public static InputData Command;
	
	public static String iv;
	
	public ServerChat() {
		startService();// 채팅 서버 시작
	}

	public void startService() {
		try {
			ui = new ServerFrame();
			ServerFrame.mod_server.setSelected(true);
			ServerFrame.mod_client.setEnabled(false);
			ServerFrame.user_info.append("Sevice Getting Ready..\n");

			// ServerSocket 객체를 생성하여 지정된 포트(59876)를 개방
			serverSocket = new ServerSocket(6666);
			ServerFrame.user_info.append("Service Ready \n");

			// 클라이언트로부터 접속이 성공할 때까지 접속 무한 대기
			connectStatus = false;
			while (!connectStatus) {
				ServerFrame.user_info.append("Waiting for client connection...\n");
				// ServerSocket 객체의 accept()메서드를 호출하여 연결대기
				// 연결 완료 시 Socket 객체 리턴됨
				socket = serverSocket.accept();

				// 접속된 클라이언트에 대한 IP 주소 정보 출력
				ServerFrame.user_info.append("The Client has accessed. (" + socket.getInetAddress() + ")\n");
				ServerFrame.user_info.append("Please Key generation and send key for using service with client\n");
				oos = new ObjectOutputStream(socket.getOutputStream());

				ois = new ObjectInputStream(socket.getInputStream());
				connectStatus = true;
				if (connectStatus == true)
					ServerFrame.connectionCheck.setText("Client : Connection");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						receiveData();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	public static void main(String[] args) {
		new ServerChat();
	}

	public void receiveData() throws ClassNotFoundException, IOException {
		try {
			Command = (InputData) ois.readObject();
			if (Command.getCommand().equals("RECIEVE_PUBLIC")) {
				receivePublic((PublicKey) Command.getObj());
			} else if (Command.getCommand().equals("RECIEVE_AES")) {
				receiveAES((EncryptedAESInfo) Command.getObj());
			} else if (Command.getCommand().equals("RECIEVE_FILE")) {
//				receiveFile(Command.getObj());
			} else if (Command.getCommand().equals("RECIEVE_MESSAGE")) {
				receiveMessage((String) Command.getObj());
			}
		} catch (EOFException e) {

		}
	}

	private void receiveFile() {
		// TODO Auto-generated method stub

	}

	public void receiveMessage(String text) {
		ServerFrame.chatTextArea.append("client : " + DecryptAES(text) + "\n");
	}

	public void receivePublic(PublicKey pk) throws ClassNotFoundException, IOException {
		client_pub = pk;
		byte[] client_pubk = client_pub.getEncoded();
		ServerFrame.otherKeyPair_info.append("\n Client Public Key : ");
		for (byte b : client_pubk)
			ServerFrame.otherKeyPair_info.append(String.format("%02X ", b));
		System.out.println("\n Client Public Key Length : " + client_pubk.length + " byte");
	}

	public void receiveAES(EncryptedAESInfo data) throws ClassNotFoundException, IOException {
		byte[] encryptedAESkey = data.getEncryptedAESkey();
		byte[] encryptedIv = data.getEncryptedIv();;
		skey = new SecretKeySpec(Decrypt_RSA(encryptedAESkey), "AES");
		byte[] decryptedIv = Decrypt_RSA(encryptedIv);
		iv = new String(decryptedIv, "UTF-8");
	}

	public byte[] Decrypt_RSA(byte[] encrypted) {

		byte[] decrypted_RSA = null;

		try {

			Cipher cipher2 = Cipher.getInstance("RSA");

			cipher2.init(Cipher.DECRYPT_MODE, ServerFrame.privateKey);
			decrypted_RSA = cipher2.doFinal(encrypted);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return decrypted_RSA;
	}

	public String DecryptAES(String ciphertext) {
	String result = null;
		
		try {
		    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		    c.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(iv.getBytes("UTF-8")));
		    
		    byte[] decrypted = Base64.getDecoder().decode(ciphertext.getBytes("UTF-8"));
		    result = new String(c.doFinal(decrypted), "UTF-8");
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}