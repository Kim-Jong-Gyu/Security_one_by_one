package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;

import GUI.ClientFrame;

public class ClientChat {
	public static Socket socket;
	ClientFrame ui;
	public static ObjectInputStream ois = null;
	public static ObjectOutputStream oos = null;
	public static PublicKey server_pub;
	
	public static boolean securityConnection;
	
	//when client get server key we should make AES in client
	public static boolean getSubpubk = false;
	
	public static boolean connectStatus;// 클라이언트 접속 여부 저장
	public static boolean stopSignal;// 쓰레드 종료 신호 저장

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
			if(connectStatus == true)
				ClientFrame.connectionCheck.setText("Server : Connection");
			new Thread(new Runnable() {

				@Override
				public void run() {
					receivePublic();
					
				}
			}).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ClientChat();
	}

//	public void receiveMessage() {
//		if(securityConnection == true)
//		{
//			try {
//				while (!stopSignal) {
//					// 클라이언트가 writeUTF() 메서드로 전송한 메세지를 입력받기
//					ClientFrame.chatTextArea.append("서버 : " + ois.readUTF() + "\n");
//				}
//				// stopSignal 이 true 가 되면 메세지 수신 종료되므로 dis와 socket 반환
//				ois.close();
//				socket.close();
//			} catch (EOFException e) {
//				// 상대방이 접속 해제할 경우 소켓이 제거되면서 호출되는 예외
//				ClientFrame.chatTextArea.append("서버 접속이 해제되었습니다.\n");
//				ClientFrame.user_info.setText("서버 연결 상태 : 미연결");
//				connectStatus = false;
//			} catch (SocketException e) {
//				ClientFrame.chatTextArea.append("서버 접속이 해제되었습니다.\n");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}	
//		}
//		else
//		{
//			
//		}
//	}
//	
	public void receivePublic(){
		try {
			server_pub = (PublicKey) ois.readObject();
			getSubpubk = true;
			byte[] server_pubk = server_pub.getEncoded();
			ClientFrame.otherKeyPair_info.append("\n Server Public Key : ");
			for (byte b : server_pubk)
				ClientFrame.otherKeyPair_info.append(String.format("%02X ", b));
			System.out.println("\n Server Public Key Length : " + server_pubk.length + " byte");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
