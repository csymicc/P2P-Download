package p2p;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
/*
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import p2p.UnitPeer;
import p2p.Client;
import p2p.Server;
*/
import p2p.Host;

public class Test2 {
/*@Test
    public void test() throws Exception
    {
	    Host host=new Host();
	    host.startServer();
	    host.startSender();
	}*/

	
	 public static void main(String[] args) throws IOException {
/*		 	FileInputStream inputfile=null;
		 	String path = "PeerInfo.cfg";
		 	String line = null;
		 	inputfile = new FileInputStream(path);
		 	String Info[] = null;
		 	BufferedReader Reader = new BufferedReader(new FileReader(path));
			Host.TotalSize=100;
		 	String HostName[] = new String [Host.TotalSize];
		 	int PeerID[] = new int [Host.TotalSize];
		 	int PortNumber[] = new int [Host.TotalSize];
		 	int FileSize[] = new int [Host.TotalSize];
		 	int i = 0;
		 	while((line = Reader.readLine())!=null){   		 	         
		 	        Info = line.split(" "); 			//			split the string line then put the corresponding elements in Info[]

		 	        HostName[i] = Info[1];
		 	        PeerID[i] = Integer.parseInt(Info[0]);
		 	        PortNumber[i] = Integer.parseInt(Info[2]);	 	
		 	        FileSize[i] = Integer.parseInt(Info[3]);
		 	        System.out.println(HostName[i]);
		 	        i++;
		 	    } 
*/
	
		 	Host Host = new Host();
	 		args = new String [3];
	 		args[0] = "java";
	 		args[1] = "192.168.0.12";
	 		args[2] = "1002";
			int HostPeerID = Host.readconfig(args);
			System.out.println(HostPeerID);
//			System.out.println(Host.TotalSize);			
			Host.begin(HostPeerID);
		 
		 	
//		 	Peer PeerTable = new Peer(HostName, PeerID[0], FileSize[0]);	
		 	
		 	
//		 	Host host=new Host();
//		    host.getmessage("helloworld");
//	        host.startServer(60001);
//	        host.startSender("192.168.0.12",60001);
	    }
}
