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
		str = str +  Server.db.get_receipt(Server.connect, Integer.parseInt(uf.getReq()));
	}
	
	public String getString() {
		return str;
	}
}
