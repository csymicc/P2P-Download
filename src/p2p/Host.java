package p2p;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import p2p.Peer;
import p2p.RTC;


public class Host {
	
	private ArrayList <String> InfoTable;
	private int LocalPeerID;
	private Peer HostPeer;
	private ArrayList <Integer> PeerID;
	private ArrayList <String> HostName;
	private ArrayList <Integer> PortNumber;
	private ArrayList <Integer> FileSize;
	private RTC RT;						//record transmission rate
	private int TotalSize;
	private boolean Optimal_Flag_Host;
	
	private Calendar Time;
	private int Number_of_Preferred_Neighbors;				//Number of Preferred Neighbors
	private int Optimistic_Unchoking_Interval;							// 
	private int Preferred_Neigbour_Interval;
	private String FileName;								//
	private int FileSizeTotal;								//
	private int PieceSize;									//
	private int PieceNumber;
	private boolean flag_preferred = false;							//flag of starting choosing preferred neighbors
	private ArrayList<Integer> Preferred_Group;				// the preferred group caculated by the 

	
	private String TypeTable [] ={"choke","unchoke","interested","not interested","have","bitfield","request","piece"}; 
	
    public Host() {	
    }
    
    static String message;
    /* start sender and send message to hostname through port*/
    
    public class Send_Thread extends Thread
    {
    	private RTC_Unit RTCU;
    	private boolean last_Flag;
    	private DataOutputStream out;
    	private DataInputStream in;
    	
    	public Send_Thread(RTC_Unit RTCU, boolean last_Flag, DataOutputStream out, DataInputStream in)
    	{
    		this.RTCU = RTCU;
    		this.last_Flag = last_Flag;
    		this.out = out;
    		this.in = in;
    	}
    	
    	public void run()
    	{
//    		System.out.println("Thread Sending Init" +last_Flag+RTCU.Pref_Flag);
    		while(true)
    		{
    			try {
					sleep(10);
					if(last_Flag != RTCU.Pref_Flag)								//being set as a preferred group
					{
						try {
							if(last_Flag)
							{
								System.out.println("ErrorAAAAAAAAAAAAAAAAAAAAAAAAAA");
								System.out.println("ErrorAAAAAAAAAAAAAAAAAAAAAAAAAA");
								System.out.println("ErrorAAAAAAAAAAAAAAAAAAAAAAAAAA");
								last_Flag = false;	
								out.write(HostPeer.gene_mess(0));
								out.flush(); 
							}
							else
							{
								 last_Flag = true;
						         out.write(HostPeer.gene_mess(1));
						         out.flush();
							}
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
					
					if(RTCU.Time_Out_Flag == true && RTCU.Pref_Flag == true)
					{
						RTCU.Time_Out_Flag = false;
							try {
								out.write(HostPeer.gene_mess(1));
								out.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
					
//					System.out.println(HostPeer.opt_uck_neigh + " boolean " + Optimal_Flag_Host);
					if(HostPeer.opt_uck_neigh == PeerID.get(RTCU.number) && Optimal_Flag_Host == true)
					{
						Optimal_Flag_Host = false;
						try {
							System.out.println("XXXXX");
							out.write(HostPeer.gene_mess(1));
							out.flush();
							RTCU.Optimal_Flag = true; 
							
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					boolean FlagQ = RTCU.Piece_Queue.isEmpty();
					if(FlagQ == false)
					{
						while(!RTCU.Piece_Queue.isEmpty())
						{
//							System.out.println(RTCU.Piece_Queue.size());
							try {
							Integer Piece_Index = RTCU.Piece_Queue.poll();
							out.write(HostPeer.gene_mess(4,Piece_Index));
							out.flush();			
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NullPointerException e) {
								break;
							}
						}
					}	
					if(RTCU.Finish_Flag == true)
					{
						RTCU.Finish_Flag = false;
						try {
							out.write(HostPeer.gene_mess(3));
							out.flush();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NullPointerException e) {
							break;
						}
					}
					
//					if(HostPeer.terminate == 0)
//						break;
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}	
					
//    			if(HostPeer.terminate == 0)
//    			{
//    				break;
 //   			}
    		}
			
		}
    }
    
    
    
    
    public void startSender(int i, int number) {
        (new Thread() {
            @Override
            public void run() {
                try {
//                	System.out.println("InetAddress.getByName(HostName.get(number))" +InetAddress.getByName(HostName.get(number))+ " PortNumber.get(number)" + PortNumber.get(number));
                	Socket s = new Socket();  
                	s.connect(new InetSocketAddress(HostName.get(i), PortNumber.get(i)), 2000);   
//                	Socket s = new Socket(HostName.get(i), PortNumber.get(i));
//                  Socket s = new Socket(HostName.get(i), PortNumber.get(i), InetAddress.getByName(HostName.get(number)), PortNumber.get(number));          

                    DataInputStream in = new DataInputStream (s.getInputStream());
                    DataOutputStream out = new DataOutputStream (s.getOutputStream());
                    System.out.println(Time.getTimeInMillis() + "Initalization: Connection to Server " + HostName.get(i) + " is built" + " port Number is " + PortNumber.get(i));
                
                   
                    byte [] handshake=HostPeer.gene_mess(8);
                	System.out.println("In put message as a Client" + handshake);
                    out.write(handshake);
                    out.flush(); 
                    HostPeer.log(0,HostName.get(i));
                    byte input [] = new byte [32];
                    in.read(input);
                    byte output [] = HostPeer.mess_res(input, PeerID.get(GetNumber(InetAddress.getByName(HostName.get(i)).getHostAddress())));
                    out.write(output);
                    out.flush(); 

                    Connection_Byte_Test(s,out,in,number,InetAddress.getByName(HostName.get(i)).getHostAddress(),null);
                
                
                } catch (UnknownHostException e) {
                	System.out.println("Client UnknownHostException");
                    e.printStackTrace();
                } catch (IOException e) {
                	System.out.println("Client IOException");
                    e.printStackTrace();
                } catch (SecurityException e){
                	System.out.println("Client SecurityException");

                } catch (IllegalArgumentException e) {
                	System.out.println("Client IllegalArgumentException");
                	
                }
            }
        }).start();
    }
    /* startServer, and listen to port */
    public void startServer(int number) {
        ServerSocket ss;
        try {
//            ss = new ServerSocket(PortNumber.get(number), Integer.MAX_VALUE, InetAddress.getLocalHost());
            ss = new ServerSocket(PortNumber.get(number));
            InetAddress.getLocalHost();
            ss.getSoTimeout();
//            System.out.println("LocalServer Address is " + ss.getLocalSocketAddress()+" Port Number is "+ PortNumber.get(number));
            while(true)
            {
                Socket s = ss.accept();
                System.out.println("Get Socket Success!");
                (new Thread() {
    	            @Override
    	            public void run(){
//    				BufferedReader in;
    				try {             

                        DataInputStream in = new DataInputStream (s.getInputStream());
                        DataOutputStream out = new DataOutputStream (s.getOutputStream());
    					System.out.println("Initialization : Connection to Client " + s.getInetAddress() + " is built" + " port Number is " +                         s.getPort());
                        byte [] input = new byte [32];
                        in.read(input);
                        byte [] handshake=HostPeer.gene_mess(8);
                    	System.out.println("The first hand shake message:" + handshake);
                    	System.out.println("The first input message:" + input);
                        out.write(handshake);
                        out.flush(); 
    					HostPeer.log(1,s.getInetAddress().getHostAddress());
    					
                        Connection_Byte_Test(s,out,in,number,s.getInetAddress().getHostAddress(),input);
                        
                    } catch (UnknownHostException e) {
                    	System.out.println("Server  UnknownHostException");
                        e.printStackTrace();
                    } catch (IOException e) {
                    	System.out.println("Time OUT~~~~~");
                        e.printStackTrace();
                    } catch (SecurityException e){
                    	System.out.println("Server  SecurityException");

                    } catch (IllegalArgumentException e) {
                    	System.out.println("Server  IllegalArgumentException");
                    	
                    }
    	            }
                }).start();
            }
        } catch (IOException e) {
        	System.out.println("ServerX  IOException");
            e.printStackTrace();
        } catch (SecurityException e){
        	System.out.println("ServerX  SecurityException");

        } catch (IllegalArgumentException e) {
        	System.out.println("ServerX  IllegalArgumentException");
        	
        }
    }
    
    public int GetNumber(String IP)
    {
    	int rtn=0;
		int length=HostName.size();
		try {
			while(rtn<length&&(!InetAddress.getByName(HostName.get(rtn)).getHostAddress().equals(IP)))
			 {
				 rtn++;
			 }

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rtn;
    }
    
    /* Set the host message*/

	
	public void Connection_Byte_Test(Socket s,DataOutputStream out, DataInputStream in, int number, String IP, byte [] line) throws IOException  
	{
		
		long Current_Time = System.currentTimeMillis();
        flag_preferred=true;
        boolean last_Flag = false;
        boolean Recieve_Send = false;
        RTC_Unit RTCU= new RTC_Unit(GetNumber(IP), 0, PieceNumber+1, out, in);				//Initlization: first element in 
		RT.Add_RTC_Unit(RTCU);												// default flag is false
        RT.Sort();
        RT.Print_Array();
//        System.out.println(last_Flag);
        Send_Thread SThread = new Send_Thread(RTCU, last_Flag, out, in);
        SThread.start();
        
        
		try{
	        if(line!=null)
	        {
			     out.write(HostPeer.mess_res(line, PeerID.get(GetNumber(IP))));				//repay
			     out.flush();
//			     System.out.println("My source ID is "+PeerID.get(GetNumber(IP)) );
	        }
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();				
		}
		
		System.out.println("Pending Process Init");
		while (true)
		{
			
//			if(HostPeer.terminate == 0)
//			{
//				break;
//			}
			
//			System.out.println(last_Flag);
			int length = HostName.size();
			int pos = GetNumber(IP);
			
			byte [] line_length = new byte [4];
			in.read(line_length);
			

			int len_line = HostPeer.byteArrayToInt(line_length);
			
	        line= new byte [len_line - 4];
	        in.read(line);
	        byte [] total_line = new byte [len_line];
	        System.arraycopy(line_length, 0, total_line, 0, 4);
	        System.arraycopy(line,0,total_line,4,line.length);  
			int type = total_line[4] & 0xFF;	
		    if(total_line.length > 3) 
		    {
				if(type == 7 || type == 6)					//request message or piece message
				{
					RTCU.Total_Rate += 1;
					
				}
				if(type == 6)
				{
					Current_Time = System.currentTimeMillis();
					if(Current_Time>RTCU.Last_Time && RTCU.Last_Time != 0)
					{
						Current_Time = System.currentTimeMillis();
						RTCU.insert((int)(Current_Time-RTCU.Last_Time));
					}
					RTCU.Last_Time = System.currentTimeMillis();
				}
				if(type == 7)
				{
					byte[] payload1 = HostPeer.get_byte(total_line, 5, 4);
					int Piece_Index = HostPeer.byteArrayToInt(payload1);
//					for(int i=0;i<RT.TableSize();i++)
//					{
//						RT.RateTable.get(i).out.write(HostPeer.gene_mess(4, Piece_Index));
//					}
					for(int i=0;i<RT.TableSize();i++)
					{
						RT.RateTable.get(i).Piece_Queue.add(Piece_Index);
					}

				}
				if (pos<length){
					byte [] response_msg=HostPeer.mess_res(total_line, PeerID.get(pos));
	
					out.write(response_msg);
					out.flush();
				}
				else 
				{
					System.out.println("can not find IP address");
				}
			}
		    line = null;
		}
//		s.close();
    }	
	
	
	
    public void getmessage(String message)
    {
    	Host.message=message;
    }
    
    public int readconfig(String[] args) throws IOException{
    	
    	InfoTable = new ArrayList <String>();
    	HostName = new ArrayList <String>();
    	PeerID = new ArrayList <Integer>();
    	PortNumber = new ArrayList <Integer>();
    	FileSize = new ArrayList <Integer>();
    	int HostPeerID = -1;
	 	String path = "PeerInfo.cfg";
	 	String line;
	 	String Info[] = null;
	 	BufferedReader Reader = new BufferedReader(new FileReader(path));
	 	int i = 0;
	 	while((line = Reader.readLine())!=null){
	 		InfoTable.add(line);
	 		Info = line.split(" "); 
	 		HostName.add(Info[1]);
	 		PeerID.add(Integer.parseInt(Info[0]));
	 		PortNumber.add(Integer.parseInt(Info[2]));
	 		FileSize.add(Integer.parseInt(Info[3]));
	 		if (Integer.parseInt(args[0]) == PeerID.get(i)){
 	        	HostPeerID = i;
	 		}
	 		i++;
	 	} 
	 	
	 	System.out.println("PieceSize "+ PieceSize + "PieceNumber "+ PieceNumber);
	 	TotalSize=InfoTable.size();
	 	return HostPeerID;
    }
    
    public void begin(int HostPeerID){
    	for(int i=0; i < TotalSize; i++){
    		if(i<HostPeerID){
    			try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	        startSender(i, HostPeerID);	 	//"192.168.0.12",60001    			
    		}
    		else if(i == HostPeerID){
    	        startServer(i);					//60001
    		}
    	}
    }
    
    public void Timer_Interval(){
    	(new Thread(){
    		public void run(){
    		do{
    			 try {
                  sleep(1000);
            } catch (InterruptedException e) {
                  e.printStackTrace();
            }
    			if(flag_preferred==true)
    		{
    				break;
    		}
    		}while (true);
    		
    		
    		new Timer().schedule(new TimerTask() {  
                @Override  
                public void run() {
/*                	if(HostPeer.terminate == 0)
                	{
                		for(int i=0;i<RT.RateTable.size();i++)
                		{
                			RT.RateTable.get(i).End_Flag = true;
                		}
                		this.cancel();
                		
                		
                	}
*/                
                Update_Pref_Group();
                //System.out.println("caonima"+RT.TableSize());	
                //RT.Print_Array();
                try {
					HostPeer.log(2, "");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                System.out.println("Preferred Group Timer:" + new Date(System.currentTimeMillis()));  
                }  
            }, 0, Preferred_Neigbour_Interval*1000);  
    		
    		
    		new Timer().schedule(new TimerTask() {  
                @Override  
                public void run() {
                try {
//                	System.out.println("Terminate"+ HostPeer.terminate);
//                	if(HostPeer.terminate == 0)
//                		this.cancel();
					Update_Optimistic_Peer();
				} catch (IOException e) {
					e.printStackTrace();
				}
                try {
					HostPeer.log(3, "");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                System.out.println("Optimistic Unchoking Interval:" + new Date(System.currentTimeMillis()));  
                }  
            }, 0, Optimistic_Unchoking_Interval*1000);      		
    		
    		}
    	}).start();
    }
    
    public void Update_Pref_Group(){
//    	System.out.println(HostPeer.Finish);
    	if(HostPeer.Finish == true)
    	{
    		for (int i=0;i<RT.RateTable.size();i++)
    		{
    			RT.RateTable.get(i).Finish_Flag = true;
    		}
    	}
//    	System.out.println("Check Start");
    	RT.Set_Rate_Flag();
/*    	while (true)
    	{
    		boolean CheckResult = RT.Check();
//    		System.out.println("RTCheckResult " + RT.Check());	/////////PROBLEM!!!!!!!!!
    		if(CheckResult == true)
    		{
    			break;
    		}
    	}
    	System.out.println("Check Success");
*/
    	RT.Set_Rate();
    	HostPeer.Pref_neigh.clear();
    	for(int i=0;i<RT.TableSize();i++)
    	{
 //   		RT.RateTable.get(i).print();
    		RT.RateTable.get(i).avg_speed();
    		RT.RateTable.get(i).Last_Time = 0;
    	}
    	
    	RT.Sort();
    	
    	int temp = Number_of_Preferred_Neighbors;
    	
//   	System.out.println("Test: RT Table Size:" + RT.TableSize());
//   	System.out.println("Test: temp value:" + temp);
    	for(int i=0;i<(RT.TableSize());i++)
    	{
//        	System.out.println("When i = "+ i + " FindInterestedGroup = " + FindInterestedGroup(i));
//			System.out.println("In Update : " + RT.RateTable.get(i).Pref_Flag);        	   
			if(FindInterestedGroup(i) && i<temp)
			{
    			RT.UpDate_Pref(i,true);
    			HostPeer.Pref_neigh.add(PeerID.get(RT.RateTable.get(i).number));
			}
			else if(!FindInterestedGroup(i) && i<temp)
			{
    			RT.UpDate_Pref(i,false);
    			temp++;
    		}
			else 
			{
    			RT.UpDate_Pref(i,false);
    		}

    	}
		System.out.println("HostPeer.Pref_neigh : " + HostPeer.Pref_neigh);
		System.out.println("Interested Group" + HostPeer.Int_neigh);
//    	RT.Print_Array();   
//    	System.out.println("Terminate XXXXX" + HostPeer.terminate);
    	if(HostPeer.terminate == 0)
    	{
    		System.exit(0);
    	}
    }
   
    public void Update_Optimistic_Peer() throws IOException{
    	Optimal_Flag_Host = false;
    	ArrayList<Integer> ck_neigh = new ArrayList<Integer>();
		for(int i : HostPeer.Int_neigh)
			if(!HostPeer.Pref_neigh.contains(i))
				ck_neigh.add(i);
		
		Random rand = new Random();
		if(ck_neigh.size() > 0){
			int n = rand.nextInt(ck_neigh.size());
			HostPeer.opt_uck_neigh = ck_neigh.get(n);
			HostPeer.log(3, Integer.toString(ck_neigh.get(n)));
			Optimal_Flag_Host = true;
		}
		System.out.println("HostPeer.opt_uck_neigh : " + HostPeer.opt_uck_neigh);
		
		return;
    }
    
    public boolean FindInterestedGroup(int i)
    {
    	//System.out.println(HostPeer.Int_neigh);
    	if(HostPeer.Int_neigh.contains(PeerID.get(RT.RateTable.get(i).number)))
    		return true;
    	return false;
    }
    
    public void readCommon() throws IOException{
    	FileReader fr = new FileReader("Common.cfg");
    	BufferedReader br = new BufferedReader(fr);
    	String s;
    	while ((s = br.readLine()) != null) {
    	// System.out.println(s);
    	String[] data = s.split(" ");
    	if (data[0].equals("NumberOfPreferredNeighbors"))
    		Number_of_Preferred_Neighbors=Integer.parseInt(data[1]);
    	else if (data[0].equals("UnchokingInterval"))
    		Preferred_Neigbour_Interval=Integer.parseInt(data[1]);
    	else if (data[0].equals("OptimisticUnchokingInterval"))
    		Optimistic_Unchoking_Interval=Integer.parseInt(data[1]);
    	else if (data[0].equals("FileName"))
    		FileName=data[1];
    	else if (data[0].equals("FileSize"))
    		FileSizeTotal=Integer.parseInt(data[1]);
    	else if (data[0].equals("PieceSize"))
    		PieceSize=Integer.parseInt(data[1]);
    	}
    	//FileSizeTotal = 9802908;
    	//FileSizeTotal = 1633818;
    	//PieceSize = 1000;
    	PieceNumber = FileSizeTotal/PieceSize;
    	//Optimistic_Unchoking_Interval = 2;
    	//Preferred_Neigbour_Interval = 1;
    	//Number_of_Preferred_Neighbors = 2;
    	flag_preferred=false;
    	Optimal_Flag_Host = false;
    }
    
    
    public String Determine_Type_Msg(byte Stream[]){
    	byte type = Stream[4];
    	String TypeString = null;
    	for(int i = 0; i < 8; i++)
    	{
    		if(type == i){
    			TypeString = TypeTable[i];
    			break;
    		}
    	}	
    	return TypeString;
    }
    
	 public void main(String[] args) throws IOException {
    	try {
			readCommon();									//input the common settings here
			int HostPeerID = readconfig(args);
		 	HostPeer = new Peer(InfoTable,Integer.parseInt(args[0]),PieceNumber, FileName,FileSizeTotal,PieceSize);
			LocalPeerID = PeerID.get(HostPeerID);
	    	RT = new RTC();
			Time = Calendar.getInstance();					//initalize Time
		    							
			Timer_Interval();
			begin(HostPeerID);	
		} catch (IOException e) {
			e.printStackTrace();
		}  	
    } 
}




/*    		if(RTCU.Rate_Test_Send_Flag_RTCU == true)
{
	RTCU.Rate_Test_Send_Flag_RTCU = false;
	try {
		System.out.println("Tpye 9 Sent Length is " + HostPeer.gene_mess(9).length);
		out.write(HostPeer.gene_mess(9));
		out.flush();
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
}
*/   



/*
if(type == 9) {
	System.out.println("message type: time message from " + PeerID.get(pos));
	int index = HostPeer.byteArrayToInt(HostPeer.get_byte(total_line, 5, 4));
	if(index != HostPeer.host_peer.peerID)
	{
		out.write(total_line);
		out.flush();
	}	
	else {
		System.out.println("Receive type 9");
		RTCU.Rate_Test_Rece_Flag_RTCU = true;
		long time = HostPeer.byteArrayToLong(HostPeer.get_byte(total_line, 9, total_line.length - 9));
		long curtime = System.currentTimeMillis();
		int RTT = (int)(curtime - time);
		RTCU.rate_per = RTT;
		System.out.println("ID " + HostPeer.host_peer.peerID + "'s RTT is " + RTT);
	}
} 
*/