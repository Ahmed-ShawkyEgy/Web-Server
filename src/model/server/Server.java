package model.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Server {

	private final int PORT_NUMBER = 30;
	private final String APP_NAME = "eBay";
	
	private HashMap<String,Connection> connectionList;
	private Queue<String> requestQueue;
	
	public static void main(String[] args) {
		Server server = new Server();
	}
	
	public Server() {
		requestQueue = new LinkedList<String>();
		connectionList = new HashMap<String,Connection>();
		Reciever reciever;
		Processor processor;
		try{
			reciever = new Reciever(PORT_NUMBER,this);
			processor = new Processor(APP_NAME, this);
		}catch(Exception e)
		{
			
			e.printStackTrace();
			return;
		}
		processor.start();
		reciever.start();
	}
	
	public boolean addConnection(Connection c)
	{
		if(connectionList.containsKey(c.getUserName()))
			return false;
		connectionList.put(c.getUserName(),c);
		return true;
	}
	
	public void closeConnection(Connection c)
	{
		if(connectionList.containsKey(c.getUserName()))
			connectionList.remove(c.getUserName());
	}
	
	public Connection getConnection(String userName)
	{
		if(!connectionList.containsKey(userName))
			return null;
		return connectionList.get(userName);
	}
	

	public Queue<String> getRequestQueue() {
		return requestQueue;
	}

	public void addRequest(String name,String request)
	{
		if(connectionList.containsKey(name))
		{
			System.out.println("_______________");
			System.out.print("Server: recieved request from "+name+"\n"+request);
			System.out.println("_______________");
			requestQueue.add(name+"\n"+request);
			System.out.println("Queue Size = "+requestQueue.size());
		}
	}
}
