import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

public class ConnectApp {
	
	public static Socket connectApp() throws Exception{
		// 파일 읽어서 서버 IP, Port 알아내기
		BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\nptxt.txt"));
		
		String str;
		String serverIP = new String();
		int serverPort = 0;
		int count = 0;
		
		while((str = bufferedReader.readLine()) != null) {
			if(count == 0) {
				serverIP = str;
				count++;
			}
			else {
				serverPort = Integer.parseInt(str);
			}	
		}
		
		bufferedReader.close();
		
		System.out.println("server IP : " + serverIP);
		System.out.println("server Port : " + serverPort);
		
		Socket clientSocket = null;
		
		try {
			clientSocket = new Socket(serverIP, serverPort);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return clientSocket;
	}
	
}