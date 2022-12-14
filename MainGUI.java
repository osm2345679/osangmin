
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MainGUI extends JFrame {
	
	Protocol myProtocol;
	
	JLabel nickname = new JLabel("nickname");
	JLabel statemsg = new JLabel("statemsg");
	JButton searchFriends = new JButton("search friends");
	JScrollPane online = new JScrollPane();
	JLabel onlineL = new JLabel("online");
	JScrollPane offline = new JScrollPane();
	JLabel offlineL = new JLabel("offline");
	JLabel api = new JLabel("api");
	Font font = new Font("맑은 고딕",Font.PLAIN,30);
	Font font2 = new Font("맑은 고딕",Font.PLAIN,15);
	Font font3 = new Font("맑은 고딕",Font.PLAIN,10);
	
	JPanel userPanel = new JPanel();
	JPanel searchPanel = new JPanel();
	JPanel onlinePanel = new JPanel();
	JPanel offlinePanel = new JPanel();
	JPanel friendPanel = new JPanel();
	JPanel apiPanel = new JPanel();
	
	MainGUI(Protocol protocol){
	
		myProtocol = protocol;
		setBoard();
		
		nickname.setText(myProtocol.getMyinfo().getNickname());
		nickname.setFont(font);
		nickname.setHorizontalAlignment(nickname.CENTER);
		
		//statemsg.setText(myProtocol.getMyinfo().getMessage());
		statemsg.setFont(font2);
		statemsg.setHorizontalAlignment(nickname.CENTER);
		
		userPanel.add(nickname);
		userPanel.add(statemsg);
		userPanel.setLayout(new GridLayout(2,0));
		userPanel.setBorder(new LineBorder(Color.black));
		userPanel.setBackground(Color.white);
		
		searchPanel.add(searchFriends);
		searchPanel.setBorder(new LineBorder(Color.black));
		searchPanel.setBackground(Color.white);
		friendPanel.setBackground(Color.white);
		friendPanel.add(userPanel);
		
		onlinePanel.setLayout(new BorderLayout());
		onlinePanel.add(onlineL,BorderLayout.NORTH);
		onlinePanel.add(online,BorderLayout.CENTER);
		onlinePanel.setBackground(Color.white);
		friendPanel.add(onlinePanel);
		
		offlinePanel.setLayout(new BorderLayout());
		offlinePanel.add(offlineL,BorderLayout.NORTH);
		offlinePanel.add(offline,BorderLayout.CENTER);
		offlinePanel.setBackground(Color.white);
		friendPanel.add(offlinePanel);
		
		friendPanel.setLayout(new GridLayout(3,0));
		friendPanel.setBorder(new LineBorder(Color.black));
		
		apiPanel.add(api);
		apiPanel.setBorder(new LineBorder(Color.black));
		apiPanel.setBackground(Color.white);
		
		ButtonListener search = new ButtonListener();
		
		searchFriends.addActionListener(search);
		
		setTitle("main");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = getContentPane();
		
		contentPane.setLayout(new BorderLayout(10,20));
		contentPane.setBackground(Color.white);
		contentPane.add(searchPanel,BorderLayout.SOUTH);
		contentPane.add(friendPanel,BorderLayout.CENTER);
		contentPane.add(apiPanel,BorderLayout.NORTH);
		
		setSize(400,650);
		setVisible(true);
	}
	
	class ButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			
			//친구찾기
		}
	}
	
	//online & offline
	//ArrayList<User> friendList = new ArrayList<>();
	//arraylist 크기만큼 for문 돌려서 getState 0,1로 분류
	
	public void setBoard() {
		
		ArrayList<User> friendList = new ArrayList<>();
		friendList = myProtocol.getFriend();
		
		ArrayList<User> onlineList = new ArrayList<>();
		ArrayList<User> offlineList = new ArrayList<>();
		
		for(int i = 0;i<friendList.size();i++) {
			if(friendList.get(i).getState() == 0) {
				offlineList.add(friendList.get(i));
			}else if(friendList.get(i).getState() == 1){
				onlineList.add(friendList.get(i));
			}
		}
		
		int sizeofOnlines = onlineList.size();
		int sizeofOfflines = offlineList.size();
		
		JButton[] onlines = new JButton[sizeofOnlines];
		JButton[] offlines = new JButton[sizeofOfflines];
		
		online.setLayout(new ScrollPaneLayout());
		online.getViewport().setBackground(Color.WHITE);
		
		for(int m=0;m<sizeofOnlines;m++) {
			onlines[m] = new JButton();
			onlines[m].setText(onlineList.get(m).getName());
			onlines[m].setFont(font3);
			//우클릭시 팝업창
			online.add(onlines[m]);
		}
		
		offline.setLayout(new ScrollPaneLayout());
		offline.setBackground(Color.white);
		
		for(int n=0;n<sizeofOfflines;n++) {
			offlines[n] = new JButton();
			offlines[n].setText(offlineList.get(n).getName());
			offlines[n].setFont(font3);
			//우클릭시 팝업창
			offline.add(offlines[n]);
		}
		//System.out.println("setBoard");
	}
}

//import org.w3c.dom.*;
//import org.xml.sax.InputSource;
//
//
//import javax.management.modelmbean.XMLParseException;
//import javax.swing.*;
//import javax.swing.border.LineBorder;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.concurrent.Executor;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

//public class MainGUI extends JFrame {;
//	Protocol myProtocol;
//	private ObjectOutputStream out;
//	private ObjectInputStream in;
//
//	ArrayList<User> onlineList = new ArrayList<>();
//    /* ------------------------------------------------- */
//	// GUI
//    
//    JLabel nickname = new JLabel("nickname");
//	JLabel statemsg = new JLabel("statemsg");
//	JButton searchFriends = new JButton("search friends");
//	JButton editStatemsg = new JButton("Edit stateMessage");
//	JLabel onlineL = new JLabel("online");
//	JLabel offlineL = new JLabel("offline");
//	JLabel api = new JLabel();
//
//	JList onlinelist = new JList();
//	JList offlinelist = new JList();
//	JMenuItem i1;
//	JMenuItem i2;
//	
//	int onIndex;
//	
//	Font font = new Font("맑은 고딕",Font.PLAIN,30);
//	Font font2 = new Font("맑은 고딕",Font.PLAIN,15);
//	Font font3 = new Font("맑은 고딕",Font.PLAIN,10);
//	
//	JPanel userPanel = new JPanel();
//	JPanel searchPanel = new JPanel();
//	JPanel onlinePanel = new JPanel();
//	JPanel offlinePanel = new JPanel();
//	JPanel friendPanel = new JPanel();
//	JPanel apiPanel = new JPanel();
//	JPanel online = new JPanel();
//	JPanel offline = new JPanel();
//	
//	public static void main(String[] args) {
//		
//	}
//	MainGUI(Protocol protocol, ObjectInputStream in, ObjectOutputStream out) {
//	
//		this.out = out;
//		this.in = in;
//		myProtocol = protocol;
//
//		setBoard();
//		
//		nickname.setText(myProtocol.getMyinfo().getNickname());
//		statemsg.setText(myProtocol.getMyinfo().getMessage());
//		userPanel.add(nickname);
//		userPanel.add(statemsg);
//		userPanel.setLayout(new GridLayout(2,0));
//		userPanel.setBorder(new LineBorder(Color.black));
//		userPanel.setBackground(Color.white);
//		
//		searchPanel.add(searchFriends);
//		searchPanel.add(editStatemsg);
//		searchPanel.setBorder(new LineBorder(Color.black));
//		searchPanel.setBackground(Color.white);
//		friendPanel.setBackground(Color.white);
//		friendPanel.add(userPanel);
//		
//		
//		online.add(onlinelist);
//		online.setBackground(Color.PINK);
//		onlinePanel.setLayout(new BorderLayout());
//		onlinePanel.add(onlineL,BorderLayout.NORTH);
//		onlinePanel.add(online,BorderLayout.CENTER);
//	
//		onlinePanel.setBackground(Color.white);
//		friendPanel.add(onlinePanel);
//		
//		offline.add(offlinelist);
//		offline.setBackground(Color.PINK);
//		offlinePanel.setLayout(new BorderLayout());
//		offlinePanel.add(offlineL,BorderLayout.NORTH);
//		offlinePanel.add(offline,BorderLayout.CENTER);
//		offlinePanel.setBackground(Color.white);
//		friendPanel.add(offlinePanel);
//		
//		i1 = new JMenuItem("상세보기");
//		i2 = new JMenuItem("채팅하기");
//		
//		i1.addActionListener(new MenuActionListener());
//		i2.addActionListener(new MenuActionListener());
//		
//		JPopupMenu popupMenu = new JPopupMenu();
//		popupMenu.add(i1);
//		popupMenu.add(i2);
//		
//		friendPanel.setLayout(new GridLayout(3,0));
//		friendPanel.setBorder(new LineBorder(Color.black));
//		
//		api.setText("현재 기온 : " + string_temp);
//		apiPanel.add(api);
//		apiPanel.setBorder(new LineBorder(Color.black));
//		apiPanel.setBackground(Color.white);
//		
////		ButtonListener search = new ButtonListener();
//		
////		searchFriends.addActionListener(search);
////		editStatemsg.addActionListener(search);
//		setTitle("main");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		Container contentPane = getContentPane();
//		
//		contentPane.setLayout(new BorderLayout(10,20));
//		contentPane.setBackground(Color.white);
//		contentPane.add(searchPanel,BorderLayout.SOUTH);
//		contentPane.add(friendPanel,BorderLayout.CENTER);
//		contentPane.add(apiPanel,BorderLayout.NORTH);
//		
//		setSize(450,650);
//		setVisible(true);
//		
//		System.out.println("test1");
//		
//		
////		addWindowListener(new WindowListener() {
////		      
////	        public void windowClosing(WindowEvent e) {
////	        	try {
////	        		ClientUpdateLasttime.updateLstime(myProtocol);
////	        	}
////	        	catch(Exception ee) {
////	        		ee.printStackTrace();
////	        	}
////	        }
////	    
////	        public void windowOpened(WindowEvent e) { }
////	        public void windowClosed(WindowEvent e) { }    
////	        public void windowIconified(WindowEvent e) { }
////	        public void windowDeiconified(WindowEvent e) { }
////	        public void windowActivated(WindowEvent e) { }
////	        public void windowDeactivated(WindowEvent e) { }
////	    
////	     }
////	   );
//		
//		
//		onlinelist.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				if (e.getButton() == e.BUTTON3) {
//					JList c = (JList) e.getComponent();
//					int x = e.getX();
//					int y = e.getY();
//					if(!onlinelist.isSelectionEmpty()&&onlinelist.locationToIndex(e.getPoint()) == onlinelist.getSelectedIndex()) {
//						int count = c.getModel().getSize();
//						int cal = count * 18;
//						if(y<=cal) {
//							popupMenu.show(onlinelist, x, y);
//						}
//						
//					}
//				}
//			}
//		});
//		
//
//		
//		offlinelist.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				if (e.getButton() == e.BUTTON3) {
//					JList c = (JList) e.getComponent();
//					int x = e.getX();
//					int y = e.getY();
//					if(!offlinelist.isSelectionEmpty()&&offlinelist.locationToIndex(e.getPoint()) == offlinelist.getSelectedIndex()) {
//						int count = c.getModel().getSize();
//						int cal = count * 18;
//						if(y<=cal) {
//							popupMenu.show(offlinelist, x, y);
//						}
//					}
//				}
//			}
//		});
//		System.out.println("test2");
//		
////		ExecutorService looppool = Executors.newFixedThreadPool (1);
////		looppool.execute(new ChatLoop(out, in));
//		
//	}
//	
////	class ButtonListener implements ActionListener{
////		
////		public void actionPerformed(ActionEvent e) {
////			
////			JButton b = (JButton)e.getSource();
////			//친구찾기
////			if(b.getText().equals("search friends")) {
////				SearchGUI gotoSearch = new SearchGUI();
////			}else if(b.getText().equals("Edit stateMessage")) {
////				EditStatemsgGUI gotoEdit = new EditStatemsgGUI(myProtocol);
////			}
////		}
////	}
////	
//	//online & offline
//	//ArrayList<User> friendList = new ArrayList<>();
//	//arraylist 크기만큼 for문 돌려서 getState 0,1로 분류	
//	public void setBoard() {
//
//		nickname.setText(myProtocol.getMyinfo().getName());
//		nickname.setFont(font);
//		nickname.setHorizontalAlignment(nickname.CENTER);
//		
//		statemsg.setFont(font2);
//		statemsg.setHorizontalAlignment(nickname.CENTER);
//		
//		ArrayList<User> friendList = new ArrayList<>();
//		friendList = myProtocol.getFriend();
//		
////		ArrayList<User> onlineList = new ArrayList<>();
//		
//		ArrayList<User> offlineList = new ArrayList<>();
//		
//		
//		for(int i = 0;i<friendList.size();i++) {
//			if(friendList.get(i).getState() == 0) {
//				offlineList.add(friendList.get(i));
//			}else if(friendList.get(i).getState() == 1){
//				onlineList.add(friendList.get(i));
//			}
//		}
//
//		String[] name = new String[5];
//		
//		for(int i=0;i<onlineList.size();i++) {
//			name[i] = onlineList.get(i).getNickname()+"  " + onlineList.get(i).getMessage()+"  "+onlineList.get(i).getLastAccess();
//		}
//		onlinelist.setListData(name);
//		
//		
//		
//		String[] name2 = new String[5];
//		
//		for(int i=0;i<offlineList.size();i++) {
//			name2[i] = offlineList.get(i).getNickname()+"  "+offlineList.get(i).getMessage()+"  "+offlineList.get(i).getLastAccess();
//			
//		}
//		offlinelist.setListData(name2);
//	}
//	
//	// end of GUI
//	/* ------------------------------------------------- */
//	
//	class MenuActionListener implements ActionListener { 
//		public void actionPerformed(ActionEvent e) {
//			int index = e.getID();
//			String cmd = e.getActionCommand(); 
//			switch(cmd) { // 메뉴 아이템의 종류 구분
//				case "상세보기" :
//					
//					break;
//				case "채팅하기" :
//					onIndex = onlinelist.getSelectedIndex();
//					onIndex = 0;
//					String oppNick = onlineList.get(onIndex).getNickname();
//					HashMap<String, String> h = new HashMap<String, String>();
//					h.put("request", "chat");
//					Protocol protocol = new Protocol(h);
//					
//					HashMap<String, String> msg = new HashMap<String, String>();
//					msg.put("request", "create");
////					msg.put("id", "test");
//					msg.put("nick", oppNick);
//					protocol.setMsg(msg);
//					System.out.println(oppNick);
//					System.out.println(protocol.getHeader());
//					System.out.println(protocol.getMsg());
//					try {
//						out.writeObject(protocol);
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					
//					break;
//			}
//		}
//	}
//	
//	// openAPI
//	static String string_temp = "0";
//	
//	// 열거형으로 정의 후 사용
//    enum WeatherValue {
//        PTY, REH, RN1, T1H, UUU, VEC, VVV, WSD
//    }
    
    
//	public static void main(String[] args) throws Exception {
//		System.out.println("dsfsd");
//	}
//        
        
    // end of Open API
    /* ------------------------------------------------- */    
       
//        
//
//        
//        while(true) {
//			try {
//				HashMap<String, String> hd = new HashMap<String, String>();
//				hd.put("chat", "chat");
//				Protocol k = new Protocol(hd);
//				out.writeObject(k);
//				Protocol p = (Protocol) in.readObject();
//				//if((p = (Protocol) in.readObject()) != null) {
//					System.out.println("dsfdsf");
//					if(p.getHeader().containsKey("request")) {
//						System.out.println("in if");
//						switch (p.getHeader().get("request")) {
//						case "chat":
//							System.out.println("in switch");
//							String nickname = p.getMsg().get("nickname");
//							int i = JOptionPane.showConfirmDialog(null, nickname + "으로부터의 채팅\n수락 / 거절", "채팅방",
//									JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
//							if (i == 0) {
//								/* 
//								 * header에 response, chat / msg에 (response, yes), (roomId, roomId)로 송신
//								 * 채팅 클래스 실행
//								*/
//								HashMap<String, String> h = new HashMap<String, String>();
//								h.put("response", "chat");
//								Protocol protocol = new Protocol(h);
//
//								HashMap<String, String> msg = new HashMap<String, String>();
//								msg.put("response", "yes");
//								msg.put("roomId", p.getMsg().get("roomId"));
//								protocol.setMsg(msg);
//								out.writeObject(protocol);
//								pool.execute(new Chat(in, out, Integer.parseInt(p.getMsg().get("roomId"))));
//							}
//							else if (i == 1) {
//								/* 
//								 * header에 response, chat / msg에 (response, no), (roomId, roomId)로 송신
//								 * */
//								HashMap<String, String> h = new HashMap<String, String>();
//								h.put("response", "chat");
//								Protocol protocol = new Protocol(h);
//
//								HashMap<String, String> msg = new HashMap<String, String>();
//								msg.put("response", "no");
//								msg.put("roomId", p.getMsg().get("roomId"));
//								protocol.setMsg(msg);
//								out.writeObject(protocol);
//								System.out.println("GG" + protocol.getHeader() + "/" + protocol.getMsg());
//							}
//							break;
//						}
//					}
//				//}
//			} catch(IOException e) {
//				e.printStackTrace();
//			} catch(ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//				
//		}
        //new MainGUI();
	/* ------------------------------------------------- */
	
	
      
	
//}