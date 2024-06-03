package database;

import java.sql.Connection;

import contract.UserResponse;
import server.Server;

public class EntryRoom {
	
	private boolean success_registry = true;
	
	
	public UserResponse registry(String username, String password, boolean signing, Connection conn) {
		if (signing) {
			UsersRow startsearching = Server.db.search_by_username(conn, username);
			if (startsearching != null) {
				success_registry = false;
				UserResponse ur = new UserResponse(success_registry, 0);
				return ur;
			}
			String hex_pass = PasswordHasher.hashPassword(password);
			Server.db.add_user(conn, username, hex_pass);
			UsersRow usr = Server.db.search_by_username(conn, username);
			UserResponse ur = new UserResponse(success_registry, usr.getId());
			return ur;
		}
		else {
			UsersRow usr = Server.db.search_by_username(conn, username);
			if (usr!=null && usr.getPassword().equals(PasswordHasher.hashPassword(password))) {
				UserResponse ur = new UserResponse(success_registry, usr.getId());
				return ur;
			}
			else {
				success_registry = false;
				UserResponse ur = new UserResponse(success_registry, 0);
				return ur;
			}
		}
	}

}
