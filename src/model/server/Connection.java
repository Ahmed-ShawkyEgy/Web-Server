package model.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

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
							
		// Reading requests
		while(true)
		{
			try 
			{
				if(!inFromClient.ready())
				{
					Thread.sleep(100);
					continue;
				}
			} catch (Exception e1) {e1.printStackTrace();}
			
			System.out.println("Connection : Begin reading input");
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
	
	public void sendResponse(HashMap<String, String>response,File file)
	{
		try{
			outToClient.writeBytes(response.get("Status")+" "+"1.1\n");
			outToClient.writeBytes(response.get("TimeStamp")+"\n");
			outToClient.writeBytes(response.get("Format")+"\n");
			outToClient.writeBytes(response.get("Connection")+"\n");
			if(file==null)
				outToClient.writeBytes("null\n");
			
			System.out.println("________");
			System.out.println("Connection: sent the following");
			for(Entry<String, String> e : response.entrySet())
			{
				System.out.println(e.getKey()+": "+e.getValue());
			}
			System.out.println("File: "+(file==null?"null":file.getName()));
			if(file!=null)
			{
				outToClient.writeBytes(file.getName()+"\n");
				outToClient.writeBytes(file.length()+"\n");
				byte[] bytes = new byte[16 * 1024];
				InputStream in = new FileInputStream(file);
				
				int count;
		        while ((count = in.read(bytes)) > 0) {
		        	outToClient.write(bytes, 0, count);
		        }
//		        outToClient.writeBytes("\n");
		        System.out.println("Connection: Sent file to Client");
		        in.close();
			}
			
			if(response.get("Connection").equals("close"))
			{
				terminate();
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	public void terminate() throws IOException
	{
		System.out.println("_____");
		System.out.println("Connection: Terminating");
		inFromClient.close();
		outToClient.close();
		socket.close();
	}
	
	
}
