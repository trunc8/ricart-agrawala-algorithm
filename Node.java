import java.util.*;

//semaphores to be defined and added wherever needed

public class Node {
	int node_id;
	boolean enter_cs;
	int max_nodes;
	int max_req_no;
	int curr_req_no;
	int no_of_pending_req;
	boolean req_cs_entry;
	List<String> ip_addr = new ArrayList<String> ();
	List<Integer> port_no = new ArrayList<Integer> ();
	List<Integer> def_list = new ArrayList<Integer> (); 
	
	Client cliObj=new Client();
	Server serObj=new Server();
	
	public Node(int id) {
		this.node_id=id;
		this.enter_cs=false;
		this.max_req_no=0;
		this.curr_req_no=0;
		this.no_of_pending_req=0;
		this.req_cs_entry=false;
		
	}
	public Node()
	{
		//System.out.println("");
	}
	
	public void read_config_file()
	{
		// reads the config.txt located in the current directory and loads the ip address and port numbers of the neighbouring nodes
		// file shall contain max no of nodes in first line and the following for each of the nodes from the second line 
			//NodeId, IPaddr,Port no
		// assign max_nodes
		// read and add into lists ip_addr and port_no
		//initialise def_list with default value of -1

		
	}
	
	public void cs_pre_requisite()
	{
		this.curr_req_no=this.max_req_no+1;
		this.req_cs_entry=true;
		for(int i=0;i<this.max_nodes;i++)
		{
			if(i==this.node_id)
				continue;
			else
			{
				//send msg to n-1 nodes
				this.cliObj.send_request(curr_req_no, i);
			}
		}
		
		
		
	}
	
	public void process_deferrec_requests() {
		for(int i=0;i<def_list.size();i++)
		{
			if(def_list.get(i)!=-1)
			{
				//deferred req
				//invoke process request method 
				String node_no_str=Integer.toString(i);
				String req_no_str=Integer.toString(def_list.get(i));
				serObj.process_request(node_no_str, req_no_str);
			}
		}
	}
	
	
	public void Node_main()
	{
		// read config file
		read_config_file();
		
		
		for(int i=0;i<this.max_nodes;i++)
		{
			if(i==this.node_id)
				continue;
			else
			{
				// create n-1 server threads
				Server ser_obj=new Server(port_no.get(i));
				ser_obj.run();
			}	
				
		}
		
		while(true)
		{
			//pre requisite for entering critical section
		    try {
		    	
		    
			while(enter_cs!=true);
			//enter critical section
			System.out.println("Node-"+node_id+" entered Critical Section");
			Thread.sleep(50);
			
			//process deferred requests
				
		    }
		    catch(Exception e)
		    {
		    	System.out.println("Exception:"+e);
		    }
		}
	}

}
