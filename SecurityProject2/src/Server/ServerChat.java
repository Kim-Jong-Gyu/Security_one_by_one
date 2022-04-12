package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Data.EncryptedAESInfo;
import Data.EncryptedFileInfo;
import GUI.ServerFrame;

public class ServerChat {
	private ServerSocket serverSocket;
	public static Socket socket;
	public static PublicKey client_pub;
	ServerFrame ui;
	public static ObjectInputStream ois = null;
	public static ObjectOutputStream oos = null;

	// between server and client about security
	public static boolean sharingkey = false;

	public static boolean connectStatus;// 클라이언트 접속 여부 저장
	public static boolean stopSignal;// 쓰레드 종료 신호 저장
	public static SecretKey skey;
	public static boolean getClipubk = false;
	public static String iv;
	public static File file;
	public static FileOutputStream fos =null;
	public static BufferedOutputStream bos = null;
	public static DataInputStream dis;
	public static DataOutputStream dos;
	
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
				dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

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

					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
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

	public void receiveMessage() throws ClassNotFoundException {
		// 멀티 쓰레딩으로 메세지 수신 처리 작업 수행
		// boolean 타입 멤버변수 stopSignal 이 false 일 동안 반복해서 메세지 수신
		if (sharingkey == true) {
			try {
				while (!stopSignal) {
					// 클라이언트가 writeUTF() 메서드로 전송한 메세지를 입력받기
					String readEncryptMessage = (String) ois.readObject();
					String result = DecryptAES(readEncryptMessage);
					ServerFrame.chatTextArea.append("client : " + result + "\n");
				}
				// stopSignal 이 true 가 되면 메세지 수신 종료되므로 dis와 socket 반환
				ois.close();
				socket.close();
			} catch (EOFException e) {
				// 상대방이 접속 해제할 경우 소켓이 제거되면서 호출되는 예외
				ServerFrame.chatTextArea.append("클라이언트 접속이 해제되었습니다.\n");
				connectStatus = false;
			} catch (SocketException e) {
				ServerFrame.chatTextArea.append("서버 접속이 해제되었습니다.\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			ServerFrame.user_info.append("You have to share the AES!\n");
		}
	}

	public void receivePublic() throws ClassNotFoundException, IOException {
		client_pub = (PublicKey) ois.readObject();
		byte[] client_pubk = client_pub.getEncoded();
		getClipubk = true;
		ServerFrame.otherKeyPair_info.append("\n Client Public Key : ");
		for (byte b : client_pubk)
			ServerFrame.otherKeyPair_info.append(String.format("%02X ", b));
	}

	public void receiveAES() throws ClassNotFoundException, IOException {
		byte[] encryptedAESkey;
		byte[] encryptedIv;
		EncryptedAESInfo obj = new EncryptedAESInfo();
		obj = (EncryptedAESInfo) ois.readObject();
		encryptedAESkey = obj.getEncryptedAESkey();
		encryptedIv = obj.getEncryptedIv();
		skey = new SecretKeySpec(DecryptRSA(encryptedAESkey), "AES");
		iv = new String(DecryptRSA(encryptedIv), "UTF-8");
		sharingkey = true;
		if (sharingkey == true) {
			ServerFrame.user_info.append("Share the secret key success\n");
		}
	}
	
	public void receieveFile() throws ClassNotFoundException, IOException {
		byte[] result_file;
		EncryptedFileInfo file_obj = new EncryptedFileInfo();
		file_obj = (EncryptedFileInfo) ois.readObject();
		file = new File("/Users/gyu/Desktop/" + file_obj.getFilename());
		fos = new FileOutputStream(file);
		bos = new BufferedOutputStream(fos);
		byte[] data = new byte[200];
		int len;
		while((len=dis.readInt())!= -1)
		{
			int tmp;
			tmp = ois.read(data,0,len);
			bos.write(data);
		}
		bos.flush();
	}

	public byte[] DecryptRSA(byte[] encrypted) {

		byte[] decryptedRSA = null;

		try {

			Cipher cipher2 = Cipher.getInstance("RSA");
			cipher2.init(Cipher.DECRYPT_MODE, ServerFrame.privateKey);
			decryptedRSA = cipher2.doFinal(encrypted);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return decryptedRSA;
	}

	public String DecryptAES(String ciphertext) {
		String result = null;

		try {
			Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher2.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(iv.getBytes("UTF-8")));
			// String - > byte
			byte[] decrypted = Base64.getDecoder().decode(ciphertext.getBytes("UTF-8"));
			result = new String(cipher2.doFinal(decrypted), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}