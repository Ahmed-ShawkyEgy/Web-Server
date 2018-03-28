package model.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import java.util.Scanner;

public class Processor extends Thread{
	
	private Queue<String> requestQueue;
	private Server server;
	private String APP_NAME;
	
	public Processor(String APP_NAME,Server server) {
			this.APP_NAME = APP_NAME;
			this.server = server;
			this.requestQueue = server.getRequestQueue();
	}
	
	public void run() 
	{
		while(true)
		{
			if(requestQueue.isEmpty())
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			try {
				
				HashMap<String, String> request = parseRequest(server.getRequestQueue().poll());
				Connection currentConnection = server.getConnection(request.get("Name"));
				if(currentConnection==null)
					continue;
				
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			
		}
	}

	
	/*
	 *  Parses a request 
	 *  Returns null when parsing fails
	 * */
	public HashMap<String, String> parseRequest(String request) throws IOException
	{
		HashMap<String, String> map = new HashMap<String,String>();
		try{
		Scanner sc = new Scanner(request);
		
		map.put("Name", sc.nextLine());
		
		map.put("Method", sc.next());
		map.put("URL", sc.next());
		map.put("Version", sc.next());
		
		sc.nextLine();

		map.put("Host", sc.nextLine());

		map.put("Format", sc.nextLine());

		map.put("Connection", sc.nextLine());
		
		sc.close();
		}catch(Exception e)
		{
			return null;
		}
		return map;
	}
	
	public boolean validate(HashMap<String, String> request)
	{
		try{
			if(!request.get("Method").toLowerCase().equals("get"))
				return false;
			
//			TODO validate URL
			
			if(!request.get("Version").equals("1.1"))
				return false;
			
			if(!request.get("Host").equals(APP_NAME))
				return false;
			
//			TODO validate Format
			
			if(!request.get("Connection").equals("keep-alive") && !request.get("Connection").equals("close"))
				return false;
				
		}catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	
}
