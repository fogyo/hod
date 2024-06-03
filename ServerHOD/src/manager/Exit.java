package manager;

public class Exit extends Command{
	
	private String str;
	
	@Override
	public void run() {
		str = "Finishing";
	}
	
	public String getString() {
		return str;
	}

}
