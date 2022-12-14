import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatRoom {
	private int id;
	private int tempId;
	private User roomOwner;
	private ArrayList<User> member = new ArrayList<>();
	
	ChatRoom(User u, int i) {
		roomOwner = u;
		member.add(u);
		tempId = i;
	}
	
	public void invite(User joiner) {
		member.add(joiner);
		for (User k : member) {
			try {
				HashMap<String, String> header = new HashMap<>();
				header.put("chat", Integer.toString(id));
				HashMap<String, String> message = new HashMap<>();
				message.put("message", joiner.getNickname() + " is joined.");
				Protocol r = new Protocol(header);
				r.setMsg(message);
				k.getOut().writeObject(r);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void deleteRoom() {
		roomOwner = null;
		member.clear();
		member = null;
	}
	
	public void exit(User u) {
		member.remove(u);
		for (User k : member) {
			try {
				HashMap<String, String> header = new HashMap<>();
				header.put("chat", Integer.toString(id));
				HashMap<String, String> message = new HashMap<>();
				message.put("message", u.getNickname() + " is left.");
				Protocol r = new Protocol(header);
				r.setMsg(message);
				k.getOut().writeObject(r);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void broadcast(String msg) {
		for (User u : member) {
			try {
				HashMap<String, String> header = new HashMap<>();
				header.put("chat", Integer.toString(id));
				HashMap<String, String> message = new HashMap<>();
				message.put("message", ">>" + msg);
				Protocol r = new Protocol(header);
				r.setMsg(message);
				u.getOut().writeObject(r);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 아래는 getter와 setter들
	 */
	public User getRoomOwner() {
		return roomOwner;
	}

	public void setRoomOwner(User roomOwner) {
		this.roomOwner = roomOwner;
	}

	public ArrayList<User> getMember() {
		return member;
	}

	public void setMember(ArrayList<User> member) {
		this.member = member;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTempId() {
		return tempId;
	}

	public void setTempId(int tempId) {
		this.tempId = tempId;
	}
}