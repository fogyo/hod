package contract;

import java.io.Serializable;

public class Request implements Serializable{
	
	
	private String command;
	private int argNum;
	private String args;
	private int UID;
	
	public Request(String command, int argNum, String args, int UID) {
		this.command = command;
		this.args = args;
		this.argNum = argNum;
		this.UID = UID;
	}
	
	public String getCom() {
		return command;
	}
	public int getArgNum(){
		return argNum;
	}
	public String getArgs() {
		return args;
	}
	
	public int getUID() {
		return UID;
	}
	public void setCom(String command) {
		this.command = command;
	}
	public void setArgNum(int argNum) {
		this.argNum = argNum;
	}
	public void setArgs(String args) {
		this.args = args;
	}
	
	public void setUID(int UID) {
		this.UID = UID;
	}
}