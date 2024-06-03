package manager;

import java.util.ArrayList;

import database.UserInfo;
import server.Server;

public class ShowReceipt extends Command{
	
	private UserInfo uf;
	private String str = "";
	
	public ShowReceipt(UserInfo uf) {
		this.uf = uf;
	}
	@Override 
	public void run() {
		int RID = Server.db.search_RID_by_name(Server.connect, uf.getReq());
		str = str +  Server.db.get_receipt(Server.connect, RID);
	}
	
	public String getString() {
		return str;
	}
}
