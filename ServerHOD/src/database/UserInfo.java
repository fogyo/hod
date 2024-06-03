package database;

import java.sql.Connection;

public class UserInfo {
	
	private int UID;
	private String request;
	
	public UserInfo(Connection conn, int UID, String request) {
		this.UID = UID;
		this.request = request;
	}
	

	public int getUID() {
		return UID;
	}
	public String getReq() {
		return request;
	}

}
