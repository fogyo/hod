package contract;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Scanner;

public class Validator {
	
	public int commandValidator(String request) {
		if (isArgsList.containsKey(request.split(" ")[0])==true) {
			return isArgsList.get(request.split(" ")[0]);
		}
		else {
			return -1;
		}
	}

	private final HashMap<String, Integer> isArgsList = new HashMap<>() {{
		put("save_receipt", 1);
		put("delete_receipt", 1);
		put("sort_receipts_by_time", 0);
		put("forbid_ingredient", 1);
		put("permit_ingredient", 1);
		put("show_saved_receipts", 1);
		put("help", 0);
		put("exit", 0);
		put("show_receipts", 0);
		put("show_ingredients", 0);
		put("show_receipt", 1);
		put("show_permitted_receipts", 1);
		put("show_permitted_ingredients", 1);
	}};
}


