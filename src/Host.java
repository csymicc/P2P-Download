package p2p;
import p2p.Peer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Host {
	
	private ArrayList <String> InfoTable;
	private Peer HostPeer;
	private ArrayList <Integer> PeerID;
	private ArrayList <String> HostName;
	private ArrayList <Integer> PortNumber;
	private ArrayList <Integer> FileSize;
	private int TotalSize;
	
    public Host() {	
    }
    
    static String message;
    /* start sender and send message to hostname through port*/
    public void startSender(int i, int number) {
        (new Thread() {
            @Override
            public void run() {
                try {
                    Socket s = new Socket(HostName.get(i), PortNumber.get(i));
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(s.getOutputStream()));
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(s.getInputStream()));
                    System.out.println("Connection to Server " + HostName.get(i) + " is built" + " port Number is " + PortNumber.get(i));
                    MainLoop(s,out,in,number,InetAddress.getByName(HostName.get(i)).getHostAddress());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } /*catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        }).start();
    }
    /* startServer, and listen to port */
    public void startServer(int number) {
        ServerSocket ss;
        try {
            ss = new ServerSocket(PortNumber.get(number));
            while(true)
            {
                Socket s = ss.accept();
                (new Thread() {
    	            @Override
    	            public void run(){
    				BufferedReader in;
    				try {
    				
    					in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    				
    					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));			
    				
    					System.out.println("Connection to Client " + s.getLocalAddress().getHostAddress() + " is built" + " port Number is " + PortNumber.get(number));
    				    String IP = s.getLocalAddress().getHostAddress();
    					MainLoop(s,out,in, number,IP);           			
    				
    					} catch (IOException e) {
    						e.printStackTrace();
    					}
    	            }
                }).start();
            }
        }
    	catch (IOException e) {
            e.printStackTrace();
        }
    }
    /* Set the host message*/
    
    public void MainLoop(Socket s,BufferedWriter out, BufferedReader in, int number, String IP) throws IOException  {					//They share the same process of receiving and sending message whatever it's server or client
 //       SendHandShake();						//should send to PeerID prior to the current one 
 //handshake
    	String handshake=HostPeer.gene_mess(8);
        out.write(handshake);
        out.newLine();
        out.flush();
        
        while(true)
        {
        String line=null;
        if ((line = in.readLine()) != null) 
        {    System.out.println(line);
             int length=HostName.size();
             int pos=0;
           //  System.out.println(Inet4Address.getByName(HostName.get(0)).getHostAddress()); System.out.println(Inet4Address.getByName(HostName.get(1)).getHostAddress()); System.out.println(Inet4Address.getByName(HostName.get(2)).getHostAddress());
           //  System.out.println(Inet4Address.getByName(HostName.get(3)).getHostAddress()); System.out.println(Inet4Address.getByName(HostName.get(4)).getHostAddress()); System.out.println(Inet4Address.getByName(HostName.get(5)).getHostAddress());
           //  System.out.println(InetAddress.getByName(HostName.get(0)).getHostAddress().length());
           //  System.out.println(IP.length());
             while(pos<length&&(!InetAddress.getByName(HostName.get(pos)).getHostAddress().equals(IP)))
             {
            	pos++;
             }
             
            // System.out.println(InetAddress.getByName(HostName.get(0)).getHostAddress());
             System.out.println(pos);
             if (pos<length)
             {String response_msg=HostPeer.mess_res(line, PeerID.get(pos));
             out.write(response_msg);
             out.newLine();
             out.flush();}
             else 
            	 System.out.println("can not find IP address");
        }
        }
    }
   
 /*   public void SendHandShake(){
    	
    }*/
    
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
	 		if (Integer.parseInt(args[2]) == PeerID.get(i)){
 	        	HostPeerID = i;
	 		}
	 		i++;
	 	} 
	 	HostPeer = new Peer(InfoTable,Integer.parseInt(args[2]),15536);
	 	TotalSize=InfoTable.size();
	 	return HostPeerID;
    }
    
    public void begin(int HostPeerID) {
    	for(int i=0; i < TotalSize; i++){
    		if(i<HostPeerID){
    	        startSender(i, HostPeerID);	 	//"192.168.0.12",60001    			
    		}
    		else if(i == HostPeerID){
    	        startServer(i);					//60001
    		}
    	}
    }
    
	 public void main(String[] args) throws IOException {
    	try {
    		args = new String [3];
    		args[0] = "java";
    		args[1] = "peerProcess";
    		args[2] = "1001";
			int HostPeerID = readconfig(args);
			System.out.println(HostPeerID);
			System.out.println(TotalSize);			
			begin(HostPeerID);	
		} catch (IOException e) {
			e.printStackTrace();
		}  	
    } 
}