package manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import database.UserInfo;


public class CommandManager {
	
	private String strCommand;
	private int numargs;
	private String reply = "";
	private UserInfo uf;
	
	public CommandManager(String strCommand, int numargs, UserInfo uf) {
		this.strCommand = strCommand;
		this.numargs = numargs;
		this.uf = uf;
	}
	
	public void startCommand() {
		if (commList.containsKey(strCommand)==true) {
			reply="";
			String[] adress = commList.get(strCommand).toString().split(" ");
	
	//0 аргов			
			if (numargs == 0) { 
				try {
					Class clazz = Class.forName(adress[1]);
					
					Method run = null;
					Method getString = null;
					try {
						run = clazz.getDeclaredMethod("run");
						getString = clazz.getDeclaredMethod("getString");
					} catch (NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
					Object command = clazz.newInstance();
					run.invoke(command);
					reply = reply + getString.invoke(command) + " ";
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}	         
			}
			
	//1 аргумент, UserInfo (Conn, UID)		
			else if(numargs == 1) {
				try {
					Class clazz = Class.forName(adress[1]);
					
					Class[] oneArg = {UserInfo.class};
					Method run = null;
					Method getString = null;
					try {
						run = clazz.getDeclaredMethod("run");
						getString = clazz.getDeclaredMethod("getString");
						Object command = clazz.getConstructor(oneArg).newInstance(uf);
						run.invoke(command);
						reply = reply + getString.invoke(command) + " ";
					} catch (NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
					
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	private static final HashMap<String, Class> commList = new HashMap<>() {{
		put("help", Help.class);
		put("save_receipt", SaveReceipt.class);
		put("delete_receipt", DeleteReceipt.class);
		put("sort_receipts_by_time", SortByTime.class);
		put("forbid_ingredient", ForbidIngrs.class);
		put("permit_ingredient", PermitIngrs.class);
		put("show_saved_receipts", ShowSaved.class);
		put("exit", Exit.class);
		put("show_receipts", ShowAllReceipts.class);
		put("show_ingredients", ShowIngrs.class);
		put("show_receipt", ShowReceipt.class);
	}};
	
	public String getCMS() {
		return reply;
	}
}
