package manager;

import java.util.HashMap;

import database.UserInfo;
import server.Server;

public class ShowAllReceipts extends Command{
	
	private String str = "";
	
	@Override
	public void run() {
		HashMap<Integer, String> receipts = new HashMap<>();
		receipts = Server.db.show_all_receipts(Server.connect);
		for (int key : receipts.keySet()) {
			str = str + String.valueOf(key) + " " + receipts.get(key) + "\n";
		}
	}
	public String getString() {
		return str;
	}

}
