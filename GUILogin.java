import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;

import java.security.NoSuchAlgorithmException;

public class GUILogin {
	JFrame login;
	ObjectInputStream in;
	ObjectOutputStream out;
	//Panel
	JPanel basePanel = new JPanel(new BorderLayout());
	//basePanel위에 centerPanel과 southPanel붙여줌
	JPanel centerPanel = new JPanel(new BorderLayout());
	//centerPanel위에 westPanel과 eastPanel붙여줌
	JPanel westPanel = new JPanel(); //JLabel idL,pwL과 JTextField id, pw올라감
	JPanel eastPanel = new JPanel(); //login 버튼 올라감
	JPanel southPanel = new JPanel(); //회원가입 버튼 올라감
	//Label
	JLabel idL = new JLabel("아이디");
	JLabel pwL = new JLabel("비밀번호");
	static //TextField
	JTextField id = new JTextField();
	static JPasswordField pw = new JPasswordField();

	//Button
	JButton loginBtn = new JButton("로그인");
	JButton joinBtn = new JButton("회원가입");


	public GUILogin(ObjectInputStream clientIn, ObjectOutputStream clientOut) throws Exception{
		in = clientIn;
		out = clientOut;
		//Panel 크기작업
		centerPanel.setPreferredSize(new Dimension(260,80));
		westPanel.setPreferredSize(new Dimension(210,75));
		eastPanel.setPreferredSize(new Dimension(90,75));
		southPanel.setPreferredSize(new Dimension(290,40));

		//Label 크기작업
		idL.setPreferredSize(new Dimension(50,30));
		pwL.setPreferredSize(new Dimension(50,30));

		//TextField 크기작업
		id.setPreferredSize(new Dimension(140,30));
		pw.setPreferredSize(new Dimension(140,30));


		//Button 크기작업
		loginBtn.setPreferredSize(new Dimension(135,25));
		joinBtn.setPreferredSize(new Dimension(135,25));


		basePanel.add(centerPanel,BorderLayout.CENTER);
		basePanel.add(southPanel,BorderLayout.SOUTH);
		centerPanel.add(westPanel,BorderLayout.CENTER);
		centerPanel.add(eastPanel,BorderLayout.SOUTH);

		westPanel.setLayout(new FlowLayout());
		eastPanel.setLayout(new FlowLayout());
		southPanel.setLayout(new FlowLayout());

		westPanel.add(idL);
		westPanel.add(id);
		westPanel.add(pwL);
		westPanel.add(pw);

		eastPanel.add(loginBtn);


		southPanel.add(joinBtn);


		//Button 이벤트 리스너 추가
		ButtonListener bl = new ButtonListener();

		loginBtn.addActionListener(bl);

		joinBtn.addActionListener(bl);


		login = new JFrame("login");
		login.setContentPane(basePanel);
		login.setSize(500,250);
		login.setLocationRelativeTo(null);
		login.setVisible(true);
		login.setResizable(true);
		login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}




	class ButtonListener implements ActionListener{

		
		public void actionPerformed(ActionEvent e) {

			String uid = id.getText();
			String upw = "";


			for(int i=0;i<pw.getPassword().length;i++) {
				upw = upw + pw.getPassword()[i];
			}

			JButton b = (JButton)e.getSource();

			if(b.getText().equals("회원가입")) {
				try {
					new SignupGUI(GUILogin.this.in, GUILogin.this.out);
					//
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else if(b.getText().equals("로그인")) {
				//id나 pw 둘중에 하나 입력안되면 에러팝업창
				//서버가 로그인 정보 일치하는지 확인
				//일치하면 메인화면 넘어감

				if(uid.equals("")||upw.equals("")) {
					JOptionPane.showMessageDialog(null, "아이디와 비밀번호 모두 입력해주세요.","로그인 실패",JOptionPane.ERROR_MESSAGE);
					System.out.println("로그인 실패 > 로그인 정보 미입력");
				}else if(uid != null && upw != null) {
					//데이터베이스에 접속해서 아이디와 비번이 일치하는지 확인한다
					try {

						if(ClientLogin.login(GUILogin.this.in, GUILogin.this.out ,uid,upw) != null) {
							System.out.println("로그인 성공");
							login.dispose();
							MainGUI main = new MainGUI(ClientLogin.login(GUILogin.this.in, GUILogin.this.out ,uid,upw));

						}else {
							System.out.println("로그인 실패 > 로그인 정보 불일치");
							JOptionPane.showMessageDialog(null, "로그인에 실패하였습니다.");
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}

}