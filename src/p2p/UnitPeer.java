package p2p;
import java.util.*;


public class UnitPeer {
    public int peerID;
    public String host_name;
    public int piece_num;
    public int port;
    public ArrayList<Integer> piece = new ArrayList<Integer>();
    public int dl_num = 0;
    
    /* set PeerID*/
    public UnitPeer() {
    	this.peerID = -1;
    	this.host_name = "";
    	this.piece_num = 0;
    }
    public UnitPeer(int PeerID, String name, String portnum, int size, boolean init_num)
    {
    	this.peerID = PeerID;
    	this.host_name = name;
    	this.piece_num = size;
    	this.port = Integer.valueOf(portnum);
    	if (init_num) {
    		for(int i = 0; i != (piece_num - 1) / 32; ++i) {
    			piece.add(0xFFFFFFFF);
    		}
    		int remain = (piece_num - 1) % 32;
    		int bit = 0;
    		for(int i = 0; i != remain + 1; ++i) {
    			bit |= 1 << (31 - i);
    		}
    		piece.add(bit);
    		System.out.println(piece_num);
    	}
    	else {
    		for(int i = 0; i != (piece_num - 1) / 32 + 1; ++i) {
    			piece.add(0);
    		}
    	}
    }
    /* Initially set the peer, showing it has the whole file or not*/
    public void initialize(int PeerID, String name, String portnum, int size, boolean init_num)
    {
    	this.peerID = PeerID;
    	this.host_name = name;
    	this.piece_num = size;
    	this.port = Integer.valueOf(portnum);
    	if (init_num) {
    		for(int i = 0; i != (piece_num - 1) / 32; ++i) {
    			piece.add(0xFFFFFFFF);
    		}
    		int remain = (piece_num - 1) % 32;
    		int bit = 0;
    		for(int i = 0; i != remain + 1; ++i) {
    			bit |= 1 << (31 - i);
    		}
    		piece.add(bit);
    	}
    	else {
    		for(int i = 0; i != (piece_num - 1) / 32 + 1; ++i) {
    			piece.add(0);
    		}
    	}
    }
    
    /*Update peer piece, To set the 'num' piece to true, mean the peer has this piece now*/
    public void Update_Piece(int index)
    {
    	int i = index / 32;
    	int c = index % 32;
    	int p = piece.get(i);
    	if((p & 1 << (31 - c)) == 0) ++dl_num;
    	p = piece.get(i) | 1 << (31 - c);
    	piece.set(i, p);	
    	return;
    }
    
    public void Update_Piece(List<Integer> bitfield)
    {
    	for(int i = 0; i != piece.size(); ++i) {
    		int t = bitfield.get(i);
    		for(int j = 0; j != 8; ++j) {
    			int p = 1 << j;
    			if((p & t) != 0) dl_num++;
    		}
    		piece.set(i, t);
    	}
    	return;
    }
    
    //return true if host peer's bitfield contains parameter
    public Boolean Contain_Piece(List<Integer> Bitfield)
    {
    	for(int i = 0; i != piece.size(); ++i) {
    		int b = Bitfield.get(i);
    		int p = piece.get(i);
    		if(p != (b | p)) return false;
    	}
    	return true;
    }
    
    public void show() {
    	String res = "";
    	for(int i : piece) {
    		res += Index_f(i);
    	}
    	System.out.println("bitfield:" + res);
    }
    
    public void show(ArrayList<Integer> bitfield) {
    	String res = "";
    	for(int i : bitfield) {
    		res += Index_f(i);
    	}
//    	System.out.println("server bitfield:" + res);
    }
    
    public String Index_f(List<Integer> a) {
    	String res = "";
    	for(int i = 0; i != piece.size(); ++i) {
    		int p = a.get(i);
    		for(int j = 31; j != -1; --j) {
    			if((p >> j & 1) != 0) 
    				res += "1";
    			else res += "0";
    		}
    	}
    	return res;
    }
    public String Index_f(int a) {
    	String res = "";
    	for(int j = 31; j != -1; --j) {
    		if((a >> j & 1) != 0) 
    			res += "1";
    		else res += "0";
    	}
    	return res;
    }
    
    
    public ArrayList<Integer> Get_Miss_Piece(List<Integer> Bitfield, List<Integer> Request)
    {
    	ArrayList<Integer> Miss_Piece = new ArrayList<Integer>();
    	for(int i = 0; i != piece.size(); ++i) {
    		int b = Bitfield.get(i);
    		int p = piece.get(i);
    		p = p | Request.get(i);
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
    
    public byte[] Bitfield() {
    	byte[] res = new byte[piece.size() * 4];
		for(int i = 0; i != piece.size(); ++i) {
    		byte[] part = new byte[] {
    		        (byte) ((piece.get(i) >> 24) & 0xFF),  
    		        (byte) ((piece.get(i) >> 16) & 0xFF),     
    		        (byte) ((piece.get(i) >> 8) & 0xFF),     
    		        (byte) (piece.get(i) & 0xFF)  
    		    	};
		    System.arraycopy(part, 0, res, i * 4, 4); 
    	}
    	return res;
    }
    
    public Boolean check_file() {
    	for(int i = 0; i != (piece_num - 1) / 32; ++i)
			if(piece.get(i) != 0xFFFFFFFF) return false;
    	int remain = (piece_num - 1) % 32 + 1;
		int bit = 0;
		for(int i = 0; i != remain; ++i) {
			bit |= 1 << (31 - i);
		}
		if(piece.get(piece.size() - 1) != bit) return false;
		return true;
    }
}

