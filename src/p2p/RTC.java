package p2p;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import p2p.RTC_Unit;

public class RTC {								//Rate Table Class
	 public ArrayList <RTC_Unit> RateTable;
	 public comparator COMP; 
	 
	 
	 public class comparator implements Comparator<Object>{
		 int rtn;
		 RTC_Unit T1;
		 RTC_Unit T2;
		 public int compare(Object obj1, Object obj2)
		 {
			 T1=(RTC_Unit)obj1;
			 T2=(RTC_Unit)obj2;
			 rtn = new Integer(T1.rate_per).compareTo(new Integer(T2.rate_per));
			 return  rtn;							//Descending order
		 }
	 } 
	 
	 public void Set_Rate()
	 {
		 for(int i=0;i<RateTable.size();i++)
		 {
			 if(RateTable.get(i).Total_Rate<3)
			 {
				 RateTable.get(i).Time_Out_Flag = true;
			 }
//			 if(RateTable.get(i).Pref_Flag == true || RateTable.get(i).Optimal_Flag == true)
//			 {
//				 RateTable.get(i).rate_per = RateTable.get(i).Total_Rate;				 
//			 }
			 RateTable.get(i).Total_Rate = 0;
//			 System.out.println(RateTable.get(i).rate_per);
		 } 
	 }
	 
	 public boolean Check()
	 {
		 for(int i=0;i<RateTable.size();i++)
		 {
			 if(RateTable.get(i).Rate_Test_Rece_Flag_RTCU == false)
			 {
				 return false;
			 }
		 }
		 return true;
	 }
	 
	 public void Set_Rate_Flag()
	 {
		 for(int i=0; i<RateTable.size();i++)
		 {
			 RateTable.get(i).Rate_Test_Send_Flag_RTCU = true;
		 }
	 }
	 
	 public void Clear_Rate_Flag()
	 {
		 for(int i=0;i<RateTable.size();i++)
		 {
			 RateTable.get(i).Rate_Test_Rece_Flag_RTCU = false;
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
	 
	 public void Print_Array()
	 {
		 for(int i=0;i<RateTable.size();i++)
		 {
			 System.out.println("The Index of the number  " +RateTable.get(i).number+ " " + RateTable.get(i).Pref_Flag + " Average Speed is " + RateTable.get(i).rate_per);
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