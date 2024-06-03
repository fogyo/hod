package manager;

import database.UserInfo;
import server.Server;

public class DeleteReceipt extends Command{
	
	private UserInfo uf;
	private String str = "";
	
	public DeleteReceipt(UserInfo uf) {
		this.uf = uf;
	}
	
	@Override
	public void run() {
		try {
			int RID = Integer.parseInt(uf.getReq());
			
			Server.db.delete_receipt_from_saved(Server.connect, RID, uf.getUID());
			str = "Deleted successfully";
		
		}catch(Exception e) {
			str = "There is no receipt with this id";

		}
	}
	public String getString() {
		return str;
	}

}
