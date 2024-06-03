package database;

public class UsersRow {
	
	private String username;
	private String password;
	private int id;
	
	public UsersRow(int id, String username, String password) {
		this.username = username;
		this.password = password;
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public int getId() {
		return id;
	}

}
