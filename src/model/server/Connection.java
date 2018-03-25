package model.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

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
		}
		this.start();
	}
	
	
	public void run() 
	{
		// Connect to the server with a valid user name
		while(true)
		{
			try {
				name = inFromClient.readLine();
				if(server.addConnection(this))
					break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void readRequest()
	{
		HashMap<String, Object> map = new HashMap<String,Object>();
		
	}


	public String getUserName() {
		return name;
	}
	
	

}
