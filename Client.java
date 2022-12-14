import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

import javax.swing.JOptionPane;

public class Client {
	public static void main(String[] args) {
		User my = null;
		Socket socket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try {
			socket = ConnectApp.connectApp();
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			new GUILogin(in, out);
			
			Scanner keyboard = new Scanner(System.in);
			int rid = -1;

			Protocol p;
			ExecutorService pool = Executors.newFixedThreadPool (20);
			String opp = null;
			
			while(true) {
				try {
					System.out.println("EWRWE");
					/*
					 * 채팅을 요청할 때
					 */
					/*
					 * header에 (request, chat), (rid, -i) / msg에 request, create로 송신
					 * -i라는 변수로 각 요청 구분 -> 매 채팅 실행마다 i 증가.
					 * 채팅 클래스 실행
					 * Chat(socket, i);
					 */
					if((opp = keyboard.nextLine()) != null) {
						HashMap<String, String> h = new HashMap<String, String>();
						h.put("request", "chat");
						h.put("rid", Integer.toString(rid));
						Protocol protocol = new Protocol(h);

						HashMap<String, String> msg = new HashMap<String, String>();
						msg.put("request", "create");
						msg.put("id", opp);
						protocol.setMsg(msg);
						out.writeObject(protocol);
						
						pool.execute(new Chat(in, out, rid));
					}
					if((p = (Protocol) in.readObject()) != null) {
						System.out.println(p.getHeader());
						if(p.getHeader().containsKey("request")) {
							switch (p.getHeader().get("request")) {
							case "chat":
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
									Protocol protocol = new Protocol(h);

									HashMap<String, String> msg = new HashMap<String, String>();
									msg.put("response", "yes");
									msg.put("roomId", p.getMsg().get("roomId"));
									protocol.setMsg(msg);
									out.writeObject(protocol);
									pool.execute(new Chat(in, out, Integer.parseInt(p.getMsg().get("roomId"))));
								}
								else if (i == 1) {
									/* 
									 * header에 response, chat / msg에 (response, no), (roomId, roomId)로 송신
									 * */
									HashMap<String, String> h = new HashMap<String, String>();
									h.put("response", "chat");
									Protocol protocol = new Protocol(h);

									HashMap<String, String> msg = new HashMap<String, String>();
									msg.put("response", "no");
									msg.put("roomId", p.getMsg().get("roomId"));
									protocol.setMsg(msg);
									out.writeObject(protocol);
									System.out.println("GG" + protocol.getHeader() + "/" + protocol.getMsg());
								}
								break;
							}
						}
					}
				} catch(IOException e) {
					e.printStackTrace();
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
					
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}