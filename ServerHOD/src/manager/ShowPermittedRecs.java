package manager;

import database.UserInfo;
import server.Server;

public class ShowPermittedRecs extends Command{
	
	private UserInfo uf;
	private String str = "";
	
	public ShowPermittedRecs(UserInfo uf) {
		this.uf = uf;
	}
	@Override 
	public void run() {
		str = str +  Server.db.get_permitted_receipts(Server.connect, uf.getUID());
	}
	
	public String getString() {
		return str;
	}

}
