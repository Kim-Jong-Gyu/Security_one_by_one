package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Client.ClientChat;

public class ClientFrame extends JFrame implements ActionListener{
	public static JRadioButton radio[] = new JRadioButton[2];
	public static JRadioButton mod_client;
	public static JTextArea chatTextArea;
	public static JRadioButton mod_server;
	public static JTextField chatTextField;
	public static JButton send_button;
	private static JPanel contentPane;
	public static JTextArea user_info;	
	
	public ClientFrame() {
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
		user_info = new JTextArea("Client : Not Connection");
		each_mode_panel.add(user_info);
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

		JTextArea textArea_1 = new JTextArea();
		textArea_1.setEditable(false);
		panel.add(textArea_1);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_1.setBounds(31, 604, 525, 117);
		contentPane.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JTextArea textArea_2 = new JTextArea();
		textArea_2.setEditable(false);
		panel_1.add(textArea_2);

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

		JTextArea textArea_5 = new JTextArea();
		panel_4.add(textArea_5);

		JPanel panel_5 = new JPanel();
		panel_5.setBounds(12, 81, 495, 39);
		panel_3.add(panel_5);

		JButton btnNewButton_2 = new JButton("Key Generation");
		panel_5.add(btnNewButton_2);

		JButton btnNewButton_1 = new JButton("Load From A File");
		panel_5.add(btnNewButton_1);

		JButton btnNewButton = new JButton("Save Into A File");
		panel_5.add(btnNewButton);

		JPanel panel_6 = new JPanel();
		panel_6.setBounds(12, 126, 495, 61);
		panel_3.add(panel_6);
		panel_6.setLayout(null);

		JButton btnNewButton_3 = new JButton("Send Public Key");
		btnNewButton_3.setBounds(36, 14, 125, 29);
		panel_6.add(btnNewButton_3);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_7.setBounds(173, 6, 316, 44);
		panel_6.add(panel_7);
		panel_7.setLayout(new BorderLayout(0, 0));

		JTextArea textArea_4 = new JTextArea();
		panel_7.add(textArea_4);
		// 라디오 버튼
		JPanel radioPanel = new JPanel();
		radioPanel.setBounds(5, 50, 203, 64);
		contentPane.add(radioPanel);
		mod_client = new JRadioButton("Client");
		radioPanel.add(mod_client);
		mod_server = new JRadioButton("Server");
		radioPanel.add(mod_server);
		setVisible(true);
	}
	public void ClientsendMessage() {
        try {
            String text = ClientFrame.chatTextField.getText();
            
            //입력된 메세지가 "/exit" 일 경우
            if(text.equals("/exit")) {
                //textArea 에 "bye" 출력 후
                //stopSignal을 true로 설정 , 스트림 반환, 소켓 반환
                ClientChat.cl_stopSignal=true;
                ClientChat.cl_dos.close();
                ClientChat.socket.close();
                
                //프로그램 종료
                System.exit(0);
            }else {
                //입력된 메세지가 "/exit"가 아닐 경우( 전송할 메세지인 경우)
                //클라이언트에게 메세지 전송
                ClientChat.cl_dos.writeUTF(text);
                //초기화 및 커서요청
                ClientFrame.chatTextField.setText("");
                ClientFrame.chatTextField.requestFocus();
                
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ClientsendMessage();
	}
}
