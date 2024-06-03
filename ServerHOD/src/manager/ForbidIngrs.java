package manager;

import database.UserInfo;
import server.Server;

public class ForbidIngrs extends Command{
	
	private String str;
	private UserInfo uf;
	
	public ForbidIngrs(UserInfo uf) {
		this.uf = uf;
	}
	
	@Override
	public void run() {
		try {
			int IID = Integer.parseInt(uf.getReq());
			
			Server.db.delete_permitted_ingridient(Server.connect, IID, uf.getUID());
			str = "Added successfully";
		
		}catch(Exception e) {
			str = "There is no ingredient with this id";

		}
	}
	public String getString() {
		return str;
	}

}
