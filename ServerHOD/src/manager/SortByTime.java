package manager;

import database.UserInfo;
import server.Server;

public class SortByTime extends Command{
	
	private String str;
	
	@Override 
	public void run() {
		str = Server.db.sort_by_time(Server.connect);
		
	}
	
	public String getString() {
		return str;
	}

}
