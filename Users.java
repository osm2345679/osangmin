import java.util.ArrayList;

public class Users {
	private static final ArrayList<User> users = new ArrayList<>();
	
	public static void Add (User u) {
		users.add(u);
	}
	
	public static User get (String s) {
		for (User n : users) {
			if(n.getId().equals(s)) {
				return n;
			}
		}
		return null;
	}
}
