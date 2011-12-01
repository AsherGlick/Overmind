package overmind.newproj;

public class Schedule 
{
	private String id;
	private String timeStart;
	private String timeEnd;
	public Schedule(String newid, String newtimeStart, String newtimeEnd)
	{
		id = newid;
		timeStart = newtimeStart;
		timeEnd = newtimeEnd;
	}
	//time formatted as such: "0y1y2-3m4m5-6d7d8 9h10h11-12m13m14-15s16s"
	public String getDate()
	{
		return timeStart.substring(0, 7);
	}
	public String getStartTime()
	{
		return timeStart.substring(9, 13);
	}
	public String getEndTime()
	{
		return timeEnd.substring(9, 13);
	}
	public String getId()
	{
		return id;
	}
	
}
