import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;



public class SignupGUI {

	JFrame signup;
	Container c;
	ObjectInputStream in;
	ObjectOutputStream out;
	
	JPanel idPanel = new JPanel();
	JPanel pwPanel = new JPanel();
	JPanel namePanel = new JPanel();
	JPanel nickPanel = new JPanel();
	JPanel emailPanel = new JPanel();
	JPanel birthPanel = new JPanel();
	
	JLabel idL = new JLabel("아이디");
	JLabel pwL = new JLabel("비밀번호");
	JLabel nameL = new JLabel("이름");
	JLabel nickL = new JLabel("별명");
	JLabel emailL = new JLabel("이메일");
	JLabel birthL = new JLabel("생일(YYYY-MM-DD)");
	
	static JTextField id = new JTextField();
	static JPasswordField pw = new JPasswordField();
	static JTextField name = new JTextField();
	static JTextField nick = new JTextField();
	static JTextField email = new JTextField();
	static JTextField birth = new JTextField();
	
	//아이디label 옆에 아이디 중복확인하기
	JButton jbtn1 = new JButton("취소");
	JButton jbtn2 = new JButton("가입하기");
	JButton checkbtn = new JButton("중복확인");

	public SignupGUI(ObjectInputStream clientIn, ObjectOutputStream clientOut) throws Exception{
		
		in = clientIn;
		out = clientOut;
		
		id.setPreferredSize(new Dimension(140,30));
		pw.setPreferredSize(new Dimension(140,30));
		name.setPreferredSize(new Dimension(140,30));
		nick.setPreferredSize(new Dimension(140,30));
		email.setPreferredSize(new Dimension(140,30));
		birth.setPreferredSize(new Dimension(140,30));
		
		idPanel.add(idL);
		idPanel.add(id);
		idPanel.setLayout(new FlowLayout());
		
		pwPanel.add(pwL);
		pwPanel.add(pw);
		pwPanel.setLayout(new FlowLayout());
		
		namePanel.add(nameL);
		namePanel.add(name);
		namePanel.setLayout(new FlowLayout());
		
		emailPanel.add(emailL);
		emailPanel.add(email);
		emailPanel.setLayout(new FlowLayout());
		
		nickPanel.add(nickL);
		nickPanel.add(nick);
		nickPanel.setLayout(new FlowLayout());
		
		birthPanel.add(birthL);
		birthPanel.add(birth);
		birthPanel.setLayout(new FlowLayout());
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(jbtn1);
		buttonPanel.add(jbtn2);
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(6,1));
		
		contentPanel.add(idPanel);
		contentPanel.add(pwPanel);
		contentPanel.add(namePanel);
		contentPanel.add(nickPanel);
		contentPanel.add(emailPanel);
		contentPanel.add(birthPanel);
		
	
		
		ButtonListener bl = new ButtonListener();
		
		jbtn1.addActionListener(bl);//취소
		jbtn2.addActionListener(bl);//가입하기
		checkbtn.addActionListener(bl);//중복확인
		
		
		//frame
		signup = new JFrame("Register");
		
		signup.add(contentPanel,BorderLayout.CENTER);
		signup.add(buttonPanel,BorderLayout.SOUTH);
		
		signup.setSize(500,400);
		signup.setResizable(true);
		signup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		signup.setVisible(true);
		
	}
	
	class ButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			
			String _id = id.getText();
			String _pw = "";
			
			
			for(int i=0;i<pw.getPassword().length;i++) {
				_pw = _pw + pw.getPassword()[i];
			}
			
			String _name = name.getText();
			String _nick = nick.getText();
			String _email = email.getText();
			String _birth = birth.getText();
			
			if(b.getText().equals("취소")) {
				signup.dispose();
				
			}else if(b.getText().equals("가입하기")) {
				if(_id.equals("")||_pw.equals("")||_name.equals("")||_nick.equals("")||_email.equals("")||_birth.equals("")){
					
					System.out.println("회원가입 실패 > 미입력");
					JOptionPane.showMessageDialog(null, "모두 입력하세요","회원가입 실패",JOptionPane.ERROR_MESSAGE);
		
				}else if(_id != null && _pw != null && _name != null && _nick != null && _email != null && _birth != null) {
					try {
						if(SignupClient.signup(in, out, _id, _pw, _name, _nick, _email, _birth) == "success"){
							System.out.println("회원가입 성공");
							JOptionPane.showMessageDialog(null, "회원가입을 축하합니다!");
							id.setText("");
							pw.setText("");
							name.setText("");
							nick.setText("");
							email.setText("");
							birth.setText("");
							signup.dispose();
						}else {
							System.out.println("회원가입 실패");
							JOptionPane.showMessageDialog(null, "회원가입에 실패하였습니다.");
							id.setText("");
							pw.setText("");
							name.setText("");
							nick.setText("");
							email.setText("");
							birth.setText("");
						}
					} catch (HeadlessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}


}