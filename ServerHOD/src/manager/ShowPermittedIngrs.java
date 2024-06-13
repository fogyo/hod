package manager;

import database.UserInfo;
import server.Server;

public class ShowPermittedIngrs extends Command{
	
	private UserInfo uf;
	private String str = "";
	
	public ShowPermittedIngrs(UserInfo uf) {
		this.uf = uf;
	}
	@Override 
	public void run() {
		str = str +  Server.db.get_permitted_ingrs(Server.connect, uf.getUID());
	}
	
	public String getString() {
		return str;
	}

}
