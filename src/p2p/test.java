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

public class test {

	 public static void main(String[] args) throws IOException {
		 	Host Host = new Host();
	 		args = new String [3];
	 		args[0] = "1004";
			Host.main(args);
	    }
}
