import java.net.*; 
import java.io.*;

public class Client extends Node {
	
	Socket client_socket;   
	DataOutputStream client_dout; 
	
	int port_no;
	
	public Client(int port_no)
	{
		this.port_no=port_no;
	}
	public void connect(int port_no)
	{
		
	    Socket socket            = null; 
	    try
        { 
        	client_socket = new Socket("localhost", port_no); 
			client_dout    = new DataOutputStream(client_socket.getOutputStream()); 
            
        } 
       
        catch(Exception i) 
        { 
            System.out.println(i); 
        } 
	   
	}
	
	
	
	
	public void send_request(int req_no,int node_no)
	{ 
	
		try
		{

  			client_dout.writeUTF("REQUEST:"+Integer.toString(node_no)+":"+Integer.toString(req_no));
		    
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		
	}
	
	public void send_reply(int node_id,String msg)
	{
		
		try
		{

		System.out.println("Sending reply to Node-"+node_id); 
		client_dout.writeUTF("REPLY:"+Integer.toString(node_id)+":OK");
	    
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
	}

}
