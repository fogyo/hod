package threads;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.util.concurrent.RecursiveTask;

import contract.MyBuffer;
import contract.Request;
import contract.Response;
import contract.UserResponse;
import contract.UserTransfer;
import database.EntryRoom;
import database.UserInfo;
import manager.CommandManager;
import server.Logger;

public class CommandTask extends RecursiveTask<String>{
	
	private UserInfo uf;
	private int usersNum;
	private Logger logs;
	private SocketChannel socketChannel;
	private Request request;
	
	public CommandTask(SocketChannel socketChannel, UserInfo uf, int usersNum, Logger logs, Request request) {
		this.uf = uf;
		this.usersNum = usersNum;
		this.logs = logs;
		this.socketChannel = socketChannel;
		this.request = request;
	}
	
	@Override
	protected String compute() {
	
        CommandManager cm = new CommandManager(request.getCom(), request.getArgNum(), uf);
        cm.startCommand();
        String reply = cm.getCMS();
        boolean flagClient = true;
        if (request.getCom().equals("exit")){
          flagClient = false;
          
        }
        Response response = new Response(reply, flagClient);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
        try {
        	ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(response);
	        ByteBuffer response_buffer = ByteBuffer.wrap(bos.toByteArray());
	        socketChannel.write(response_buffer);
		} catch (IOException e) {
		}
        
        if (!flagClient) {
          logs.log("User disconnected: exit command");
          usersNum = usersNum - 1;
            try {
                socketChannel.close();
            } catch (IOException ex) {
              logs.log(ex.toString());
              throw new RuntimeException(ex);
        	}
        }
	        
        return "Added to thread";
    }
}
