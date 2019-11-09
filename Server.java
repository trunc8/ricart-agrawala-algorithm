import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*; 

public class Server extends Node implements Runnable
{
	
	 Socket          socket   = null; 
	 ServerSocket    server   = null; 
	 DataInputStream in       =  null; 
	 int port_no;
	 int corr_cli_index;

	
	
	public Server()
	{
		
	}
	public Server(int port_no,int cli_index)
	{
		
		
		this.port_no=port_no;
		this.corr_cli_index=cli_index;

		
	}


	public String read_msg()
	{
		String res="";
		try
		{
		 if(in.available()>0)
		 {

		 String line=(String)in.readUTF();
		 System.out.println("Msg received:"+line);
		
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
		 else
		 {
			 Thread.sleep(5000);
		 }
		}
		catch(Exception e)
		{
			//System.out.println("Exception:"+e);
		}
		
		return res;
	}
	
	

	synchronized public void process_request(String node_no_str, String req_no_str)
	{
		int node_no=Integer.parseInt(node_no_str);
		int req_no=Integer.parseInt(req_no_str);

		if(Node.req_cs_entry)
		{
			if(req_no<Node.curr_req_no)
			{
				//send reply
				Node.cliObj.get(corr_cli_index).send_reply(node_no, "OK");
			    if(Node.max_req_no<req_no)
					Node.max_req_no=req_no;
			}
			else if (req_no>Node.curr_req_no)
			{
				//defer reply
				Node.def_list.set(node_no, req_no);
				if(Node.max_req_no<req_no)
					Node.max_req_no=req_no;
			}
			else
			{
				if(node_no<Node.node_id)
				{
					//send reply
					Node.cliObj.get(corr_cli_index).send_reply(node_no, "OK");
					if(Node.max_req_no<req_no)
						Node.max_req_no=req_no;
				}
				else
				{
					//defer reply
					Node.def_list.set(node_no,req_no);
					System.out.println("Request from Node-"+node_no+" is deferred. Request Vale:"+ req_no);
					if(Node.max_req_no<req_no)
						Node.max_req_no=req_no;
				}
			}
			
		}
		else
		{
			//send reply
			Node.cliObj.get(corr_cli_index).send_reply(node_no, "OK");
			if(Node.max_req_no<req_no)
				Node.max_req_no=req_no;
		}
		
	}

	synchronized public  void  process_reply(String msg)
	{
		try
		{
		no_of_pending_req=no_of_pending_req-1;
		
		if(no_of_pending_req==0)
		{
			enter_cs=true;
		
			Thread.sleep(10);
		}

		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
	}
	
	public void run()
	{
	

		
		try
		{
			System.out.println("Server Listening @"+port_no);
			server = new ServerSocket(port_no);
			Thread.sleep(10);
			socket = server.accept();
			in = new DataInputStream(socket.getInputStream()); 
			System.out.println("Connection Accepted @"+ port_no);
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		
		
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
