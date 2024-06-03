package manager;

import java.util.HashMap;

import database.UserInfo;
import server.Server;

public class ShowIngrs extends Command{
	
	private String str;
	
	@Override
	public void run() {
		HashMap<Integer, String> ingredients = new HashMap<>();
		ingredients = Server.db.show_all_ingredients(Server.connect);
		for (int key : ingredients.keySet()) {
			str = str + String.valueOf(key) + " " + ingredients.get(key) + "\n";
		}
	}
	public String getString() {
		return str;
	}

}
