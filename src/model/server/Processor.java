package model.server;

import java.util.Queue;

public class Processor extends Thread{
	
	private Queue<Connection> connection_queue;

	
	public Processor(Queue<Connection> connection_queue) {
			this.connection_queue = connection_queue;
	}
	
	public void run() 
	{
		while(true)
		{
			if(connection_queue.isEmpty())
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			
			
		}
	}
	
	
}
