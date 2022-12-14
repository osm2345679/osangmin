import java.io.*;
import java.net.Socket;

public class User implements Serializable{
	private String id;
	private String pw;
	private String name;
	private String nickname;
	private String birth;
	private String email;
	private String message;
	private int state;
	private String lastAccess;
	
	private int type;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	// search user
	User(String id){
		this.id = id;
	}
	
	User(int type, String id, String nickname){
		this.type = type;
		this.id = id;
		this.nickname = nickname;
	}
	
	// login
	User(String id, String pw) throws Exception{
		this.id = id;
		this.pw = pw;
	}
	
	User(String nickname, String message, int state){
		this.nickname = nickname;
		this.message = message;
		this.state = state;
	}
	
	User(String nickname, String message, int state, String lastAccess){
		this.nickname = nickname;
		this.message = message;
		this.state = state;
		this.lastAccess = lastAccess;
	}
	
	// sign up
	User(String id, String pw, String name, String nickname, String birth, String email) throws Exception{
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.nickname = nickname;
		this.birth = birth;
		this.email = email;
		this.message = null;
		this.state = 0;
		this.lastAccess = "";
	}
	
	
	public String getId() {
		return this.id;
	}
	
	public String getPw() {
		return this.pw;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getNickname() {
		return this.nickname;
	}
	
	public String getBirth() {
		return this.birth;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public int getState() {
		return this.state;
	}
	
	public String getLastAccess() {
		return this.lastAccess;
	}

	public void setNickname(String newNickName) {
		// TODO Auto-generated method stub
		this.nickname = newNickName;
	}

	public void setMessage(String newMessage) {
		// TODO Auto-generated method stub
		this.message = newMessage;
	}

	public void setState(int newState) {
		// TODO Auto-generated method stub
		this.state = newState;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}

	public void setId(String _myid) {
		// TODO Auto-generated method stub
		this.id = _myid;
	}
}