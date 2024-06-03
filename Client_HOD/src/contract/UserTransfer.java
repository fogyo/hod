package contract;

import java.io.Serializable;

public class UserTransfer implements Serializable{
	
	private String username;
	private String password;
	private boolean signing;
	
	public UserTransfer(String username, String password, boolean signing) {
		this.username = username;
		this.password = password;
		this.signing = signing;
	}
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public boolean getSigning() {
		return signing;
	}
}


