import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;

public class Protocol implements Serializable{
	private HashMap<String, String> header;
	private HashMap<String, String> msg;
	private User myinfo;
	private User userinfo;
	private ArrayList<User> friendList = new ArrayList<>();
	private ArrayList<User> searchList = new ArrayList<>();
	
	// Client to Server : 유저 검색
	Protocol(HashMap<String, String>header, User myinfo, User userinfo){
		this.header = header;
		this.myinfo = myinfo;
		this.userinfo = userinfo;
	}
	
	Protocol(HashMap<String, String>header, ArrayList<User> searchList){
		this.header = header;
		this.searchList = searchList;
	}
	
	// Client to Server : 회원가입 & 로그인
	Protocol(HashMap<String, String> header, User myinfo){
		this.header = header;
		this.myinfo = myinfo;
	}
	
	// Server to Client : 로그인
	Protocol(HashMap<String, String> header){
		this.header = header;
	}
	
	Protocol(HashMap<String, String> header, User myinfo, ArrayList<User> friendList){
		this.header = header;
		this.myinfo = myinfo;
		this.friendList = friendList;
	}
	
	
	public User getMyinfo() {
		return myinfo;
	}
	
	public User getUserinfo() {
		return userinfo;
	}
	
	public void setMyinfo(User u) {
		this.myinfo = u;
	}
	
	public ArrayList<User> getFriend() {
		return friendList;
	}
	
	public ArrayList<User> getSearch(){
		return searchList;
	}
	
	
	public HashMap<String, String> getHeader() {
		return header;
	}
	public void setHeader(HashMap<String, String> header) {
		this.header = header;
	}
	public HashMap<String, String> getMsg() {
		return msg;
	}
	public void setMsg(HashMap<String, String> msg) {
		this.msg = msg;
	}
}