import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

public class Chat extends JFrame implements Runnable{
	private JTextArea output;
	private JTextField input;
	private JButton sendBtn;
	private JButton fileBtn;

	private int roomId; // 방 id
	//private String name; // 입장 시 저장되는 클라이언트의 닉네임
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;

	Chat(ObjectInputStream clientIn, ObjectOutputStream clientOut, int i) {
		in = clientIn;
		out = clientOut;
		roomId = i;
	}
	
	ActionListener listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e){
				try{
					//서버로 보냄 
					//JTextField값을 서버로보내기
					//버퍼 비우기
					String msg=input.getText();

					HashMap<String, String> header = new HashMap<>();
					header.put("chat", Integer.toString(roomId));
					HashMap<String, String> message = new HashMap<>();
					message.put("message", msg);
					Protocol p = new Protocol(header);
					p.setMsg(message);
					out.writeObject(p);
					
					input.setText("");
					
				}catch(IOException io){
					io.printStackTrace();
				}
		}
		
	};
	
	class Read implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Protocol p;
			boolean termination = true;
			/*
			 *  상대 입장 확인하고 ui 사용 허가하는 부분
			 */
			
			System.out.println("Waiting enter...");
			
			while(termination) {
				if (roomId >= 0) {
					break;
				}
				try {
					if((p = (Protocol) in.readObject()) != null) {
						System.out.println("check");
						if(p.getHeader().containsValue(Integer.toString(roomId))) {
							if(p.getHeader().containsKey("response")) {
								if((p.getHeader().get("response").equals("chatStart")) && (p.getHeader().get("roomId").equals(Integer.toString(roomId)))) {
									roomId = Integer.parseInt(p.getMsg().get("newRoomId"));
									break;
								}
							} else if(p.getHeader().containsKey("request")) {
								if ((p.getHeader().get("request").equals("chatExit")) && (p.getHeader().get("roomId").equals(Integer.toString(roomId)))) {
									System.out.println("chec222k");
									termination = false;
									dispose();
									break;
								}
							}
						}
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			input.setEditable(true);
			sendBtn.addActionListener(listener);
			
			System.out.println("Strat chat from room " + Integer.toString(roomId));
			
			while(termination) {
				try {
					if((p = (Protocol) in.readObject()) != null) {
						if ((p.getHeader().containsKey("chat")) && (Integer.parseInt(p.getHeader().get("chat")) == roomId)) {
							String msg = p.getMsg().get("message");
							/*
							 * 텍스트 필드에 msg 반영
							 */
							output.append(msg+"\n");
							
							int pos=output.getText().length();
							output.setCaretPosition(pos);
						} else if(p.getHeader().containsKey("request")) {
							if ((p.getHeader().get("request").equals("chatExit")) && (p.getHeader().get("roomId").equals(Integer.toString(roomId)))) {
								dispose();
								break;
							}
						}
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		output = new JTextArea();
		output.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		output.setEditable(false);
		JScrollPane scroll = new JScrollPane(output);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		// 하단에 버튼과 TextArea 넣기
		JPanel bottom = new JPanel();
		JPanel bottomText = new JPanel();
		JPanel bottomBtn = new JPanel();
		
		bottom.setLayout(new BorderLayout());
		bottom.add("Center", bottomText);
		bottom.add("East", bottomBtn);
		
		// 컴포넌트 만들기
		input = new JTextField(12);
		sendBtn = new JButton("보내기");
		fileBtn = new JButton("파일");
		
		bottomText.add(input);
		bottomBtn.add(sendBtn);
		bottomBtn.add(fileBtn);
		
		// container에 붙이기
		Container c = this.getContentPane();
		c.add("Center", scroll);
		c.add("South", bottom);
		
		// 윈도우 창 설정
		setBounds(300, 300, 300, 300);
		setVisible(true);
		
		
		input.setEditable(false);
		ExecutorService pool = Executors.newFixedThreadPool (1);
		pool.execute(new Read());

		// 윈도우 이벤트
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					HashMap<String, String> header = new HashMap<>();
					header.put("request", "chat");
					HashMap<String, String> message = new HashMap<>();
					message.put("request", "exit");
					message.put("roomId", Integer.toString(roomId));
					Protocol p = new Protocol(header);
					p.setMsg(message);
					out.writeObject(p);
				} catch (IOException io) {
					
				}
			}
		});
		
		/*
		 * 보내기 버튼이나 엔터 눌렀을 때 메시지 전송.
		 * 
		 */
		/*
		HashMap<String, String> header = new HashMap<>();
		header.put("chat", Integer.toString(roomId));
		HashMap<String, String> msg = new HashMap<>();
		msg.put("message", 보낼 내용);
		Protocol p = new Protocol(header);
		p.setMsg(msg);
		out.writeObject(p);
		*/
	
	}
}