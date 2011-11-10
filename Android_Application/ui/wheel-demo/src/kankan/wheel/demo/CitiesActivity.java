package kankan.wheel.demo;

import kankan.wheel.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
//import android.content.Context;
//import android.widget.ArrayAdapter;
import android.widget.DatePicker;
//import android.widget.GridView;
import android.widget.TextView;
import android.widget.TimePicker;

public class CitiesActivity extends Activity {
    
	//the date info for today
	Calendar calendar;
	//the date info for the currently selected day
	Calendar setCalendar;
	Calendar oldSetCalendar;

    static final int DATE_DIALOG_ID = 0;
    
    DatePickerDialog datePickDialog;
    
	// Time changed flag
	private boolean timeChanged = false;
	
	// Time scrolled flag
	private boolean timeScrolled = false;

    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.middleman);
        
    }
    
  /*  public void onDateSet(DatePicker view, int year, 
                                  int monthOfYear, int dayOfMonth) {
            	
                oldSetCalendar = setCalendar;
                setCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth, 0, 0);
                setDate(false);
            }
   */
    
    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
    			
    			//once a date is set from the date dialog box..
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
                {	
                	//save the old selected date
                	oldSetCalendar = setCalendar;
                	//set the calendar according date selected
                    setCalendar = new GregorianCalendar(year, monthOfYear+1, dayOfMonth, 0, 0);
                    //set the displays to the new date - not initial set so false
                    setDate(false);
                }
            };
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
        	//create a new date picker dialog box; month-1 due to java Calendar using 1-12 and android Calendar using 0-11
            datePickDialog = new DatePickerDialog(this,
                        mDateSetListener,
                        setCalendar.get(Calendar.YEAR), setCalendar.get(Calendar.MONTH)-1, setCalendar.get(Calendar.DAY_OF_MONTH));
            return datePickDialog;
        }
        return null;
    }
    
    public void dateDialog(View view) {
    	
    	String titleText = getMonthName(setCalendar.get(Calendar.MONTH)) + " " + 
    		Integer.toString(setCalendar.get(Calendar.DAY_OF_MONTH)) + ", " + Integer.toString(setCalendar.get(Calendar.YEAR)) + "";
    	
    	//show dialog box and set initial title
    	showDialog(DATE_DIALOG_ID);
    	datePickDialog.setTitle(titleText);
    	
    	return;
    }
    
    //switchToScheduler sets the content view to the scheduler view
    public void switchToScheduler(View view) {
        //switch content view to cities_layout.xml
    	setContentView(R.layout.cities_layout);
    	
    	calendar = GregorianCalendar.getInstance();
    	//setCalendar initially set to same date, this is only done when you initialize the scheduler
    	setCalendar = GregorianCalendar.getInstance();
    	setDate(true);
    	
    	
    	 //begin time wheel code
		final WheelView hours = (WheelView) findViewById(R.id.hour);
		//create time wheel listing numbers 1-12
		hours.setViewAdapter(new NumericWheelAdapter(this, 1, 12));
	
		final WheelView mins = (WheelView) findViewById(R.id.mins);
		//create time wheel listing numbers 0-59
		mins.setViewAdapter(new NumericWheelAdapter(this, 0, 59, "%02d"));
		mins.setCyclic(true);
	
        final WheelView ampm = (WheelView) findViewById(R.id.ampm);
        //create wheel listing "AM" and "PM"
        ArrayWheelAdapter<String> ampmAdapter =
            new ArrayWheelAdapter<String>(this, new String[] {"AM", "PM"});
        ampmAdapter.setItemResource(R.layout.wheel_text_item);
        ampmAdapter.setItemTextResource(R.id.text);
        ampm.setViewAdapter(ampmAdapter);
        
        /*
        int mintime = 9;
        int maxtime = 5;
        final WheelView endhour = (WheelView) findViewById(R.id.endhour);
        
        int boundhours = 0;
        if(mintime < 12 && mintime > 0)
        {
        	boundhours = 12-mintime;
        }
        
        if(maxtime < 12 && maxtime > 0)
        {
        	boundhours += maxtime;
        }
        int[] numbers = new int[boundhours];
        numbers[0]=mintime;
        int count=1;
        for(int i = mintime+1; i < 12; i++)
        {
        	numbers[count]=i;
        	count++;
        }
        
        ArrayWheelAdapter<String> endhourAdapter =
            new ArrayWheelAdapter<String>(this, new String[] {"", "PM"});
        ampmAdapter.setItemResource(R.layout.wheel_text_item);
        ampmAdapter.setItemTextResource(R.id.text);
        ampm.setViewAdapter(ampmAdapter);
    */
		
		
			// set current time
		Calendar c = Calendar.getInstance();
		int curHours = c.get(Calendar.HOUR_OF_DAY);
		int curMinutes = c.get(Calendar.MINUTE);
	
		hours.setCurrentItem(curHours);
		mins.setCurrentItem(curMinutes);
	
	
		// add listeners for when the wheel changes
		addChangingListener(mins, "min");
		addChangingListener(hours, "hour");
	
		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!timeScrolled) {
					timeChanged = true;
					//picker.setCurrentHour(hours.getCurrentItem());
					//picker.setCurrentMinute(mins.getCurrentItem());
					timeChanged = false;
				}
			}
		};
		hours.addChangingListener(wheelListener);
		mins.addChangingListener(wheelListener);
		
		//adds a listener to clicking on the wheel
		OnWheelClickedListener click = new OnWheelClickedListener() {
            public void onItemClicked(WheelView wheel, int itemIndex) {
                wheel.setCurrentItem(itemIndex, true);
            }
        };
        hours.addClickingListener(click);
        mins.addClickingListener(click);

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				timeScrolled = true;
			}
			public void onScrollingFinished(WheelView wheel) {
				timeScrolled = false;
				timeChanged = true;
				//picker.setCurrentHour(hours.getCurrentItem());
				//picker.setCurrentMinute(mins.getCurrentItem());
				timeChanged = false;
			}
		};
		
		hours.addScrollingListener(scrollListener);
		mins.addScrollingListener(scrollListener);
		
		/*picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			public void onTimeChanged(TimePicker  view, int hourOfDay, int minute) {
				if (!timeChanged) {
					//hours.setCurrentItem(hourOfDay, true);
					//mins.setCurrentItem(minute, true);
				}
			}
		});*/
    	
    }
    
    //Preconditions:  the calendar for the affected date is already set/has already been adjusted
    //Postconditions: the whichDay text field is set
    public void setDate(boolean original)
    {
    	//tv is the day, ie. Monday Tuesday etc.
    	TextView tv = (TextView) findViewById(R.id.whichDay);
    	//currDate is the date, ie. Nov. 12, 2011
    	TextView currDate = (TextView) findViewById(R.id.selectedDate); 
    	//String day = (tv.getText()).toString();
    	
    	//c is the calendar we're using - ie. today (the initial) or the selected date
    	Calendar c = null;
    	
    	//original is whether or not we're setting the info for date for the first time
    	if(original)
    	{
    		//if it is the first time, use today's date
    		c = calendar;
    	}
    	else
    	{
    		//otherwise use the date thats currently selected
    		c = setCalendar;
    	}    	
    	
    	String dayOfWeek="Unknown";
    	switch (c.get(Calendar.DAY_OF_WEEK))
		{
    		case 1:  dayOfWeek = "Sunday";       	break;
            case 2:  dayOfWeek = "Monday";      	break;
            case 3:  dayOfWeek = "Tuesday";         break;
            case 4:  dayOfWeek = "Wednesday";       break;
            case 5:  dayOfWeek = "Thursday";        break;
            case 6:  dayOfWeek = "Friday";          break;
            case 7:  dayOfWeek = "Saturday";        break;
		}
    	
    	tv.setText(dayOfWeek);
    	
    	String monthString= getMonthName(c.get(Calendar.MONTH));
    	
    	String date = monthString + " " + Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + ", " + Integer.toString(c.get(Calendar.YEAR)) + "";
    	currDate.setText(date);
    
    	return;
    }
    //getMonthName takes an integer 1-12 and returns the corresponding month (string)
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
    
    //nextDay adds a day to the selected date and sets the display accordingly
    public void nextDay(View view)
    {
    	//select the next day
    	setCalendar.add(Calendar.DAY_OF_MONTH, 1);
    	//update the text fields - not initial so false
    	setDate(false);
    	
    	return;
    }
    
    //prevDay subtracts a day from the selected date and sets the display accordingly
    public void prevDay(View view)
    {
    	//select the previous day
    	setCalendar.add(Calendar.DAY_OF_MONTH, -1);
    	//update the texts fields - not initial so false
    	setDate(false);    	
    	
    	return;
    }
    
  
	 //Adds changing listener for wheel that updates the wheel label
	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				//wheel.setLabel(newValue != 1 ? label + "s" : label);
			}
		});
	}  
}