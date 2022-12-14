import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerLogin {
	public static void ServerLogin(ObjectOutputStream outToClient, ObjectInputStream inFromClient, Protocol requestProtocol) {
		try {
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			ResultSet frs = null;
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				String url = "jdbc:mysql://localhost/network";
				String user = "root";
				String passwd = "12345";
				con = DriverManager.getConnection(url, user, passwd);
				System.out.println(con);
				try {
					stmt = con.createStatement();
					String sql = "select * from user where id = \"" + requestProtocol.getMyinfo().getId() + "\"";
					rs = stmt.executeQuery(sql);
										
					while(rs.next()) {
						if(rs.getString(2).equals(requestProtocol.getMyinfo().getPw())) {
							try {
								// 상태 업데이트
								stmt = con.createStatement();
								String update = "update user set state = 1 where id = \"" + requestProtocol.getMyinfo().getId() + "\"";
								int count = stmt.executeUpdate(update);
								System.out.println("UPDATE ROW : " + count);
								// 내 정보
								/////////////
								String _myid = rs.getString(1);
								/////////////
								String _myNickname = rs.getString(4);
								String _myMessage = rs.getString(7);
								int _myState = rs.getInt(8);
								//////
								requestProtocol.getMyinfo().setId(_myid);
								//////
								requestProtocol.getMyinfo().setNickname(_myNickname);
								requestProtocol.getMyinfo().setMessage(_myMessage);
								requestProtocol.getMyinfo().setState(_myState);
								// 친구 리스트
								stmt = con.createStatement();
								String friendsql = "select u2.nickname, u2.message, u2.state, u2.lastaccess from user as u1 inner join friends as f on u1.id = f.f1  inner join user as u2 on f.f2 = u2.id where u1.id = \"" + requestProtocol.getMyinfo().getId() + "\"";
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
								
								Protocol resultProtocol = new Protocol(header, requestProtocol.getMyinfo(), friendList);
								
								outToClient.writeObject(resultProtocol);
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
							
							outToClient.writeObject(resultProtocol);
							
							System.out.println("User Login fail");
						}
					}
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			con.close();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}