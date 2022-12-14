import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ClientUpdateLasttime {
	public static void updateLstime(Protocol myProtocol) throws Exception {
		ObjectOutputStream outToServer = null;
		ObjectInputStream inFromServer = null;
		Socket socket = null;
		Protocol responseProtocol = null;
		
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("request", "updatetime");
		
		try {
			socket = new Socket("localhost", 7777);
			outToServer = new ObjectOutputStream(socket.getOutputStream());
			inFromServer = new ObjectInputStream(socket.getInputStream());
			User test = new User(myProtocol.getMyinfo().getId());
			Protocol requestProtocol = new Protocol(header, test);
			outToServer.writeObject(requestProtocol);
			
			responseProtocol = (Protocol) inFromServer.readObject();
			System.out.println(responseProtocol);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}