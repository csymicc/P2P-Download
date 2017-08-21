package p2p;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import p2p.Host;

public class Test5 {
	
	 public static void main(String[] args) throws IOException {
		 	Host Host = new Host();
	 		args = new String [3];
	 		args[0] = "java";
	 		args[1] = "192.168.0.5";
	 		args[2] = "1005";
			Host.main(args); 
	    }
}
