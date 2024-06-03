package manager;

import database.UserInfo;
import server.Server;

public class PermitIngrs extends Command{
	
	private UserInfo uf;
	private String str = "";
	
	public PermitIngrs(UserInfo uf) {
		this.uf = uf;
	}
	
	
	public void run() {
		try {
			int IID = Integer.parseInt(uf.getReq());
			
			Server.db.add_permitted_ingredient(Server.connect, IID, uf.getUID());
			str = "Added successfully";
		
		}catch(Exception e) {
			str = "There is no ingredient with this id";

		}
	}
	public String getString() {
		return str;
	}

}
