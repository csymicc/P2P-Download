package p2p;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class Peer2{
	
	public UnitPeer host_peer = new UnitPeer();
	public List<UnitPeer> neighbours = Collections.synchronizedList(new ArrayList<UnitPeer>());
	public Map<Integer, Integer> IDmap = new ConcurrentHashMap<Integer, Integer>();
	public Set<Integer> Pref_neigh = new ConcurrentSkipListSet<Integer>();
	public Set<Integer> Int_neigh = new ConcurrentSkipListSet<Integer>();
	public Set<Integer> Uck_server = new ConcurrentSkipListSet<Integer>();
	public int opt_uck_neigh = 0;
	public List<String> file = Collections.synchronizedList(new ArrayList<String>());
	public List<Integer> Request = Collections.synchronizedList(new ArrayList<Integer>());
	public List<Integer> Req_Piece = Collections.synchronizedList(new ArrayList<Integer>());
	public Boolean Finish = false;
	public String FILENAME;
	private DateFormat dform = new SimpleDateFormat("MM/dd HH:mm:ss");
	private Calendar Date = Calendar.getInstance();
	

	
	
	
	public Peer2(ArrayList<String> peers, int host_ID, int size) throws IOException {
		for(int i = 0; i != peers.size(); ++i) {
			String[] info = peers.get(i).split(" ");
			int ID = Integer.valueOf(info[0]);
			if(ID == host_ID) {
				System.out.println(info[3].charAt(0));
				host_peer.initialize(host_ID, info[1], info[2], size, info[3].charAt(0) == '1');
				neighbours.add(new UnitPeer(ID, info[1], info[2], size, info[3].charAt(0) == '1'));
				IDmap.put(ID, neighbours.size() - 1);
				Finish = info[3].charAt(0) == '1';
			}
			else {
				neighbours.add(new UnitPeer(ID, info[1], info[2], size, info[3].charAt(0) == '1'));
				IDmap.put(ID, neighbours.size() - 1);
			}
			Req_Piece.add(-1);
		}
		
		FILENAME = "log_peer_" + Integer.toString(host_peer.peerID) + ".log";
		
		host_peer.show();
		
		for(int i = 0; i != size; ++i) file.add("");
		
		for(int i = 0; i != (size - 1) / 32 + 1; ++i) Request.add(0);
	}
	
	public Peer2(ArrayList<String> peers, int host_ID, int size, String source_file, int length) throws IOException {
		for(int i = 0; i != peers.size(); ++i) {
			String[] info = peers.get(i).split(" ");
			int ID = Integer.valueOf(info[0]);
			if(ID == host_ID) {
				host_peer.initialize(host_ID, info[1], info[2], size, info[3].charAt(0) == '1');
				neighbours.add(new UnitPeer(ID, info[1], info[2], size, info[3].charAt(0) == '1'));
				IDmap.put(ID, neighbours.size() - 1);
				Finish = info[3].charAt(0) == '1';
			}
			else {
				neighbours.add(new UnitPeer(ID, info[1], info[2], size, info[3].charAt(0) == '1'));
				IDmap.put(ID, neighbours.size() - 1);
			}
			Req_Piece.add(-1);
		}
		
		FILENAME = "log_peer_" + Integer.toString(host_peer.peerID) + ".log";
		
		if(Finish) {
			File f = new File(source_file);
			FileInputStream fis = new FileInputStream(f);
			byte[] c1 = new byte[length];
			for(int i = 0; i != size; ++i) {
				fis.read(c1);
				file.add(new String(c1, "ISO-8859-1"));
			}
			//byte[] c2 = new byte[tlength - (size - 1) * length];
			//fis.read(c2);
			//file.add(new String(c2));
			//System.out.println(file);
			fis.close();
		}
		else
			for(int i = 0; i != size; ++i) file.add("");
		
		for(int i = 0; i != (size - 1) / 32 + 1; ++i) Request.add(0);
	}
	
	public void Peer_init(List<String> peers, int host_ID, int size) throws IOException {
		for(int i = 0; i != peers.size(); ++i) {
			String[] info = peers.get(i).split(" ");
			int ID = Integer.valueOf(info[0]);
			if(ID == host_ID) {
				host_peer.initialize(host_ID, info[1], info[2], size, info[3].charAt(0) == '1');
				Finish = info[3].charAt(0) == '1';
			}
			else {
				neighbours.add(new UnitPeer(ID, info[1], info[2], size, info[3].charAt(0) == '1'));
				IDmap.put(ID, neighbours.size() - 1);
			}
		}
		
		FILENAME = "log_peer_" + Integer.toString(host_peer.peerID) + ".log";
		
		
		for(int i = 0; i != size; ++i) file.add("");
		
		for(int i = 0; i != (size - 1) / 32 + 1; ++i) Request.add(0);
		
		return;
	}
	
	
	public void Peer_init(List<String> peers, int host_ID, int size, String source_file, int length) throws IOException {
		for(int i = 0; i != peers.size(); ++i) {
			String[] info = peers.get(i).split(" ");
			int ID = Integer.valueOf(info[0]);
			if(ID == host_ID) {
				host_peer.initialize(host_ID, info[1], info[2], size, info[3].charAt(0) == '1');
				Finish = info[3].charAt(0) == '1';
			}
			else {
				neighbours.add(new UnitPeer(ID, info[1], info[2], size, info[3].charAt(0) == '1'));
				IDmap.put(ID, neighbours.size() - 1);
			}
		}
		
		FILENAME = "log_peer_" + Integer.toString(host_peer.peerID) + ".log";
		
		if(Finish) {
			File f = new File(source_file);
			FileInputStream fis = new FileInputStream(f);
			byte[] c = new byte[length];
			for(int i = 0; i != size; ++i) {
				fis.skip(i * length);
				fis.read(c);
				file.add(new String(c, "ISO-8859-1"));
			}
			fis.close();
		}
		else 
			for(int i = 0; i != size; ++i) file.add("");
		
		for(int i = 0; i != (size - 1) / 32 + 1; ++i) Request.add(0);
		
		return;
	}
	
	public int byteArrayToInt(byte[] b) {  
		return   b[3] & 0xFF |  
	            (b[2] & 0xFF) << 8 |  
	            (b[1] & 0xFF) << 16 |  
	            (b[0] & 0xFF) << 24;  
	}
	
	public byte[] intToByteArray(int a) {  
	    return new byte[] {
	        (byte) ((a >> 24) & 0xFF),
	        (byte) ((a >> 16) & 0xFF),     
	        (byte) ((a >> 8) & 0xFF),     
	        (byte) (a & 0xFF)
	    };
	}
	
	public long byteArrayToLong(byte[] b) {
		long res = 0;
		for(int i = 0; i != 8; ++i) {
			res |= (long)(b[7 - i] & 0xFF) << (8 * i);
		}
		return res;
	}
	
	public byte[] LongToByteArray(long a) {  
	    byte[] b = new byte[8];  
	    for (int i = 0; i != 8; i++) {  
	        b[i] = (byte)(a >>> (56 - (i * 8)));  
	    }  
	    return b;
	};
	
	public byte[] get_byte(byte[] a, int l, int lens) {
		byte[] res = new byte[lens];
		for(int i = l; i != lens + l; ++i)
			res[i - l] = a[i];
		return res;
	}

	public void turn_bits(List<Integer> a, int index, int b)
    {
		if(b == 1) {
			int i = index / 32;
			int c = index % 32;
			int p = a.get(i) | 1 << (31 - c);
			a.set(i, p);	
			return;
		}
		else if(b == 0){
			int i = index / 32;
			int c = index % 32;
			int p = a.get(i) ^ 1 << (31 - c);
			a.set(i, p);
			return;
		}
    }
	
	//generate message 0,1,2,3,5 and handshake message
	public byte[] gene_mess(int mess_type) throws UnsupportedEncodingException {
		switch(mess_type) {
		//generate choke message
		case 0 : {
			byte[] mess = new byte[5];
			byte[] head = intToByteArray(mess.length);
			System.arraycopy(head, 0, mess, 0, head.length);
			return mess;
		}
		//generate unchoke message
		case 1 : {
			byte[] mess = new byte[5];
			byte[] head = intToByteArray(mess.length);
			System.arraycopy(head, 0, mess, 0, head.length);
			mess[4] = (byte) mess_type;
			return mess;
		}
		//generate interested message
		case 2 : {
			byte[] mess = new byte[5];
			byte[] head = intToByteArray(mess.length);
			System.arraycopy(head, 0, mess, 0, head.length);
			mess[4] = (byte) mess_type;
			return mess;
		}
		//generate uninterested message
		case 3 : {
			byte[] mess = new byte[5];
			byte[] head = intToByteArray(mess.length);				
			System.arraycopy(head, 0, mess, 0, head.length);
			mess[4] = (byte) mess_type;
			return mess;
		}
		//generate bitfield message
		case 5 : {
			byte[] payload = host_peer.Bitfield();
			byte[] mess = new byte[5 + payload.length];
			byte[] head = intToByteArray(mess.length);
			System.arraycopy(head, 0, mess, 0, head.length);
			mess[4] = (byte) mess_type;
			System.arraycopy(payload, 0, mess, 5, payload.length);
			return mess;
		}
		case 8 : {
			byte[] mess = new byte[32];
			String s = "P2PFILESHARINGPROJ";
			byte[] head = s.getBytes("ISO-8859-1");
			byte[] id = intToByteArray(host_peer.peerID);
			System.arraycopy(head, 0, mess, 0, head.length); 
		    System.arraycopy(id, 0, mess, 28, id.length); 
		    return mess;
		}
		case 9 : {
			byte[] mess = new byte[17];
			byte[] head = intToByteArray(mess.length);
			byte[] id = intToByteArray(host_peer.peerID);
			byte[] time = LongToByteArray(System.currentTimeMillis());
			System.arraycopy(head, 0, mess, 0, head.length);
			mess[4] = (byte) mess_type;
			System.arraycopy(id, 0, mess, 5, id.length);
			System.arraycopy(time, 0, mess, 9, time.length);
			return mess;
		}
		}
		byte[] error = new byte[0];
		return error;
	}
	
	//generate message 4
	public byte[] gene_mess(int mess_type, int parameter) throws IOException {
		switch(mess_type) {
		//generate have message
		case 4: {
			byte[] mess = new byte[9];
			byte[] head = intToByteArray(mess.length);
			byte[] payload = intToByteArray(parameter);
			System.arraycopy(head, 0, mess, 0, head.length);
			mess[4] = (byte) mess_type;
			System.arraycopy(payload, 0, mess, 5, payload.length);
			return mess;
		}
		//generate request message
		case 6: {
			ArrayList<Integer> bitfield = neighbours.get(IDmap.get(parameter)).piece;
			host_peer.show(bitfield);
			ArrayList<Integer> Miss_Piece = host_peer.Get_Miss_Piece(bitfield, Request);
			if(!Miss_Piece.isEmpty()) {
				Random rand = new Random();
				int n = rand.nextInt(Miss_Piece.size());
				int index = Miss_Piece.get(n);
				System.out.println("Request index" + index);
				turn_bits(Request, index, 1);
				Req_Piece.set(IDmap.get(parameter), index);
				byte[] mess = new byte[9];
				byte[] head = intToByteArray(mess.length);
				byte[] payload = intToByteArray(index);
				System.arraycopy(head, 0, mess, 0, head.length);
				mess[4] = (byte) mess_type;
				System.arraycopy(payload, 0, mess, 5, payload.length);
				return mess;
			}
			else return new byte[0];
		}
		}
		byte[] error = new byte[0];
		return error;
	}
	
	
	//generate piece message
	public byte[] gene_mess(int mess_type, int pieceload1, byte[] pieceload2) throws IOException {
		//generate piece message
		byte[] payload1 = intToByteArray(Integer.valueOf(pieceload1));
		byte[] payload2 = pieceload2;
		byte[] mess = new byte[9 + payload2.length];
		byte[] head = intToByteArray(mess.length);
		System.arraycopy(head, 0, mess, 0, head.length);
		mess[4] = (byte) mess_type;
		System.arraycopy(payload1, 0, mess, 5, payload1.length);
		System.arraycopy(payload2, 0, mess, 9, payload2.length);
		return mess;
	}
	
	//get response to receiving message
	public byte[] mess_res(byte[] mess, int source_ID) throws IOException {
		if(mess.length < 4) return new byte[0];
						
		//response message
		
		
		//check if message is a handshake message
		if(mess.length >= 32) {
			String headline = new String(get_byte(mess, 0, 18), "ISO-8859-1");
			if(headline.equals("P2PFILESHARINGPROJ")) {
				System.out.println("message type: handshake message from " + source_ID);
				int ID = byteArrayToInt(get_byte(mess, 28, 4));
				System.out.println("ID"+ID);
			//	if(ID == source_ID)	{
					byte[] res = gene_mess(5);
					System.out.println("message type: bitfield message to " + source_ID);
				//}
				host_peer.show();
				return res;
			}
		}
		//message type
		int type = mess[4] & 0xFF;
		System.out.println("mess type " + type);
		
		//message is an actual message
		switch(type) {
		//message is a choke message
		case 0: {
			System.out.println("message type: choke message from " + source_ID);
			log(5, Integer.toString(source_ID));
			if(Uck_server.contains(source_ID))
				Uck_server.remove(source_ID);
			if(Req_Piece.get(IDmap.get(source_ID)) != -1) {
				
				System.out.println(Req_Piece.get(IDmap.get(source_ID)));
				int index = Req_Piece.get(IDmap.get(source_ID));
				turn_bits(Request, index, 0);
				Req_Piece.set(IDmap.get(source_ID), -1);
			}
			break;
		}
		//message is an unchoke message
		case 1: {
			System.out.println("message type: unchoke message from " + source_ID);
			log(4, Integer.toString(source_ID));
			if(!Uck_server.contains(source_ID)) {
				Uck_server.add(source_ID);
				System.out.println("message type: request message to " + source_ID);
				return gene_mess(6, source_ID);
			}
			else break;
		}
		//message is an interested message
		case 2: {
			log(7, Integer.toString(source_ID));
			System.out.println("message type: interested message from " + source_ID);
			if(!Int_neigh.contains(source_ID))
				Int_neigh.add(source_ID);
			break;
		}
		//message is an uninterested message
		case 3: {
			log(8, Integer.toString(source_ID));
			System.out.println("message type: uninterested message from " + source_ID);
			if(Int_neigh.contains(source_ID))
				Int_neigh.remove(source_ID);
			break;
		}
		//message is an have message
		case 4: {
			System.out.println("message type: have message from " + source_ID);
			//Update neighbour's corresponding piece index 
			int index = byteArrayToInt(get_byte(mess, 5, 4));
			neighbours.get(IDmap.get(source_ID)).Update_Piece(index);
			
			String[] content = new String[2];
			content[0] = Integer.toString(source_ID);
			content[1] = Integer.toString(index);
			log(6, content);
			
			//decide whether an interest message should be sent
			if(host_peer.Contain_Piece(neighbours.get(IDmap.get(source_ID)).piece)) 
				return gene_mess(3);
			else 
				return gene_mess(2);
		}
		//message is a bitfield message
		case 5: {
			System.out.println("message type: bitfield message from " + source_ID);
			//Set corresponding neighbour's bitfield
			byte[] payload = get_byte(mess, 5, mess.length - 5);
			ArrayList<Integer> bitfield = new ArrayList<Integer>();
			System.out.println(bitfield.size());
			for(int i = 0; (int) i != (payload.length / 4); ++i) {
				byte[] unit = get_byte(payload, 4 * i, 4);
				bitfield.add(byteArrayToInt(unit));
			}
			neighbours.get(IDmap.get(source_ID)).Update_Piece(bitfield);
			
			//host_peer.show();
			//host_peer.show(bitfield);
			//decide whether an interest message should be sent
			if(host_peer.Contain_Piece(bitfield)) {
				System.out.println("message type: uninterested message to " + source_ID);
				return gene_mess(3);
			}
			else {
				System.out.println("message type: interested message to " + source_ID);
				return gene_mess(2);
			}
		}
		//message is a request message
		case 6: {
			System.out.println("message type: request message from " + source_ID);
			if(Pref_neigh.contains(source_ID)) {
				byte[] piece_index = get_byte(mess, 5, 4);
				int index = byteArrayToInt(piece_index);
				System.out.println("Request index:" + index);
				byte[] respayload = file.get(index).getBytes("ISO-8859-1");
				System.out.println("message type: piece message to " + source_ID + "  mess length:");
				return gene_mess(7, index, respayload);
			}
			else 
				return gene_mess(0);
		}
		//message is a piece message
		case 7: {
			System.out.println("message type: piece message from " + source_ID);
			byte[] payload1 = get_byte(mess, 5, 4);
			byte[] payload2 = get_byte(mess, 9, mess.length - 9);
			int index = byteArrayToInt(payload1);
			System.out.println("receive piece is" + index);
			Req_Piece.set(IDmap.get(source_ID), -1);
			host_peer.Update_Piece(index);
			file.set(index, new String(payload2, "ISO-8859-1"));
			
			String[] content = new String[2];
			content[0] = Integer.toString(source_ID);
			content[1] = Integer.toString(index);
			log(9, content);
			
			if(host_peer.check_file()) {
				Finish = true;
				log(10, Integer.toString(source_ID));
				FileWriter fw = new FileWriter("download.txt", true);
				for(int i = 0; i != file.size(); ++i)
					fw.write(file.get(i));
				fw.close();
			}
			else if(host_peer.Contain_Piece(neighbours.get(IDmap.get(source_ID)).piece)) {
				System.out.println("message type: uninterested message to " + source_ID);
				return gene_mess(3);
			}
			else {
				System.out.println("message type: request message to " + source_ID);
				return gene_mess(6, source_ID);
			}
			break;
		}
		}
		return new byte[0];
	}
	
	public void log(int type, String payload) throws IOException {
		String content = "[" + new String(dform.format(Date.getTime())) + "]: ";
		String peerID = Integer.toString(host_peer.peerID);
		switch(type) {
		case 0 : {
			content += "Peer [" + peerID + "] makes a connection to Peer [" + payload + "].\r\n"; 
			break;
		}
		case 1 : {
			content += "Peer [" + peerID + "] is connected from Peer [" + payload + "].\r\n"; 
			break;
		}
		case 2 : {
			if(!Pref_neigh.isEmpty()) {
				String n = new String();
				for(int i : Pref_neigh) {
					n += Integer.toString(i) + ",";
				}
				n.substring(0, n.length() - 1);
				content += "Peer [" + peerID + "] has the preferred neighbors [" + n + "].\r\n";
			}
			break;
		}
		case 3 : {
			if(opt_uck_neigh != 0) {
				String n = Integer.toString(opt_uck_neigh);
				n.substring(0, n.length() - 1);
				content += "Peer [" + peerID + "] has the optimistically unchoked neighbor [" + n + "].\r\n";
			}
			break;
		}
		case 4 : {
			content += "Peer [" + peerID + "] is unchoked by [" + payload + "].\r\n";
			break;
		}
		case 5 : {
			content += "Peer [" + peerID + "] is choked by [" + payload + "].\r\n";
			break;
		}
		case 7 : {
			content += "Peer [" + peerID + "] received the 'interested' message from [" + payload + "].\r\n";
			break;
		}
		case 8 : {
			content += "Peer [" + peerID + "] received the 'not interested' message from [" + payload + "].\r\n";
			break;
		}
		case 10 : {
			content += "Peer [" + peerID + "] has downloaded the complete file.\r\n";
		}
		}
		FileWriter fw = new FileWriter(FILENAME, true);
		fw.write(content);
		fw.close();
	}
	
	
	public void log(int type, String payload[]) throws IOException {
		String content = "[" + new String(dform.format(Date.getTime())) + "]: ";
		String peerID = Integer.toString(host_peer.peerID);
		switch(type) {
		case 6 : {
			content += "Peer [" + peerID + "] received the 'have' message from [" + payload[0] + "] for the piece [" + payload[1] + "].\r\n";
			break;
		}
		case 9 : {
			content += "Peer [" + peerID + "] has downloaded the piece [" + payload[0] + "] from [" + payload[1] + "].\r\n";
			break;
		}
		}
		FileWriter fw = new FileWriter(FILENAME, true);
		fw.write(content);
		fw.close();
	}
	
	
	
	//generate optmistically unchoked neighbor
	//first string is an unchoked meesage
	//second string represents peer_ID
/*	public byte[] gene_opt() throws IOException {
		String[] res = new String[2];
		ArrayList<Integer> ck_neigh = new ArrayList<Integer>();
		for(int i : Int_neigh)
			if(!Pref_neigh.contains(i))
				ck_neigh.add(i);
		
		Random rand = new Random();
		int n = rand.nextInt(ck_neigh.size());
		res[0] = gene_mess(1);
		res[1] = Integer.toString(ck_neigh.get(n));
		log(3, res[1]);
		return res;
	}*/
}
