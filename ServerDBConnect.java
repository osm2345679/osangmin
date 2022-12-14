import java.sql.*;

public class ServerDBConnect {
	public static Connection connectDB() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network";
			String user = "root";
			String passwd = "8752";
			con = DriverManager.getConnection(url, user, passwd);
			System.out.println(con);
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return con;
	}
	
	public static void disconnectDB(Connection con) {
		try {
			if(con != null && !con.isClosed()) con.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
}