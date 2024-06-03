package manager;

import java.util.ArrayList;

import database.UserInfo;
import server.Server;

public class ShowSaved extends Command{
	
	private UserInfo uf;
	private String str = "";
	
	public ShowSaved(UserInfo uf) {
		this.uf = uf;
	}
	@Override 
	public void run() {
		ArrayList<Integer> RIDs = Server.db.show_saved(Server.connect, uf.getUID());
		for (Integer key : RIDs) {
			str = str +  Server.db.get_receipt(Server.connect, key) + "\n";
		}
	}
	public String getString() {
		return str;
	}
	
}