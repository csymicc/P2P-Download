package p2p;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import p2p.Host;

public class Test2 {
	
	 public static void main(String[] args) throws IOException {
		 	Host Host = new Host();
//	 		args = new String [3];
//	 		args[0] = "java";
//	 		args[1] = "192.168.0.14";
//	 		args[2] = "1002";
		 	args[0] = "1002";
			Host.main(args);
	    }
}
