import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class EditStatemsgGUI extends JFrame{
	Protocol loginProtocol;
	
	JLabel head;
	JTextField change;
	JButton btn;
	
	String _myid;
	String _statemsg;
	
	public EditStatemsgGUI(Protocol loginProtocol) {
		
		this.loginProtocol = loginProtocol;
		this._myid = loginProtocol.getMyinfo().getId();
		
		String nowmsg = loginProtocol.getMyinfo().getMessage();
		
		Font font = new Font("Serif", Font.BOLD, 20);
		
		setTitle("Change my state message");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c = getContentPane();
		c.setBackground(Color.WHITE);
		c.setLayout(null);
		
		head = new JLabel("Change my state message");
		head.setBounds(82, 10, 400, 80);
		head.setFont(font);
		c.add(head);
		
		change = new JTextField(nowmsg);
		change.setBounds(90, 100, 200, 30);
		c.add(change);

		btn = new JButton("업데이트");
		btn.setBounds(140, 150, 100, 30);
		c.add(btn);
		
		setSize(400, 300);
		setVisible(true);
		
		
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				_statemsg = change.getText();
				System.out.println(_statemsg);
				try {
					ClientEditStatemsg.changemsg(_myid, _statemsg);
				}
				catch(Exception ee) {
					ee.printStackTrace();
				}
			}
		});
		
	}
}