package client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import contract.MyBuffer;
import contract.Request;
import contract.Response;
import contract.UserResponse;
import contract.UserTransfer;
import contract.Validator;



public class Client {
	
private static boolean availability; 
	
	private static SocketChannel connectToServer() {
		SocketChannel channel;
		try {
			channel = SocketChannel.open();
			channel.connect(new InetSocketAddress("127.0.0.1", 5452));
			availability = true;
	    	return channel;
		} catch (IOException e) {
			availability = false;
			System.out.println("Server is unavailable");
		}
		return null;
	}
	
    
	public static void main(String args[])throws IOException, InterruptedException {
		boolean connectionTries = true;
		Scanner sc = new Scanner(System.in);
		int UID = 0;
		while (connectionTries) {
			SocketChannel channel;
			System.out.println("Press any key, to try to connect to server");
			String anyKey = sc.nextLine();
			try {
				channel = connectToServer();
				Validator vd = new Validator();
				
				if (availability) {
					
					System.out.println("Connected successfully");
					
					SignObject so = new SignObject();
					boolean success_registry = false;
					
					//регистрация
					while (!success_registry) {
						boolean sign = so.sign();
						UserTransfer ut = so.enterInfo(sign);
						
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
				        ObjectOutputStream oos = new ObjectOutputStream(bos);
				        oos.writeObject(ut);
				        ByteBuffer response_buffer = ByteBuffer.wrap(bos.toByteArray());
				        channel.write(response_buffer);
				        
				        ByteBuffer buffer = ByteBuffer.allocate(1024);
						MyBuffer myBuffer = new MyBuffer();
						int bytesRead;
						while ((bytesRead = channel.read(buffer)) > 0) {
						    buffer.flip();
						    myBuffer.addBytes(buffer.array());
						    buffer.clear();
						    if (bytesRead<1024) {
						    	break;
						    }
						}
						
				        ByteArrayInputStream bis = new ByteArrayInputStream(myBuffer.toByteBuffer().array());
				        ObjectInputStream ois = new ObjectInputStream(bis);
				        UserResponse usr = (UserResponse) ois.readObject();
				        success_registry = usr.getSuccess();
				        UID = usr.getUId();
					}
					
					System.out.println("Enter help for commands");
					//работа с пользователем
					while (sc.hasNext()) {
						
						//отправка данных
						
						String strRequest = sc.nextLine();
						int num_args = vd.commandValidator(strRequest);						
						if (num_args == 1){
							Request request;
							try {								
								request = new Request(strRequest.split(" ")[0], 1, strRequest.split(" ")[1], UID);
							}catch (Exception e) {
								request = new Request(strRequest.split(" ")[0], 1, null, UID);
							}
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
					        ObjectOutputStream oos = new ObjectOutputStream(bos);
					        oos.writeObject(request);
					        ByteBuffer response_buffer = ByteBuffer.wrap(bos.toByteArray());
					        channel.write(response_buffer);
						}
						else if(num_args == 0) {
							Request request = new Request(strRequest.split(" ")[0], 0, null, UID);
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
					        ObjectOutputStream oos = new ObjectOutputStream(bos);
					        oos.writeObject(request);
					        ByteBuffer response_buffer = ByteBuffer.wrap(bos.toByteArray());
					        channel.write(response_buffer);
						}
						else {
							System.out.println("Wrong command");
							continue;
						}
			
						//получение данных
						
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						MyBuffer myBuffer = new MyBuffer();
						int bytesRead;
						while ((bytesRead = channel.read(buffer)) > 0) {
						    buffer.flip();
						    myBuffer.addBytes(buffer.array());
						    buffer.clear();
						    if (bytesRead<1024) {
						    	break;
						    }
						}
				        ByteArrayInputStream bis = new ByteArrayInputStream(myBuffer.toByteBuffer().array());
				        ObjectInputStream ois = new ObjectInputStream(bis);
						try {
							Response response = (Response) ois.readObject();
							if(response.getFlag() == false) {
								System.out.println(response.getRep());
								channel.close();
								connectionTries = false;
								break;
							}
							else {
								System.out.println(response.getRep());
							}
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	        
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
