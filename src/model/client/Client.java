package model.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Client {

	private DataOutputStream outToServer;
	private BufferedReader inFromServer; 
	private Socket clientSocket;
	private String name;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client c = new Client();
		Scanner sc = new Scanner(System.in);
//		while(true)
//		{
//			if(c.setName(sc.nextLine()))
//				break;
//		}
		c.setName("Ahmed");
		
		c.sendRequest("a", "txt jpeg", "keep-alive");
		c.recieveResponse();
		
		c.sendRequest("balabizooooooooooooooo", "txt jpeg", "keep-alive");
		c.recieveResponse();
		
		c.sendRequest("b", "jpeg txt", "keep-alive");
		c.recieveResponse();
//		
//		c.sendRequest("aaaaaaaaaaaaaaaaaaaa", "txt jpeg", "keep-alive");
//		c.recieveResponse();
		
		
		while(true)
		{
			c.sendRequest(sc.nextLine(), sc.nextLine(), sc.nextLine());
			c.recieveResponse();
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
		System.out.println("__________");
		System.out.println("Client: Sent request:");
		System.out.println(request);
		try{
			outToServer.writeBytes(request);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public void recieveResponse() throws IOException{
		String r = "";
		for(int i = 0; i < 5;i++)
		{
			r += inFromServer.readLine()+"\n";
		}
		
		System.out.println("Client: "+name+"recieved:");
		System.out.println(r);
		
		HashMap<String, String> response = parseResponse(r);
		System.out.println("____________\nFrom Client");
		for(Entry<String, String> e : response.entrySet())
		{
			System.out.println(e.getKey()+": "+e.getValue());
		}
		if(!response.get("File").equals("null"))
			recieveFile(response.get("File"));
	}

	public void recieveFile(String fileName) throws IOException
	{
		int len = Integer.parseInt(inFromServer.readLine());
		
		File f = new File("clients/"+name+"/"+fileName);
		if(!f.exists())
			f.createNewFile();
		FileOutputStream out = new FileOutputStream(f);
//		byte[] bytes = new byte[16*1024];
		
//		int count;
		System.err.println("Start Recieving file");
//        while ((count = in.read(bytes)) > 0) {
//            out.write(bytes, 0, count);
//        }
		for(int i = 0; i < len;i++)
		{
			
			out.write(inFromServer.read());
		}
        
        out.close();
        System.err.println("End recieving file");
	}
	
	public HashMap<String,String> parseResponse(String r)
	{
		HashMap<String, String> response = new HashMap<String, String>();
		Scanner sc = new Scanner(r);
		
		response.put("Code", sc.next());
		response.put("Status", sc.next());
		response.put("Version", sc.next());
		
		sc.nextLine();
		
		response.put("TimeStamp", sc.nextLine());
		response.put("Format", sc.nextLine());
		response.put("Connection", sc.nextLine());
		
		response.put("File", sc.nextLine());

		sc.close();

		return response;
	}


	public String getName() {return name;}

	public boolean setName(String name) throws IOException {
		if(this.name!=null)return false;
		
		outToServer.writeBytes(name+"\n");
		String response = inFromServer.readLine();
		
		// If the server accepted the user name
		if(response.equals("true"))
		{
			this.name = name;
			createDir();
			return true;
		}
		return false;
	}
	
	public void createDir()
	{
		File f = new File("clients/"+name);
		if(!f.exists())
		{
			f.mkdirs();
		}
		
	}
	
	
}
