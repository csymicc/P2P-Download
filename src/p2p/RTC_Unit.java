package p2p;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RTC_Unit {					//Rate Table Class Unit
	public int rate_per;						//Total_Rate/Total_Time
	public int number;
	public boolean Pref_Flag;
	public boolean Optimal_Flag;
	public int Total_Rate;						//Data Transmitted
	public long Total_Time;						//Total time cost for Transmiting data			
	public long Last_Time;
	public boolean Rate_Test_Send_Flag_RTCU;
	public boolean Rate_Test_Rece_Flag_RTCU;
	public Queue <Integer> Piece_Queue;
	public DataOutputStream out;
	public DataInputStream in;
	public int[] speed_test={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int array_index=0;
	public boolean End_Flag = false;
	public boolean Finish_Flag = false;
	public boolean Time_Out_Flag = false;
	
	RTC_Unit(int number, int rate_per, int PieceNumber, DataOutputStream out, DataInputStream in)
	{
		this.number=number;
		this.rate_per=rate_per;
		this.Pref_Flag = false;
		this.Optimal_Flag = false;
		Total_Rate = 0;
//		Total_Time = 0;
		Rate_Test_Send_Flag_RTCU = false;
		Rate_Test_Rece_Flag_RTCU = false;
		Piece_Queue = new LinkedBlockingQueue <Integer> (PieceNumber);
		this.out = out;
		this.in = in;
		Last_Time = 0;
		End_Flag = false;
		Finish_Flag = false;
		Time_Out_Flag = false;
	}
	
	public int avg_speed()
	{
		int sum=0,length=0;
		for (int unit:this.speed_test)
		{
			if (unit!=-1)
			{
				sum+=unit;
				length++;
			}
		}
		if (length==0)
			rate_per=0;
		else
		    rate_per = sum/length;
		return rate_per;
	}
	
	public void insert(int i)
	{
		this.speed_test[this.array_index]=i;
		if (array_index<speed_test.length-1)
		   this.array_index++;
		else
		   this.array_index=0;
	}
	
	public void print()
	{
		System.out.print("Speed list is");
		for(int unit:this.speed_test)
			System.out.print(unit+"    ");
		System.out.println(" ");
	}
	
//	public void Reset_Average_Rate()
//	{
//		rate_per = Integer.MAX_VALUE;
//	}
}