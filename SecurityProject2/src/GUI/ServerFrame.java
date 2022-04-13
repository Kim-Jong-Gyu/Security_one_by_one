package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Data.InputData;
import Server.ServerChat;

public class ServerFrame extends JFrame implements ActionListener {
	public static JRadioButton radio[] = new JRadioButton[2];
	public static JRadioButton mod_client;
	public static JTextArea chatTextArea;
	public static JRadioButton mod_server;
	public static JTextField chatTextField;
	public static JTextArea connectionCheck;
	public static JButton keyGenerate_button;
	public static JButton send_button;
	public static JButton sendPublic_button;
	private static JPanel contentPane;
	public static JTextArea user_info;
	public static JTextArea keyPair_info;
	public static JTextArea otherKeyPair_info;
	public static PublicKey publicKey;
	public static PrivateKey privateKey;
	public static byte[] pubk;
	public static byte[] prik;

	public ServerFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 581, 763);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel each_mode_panel = new JPanel();
		each_mode_panel.setBackground(SystemColor.window);
		each_mode_panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		each_mode_panel.setBounds(220, 32, 340, 82);
		contentPane.add(each_mode_panel);
		each_mode_panel.setLayout(new BorderLayout(0, 0));

		// 첫번째
		user_info = new JTextArea();
		each_mode_panel.add(user_info);
		JScrollPane scroll_1 = new JScrollPane(user_info);
		each_mode_panel.add(scroll_1);
		
		user_info.setEditable(false);
		user_info.setEditable(false);
		user_info.setBackground(Color.WHITE);

		JLabel lblNewLabel = new JLabel("Commmuncation Mode");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblNewLabel.setBounds(20, 20, 177, 28);
		contentPane.add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel.setBounds(31, 147, 529, 38);
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		// connection client check
		connectionCheck = new JTextArea("Client : Not Connection");
		connectionCheck.setEditable(false);
		panel.add(connectionCheck);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_1.setBounds(31, 604, 525, 64);
		contentPane.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JTextArea File_Info = new JTextArea();
		File_Info.setEditable(false);
		panel_1.add(File_Info);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_2.setBounds(31, 428, 525, 146);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_8.setBounds(6, 6, 513, 89);
		panel_2.add(panel_8);
		panel_8.setLayout(new BorderLayout(0, 0));

		// ChatTextAtrea

		chatTextArea = new JTextArea();
		chatTextArea.setEditable(false);
		panel_8.add(chatTextArea);
		JScrollPane ChatScrollPane = new JScrollPane(chatTextArea);
		panel_8.add(ChatScrollPane);

		send_button = new JButton("Send");
		send_button.setBounds(423, 100, 96, 29);
		panel_2.add(send_button);

		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_9.setBounds(6, 100, 418, 35);
		panel_2.add(panel_9);
		panel_9.setLayout(new BorderLayout(0, 0));

		chatTextField = new JTextField();
		panel_9.add(chatTextField);
		chatTextField.addActionListener(this);
		send_button.addActionListener(this);
		chatTextField.requestFocus();
		//

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_3.setBounds(31, 215, 525, 193);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_4.setBounds(2, 2, 521, 67);
		panel_3.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		// key_information
		keyPair_info = new JTextArea();
		panel_4.add(keyPair_info);
		JScrollPane keyInfoScrollPane = new JScrollPane(keyPair_info);
		panel_4.add(keyInfoScrollPane);

		JPanel panel_5 = new JPanel();
		panel_5.setBounds(12, 81, 495, 39);
		panel_3.add(panel_5);

		keyGenerate_button = new JButton("Key Generation");
		keyGenerate_button.addActionListener(this);
		panel_5.add(keyGenerate_button);

		JButton btnNewButton_1 = new JButton("Load From A File");
		panel_5.add(btnNewButton_1);

		JButton btnNewButton = new JButton("Save Into A File");
		panel_5.add(btnNewButton);

		JPanel panel_6 = new JPanel();
		panel_6.setBounds(12, 126, 495, 61);
		panel_3.add(panel_6);
		panel_6.setLayout(null);

		sendPublic_button = new JButton("Send Public Key");
		sendPublic_button.setBounds(36, 14, 125, 29);
		sendPublic_button.addActionListener(this);
		panel_6.add(sendPublic_button);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_7.setBounds(173, 6, 316, 44);
		panel_6.add(panel_7);
		panel_7.setLayout(new BorderLayout(0, 0));

		otherKeyPair_info = new JTextArea();
		
		panel_7.add(otherKeyPair_info);
		JScrollPane scroll_3 = new JScrollPane(otherKeyPair_info);
		panel_7.add(scroll_3);
		
		// 라디오 버튼
		JPanel radioPanel = new JPanel();
		radioPanel.setBounds(5, 50, 203, 64);
		contentPane.add(radioPanel);
		mod_client = new JRadioButton("Client");
		radioPanel.add(mod_client);
		mod_server = new JRadioButton("Server");
		radioPanel.add(mod_server);
		
		JButton btnNewButton_2 = new JButton("Search File");
		btnNewButton_2.setBounds(422, 680, 117, 29);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Send File");
		btnNewButton_3.setBounds(422, 706, 117, 29);
		contentPane.add(btnNewButton_3);
		
		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_10.setBounds(30, 680, 390, 44);
		contentPane.add(panel_10);
		panel_10.setLayout(new BorderLayout(0, 0));
		
		JTextArea Send_Info = new JTextArea();
		panel_10.add(Send_Info);
		setVisible(true);

	}

	public void ServerSendMessage() {
		try {
			InputData data = new InputData();
			String text = ServerFrame.chatTextField.getText();
			data = new InputData();
			data.setCommand("RECIEVE_MESSAGE");
			data.setObj(EncryptAES(text));
			// 입력된 메세지가 "/exit" 일 경우
			if (text.equals("/exit")) {
				// textArea 에 "bye" 출력 후
				// stopSignal을 true로 설정 , 스트림 반환, 소켓 반환
				ServerChat.oos.close();
				ServerChat.socket.close();

				// 프로그램 종료
				System.exit(0);
			} else {
				// 입력된 메세지가 "/exit"가 아닐 경우( 전송할 메세지인 경우)
				// 클라이언트에게 메세지 전송
				ServerChat.oos.writeObject(data);
				// 초기화 및 커서요청
				ServerChat.oos.flush();
				chatTextField.setText("");
				chatTextField.requestFocus();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String EncryptAES(String plaintext)
	{
		String result = null;
		
		try {
		    
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		    c.init(Cipher.ENCRYPT_MODE, ServerChat.skey, new IvParameterSpec(ServerChat.iv.getBytes()));	 
		    byte[] encrypted = c.doFinal(plaintext.getBytes("UTF-8"));
		    result = new String(Base64.getEncoder().encode(encrypted));
		 
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void GenerateRSAKey() throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		KeyPair keyPair = generator.generateKeyPair();
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
		System.out.println("\n=== RSA Key Generation ===");
		byte[] pubk = publicKey.getEncoded();
		byte[] prik = privateKey.getEncoded();

		keyPair_info.append("\n Server Public Key : ");
		for (byte b : pubk)
			keyPair_info.append(String.format("%02X ", b));
		keyPair_info.append("\n Server Private Key : ");
		for (byte b : prik)
			keyPair_info.append(String.format("%02X ", b));
		}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == send_button) {
			ServerSendMessage();
		} else if (e.getSource() == keyGenerate_button) {
			try {
				GenerateRSAKey();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == sendPublic_button) {
			try {
				InputData data = new InputData();
				data.setCommand("RECIEVE_PUBLIC");
				data.setObj(publicKey);
				ServerChat.oos.writeObject(data);
				ServerChat.oos.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
