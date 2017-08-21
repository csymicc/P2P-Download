package p2p;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import p2p.RTC_Unit;

public class RTC {								//Rate Table Class
	 public ArrayList <RTC_Unit> RateTable;
	 public comparator COMP; 

	 
	 public class comparator implements Comparator{
		 int rtn;
		 RTC_Unit T1;
		 RTC_Unit T2;
		 public int compare(Object obj1, Object obj2)
		 {
			 T1=(RTC_Unit)obj1;
			 T2=(RTC_Unit)obj2;
			 rtn=new Double(T1.rate_per).compareTo(new Double (T2.rate_per));
			 return rtn;
		 }
	 } 
	 public RTC ()
	 {
		 this.RateTable = new ArrayList <RTC_Unit> ();
		 this.COMP = new comparator();
	 }
	 
	 public int TableSize()
	 {
		 return RateTable.size();
	 }
	 
	 public void UpDate_Pref(int i, boolean B)
	 {
		 RateTable.get(i).Pref_Flag = B; 
	 }
	 
	 public void GetIndex(int i)
	 {
		 
		 
	 }
	 
	 public void Print_Array()
	 {
		 for(int i=0;i<RateTable.size();i++)
		 {
			 System.out.println(RateTable.get(i).number+ " " + RateTable.get(i).Pref_Flag + " " + RateTable.get(i).rate_per);
		 }
	 }
	 
	 public void Sort()
	 {	 
		 Collections.sort(this.RateTable,this.COMP);
	 }
	 
	 public void Add_RTC_Unit(RTC_Unit RTCU)
	 {
		 RateTable.add(RTCU);
	 }
	 
}