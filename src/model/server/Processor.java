package model.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
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
				
				HashMap<String, String> response = new HashMap<String , String>();
				response.put("Status","200 OK");
				response.put("TimeStamp", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
				response.put("Connection", request.get("Connection"));
				File f = null;
				if(!isValideRequest(request))
				{
					response.put("Status", "404 Not-Found");
					response.put("Format", "null");
				}
				else
				{
					response.put("Format",getExtension(request));
					f = new File("docroot/"+getExtension(request));
				}
				currentConnection.sendResponse(response, f);
				
				
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
	
	public boolean isValideRequest(HashMap<String, String> request)
	{
		try{
			if(!request.get("Method").toLowerCase().equals("get"))
				return false;


			String s = getExtension(request);
			if(s.equals("null"))
				return false;			
			
			if(!request.get("Version").equals("1.1"))
				return false;
			
			if(!request.get("Host").equals(APP_NAME))
				return false;
			
			if(!request.get("Connection").equals("keep-alive") && !request.get("Connection").equals("close"))
				return false;
				
		}catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	public String getExtension(HashMap<String, String> request)
	{
		String[] formats = request.get("Format").split(" ");
		
		String s = "null";
		for(String format : formats)
		{
			File f = new File("docroot/"+request.get("URL")+format);
			if(f.exists())
				return format;
		}
		return s;
		
	}
	
	
	
}
