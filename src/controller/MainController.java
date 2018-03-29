package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import view.AppView;
import view.ConnectView;
import model.client.Client;

public class MainController implements ActionListener{

	public static void main(String[] args) throws UnknownHostException, IOException {
		
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

			break;
			
			case "quit":
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
