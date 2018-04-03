package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.client.Client;
import view.AppView;
import view.ConnectView;

public class MainController implements ActionListener{

	boolean isConnected = true;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		@SuppressWarnings("unused")
		MainController mc = new MainController();
	}
	
	JFrame curFrame;
	Client c;
	
	public MainController() 
	{
		try {
			c = new Client(this);
		} catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		curFrame = new ConnectView(this);
		
		
	}
	
	


	public void actionPerformed(ActionEvent e) 
	{
		String cmd = e.getActionCommand();
		StringTokenizer st = new StringTokenizer(cmd);
		if(st.countTokens()==0)
		{
			infoBox("Invalid Input!!", "Error");
			return;
		}
		switch (st.nextToken())
		{
			case "userName":
				if(st.countTokens() != 1)
				{
					infoBox("Invalid User name!", "Error");
					return;
				}
				String name = st.nextToken();
			try {
				boolean flag = c.setName(name);
				if(flag)
				{
					render();
				}
				else
				{
					infoBox("Invalid User name!", "Error");
					return;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
				
			break;
			
			case "request":
				if(!isConnected)
				{
					((AppView)curFrame).print("__________");
					((AppView)curFrame).print("Not connected to the server");
					return;
				}
				String rest = st.nextToken();
				while(st.hasMoreTokens())
					rest += " "+st.nextToken();
				String[] param = rest.split("\\|");
				if(param[2].equals("close"))
					isConnected = false;
				
				((AppView)curFrame).print("__________");
				((AppView)curFrame).print("Request:");
				
				String req = c.sendRequest(param[0], param[1], param[2]);
				Scanner scc = new Scanner(req);
				
				((AppView)curFrame).print(scc.nextLine());
				((AppView)curFrame).print(scc.nextLine());
				((AppView)curFrame).print(scc.nextLine());
				((AppView)curFrame).print(scc.nextLine());
			try {
				HashMap<String, String> respond = c.recieveResponse();
				((AppView)curFrame).print("__________");
				((AppView)curFrame).print("Response:");

				((AppView)curFrame).print(respond.get("Status")+" "+respond.get("Code")+
						" "+respond.get("Version"));
				((AppView)curFrame).print(respond.get("TimeStamp"));
				((AppView)curFrame).print(respond.get("Format"));
				((AppView)curFrame).print(respond.get("Connection"));
//				for(Entry<String, String> x : respond.entrySet())
//				{
//					((AppView)curFrame).print(x.getKey()+":"+x.getValue());
//				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
				
			break;
			
			case "quit":
				if(!isConnected)
				{
					terminate();
				}
				c.sendRequest("~", "", "close");
			try {
				c.recieveResponse();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
			
		}
		
	}
	
	public void render()
	{
		curFrame.dispose();
		curFrame = new AppView(this,c.getName());
		File dir = new File("docroot");
		for(File f : dir.listFiles())
		{
			((AppView)curFrame).addMember(f.getName().replaceFirst("[.][^.]+$", ""));
		}
	}

	
	
	public void terminate()
	{
		System.out.println("Controller: Terminating");
		System.exit(0);
	}
	
	 public void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}
