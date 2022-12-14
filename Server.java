import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.sql.*;

public class Server {
	class Lists {
		static final ArrayList<User> users = new ArrayList<>();
		static final ArrayList<ChatRoom> rooms = new ArrayList<>();
		
		public static synchronized void add(User u) {
			users.add(u);
		}
		public static synchronized User get(String id) {
			for (User u : users) {
				if (u.getId().equals(id)) {
					return u;
				}
			}
			return null;
		}
		public static synchronized void toJoiner(String id, String nickname, String roomId) {
			System.out.println("tojoiner");
			for (User u : users) {
				if (u.getNickname().equals(id)) {
					HashMap<String, String> header1 = new HashMap<>();
					header1.put("request", "chat");
					HashMap<String, String> msg1 = new HashMap<>();
					msg1.put("nickname", nickname);
					msg1.put("roomId", roomId);
					Protocol r = new Protocol(header1);
					r.setMsg(msg1);
					
					try {
						u.getOut().writeObject(r);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println("From: " + nickname + " / To: " + u.getNickname());
				}
			}
			
//			for (User u : users) {
//				if (u.getId().equals(id)) {
//					HashMap<String, String> header1 = new HashMap<>();
//					header1.put("request", "chat");
//					HashMap<String, String> msg1 = new HashMap<>();
//					msg1.put("nickname", nickname);
//					msg1.put("roomId", roomId);
//					Protocol r = new Protocol(header1);
//					r.setMsg(msg1);
//					
//					try {
//						u.getOut().writeObject(r);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					System.out.println("From: " + nickname + " / To: " + u.getId());
//				}
//			}
		}
		
		public static synchronized void add(ChatRoom c) {
			rooms.add(c);
		}
		public static synchronized int indexOf(ChatRoom c) {
			return rooms.indexOf(c);
		}
		public static synchronized ChatRoom get(int i) {
			return rooms.get(i);
		}
		public static synchronized void remove(ChatRoom c) {
			rooms.remove(c);
		}
	}
	
	public static void main(String[] args) {
		int nPort = 7777;
	
		ServerSocket listener = null;
		Socket socket = null;
		try {
			listener= new ServerSocket(nPort);
			ExecutorService pool = Executors.newFixedThreadPool(20);
			System.out.println("Start Server & Waiting Connect (Port# = " + nPort + ")");
			System.out.println("<------------------------------------------------------------>");
			System.out.println();
		

			while(true) {
				socket = listener.accept();
				pool.execute(new Capitalizer(socket));
			}
		}
		catch (IOException e) {
			System.out.println(e.getMessage()); // 소켓 생성에서 발생할 수 있는 오류 처리
		} 
		finally {
			try {
				if(listener != null) listener.close(); // 서버 소켓 닫기
			} 
			catch (IOException e) {
				System.out.println("Error");
			}
		}
	}
	
	private static class Capitalizer implements Runnable{
		private Socket socket;
		
		Capitalizer(Socket socket){
			this.socket = socket;
		}
		
		@Override
		public void run() {
			ObjectOutputStream outToClient = null;
			ObjectInputStream inFromClient = null;		
			User client = null;
			Protocol requestProtocol;
			System.out.println("User connect IP: " + socket.getInetAddress() + " Port: " + socket.getPort());
			try {
				outToClient = new ObjectOutputStream(socket.getOutputStream());
				inFromClient = new ObjectInputStream(socket.getInputStream());
				
				while(true) {
					try {
						if((requestProtocol = (Protocol) inFromClient.readObject()) != null) {
						//Protocol requestProtocol = (Protocol) inFromClient.readObject();
						System.out.println(requestProtocol.getHeader());
						System.out.println(requestProtocol.getMsg());
						System.out.println("end");
						if(requestProtocol.getHeader().containsKey("request")) {
							
							if(requestProtocol.getHeader().get("request").equalsIgnoreCase("login")) {
								ServerLogin.ServerLogin(outToClient, inFromClient, requestProtocol);
								client = requestProtocol.getMyinfo();
								client.setOut(outToClient);
								client.setIn(inFromClient);
								Lists.add(client);
							
							}
							/*
							else if(requestProtocol.getHeader().get("request").equalsIgnoreCase("register")) {
							ServerSignup.signup(outToClient, requestProtocol);
							}
							else if(requestProtocol.getHeader().get("request").equalsIgnoreCase("search")) {
							ServerSearch.search(outToClient, requestProtocol);
							} // end of search
							else if(requestProtocol.getHeader().get("request").equalsIgnoreCase("addfriend")) {
							ServerAddFriend.addFriend(outToClient, requestProtocol);
							}
							else if(requestProtocol.getHeader().get("request").equalsIgnoreCase("adddetail")) {
								ServerAdddetail.addDetail(outToClient, requestProtocol);
							}
							else if(requestProtocol.getHeader().get("request").equalsIgnoreCase("changemsg")) {
								ServerChangeMsg.changemsg(outToClient, requestProtocol);
							}
							else if(requestProtocol.getHeader().get("request").equalsIgnoreCase("updatetime")) {
								ServerUpdateLstime.updateLstime(outToClient, requestProtocol);
							}*/
							else if(requestProtocol.getHeader().get("request").equalsIgnoreCase("chat")) {
								switch (requestProtocol.getMsg().get("request")) {
								case "create":
									System.out.println("create");
									/* 
									 * 방을 만들고, 현재 통신하고 있는 클라이언트를 방에 추가.
									 * 받은 아이디에 해당하는 유저에게 요청 전송.
									 */
									ChatRoom room = new ChatRoom(client);
									Lists.add(room);
									room.setId(Lists.indexOf(room));
									
									HashMap<String, String> header = new HashMap<>();
									header.put("request", "goChat");
									HashMap<String, String> msg = new HashMap<>();
									msg.put("roomId", Integer.toString(room.getId()));
									Protocol create = new Protocol(header);
									create.setMsg(msg);
									outToClient.writeObject(create);
									
									System.out.println(create.getHeader());
									//Users.get("admin").getOut().writeObject(create);
									//Users.get("test").getOut().writeObject(create);
									
									//Lists.toJoiner(requestProtocol.getMsg().get("id"), client.getNickname(), Integer.toString(room.getId()));
									Lists.toJoiner(requestProtocol.getMsg().get("nick"), client.getNickname(), Integer.toString(room.getId()));
									/*for (User u : users) {
										if (u.getId().equals(request.getMsg().get("id"))) {
											HashMap<String, String> header1 = new HashMap<>();
											header1.put("request", "chat");
											HashMap<String, String> msg1 = new HashMap<>();
											msg1.put("nickname", client.getNickname());
											msg1.put("roomId", Integer.toString(room.getId()));
											Protocol r = new Protocol(header1);
											r.setMsg(msg1);
											
											u.getOut().writeObject(r);
											
											System.out.println("From: " + client.getId() + " / To: " + u.getId());
										}
									}*/

									break;
								case "exit" :
									/*
									* 채팅방 나가기 요청
									* 방에서 해당 유저를 삭제하고 방 인원수가 1명인 경우 방 오너 교체, 0명인 경우 방 리스트에서 삭제
									*/
									int roomId = Integer.parseInt(requestProtocol.getMsg().get("roomId"));
									Lists.get(roomId).exit(client);
									ArrayList<User> member = Lists.get(roomId).getMember();
									System.out.println(member.size());
									if (member.size() > 1) {
										Lists.get(roomId).setRoomOwner(member.get(0));
									}
									else {
										HashMap<String, String> h = new HashMap<>();
										h.put("request", "chatExit");
										h.put("roomId", Integer.toString(roomId));
										
										Protocol r = new Protocol(h);
										member.get(0).getOut().writeObject(r);
										Lists.get(roomId).deleteRoom();
										Lists.remove(Lists.get(roomId));
									}
									break;
								}
								break;
							}
						}
						else if (requestProtocol.getHeader().containsKey("response")) {
							System.out.println("Response");
							switch (requestProtocol.getHeader().get("response")) {
							case "chat":
								System.out.println("Response : " + requestProtocol.getMsg().get("response"));
								/*
								 * 예 / 아니오에 따라 피요청자를 방에 추가하거나 방을 삭제
								 */
								if (requestProtocol.getMsg().get("response").equals("yes")) {
									System.out.println("In r=y");
//									
									ChatRoom c = Lists.get(Integer.parseInt(requestProtocol.getMsg().get("roomId")));
//									int tempId = c.getTempId();
//									HashMap<String, String> hn = new HashMap<>();
//									hn.put("response", "chatStart");
//									hn.put("roomId", Integer.toString(tempId));
//									HashMap<String, String> msgn = new HashMap<>();
//									msgn.put("newRoomId", Integer.toString(c.getId()));
									
//									
//									
//									Protocol rnn = new Protocol(hn);
//									rnn.setMsg(msgn);
//									System.out.println("Before sts");
//									
//									System.out.println(c.getRoomOwner().getId());
//									System.out.println(c.getRoomOwner().getNickname());
									
									//Lists.get(c.getRoomOwner().getId()).getOut().writeObject(rnn);
//									c.getRoomOwner().getOut().writeObject(rnn);
									c.invite(client);
									
									
									//String s = Lists.get(roomId).getRoomOwner().getId();
									//Users.get(s).getOut().writeObject(r);
								}
								else if (requestProtocol.getMsg().get("response").equals("no")) {
									ChatRoom c = Lists.get(Integer.parseInt(requestProtocol.getMsg().get("roomId")));
									HashMap<String, String> header = new HashMap<>();
									header.put("request", "chatExit");
									header.put("roomId", Integer.toString(c.getId()));
								
									Protocol r = new Protocol(header);
									c.getRoomOwner().getOut().writeObject(r);
									System.out.println("close " + c + "room");
									c.deleteRoom();
									Lists.remove(c);
								}
								break;
							}
						} else if (requestProtocol.getHeader().containsKey("chat")) {
							System.out.println("broadcast" +requestProtocol.getMsg().get("message"));
							String message = requestProtocol.getMsg().get("message");
							Lists.get(Integer.parseInt(requestProtocol.getHeader().get("chat"))).broadcast(message);
							Lists.get(Integer.parseInt(requestProtocol.getHeader().get("chat"))).broadcast(message);
							Lists.get(Integer.parseInt(requestProtocol.getHeader().get("chat"))).broadcast(message);
						}
					}
					} catch (IOException e) {
						e.printStackTrace();
					} 
					catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			finally {
//				try {
//					if(socket != null) socket.close();
//					
//				}
//				catch(IOException e) {
//					e.printStackTrace();
//				}
			}
		}
		
	}
	
	
}