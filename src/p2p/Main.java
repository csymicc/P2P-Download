package p2p;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import p2p.Peer;

class Main {
	static String Index_f(ArrayList<Integer> a) {
    	String res = "";
    	for(int i = 0; i != a.size(); ++i) {
    		int p = a.get(i);
    		for(int j = 31; j != -1; --j) {
    			if((p >> j & 1) != 0) 
    				res += "1";
    			else res += "0";
    		}
    	}
    	return res;
    }
	
	public ArrayList<Integer> Get_Miss_Piece(ArrayList<Integer> Bitfield, ArrayList<Integer> Request)
    {
    	ArrayList<Integer> Miss_Piece = new ArrayList<Integer>();
    	for(int i = 0; i != Bitfield.size(); ++i) {
    		int b = Request.get(i);
    		b = b | Bitfield.get(i);
    		int p = Bitfield.get(i);
    		if(p != (b | p)) {
    			int diff = (b | p) ^ p;
    			for(int j = 31; j != -1; --j) {
    				if((diff >> j & 1) != 0) 
    					Miss_Piece.add((31 - j) + 32 * i);
    			}
    		}
    	}
    	return Miss_Piece;
    }
	
	public byte[] intToByteArray(int a) {  
	    return new byte[] {
	        (byte) ((a >> 24) & 0xFF),
	        (byte) ((a >> 16) & 0xFF),     
	        (byte) ((a >> 8) & 0xFF),     
	        (byte) (a & 0xFF)
	    };
	}
	
	
	public static long byteArrayToLong(byte[] b) {
		long res = 0;
		for(int i = 0; i != 8; ++i) {
			res |= (long)(b[7 - i] & 0xFF) << (8 * i);
		}
		return res;
	}
	
	
	public static byte[] LongToByteArray(long a) {  
	    byte[] b = new byte[8];  
	    for (int i = 0; i != 8; i++) {  
	        b[i] = (byte)(a >>> (56 - (i * 8)));  
	    }  
	    return b;
	};
	
	
	public static void main(String[] args) throws IOException {
		
		
		List<Integer> Req_Piece = new ArrayList<Integer>();
		Req_Piece.add(1);
		Req_Piece.add(1);
		Req_Piece.add(1);
		Req_Piece.add(1);
		for(int i : Req_Piece) {
			i = 2;
		}
		System.out.println(Req_Piece);
		
		
		/*
		
	
		String[] s = {"1001 lin114-00.cise.ufl.edu 6008 1",
				"1002 lin114-01.cise.ufl.edu 6008 0",
				"1003 lin114-02.cise.ufl.edu 6008 0",
				"1004 lin114-03.cise.ufl.edu 6008 0",
				"1005 lin114-04.cise.ufl.edu 6008 0",
				"1006 lin114-05.cise.ufl.edu 6008 0"};
		ArrayList<String> peers = new ArrayList<String>();
		peers.add("1001 lin114-00.cise.ufl.edu 6008 1");
		peers.add("1002 lin114-01.cise.ufl.edu 6008 0");
		
		int a = 1001;

		Peer peer1 = new Peer(peers, 1001, 1001, "Untitled.png", 4489121, 4489);
		Peer peer2 = new Peer(peers, 1002, 1001);
		
		byte[] res1 = peer1.gene_mess(8);
		byte[] res2 = peer2.gene_mess(8);
		
		peer2.host_peer.piece.set(0, 3);
		peer2.host_peer.piece.set(1, 3);
		
		int j = 0;
		while(j++ < 3) {
			byte[] tmp1;
			byte[] tmp2;
			System.out.println("peer1:");
			tmp1 = peer1.mess_res(res2, 1002);
			System.out.println("peer2:");
			tmp2 = peer2.mess_res(res1, 1001);
			res2 = tmp2;
			res1 = tmp1;
		}
		int ssss = 0;
		/*
		byte[] have;
		res1 = peer1.gene_mess(1);
		peer1.Pref_neigh.add(1002);
		System.out.println("start sending:");
		res2 = peer2.mess_res(res1, 1001);
		while(j++ < 1004) {
			System.out.println("peer2:");
			res1 = peer1.mess_res(res2, 1002);
			byte[] payload2 = peer2.get_byte(res1, 5, 4);
			int Piece_Index2 = peer2.byteArrayToInt(payload2);
			System.out.println(Piece_Index2);
			System.out.println("");
			System.out.println("peer1:");
			res2 = peer2.mess_res(res1, 1001);
			System.out.println("jskdie" + res2.length);
			byte[] payload1 = peer2.get_byte(res2, 5, 4);
			int Piece_Index = peer2.byteArrayToInt(payload1);
			have = peer2.gene_mess(4, Piece_Index);
			peer1.mess_res(have, 1002);
			System.out.println("*******");
			System.out.println("");
		}
		
		System.out.println("peer2:");
		res1 = peer1.mess_res(res2, 1002);
		byte[] payload2 = peer2.get_byte(res1, 5, 4);
		int Piece_Index2 = peer2.byteArrayToInt(payload2);
		System.out.println(Piece_Index2);
		System.out.println("");
		System.out.println("peer1:");
		res2 = peer2.mess_res(res1, 1001);
		System.out.println("jskdie" + res2.length);
		System.out.println("*******");
		System.out.println("");
		
		
		
		
		File file = new File("Host.txt");
		FileInputStream fis = new FileInputStream(file);
		List<String> f = new ArrayList<String>();
		byte[] bs = new byte[1000];
		for(int i = 0; i != 100; ++i) {
			fis.skip(i * 1000);
			fis.read(bs);
			f.add(new String(bs));
		}
		FileWriter fw = new FileWriter("H.txt", true);
		for(int i = 0; i != 100; ++i) {
			fw.write(f.get(i));
		}
		fw.close();
		//String s2 = host2.mess_res(s1, 1001);

		
	//	byte[] mess1 = s1.getBytes();
		//byte[] mess2 = s2.getBytes();
		
	//	System.out.println("68 " + mess2.length);
	//	System.out.println("    ");
	//	System.out.println("    ");
		//int type2 = mess2[4] & 0xFF;
		
		//int piece_num = host2.byteArrayToInt(host2.get_byte(mess2, 5, 4));
		
		//System.out.println("184 Request " + host_peer.Index_f(Request))
		//System.out.println("type2 " + type2 + " " + piece_num);
		
		*/

	}
}
