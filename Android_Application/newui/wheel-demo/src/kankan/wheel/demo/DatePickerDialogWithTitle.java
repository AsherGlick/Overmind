package kankan.wheel.demo;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.widget.DatePicker;


class DatePickerDialogWithTitle extends android.app.DatePickerDialog {
    public DatePickerDialogWithTitle(Context context, OnDateSetListener callBack,
            int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
        updateTitle(year, monthOfYear, dayOfMonth);
    }
    public void onDateChanged(DatePicker view, int year,
            int month, int day) {
        updateTitle(year, month, day);
    }
    private void updateTitle(int year, int month, int day) {
        //make a new calendar with the param date
    	//month+1 because it is sent as -1 (due to difference between using 0-11 and 1-12)
    	Calendar mCalendar = new GregorianCalendar(year, month+1, day);
        //set the date picker dialog title to the day of the week
        String titleText = getDayName(mCalendar.get(Calendar.DAY_OF_WEEK));
        
        setTitle(titleText);
    }
    //returns day name for corresponding number
    public String getDayName(int d)
    {
    	String dayOfWeek = "";
    	switch (d)
		{
    	    case 1:  dayOfWeek = "Sunday";          break;
            case 2:  dayOfWeek = "Monday";          break;
            case 3:  dayOfWeek = "Tuesday";         break;
            case 4:  dayOfWeek = "Wednesday";       break;
            case 5:  dayOfWeek = "Thursday";        break;
            case 6:  dayOfWeek = "Friday";          break;
            case 7:  dayOfWeek = "Saturday";        break;
		}
    	return dayOfWeek;
    }
    
    //returns month name for corresponding number (1-12)
    public String getMonthName(int m)
    {
    	String monthString="";
    	switch (m) {
        case 1:  monthString = "Jan.";       break;
        case 2:  monthString = "Feb.";      break;
        case 3:  monthString = "Mar.";         break;
        case 4:  monthString = "Apr.";         break;
        case 5:  monthString = "May";           break;
        case 6:  monthString = "Jun.";          break;
        case 7:  monthString = "Jul.";          break;
        case 8:  monthString = "Aug.";        break;
        case 9:  monthString = "Sept.";     break;
        case 10: monthString = "Oct.";       break;
        case 11: monthString = "Nov.";      break;
        case 12: monthString = "Dec.";      break;
        default: monthString = "Invalid month"; break;
    	}
    	return monthString;
    }
}
