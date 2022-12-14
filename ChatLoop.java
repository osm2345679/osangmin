import java.io.IOException;
import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

public class ChatLoop implements Runnable {
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	ChatLoop(ObjectOutputStream out, ObjectInputStream in) {
		this.out = out;
		this.in = in;
	}
	
	
	
	@Override
	public void run() {
		Protocol p;
		ExecutorService pool = Executors.newFixedThreadPool (20);
		
		// TODO Auto-generated method stub
		System.out.println("loop");
		while(true) {
			try {
				//Protocol p = (Protocol) in.readObject();
				if((p = (Protocol) in.readObject()) != null) {
					System.out.println("dsfdsf");
					if(p.getHeader().containsKey("request")) {
						System.out.println("in if");
						switch (p.getHeader().get("request")) {
						case "chat":
							System.out.println("in switch");
							String nickname = p.getMsg().get("nickname");
							int i = JOptionPane.showConfirmDialog(null, nickname + "으로부터의 채팅\n수락 / 거절", "채팅방",
									JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
							if (i == 0) {
								/* 
								 * header에 response, chat / msg에 (response, yes), (roomId, roomId)로 송신
								 * 채팅 클래스 실행
								*/
								HashMap<String, String> h = new HashMap<String, String>();
								h.put("response", "chat");
								Protocol protocol1 = new Protocol(h);

								HashMap<String, String> msg = new HashMap<String, String>();
								msg.put("response", "yes");
								msg.put("roomId", p.getMsg().get("roomId"));
								protocol1.setMsg(msg);
								out.writeObject(protocol1);
								System.out.println(protocol1.getHeader());
								pool.execute(new Chat(in, out, Integer.parseInt(p.getMsg().get("roomId"))));
							}
							else if (i == 1) {
								/* 
								 * header에 response, chat / msg에 (response, no), (roomId, roomId)로 송신
								 * */
								HashMap<String, String> h = new HashMap<String, String>();
								h.put("response", "chat");
								Protocol protocol2 = new Protocol(h);

								HashMap<String, String> msg = new HashMap<String, String>();
								msg.put("response", "no");
								msg.put("roomId", p.getMsg().get("roomId"));
								protocol2.setMsg(msg);
								out.writeObject(protocol2);
								System.out.println("GG" + protocol2.getHeader() + "/" + protocol2.getMsg());
							}
							break;
						case "goChat":
							pool.execute(new Chat(in, out, Integer.parseInt(p.getMsg().get("roomId"))));
						}
					}
				}
			} catch(IOException e) {
				//e.printStackTrace();
			} catch(ClassNotFoundException e) {
				//e.printStackTrace();
			}
	}
	}
	
}
