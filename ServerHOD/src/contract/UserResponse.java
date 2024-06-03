package contract;

import java.io.Serializable;

public class UserResponse implements Serializable{
	
	private boolean registry_success;
	private int user_id;
	
	public UserResponse (boolean registry_success, int user_id) {
		this.registry_success = registry_success;
		this.user_id = user_id;
	}
	
	public boolean getSuccess() {
		return registry_success;
	}
	public int getUId() {
		return user_id;
	}

}