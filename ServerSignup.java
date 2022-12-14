import java.sql.*;
import java.util.HashMap;
import java.net.*;
import java.io.*;

public class ServerSignup {
	
	
	public static void serverSignup(ObjectOutputStream outToClient, Connection con, Protocol protocol) throws Exception {
		PreparedStatement pstmt = null;
		
		// signup
		try {
			String insert = "insert into user(`id`,`pw`,`name`,`nickname`,`birth`,`email`,`state`,`lastaccess`) values(?, ?, ?, ?, ?, ?, 0, default)";
			pstmt = con.prepareStatement(insert);
			
			String _id = protocol.getMyinfo().getId();
			String _pw = protocol.getMyinfo().getPw();
			String _name = protocol.getMyinfo().getName();
			String _nickname = protocol.getMyinfo().getNickname();
			String _birth = protocol.getMyinfo().getBirth();
			String _email = protocol.getMyinfo().getEmail();
			
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
			outToClient.writeObject(p);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}