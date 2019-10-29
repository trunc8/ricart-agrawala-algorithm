
public class Client {
	public void connect(int node_no)
	{
		//fetch ip_addt and port no of the node
		//connect to server and return sock id
	}
	
	
	public int write_msg(int sock_id, String msg) 
	{
		int res=-1;
		//socket write 
		return res;
		
	}
	
	public boolean send_request(int req_no,int node_no)
	{ 
		boolean res=false;
	    //invoke connect 
		//invoke writemsg
		//check return value and return true or false 
	    
		return res;
	}
	
	public boolean send_reply(int node_id,String msg)
	{
		boolean res=false;
		// invoke connect method for node passed as argument
		// invoke write_msg 
		//check return value and return true or false 
		return res;
	}

}
