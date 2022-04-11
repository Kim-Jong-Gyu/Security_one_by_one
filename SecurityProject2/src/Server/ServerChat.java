package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import GUI.ClientFrame;
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
	public static boolean stopSignal;// 쓰레드 종료 신호 저장
	public static SecretKey skey;

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
				try {
					receivePublic();
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void main(String[] args) {
		new ServerChat();
	}

//	public void receiveMessage() {
//		// 멀티 쓰레딩으로 메세지 수신 처리 작업 수행
//		// boolean 타입 멤버변수 stopSignal 이 false 일 동안 반복해서 메세지 수신
//		try {
//			while (!stopSignal) {
//				// 클라이언트가 writeUTF() 메서드로 전송한 메세지를 입력받기
//				ServerFrame.chatTextArea.append("클라이언트 : " + ois.readUTF() + "\n");
//			}
//			// stopSignal 이 true 가 되면 메세지 수신 종료되므로 dis와 socket 반환
//			ois.close();
//			socket.close();
//		} catch (EOFException e) {
//			// 상대방이 접속 해제할 경우 소켓이 제거되면서 호출되는 예외
//			ServerFrame.chatTextArea.append("클라이언트 접속이 해제되었습니다.\n");
//			connectStatus = false;
//		} catch (SocketException e) {
//			ServerFrame.chatTextArea.append("서버 접속이 해제되었습니다.\n");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public void receivePublic() throws ClassNotFoundException, IOException {
		client_pub = (PublicKey) ois.readObject();
		byte[] client_pubk = client_pub.getEncoded();
		ServerFrame.otherKeyPair_info.append("\n Client Public Key : ");
		for (byte b : client_pubk)
			ServerFrame.otherKeyPair_info.append(String.format("%02X ", b));
		System.out.println("\n Client Public Key Length : " + client_pubk.length + " byte");
	}
	
	public void receiveAES() throws ClassNotFoundException, IOException {
		byte[] encryptedAESkey = null;
		encryptedAESkey = (byte[])ois.readObject();
		skey = new SecretKeySpec(Decrypt_RSA(encryptedAESkey,ServerFrame.privateKey), "AES");
	}

	public byte[] Decrypt_RSA(byte[] encrypted, PrivateKey privateKey) {

		byte[] decrypted_RSA = null;

		try {

			Cipher cipher2 = Cipher.getInstance("RSA");

			cipher2.init(Cipher.DECRYPT_MODE, privateKey);
			decrypted_RSA = cipher2.doFinal(encrypted);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return decrypted_RSA;
	}

	public String DecryptAES(String ciphertext) {
		String result = null;
		
		try {
		    Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
		    cipher2.init(Cipher.DECRYPT_MODE, skey);
		    
		    // String - > byte
		    byte[] decrypted = Base64.getDecoder().decode(ciphertext.getBytes("UTF-8"));
		    result = new String(cipher2.doFinal(decrypted), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}