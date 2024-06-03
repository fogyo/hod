package manager;

import database.UserInfo;
import server.Server;

public class SaveReceipt extends Command{
	
	private UserInfo uf;
	private String str = "";
	
	public SaveReceipt(UserInfo uf) {
		this.uf = uf;
	}
	
	@Override
	public void run() {
		try {
			int RID = Integer.parseInt(uf.getReq());
			
			Server.db.add_receipt_to_saved(Server.connect, RID, uf.getUID());
			str = "Added successfully";
		
		}catch(Exception e) {
			str = "There is no receipt with this id";

		}
	}
	public String getString() {
		return str;
	}


}
