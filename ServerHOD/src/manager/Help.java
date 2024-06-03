package manager;

public class Help extends Command{
	
	private String str = "";
	
	@Override 
	public void run() {
		str = "save_receipt delete_receipt sort_receipts_by_time"
				+ " forbid_ingredient permit_ingredient "
				+ "show_saved_receipts help exit show_receipts "
				+ " show_ingredients show_receipt";
	}
	
	public String getString() {
		return str;
	}

}
