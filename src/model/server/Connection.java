package model.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Connection extends Thread{
	
	private Socket socket;
	private BufferedReader inFromClient ; 
	private DataOutputStream  outToClient;
	private String name;
	private Server server;
	
	public Connection(Socket connectionSocket,Server server)
	{
		socket = connectionSocket;
		this.server = server;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToClient = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	
	public void run() 
	{
		// Connect to the server with a valid user name
		while(true)
		{
			try {
				if(!inFromClient.ready())
				{
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
				name = inFromClient.readLine();
				if(server.addConnection(this))
					break;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			try{
				outToClient.writeBytes("false\n");
			}catch(Exception e)
			{
				e.printStackTrace();
				return;
			}
		}
		
		try{
			outToClient.writeBytes("true\n");
		}catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
							System.out.println("Connection: Accepted name = "+name);
		while(true)
		{
			String request = "";
			try{
				for(int i = 0; i < 4;i++)				
					request += inFromClient.readLine()+"\n";
				server.addRequest(name, request);
			
			}catch(Exception e)
			{
				e.printStackTrace();
				return;
			}
			
		}
	}
	
	
	

	public String getUserName() {
		return name;
	}
	
	

}
