package model.server;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Server {

	private final int portNumber = 30;
	// TODO identify uniquely by name
	private HashSet<String> usernameList;
	private Queue<Connection> connectionQueue;
	
	public Server() {
		connectionQueue = new LinkedList<Connection>();
		usernameList = new HashSet<String>();
		try{
			Reciever reciever = new Reciever(portNumber,this);
			reciever.start();
		}catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	public boolean addConnection(Connection c)
	{
		if(usernameList.contains(c.getUserName()))
			return false;
		usernameList.add(c.getUserName());
		connectionQueue.add(c);
		return true;
	}
	
	
}
