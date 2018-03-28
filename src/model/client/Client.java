package model.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	private DataOutputStream outToServer;
	private BufferedReader inFromServer; 
	private Socket clientSocket;
	private String name;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client c = new Client();
//		c.setName("ahmed");
		Scanner sc = new Scanner(System.in);
		while(true)
		{
			if(c.setName(sc.nextLine()))
				break;
		}
		
		while(true)
		{
			c.sendRequest(sc.nextLine(), sc.nextLine(), sc.nextLine());
		}
	}
	
	public Client() throws UnknownHostException, IOException {
		
		clientSocket = new Socket("localhost",30);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
		name = null;
	}
	
	
	
	public void sendRequest(String url,String format,String connection)
	{
		if(name==null)
			return;
		String request = "GET "+url+" 1.1\neBay\n"+format+"\n"+connection+"\n";
		try{
			outToServer.writeBytes(request);
		}catch(Exception e)
		{
			
		}
	}
	
	// TODO Implement
	public void recieveRespond(){}



	public String getName() {return name;}

	public boolean setName(String name) throws IOException {
		if(this.name!=null)return false;
		
		outToServer.writeBytes(name+"\n");
		String response = inFromServer.readLine();
		
		// If the server accepted the user name
		if(response.equals("true"))
		{
			this.name = name;
			return true;
		}
		return false;
	}
	
	
}
