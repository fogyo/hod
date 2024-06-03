package contract;

import java.io.Serializable;

public class Response implements Serializable{

	private String reply;
	private boolean flag;
	
	public Response(String reply, boolean flag) {
		this.reply = reply;
		this.flag = flag;
	}
	
	public String getRep() {
		return reply;
	}
	public boolean getFlag() {
		return flag;
	}
}
