package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.util.Iterator;

import contract.MyBuffer;
import contract.Request;
import contract.UserResponse;
import contract.UserTransfer;
import database.DBFunctions;
import database.EntryRoom;
import database.UserInfo;
import threads.CommandProxy;
import threads.CommandTask;


public class Server {
	
	private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 5452;
    public static boolean script = false;
    private static boolean first_entry = true;
    public static DBFunctions db = new DBFunctions();
    public static Connection connect;
    
    public static void main(String[] args) throws IOException {
    	
    	Logger logs = new Logger("log.log");
    	
    	CommandProxy cp = new CommandProxy();
    	
    	DBFunctions db = new DBFunctions();
    	connect = db.connection_to_db("HOD", "postgres", "1234");
    	logs.log("Database connected HOD");
    	
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
        serverSocketChannel.configureBlocking(false);
        logs.log("Server started. PORT: " + SERVER_PORT);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        int usersNum = 0;
        SocketChannel socketChannel = null;;
        
		while (true) {
            try {
                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {

                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isAcceptable()) {
                    	socketChannel = ((ServerSocketChannel) key.channel()).accept();
                        usersNum = usersNum + 1;
                        logs.log("Connected users: " + String.valueOf(usersNum));
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                    else if (key.isReadable()) {            	
                    	socketChannel = (SocketChannel) key.channel();

                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        MyBuffer myBuffer = new MyBuffer();
                        while ((socketChannel.read(buffer) > 0)) {
                            buffer.flip();
                            myBuffer.addBytes(buffer.array());
                            buffer.clear();
                        }
                        
                      //Работа с зареганным пользователем
                        
                        try {
                        	ByteArrayInputStream bis = new ByteArrayInputStream(myBuffer.toByteBuffer().array());
                            ObjectInputStream ois = new ObjectInputStream(bis);
                        	Request request = (Request) ois.readObject();
                            UserInfo uf = new UserInfo(connect, request.getUID(), request.getArgs());
                            CommandTask ct = new CommandTask(socketChannel, uf, usersNum, logs, request);
                            cp.CommandToPool(ct);
                        }
                        
                      //Регистрация пользователя                      
                        
                        catch (Exception e) {
                        	ByteArrayInputStream bis = new ByteArrayInputStream(myBuffer.toByteBuffer().array());
                            ObjectInputStream ois = new ObjectInputStream(bis);
                        	UserTransfer ut;
							try {
								ut = (UserTransfer) ois.readObject();
								 String username = ut.getUsername();
	                            String password = ut.getPassword();
	                            boolean signing = ut.getSigning();
	                            EntryRoom er = new EntryRoom();
	                            UserResponse usresp = er.registry(username, password, signing, connect);
	                            
	                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	                            ObjectOutputStream oos = new ObjectOutputStream(bos);
	                            oos.writeObject(usresp);
	                            ByteBuffer response_buffer = ByteBuffer.wrap(bos.toByteArray());
	                            socketChannel.write(response_buffer);
							} catch (ClassNotFoundException e1) {
							}                           
                        }   
                    }
                }
                } catch (IOException e) {
            	try {
					socketChannel.close();
		        	logs.log("User disconnected: " + e.toString());
		        	usersNum = usersNum - 1;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					logs.log(e1.toString());
				}
            }

        }

    }

}
