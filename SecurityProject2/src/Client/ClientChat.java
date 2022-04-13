package Client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import Data.InputData;
import GUI.ClientFrame;

public class ClientChat {
	public static Socket socket;
	ClientFrame ui;
	public static ObjectInputStream ois = null;
	public static ObjectOutputStream oos = null;
	public static PublicKey server_pub;

	public static boolean connectStatus;// 클라이언트 접속 여부 저장
	public static boolean stopSignal;// 쓰레드 종료 신호 저장
	public static InputData Command;

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

	public void receiveData() throws ClassNotFoundException, IOException {
		try {
			Command = (InputData) ois.readObject();
			if (Command.getCommand().equals("RECIEVE_PUBLIC")) {
				receivePublic((PublicKey) Command.getObj());
			} else if (Command.getCommand().equals("RECIEVE_FILE")) {
//					receiveFile(Command.getObj());
			} else if (Command.getCommand().equals("RECIEVE_MESSAGE")) {
				receiveMessage((String) Command.getObj());
			}
		} catch (EOFException e) {
		}
	}

	public void receiveMessage(String data) {
		ClientFrame.chatTextArea.append("서버 : " + DecryptAES(data) + "\n");
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
