package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import GUI.ServerFrame;

public class ServerChat {
	private ServerSocket serverSocket;
	public static Socket socket;
	ServerFrame ui;
	public static DataInputStream dis = null;
	public static DataOutputStream dos = null;

	public static boolean connectStatus;// 클라이언트 접속 여부 저장
	public static boolean stopSignal;// 쓰레드 종료 신호 저장

	public ServerChat() {
		startService();// 채팅 서버 시작
	}

	public void startService() {
		try {
			ui = new ServerFrame();
			ServerFrame.mod_server.setSelected(true);
			ServerFrame.mod_client.setEnabled(false);
			ServerFrame.chatTextArea.append("채팅 서비스 준비중..\n");

			// ServerSocket 객체를 생성하여 지정된 포트(59876)를 개방
			serverSocket = new ServerSocket(6666);
			ServerFrame.chatTextArea.append("서비스 준비 완료\n");

			// 클라이언트로부터 접속이 성공할 때까지 접속 무한 대기
			connectStatus = false;
			while (!connectStatus) {
				ServerFrame.chatTextArea.append("클라이언트 접속 대기중...\n");
				// ServerSocket 객체의 accept()메서드를 호출하여 연결대기
				// 연결 완료 시 Socket 객체 리턴됨
				socket = serverSocket.accept();
				// 접속된 클라이언트에 대한 IP 주소 정보 출력
				ServerFrame.chatTextArea.append("클라이언트가 접속 하였습니다. (" + socket.getInetAddress() + ")\n");

				dos = new DataOutputStream(socket.getOutputStream());

				dis = new DataInputStream(socket.getInputStream());
				System.out.println("qqwerewtewtw");
				connectStatus = true;
				ServerFrame.user_info.setText("Client : Connection");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				receiveMessage();
			}
		}).start();
	}

	public static void main(String[] args) {
		new ServerChat();
	}

	public void receiveMessage() {
		// 멀티 쓰레딩으로 메세지 수신 처리 작업 수행
		// boolean 타입 멤버변수 stopSignal 이 false 일 동안 반복해서 메세지 수신
		try {
			while (!stopSignal) {
				// 클라이언트가 writeUTF() 메서드로 전송한 메세지를 입력받기
				ServerFrame.chatTextArea.append("클라이언트 : " + dis.readUTF() + "\n");
			}
			// stopSignal 이 true 가 되면 메세지 수신 종료되므로 dis와 socket 반환
			dis.close();
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
	}
}