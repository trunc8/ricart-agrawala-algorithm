import java.util.*;
import java.io.*;
import java.sql.Timestamp;




public class Node {
	static int node_id;
	static boolean enter_cs;
	static int max_nodes;
	static int max_req_no;
	static int curr_req_no;
	static public int no_of_pending_req;
	static boolean req_cs_entry;
	int no_of_times;
	List<String> ip_addr = new ArrayList<String> ();
	List<Integer> port_no = new ArrayList<Integer> ();
	
	static List<Integer> def_list = new ArrayList<Integer> (); 
	static List<Client> cliObj=new ArrayList<Client>();
	static List<Server> serObj=new ArrayList<Server>();
	static List<Thread> serThreads=new ArrayList<Thread>();
	
	Timestamp ts;
	
	public Node(int id, int ctr) {
		Node.node_id=id;
		Node.enter_cs=false;
		Node.max_req_no=0;
		Node.curr_req_no=0;
		Node.no_of_pending_req=0;
		Node.req_cs_entry=false;
		this.no_of_times=ctr;

		
	}
	public Node()
	{

	}
	
	
	public void read_config_file()
	{
				
		try
		{
		File file = new File("config.txt");
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String str; 
		String[] words;
		int node_no;
		boolean firstentry=true;
		
		while ((str = br.readLine()) != null) 
		{
		  
		    if(firstentry)
		    {
		    	max_nodes=Integer.parseInt(str);
//		    	System.out.println(str);
		    	firstentry=false;
		    	
		    }
		    else
		    {
//		    	  System.out.println(str);
		    	words=str.split(" ");
		    	node_no=Integer.parseInt(words[0]);
		    	ip_addr.add(node_no,words[1]);
		    	port_no.add(node_no,Integer.parseInt(words[2]));
		    	def_list.add(node_no,-1);
		    }
		    
		}
		br.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e);
		}
		
	}
	
	public void cs_pre_requisite()
	{
		Node.curr_req_no=Node.max_req_no+1;
		Node.req_cs_entry=true;
		Node.no_of_pending_req=Node.max_nodes-1;

		for(int i=0;i<Node.max_nodes;i++)
		{
			if(i==Node.node_id)
				continue;
			    Node.cliObj.get(i).send_request(curr_req_no, Node.node_id);
			
		}
		
		
		
	}
	
	public void process_deferred_requests() {
		for(int i=0;i<def_list.size();i++)
		{
			if(def_list.get(i)!=-1)
			{
				
				String node_no_str=Integer.toString(i);
				String req_no_str=Integer.toString(def_list.get(i));
				Node.serObj.get(i).process_request(node_no_str, req_no_str);
				def_list.set(i,-1);
			}
		}
	}
	
	

	public void Node_main()
	{
		
		try
		{
		
//		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		read_config_file();
		
			
		for(int i=0;i<Node.max_nodes;i++)
		{
			
			if(i==Node.node_id)
			{

				Node.serObj.add(i,new Server(0,i));
				Thread t1 = new Thread(Node.serObj.get(i));
				Node.serThreads.add(i,t1);

			}
			else
			{

				int temp_port_no=port_no.get(i);
				Node.serObj.add(i,new Server(temp_port_no+Node.node_id,i));
				Thread t1 = new Thread(Node.serObj.get(i));
				Node.serThreads.add(i,t1);
				t1.start();
			}	
				
		}
		
		Thread.sleep(10000);
		
		
		for(int i=0;i<Node.max_nodes;i++)
		{
			
			if(i==Node.node_id)
			{
				int temp_port_no=port_no.get(i);
				for(int j=0;j<Node.max_nodes;j++)
				{
					if(j==Node.node_id)
					{
						cliObj.add(j,new Client(temp_port_no+j));
					}
					else
					{
						cliObj.add(j,new Client(temp_port_no+j));
						cliObj.get(j).connect(temp_port_no+j);
						
					}
					
				}
							
			}
			
		}

		for(int i=0;i<no_of_times;i++)
		{
				Thread.sleep(10000);

		    	cs_pre_requisite();
		    
		    	while(Node.enter_cs==false)
		    	{
		    		Thread.sleep(10);
		    	}
				//enter critical section
		    	ts = new Timestamp(System.currentTimeMillis()); 
				System.out.println("Node-"+node_id+" entered Critical Section @ "+ts);
				Thread.sleep(2000);
				
				//reset variables
				Node.enter_cs=false;
				Node.req_cs_entry=false;
				ts = new Timestamp(System.currentTimeMillis()); 
				System.out.println("Node-"+node_id+" exited Critical Section @ "+ts);
			
				process_deferred_requests();
				
			
			
		}

		Thread.sleep(10000);
		
		for(int i=0;i<Node.max_nodes;i++)
		{
			if(i!=Node.node_id)
			{
				serObj.get(i).server.close();
				
			}
		}
		System.out.println("Completed");
		System.exit(0);

		    
		}
		    catch(Exception e)
		    {
		    	System.out.println("Exception:"+e);
		    }
		
	}

}
