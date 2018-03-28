package model.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Reciever extends Thread {

	private ServerSocket welcomeSocket;
	private Server server;
	
	public Reciever(int PORT_NUMBER,Server server) throws IOException
	{
		welcomeSocket = new ServerSocket(PORT_NUMBER);
		this.server = server;
	}
	
	
	public void run() {
	
		while(true)
		{
			try{
				Socket connectionSocket = welcomeSocket.accept();
				System.out.println("Accepted Client");
				Connection c = new Connection(connectionSocket,server);
				c.start();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	
	}
}
