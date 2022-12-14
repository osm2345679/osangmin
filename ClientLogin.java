import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientLogin{
	public static Protocol login(ObjectInputStream in, ObjectOutputStream out, String _id, String _pw) throws Exception{
			String msg = null;
			
			AES256 aes256 = new AES256();
			
			HashMap<String, String> header = new HashMap<String, String>();
			header.put("request", "login");
			
			User myinfo = new User(_id, aes256.encrypt(_pw));
			Protocol protocol = new Protocol(header, myinfo);
			
			Protocol resultProtocol = null;
			
			// 유저 객체 생성해서 서버로 데이터 전송하기 & 결과 메세지 받기
			try {
				out.writeObject(protocol);
				System.out.println("success send protocol to server");
				
				resultProtocol = (Protocol) in.readObject();
				System.out.println("success receive protocol from server");

				if(resultProtocol.getHeader().get("state").equals("fail")) {
					System.out.println("Fail to Login");
					System.out.println();
					msg = "fail";
				}
				else if(resultProtocol.getHeader().get("state").equals("success")) {
					System.out.println("Success to Login");
					System.out.println();
					
					ArrayList<User> friendList = new ArrayList<>();
					friendList = resultProtocol.getFriend();
					
					System.out.println("<--------------- FRIEND LIST INFORMATION --------------->");
					System.out.println("My Nickname: " + resultProtocol.getMyinfo().getNickname() + "\t\tMy Message: " + resultProtocol.getMyinfo().getMessage() + "\t\tMy State: " + resultProtocol.getMyinfo().getState());
					System.out.println();
						
					System.out.println("<--------------- FRIEND LIST INFORMATION --------------->");
					for(int i = 0; i < friendList.size(); i++) {
						System.out.println("Friend Nickname: " + friendList.get(i).getNickname() + "\t\tFriend Message: " + friendList.get(i).getMessage() + "\t\tFriend State: " + friendList.get(i).getState() + "\t\tFriend Lastaccess: " + friendList.get(i).getLastAccess());
					}
					msg = "success";
					
				}
				
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			
			return resultProtocol;
		}
	
}