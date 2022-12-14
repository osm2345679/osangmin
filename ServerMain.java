import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

public class ServerMain {
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
				if (u.getId().equals(id)) {
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
					
					System.out.println("From: " + nickname + " / To: " + u.getId());
				}
			}
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
	//private static final ArrayList<User> users = new ArrayList<>();
	//private static final ArrayList<ChatRoom> rooms = new ArrayList<>();
	//private static final HashMap<String, ObjectOutputStream> tests = new HashMap<>();

	public static void main(String[] args) throws Exception {
		ServerSocket listener = null;
		Socket sock = null;
		try {
			listener = new ServerSocket(7777); // 서버 소켓 생성
			ExecutorService pool = Executors.newFixedThreadPool(20);
			System.out.println("Server Start ---");
			while (true) {
				sock = listener.accept(); // 요청 대기
				System.out.println("Connect User : " + sock.getInetAddress() + "//" + sock.getPort());
				pool.execute(new Capitalizer(sock)); // 연결 되면 Capitalizer 클래스 호출
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
	
	private static class Capitalizer implements Runnable {
		public Socket socket;
		
		Capitalizer(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			ObjectOutputStream out = null;
			ObjectInputStream in = null;
			User client = null;
			Protocol request;
			try {
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
				
				while (true) {
					if((request = (Protocol) in.readObject()) != null) {
						if(request.getHeader().containsKey("request")) {
							switch (request.getHeader().get("request")) {
							case "login":
								try {
									Connection con = ServerDBConnect.connectDB();
									
									Statement stmt = null;
									ResultSet rs = null;
									ResultSet frs = null;
									try {
										stmt = con.createStatement();
										String sql = "select * from user where id = \"" + request.getMyinfo().getId() + "\"";
										rs = stmt.executeQuery(sql);
															
										while(rs.next()) {
											if(rs.getString(2).equals(request.getMyinfo().getPw())) {
												try {
													// 상태 업데이트
													stmt = con.createStatement();
													String update = "update user set state = 1 where id = \"" + request.getMyinfo().getId() + "\"";
													int count = stmt.executeUpdate(update);
													System.out.println("UPDATE ROW : " + count);
													// 내 정보
													String _myNickname = rs.getString(4);
													String _myMessage = rs.getString(7);
													int _myState = rs.getInt(8);
													request.getMyinfo().setNickname(_myNickname);
													request.getMyinfo().setMessage(_myMessage);
													request.getMyinfo().setState(_myState);
													// 친구 리스트
													stmt = con.createStatement();
													String friendsql = "select u2.nickname, u2.message, u2.state, u2.lastaccess from user as u1 inner join friends as f on u1.id = f.f1  inner join user as u2 on f.f2 = u2.id where u1.id = \"" + request.getMyinfo().getId() + "\"";
													frs = stmt.executeQuery(friendsql);
													ArrayList<User> friendList = new ArrayList<>();
													while(frs.next()) {
														String _fNickname = frs.getString(1);
														String _fMessage = frs.getString(2);
														int _fState = frs.getInt(3);
														String _fLastAccess = frs.getString(4);
														
														User friend = new User(_fNickname, _fMessage, _fState, _fLastAccess);
														
														friendList.add(friend);
													}
													// 프로토콜 생성
													HashMap<String, String> header = new HashMap<String, String>();
													header.put("response", "login");
													header.put("state", "success");
													
													Protocol resultProtocol = new Protocol(header, request.getMyinfo(), friendList);
													
													out.writeObject(resultProtocol);
													System.out.println("User Login complete");
												}
												catch(SQLException e) {
													e.printStackTrace();
												}
											}
											else {
												HashMap<String, String> header = new HashMap<String, String>();
												header.put("response", "login");
												header.put("state", "fail");
												
												Protocol resultProtocol = new Protocol(header);
												
												out.writeObject(resultProtocol);
												
												System.out.println("User Login fail");
											}
										}
									}
									catch(SQLException e) {
										e.printStackTrace();
									}
									
									ServerDBConnect.disconnectDB(con);
								} 
								// login
								catch(Exception e) {
									e.printStackTrace();
								}
								request.getMyinfo().setOut(out);
								request.getMyinfo().setIn(in);
								//users.add(request.getMyinfo());
								client = request.getMyinfo();
								
								//Users.Add(request.getMyinfo());
								//tests.put(request.getMyinfo().getId(), out);
								Lists.add(request.getMyinfo());
								break;
							case "register":
								Connection con = ServerDBConnect.connectDB();
								PreparedStatement pstmt = null;
								
								// signup
								try {
									String insert = "insert into user(`id`,`pw`,`name`,`nickname`,`birth`,`email`,`state`,`lastaccess`) values(?, ?, ?, ?, ?, ?, 0, default)";
									pstmt = con.prepareStatement(insert);
									
									String _id = request.getMyinfo().getId();
									String _pw = request.getMyinfo().getPw();
									String _name = request.getMyinfo().getName();
									String _nickname = request.getMyinfo().getNickname();
									String _birth = request.getMyinfo().getBirth();
									String _email = request.getMyinfo().getEmail();
									
									pstmt.setString(1, _id);
									pstmt.setString(2, _pw);
									pstmt.setString(3, _name);
									pstmt.setString(4, _nickname);
									pstmt.setString(5, _birth);
									pstmt.setString(6, _email);
									
									int count = pstmt.executeUpdate();
									System.out.println("INSERT ROW : " + count);
									
									HashMap<String, String> header = new HashMap<>();
									header.put("response", "register");
									Protocol p = new Protocol(header);
									out.writeObject(p);
								}
								catch(SQLException e) {
									e.printStackTrace();
								}
								break;
							case "fileTrans":
								ArrayList<User> mem = Lists.get(Integer.parseInt(request.getHeader().get("roomId"))).getMember();
								for (User u : mem) {
									if (u.getId().equals(client.getId())) {
										continue;
									}
									HashMap<String, String> header = new HashMap<>();
									header.put("chat", "fileTrans");
									header.put("roomId", request.getHeader().get("roomId"));
									Protocol f = new Protocol(header);
									f.setMsg(request.getMsg());
									out.writeObject(f);
								}
								break;
							case "chat":
								switch (request.getMsg().get("request")) {
								case "create":
									/* 
									 * 방을 만들고, 현재 통신하고 있는 클라이언트를 방에 추가.
									 * 받은 아이디에 해당하는 유저에게 요청 전송.
									 */
									ChatRoom room = new ChatRoom(client, Integer.parseInt(request.getHeader().get("rid")));
									Lists.add(room);
									int tempId = room.getId();
									room.setId(Lists.indexOf(room));
									
									HashMap<String, String> header = new HashMap<>();
									header.put("response", "chat");
									header.put("rid", request.getHeader().get("rid"));
									HashMap<String, String> msg = new HashMap<>();
									msg.put("roomId", Integer.toString(room.getId()));
									Protocol create = new Protocol(header);
									create.setMsg(msg);
									out.writeObject(create);
									
									System.out.println(create.getHeader());
									//Users.get("admin").getOut().writeObject(create);
									//Users.get("test").getOut().writeObject(create);
									
									Lists.toJoiner(request.getMsg().get("id"), client.getNickname(), Integer.toString(room.getId()));
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
									int roomId = Integer.parseInt(request.getMsg().get("roomId"));
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
						} else if (request.getHeader().containsKey("response")) {
							System.out.println("Response");
							switch (request.getHeader().get("response")) {
							case "chat":
								/*
								 * 예 / 아니오에 따라 피요청자를 방에 추가하거나 방을 삭제
								 */
								if (request.getMsg().get("response").equals("yes")) {
									ChatRoom c = Lists.get(Integer.parseInt(request.getMsg().get("roomId")));
									int tempId = c.getTempId();
									HashMap<String, String> header = new HashMap<>();
									header.put("response", "chatStart");
									header.put("roomId", Integer.toString(tempId));
									HashMap<String, String> msg = new HashMap<>();
									msg.put("newRoomId", Integer.toString(c.getId()));
									
									Protocol r = new Protocol(header);
									r.setMsg(msg);
									c.getRoomOwner().getOut().writeObject(r);
									c.invite(client);
									
									//String s = Lists.get(roomId).getRoomOwner().getId();
									//Users.get(s).getOut().writeObject(r);
								}
								else if (request.getMsg().get("response").equals("no")) {
									ChatRoom c = Lists.get(Integer.parseInt(request.getMsg().get("roomId")));
									int tempId = c.getTempId();
									HashMap<String, String> header = new HashMap<>();
									header.put("request", "chatExit");
									header.put("roomId", Integer.toString(tempId));
									
									Protocol r = new Protocol(header);
									c.getRoomOwner().getOut().writeObject(r);
									System.out.println("close " + c + "room");
									c.deleteRoom();
									Lists.remove(c);
								}
							break;
							}
						} else if (request.getHeader().containsKey("chat")) {
							String message = request.getMsg().get("message");
							Lists.get(Integer.parseInt(request.getHeader().get("chat"))).broadcast(message);
						}
					}	
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			finally {
				
				try {
					if(socket != null) socket.close(); // 통신용 소켓 닫기
				} 
				catch (IOException e) {
					System.out.println("Error");
				}
				
			}
			
		}
	}
}