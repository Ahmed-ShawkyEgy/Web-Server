package model.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Reciever extends Thread {

	private ServerSocket welcomeSocket;
	private Server server;
	
	public Reciever(int portNumber,Server server) throws IOException
	{
		welcomeSocket = new ServerSocket(portNumber);
		this.server = server;
	}
	
	
	public void run() {
	
		while(true)
		{
			try{
				Socket connectionSocket = welcomeSocket.accept();
				Connection c = new Connection(connectionSocket,server);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	
	}
}
