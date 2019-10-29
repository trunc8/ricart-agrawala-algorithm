import java.net.*; 
import java.io.*; 

public class Server extends Node implements Runnable
{
	
	 Socket          socket   = null; 
	 ServerSocket    server   = null; 
	 DataInputStream in       =  null; 
	
	
	
	public Server()
	{
		
	}
	public Server(int port_no)
	{
		super();
		try
		{
			server = new ServerSocket(port_no);
			socket = server.accept();
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		
		
	}

	public String read_msg()
	{
		String res="";
		try
		{
		 in = new DataInputStream(new BufferedInputStream(socket.getInputStream())); 
		 String line=in.readUTF();
		 String res_arr[]=line.split(":",3); // req has three parts: request:node_no:req_no or reply:node_no:ok
		 if(res_arr[0].equals("REQUEST"))
		 {
			 process_request(res_arr[1],res_arr[2]);
		 }
		 else
		 {
			 process_reply(res_arr[1]);
		 }
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		
		return res;
	}
	
	
	
	public void process_request(String node_no_str, String req_no_str)
	{
		int node_no=Integer.parseInt(node_no_str);
		int req_no=Integer.parseInt(req_no_str);
		if(super.req_cs_entry)
		{
			if(req_no<super.curr_req_no)
			{
				//send reply
				super.cliObj.send_reply(node_no, "OK");
			}
			else if (req_no>super.curr_req_no)
			{
				//defer reply
				super.def_list.set(node_no, req_no);
			}
			else
			{
				if(node_no<super.node_id)
				{
					//send reply
					super.cliObj.send_reply(node_no, "OK");
				}
				else
				{
					//defer reply
					super.def_list.set(node_no,req_no);
				}
			}
			
		}
		else
		{
			//send reply
			super.cliObj.send_reply(node_no, "OK");
		}
	}
	
	public void process_reply(String msg)
	{
		//acquire semaphore
		super.no_of_pending_req=super.no_of_pending_req-1;
		if(super.no_of_pending_req==0)
			super.enter_cs=true;
		//release semaphore	
	}
	
	public void run()
	{
	
		while(true)
		{
			try 
			{
				read_msg();
				Thread.sleep(10);
			}
			catch(Exception e)
			{
				System.out.println("Exception:"+e);
				
			}
		}
	}
}
