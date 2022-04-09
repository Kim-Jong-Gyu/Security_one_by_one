package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import GUI.ClientFrame;

public class ClientChat {
	public static Socket socket;
	ClientFrame ui;
	public static DataInputStream cl_dis = null;
	public static DataOutputStream cl_dos = null;

	public static boolean cl_connectStatus;// 클라이언트 접속 여부 저장
	public static boolean cl_stopSignal;// 쓰레드 종료 신호 저장

	public ClientChat() {
		startService();
	}

	public void startService() {
		ui = new ClientFrame();
		ClientFrame.mod_client.setSelected(true);
		ClientFrame.mod_server.setEnabled(false);
		try {
			ClientFrame.chatTextArea.append("서버에 접속을 시도 중입니다....\n");

			// socket 객체를 생성하여 IP 주소와 포트번호 전달->서버 접속시도
			socket = new Socket("localhost", 6666);
			cl_dis = new DataInputStream(socket.getInputStream());
			System.out.println(cl_dis.toString());
			cl_dos = new DataOutputStream(socket.getOutputStream());

			ClientFrame.chatTextArea.append("서버 접속 완료\n");
			ClientFrame.user_info.setText("서버 연결 상태 : 연결됨\n");
			cl_connectStatus = true;
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					receiveMessage();
				}
			}).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ClientChat();
	}

	public void receiveMessage() {
		try {
			System.out.println("check receiveMessage from server");
			while (!cl_stopSignal) {
				// 클라이언트가 writeUTF() 메서드로 전송한 메세지를 입력받기
				ClientFrame.chatTextArea.append("서버 : " + cl_dis.readUTF() + "\n");
			}
			// stopSignal 이 true 가 되면 메세지 수신 종료되므로 dis와 socket 반환
			cl_dis.close();
			socket.close();
		} catch (EOFException e) {
			// 상대방이 접속 해제할 경우 소켓이 제거되면서 호출되는 예외
			ClientFrame.chatTextArea.append("서버 접속이 해제되었습니다.\n");
			ClientFrame.user_info.setText("서버 연결 상태 : 미연결");
			cl_connectStatus = false;
		} catch (SocketException e) {
			ClientFrame.chatTextArea.append("서버 접속이 해제되었습니다.\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
