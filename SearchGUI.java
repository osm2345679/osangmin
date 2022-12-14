import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

public class SearchGUI extends JFrame {
	JLabel head;
	JTextField search;
	JButton btn;
	JLabel statePage;
	
	String myid = "tjswnsdh";
	String userid;
	
	ArrayList<User> sampleList;
	
	JMenuItem i1;
	JMenuItem i2;
	
	
	public SearchGUI() {
		Font font = new Font("Serif", Font.BOLD, 30);
		
		setTitle("Search User");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Container c = getContentPane();
		c.setBackground(Color.WHITE);
		c.setLayout(null);
		
		head = new JLabel("SEARCH USER");
		head.setBounds(140, 50, 400, 100);
		head.setFont(font);
		c.add(head);
		
		search = new JTextField();
		search.setBounds(145, 150, 200, 30);
		c.add(search);
		
		btn = new JButton("검색");
		btn.setBounds(210, 200, 80, 30);
		c.add(btn);
		
		statePage = new JLabel("검색어를 입력해주세요");
		statePage.setBounds(185, 250, 180, 50);
		c.add(statePage);
		
		setSize(500, 900);
		setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setBounds(150, 500, 200, 400);
		panel.setBackground(Color.white);
		c.add(panel);
		
		JList onlineList = new JList();
		onlineList.setSize(50, 40);
		panel.add(onlineList);
		
		i1 = new JMenuItem("상세보기");
		i2 = new JMenuItem("친구추가");
		
		i1.addActionListener(new MenuActionListener());
		i2.addActionListener(new MenuActionListener());
		
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(i1);
		popupMenu.add(i2);
		
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(search.getText().isEmpty()) {
					statePage.setText("검색어를 입력해주세요");
				}
				else {
					statePage.setText("사용자 검색이 완료되었습니다");
					statePage.setLocation(170, 250);
					userid = search.getText();
					System.out.println(userid);
					try {
						sampleList = ClientSearch.search(myid, userid).getSearch();
						String[] usList = new String[50];
						for(int i = 0; i < sampleList.size(); i++) {
							usList[i] = sampleList.get(i).getId() + "[" + sampleList.get(i).getNickname() + "]";
						}
						
						onlineList.setListData(usList);
					}
					catch(Exception ee){
						ee.printStackTrace();
					}
				}
			}
		});
		
		onlineList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == e.BUTTON3) {
					JList c = (JList) e.getComponent();
					int x = e.getX();
					int y = e.getY();
					if(!onlineList.isSelectionEmpty()&&onlineList.locationToIndex(e.getPoint()) == onlineList.getSelectedIndex()) {
						int count = c.getModel().getSize();
						int cal = count * 18;
						if(y<=cal) {
							popupMenu.show(onlineList, x, y);
						}
					}
				}
			}
		});
		
	}

	class MenuActionListener implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			int index = e.getID();
			String cmd = e.getActionCommand(); 
			switch(cmd) { // 메뉴 아이템의 종류 구분
				case "상세보기" :
					try {
						Protocol responseProtocol = ClientAddDetail.addDetail(myid, "testtest");
						statePage.setText("<html>"
								+ "ID:   " + responseProtocol.getMyinfo().getId() + "<br>"
								+ "Name:   " + responseProtocol.getMyinfo().getName() + "<br>"
								+ "NickName:   " + responseProtocol.getMyinfo().getNickname() + "<br>"
								+ "Email:   " + responseProtocol.getMyinfo().getBirth() + "<br>"
								+ "Birth:   " + responseProtocol.getMyinfo().getEmail() + "<br>"
								+ "Message:   " + responseProtocol.getMyinfo().getMessage() + "<br>"
								+ "State:   " + responseProtocol.getMyinfo().getState() + "<br>"
								+ "Lastaccess:   " + responseProtocol.getMyinfo().getLastAccess() + "<br>"
								+ "</html>");
						statePage.setSize(400, 150);
						statePage.setLocation(180, 270);
						
					}
					catch(Exception ee) {
						ee.printStackTrace();
					}
					break;
				case "친구추가" :
					try {
						Protocol responseProtocol = ClientAddFriend.addFriend(myid, "testtest");
						if(responseProtocol.getHeader().get("responseMsg").equalsIgnoreCase("s")) {
							statePage.setText("친구추가가 완료되었습니다");
						}
						else if(responseProtocol.getHeader().get("responseMsg").equalsIgnoreCase("f")) {
							statePage.setText("이미 존재하는 친구입니다");
						}
						statePage.setLocation(170, 250);
					}
					catch(Exception ee) {
						ee.printStackTrace();
					}
					break;
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		SearchGUI aaa = new SearchGUI();

	}
}