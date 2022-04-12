
package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Client.ClientChat;
import Data.DivideFileInfo;
import Data.EncryptedAESInfo;
import Data.EncryptedFileInfo;

public class ClientFrame extends JFrame implements ActionListener {
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
	public static String iv;
	//
	public static boolean checkMakeRSA = false;
	public static JButton sendSecret_button;
	public static SecretKey skey;
	public static JButton Send_file_btn;
	public static JTextArea File_info;
	public static JTextArea Upload_info;
	public static JButton Search_file_btn;
	public static String File_location;
	public static EncryptedFileInfo fileObj;

	public ClientFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 604, 806);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel each_mode_panel = new JPanel();
		each_mode_panel.setBackground(SystemColor.window);
		each_mode_panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		each_mode_panel.setBounds(220, 32, 340, 59);
		contentPane.add(each_mode_panel);
		each_mode_panel.setLayout(new BorderLayout(0, 0));

		// 첫번째
		user_info = new JTextArea();
		each_mode_panel.add(user_info);
		JScrollPane UserScrollPane = new JScrollPane(user_info);
		each_mode_panel.add(UserScrollPane);
		user_info.setEditable(false);
		user_info.setEditable(false);
		user_info.setBackground(Color.WHITE);

		JLabel lblNewLabel = new JLabel("Commmuncation Mode");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblNewLabel.setBounds(20, 20, 177, 28);
		contentPane.add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel.setBounds(31, 103, 529, 38);
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		// connection client check
		connectionCheck = new JTextArea("Server : Not Connection");
		connectionCheck.setEditable(false);
		panel.add(connectionCheck);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_1.setBounds(31, 582, 525, 196);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_10.setBounds(6, 6, 513, 78);
		panel_1.add(panel_10);
		panel_10.setLayout(new BorderLayout(0, 0));

		File_info = new JTextArea();
		panel_10.add(File_info);

		JPanel panel_11 = new JPanel();
		panel_11.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_11.setBounds(11, 96, 501, 45);
		panel_1.add(panel_11);
		panel_11.setLayout(new BorderLayout(0, 0));

		Upload_info = new JTextArea();
		panel_11.add(Upload_info);

		Search_file_btn = new JButton("Search");
		Search_file_btn.setBounds(98, 148, 117, 29);
		Search_file_btn.addActionListener(this);
		panel_1.add(Search_file_btn);

		Send_file_btn = new JButton("Send");
		Send_file_btn.setBounds(277, 148, 117, 29);
		Send_file_btn.addActionListener(this);
		panel_1.add(Send_file_btn);

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
		panel_3.setBounds(31, 153, 529, 255);
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

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_7.setBounds(11, 6, 478, 44);
		panel_6.add(panel_7);
		panel_7.setLayout(new BorderLayout(0, 0));
		JScrollPane otherScrollPane = new JScrollPane(otherKeyPair_info);
		panel_7.add(otherScrollPane);

		otherKeyPair_info = new JTextArea();
		otherKeyPair_info.setBounds(6, 7, 482, 42);

		panel_7.add(otherKeyPair_info);

		sendPublic_button = new JButton("Send Public Key");
		sendPublic_button.setBounds(67, 187, 166, 29);
		panel_3.add(sendPublic_button);

		sendSecret_button = new JButton("Send Secret Key");
		sendSecret_button.setBounds(275, 187, 152, 29);
		panel_3.add(sendSecret_button);
		
		JButton btnNewButton_2 = new JButton("Receieve Public Key");
		btnNewButton_2.setBounds(66, 220, 166, 29);
		panel_3.add(btnNewButton_2);
		sendSecret_button.addActionListener(this);
		sendPublic_button.addActionListener(this);
		// 라디오 버튼
		JPanel radioPanel = new JPanel();
		radioPanel.setBounds(6, 50, 203, 38);
		contentPane.add(radioPanel);
		mod_client = new JRadioButton("Client");
		radioPanel.add(mod_client);
		mod_server = new JRadioButton("Server");
		radioPanel.add(mod_server);
		setVisible(true);
	}

	public void ClientsendMessage() {
		try {

			String text = chatTextField.getText();
			String result = EncryptAES(text);

			// 입력된 메세지가 "/exit" 일 경우
			if (text.equals("/exit")) {
				// textArea 에 "bye" 출력 후
				// stopSignal을 true로 설정 , 스트림 반환, 소켓 반환
				ClientChat.stopSignal = true;
				ClientChat.oos.close();
				ClientChat.socket.close();

				// 프로그램 종료
				System.exit(0);
			} else {
				// 입력된 메세지가 "/exit"가 아닐 경우( 전송할 메세지인 경우)
				// 클라이언트에게 메세지 전송

				ClientChat.oos.writeObject(result);
				// 초기화 및 커서요청
				chatTextField.setText("");
				chatTextField.requestFocus();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
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

		keyPair_info.append("\n Public Key : ");
		for (byte b : pubk)
			keyPair_info.append(String.format("%02X ", b));
		System.out.println("\n Public Key Length : " + pubk.length + " byte");
		keyPair_info.append("\n Private Key : ");
		for (byte b : prik)
			keyPair_info.append(String.format("%02X ", b));
		System.out.println("\n Private Key Length : " + prik.length + " byte");
		checkMakeRSA = true;
	}

	public byte[] GenerateAES() throws NoSuchAlgorithmException {
		KeyGenerator keyGen2 = KeyGenerator.getInstance("AES");
		keyGen2.init(256);
		skey = keyGen2.generateKey();
		byte[] printKey2 = skey.getEncoded();
		System.out.print("Secret key generation complete: ");
		for (byte b : printKey2)
			System.out.printf("%02X ", b);
		System.out.print("\nLength of secret key: " + printKey2.length + " byte");
		return printKey2;
	}

	public String EncryptAES(String plaintext) {
		String result = null;
		try {

			Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher2.init(Cipher.ENCRYPT_MODE, skey, new IvParameterSpec(iv.getBytes()));

			byte[] encryptedAES = cipher2.doFinal(plaintext.getBytes("UTF-8"));
			result = new String(Base64.getEncoder().encode(encryptedAES));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public byte[] EncryptRSA(byte[] plaintext) {
		byte[] encryptedSecret = null;

		try {

			Cipher cipher = Cipher.getInstance("RSA");

			cipher.init(Cipher.ENCRYPT_MODE, ClientChat.server_pub);
			encryptedSecret = cipher.doFinal(plaintext);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedSecret;
	}

	public void sendFile() throws IOException {
		File file = new File(File_location);
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		fileObj = new EncryptedFileInfo();
		fileObj.setFilename(file.getName());
		// maximum size
		// 2048byte read
		byte[] data = new byte[200];
		int len;
		int fileNum = 0;
		while ((len = bis.read(data)) != -1) {
			fileNum++;
		}
		DivideFileInfo[] arr = new DivideFileInfo[fileNum];
		while ((len = bis.read(data)) != -1) {
			ClientChat.oos.writeInt(len); // 데이터 크기 전송
//			ClientChat.oos.writeObject(EncryptRSA(data),0,len);  //데이터 내용 전송
			ClientChat.dos.flush();
		}
		arr = Arrays.copyOfRange(arr, 0, fileNum);
		fileObj.setEncryptedFile(arr);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == send_button) {
			ClientsendMessage();
		} else if (e.getSource() == keyGenerate_button) {
			try {
				GenerateRSAKey();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == sendPublic_button) {
			if (checkMakeRSA == false) {
				user_info.append("Should make public key\n");
			} else {
				try {
					ClientChat.oos.writeObject(publicKey);
					ClientChat.oos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == sendSecret_button) {
			try {
				if (ClientChat.getSubpubk == false) {
					user_info.append("should get the server public key\n");
				} else {
					EncryptedAESInfo obj = new EncryptedAESInfo();
					byte[] encryptedAESkey;
					byte[] encryptedIv;
					iv = "1234567812345678";
					encryptedIv = EncryptRSA(iv.getBytes("UTF-8"));
					encryptedAESkey = EncryptRSA(GenerateAES());
					obj.setEncryptedAESkey(encryptedAESkey);
					obj.setEncryptedIv(encryptedIv);
					ClientChat.oos.writeObject(obj);
					ClientChat.oos.flush();
				}
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == Search_file_btn) {
			JFileChooser jfc = new JFileChooser();
			if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File_location = jfc.getSelectedFile().toString();
				Upload_info.append("location : " + jfc.getSelectedFile().toString() + "\n");
			}
		} else if (e.getSource() == Send_file_btn) {
			try {
				ClientChat.oos.writeObject(fileObj);
				ClientChat.oos.flush();
				Upload_info.setText("");
				Upload_info.requestFocus();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
