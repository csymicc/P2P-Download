package p2p;

import java.io.IOException;

public class RTC_Unit {					//Rate Table Class Unit
	int rate_per;
	int number;
	public boolean Pref_Flag;
	
	RTC_Unit(int number, int rate_per)
	{
		this.number=number;
		this.rate_per=rate_per;
		this.Pref_Flag = false;
	}
}