package kankan.wheel.demo;

import kankan.wheel.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import kankan.wheel.demo.DatePickerDialogWithTitle;

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

public class SchedulerActivity extends Activity {
    
	//the date info for today
	Calendar calendar;
	//the date info for the currently selected day
	Calendar setCalendar;
	Calendar oldSetCalendar;

    static final int DATE_DIALOG_ID = 0;
    
    DatePickerDialogWithTitle datePickDialog;
    
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
    private DatePickerDialogWithTitle.OnDateSetListener mDateSetListener =
            new DatePickerDialogWithTitle.OnDateSetListener() {
    			
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
            datePickDialog = new DatePickerDialogWithTitle(this,
                        mDateSetListener,
                        setCalendar.get(Calendar.YEAR), setCalendar.get(Calendar.MONTH)-1, setCalendar.get(Calendar.DATE) ); //DATE is synonymous with DAY_OF_MONTH
            
            return datePickDialog;
        }
        return null;
    }
    
    public void dateDialog(View view) {
    	
    	//show date picker dialog box 
    	showDialog(DATE_DIALOG_ID);
    	//update date picker dialog to account for recent changes in date when the user uses the next/previous day arrow buttons
    	datePickDialog.updateDate(setCalendar.get(Calendar.YEAR), setCalendar.get(Calendar.MONTH)-1, setCalendar.get(Calendar.DATE));
    	
    	return;
    }
    
    //switchToScheduler sets the content view to the scheduler view
    public void switchToScheduler(View view) {
        //switch content view to scheduler_layout.xml
    	setContentView(R.layout.scheduler_layout);
    	
    	calendar = GregorianCalendar.getInstance();
    	//setCalendar initially set to same date, this is only done when you initialize the scheduler
    	setCalendar = GregorianCalendar.getInstance();
    	setDate(true);
    	
    	
    	 //begin time wheel code
		final WheelView hoursStart = (WheelView) findViewById(R.id.hoursStart);
		//create time wheel listing numbers 1-12 with font size 22
		hoursStart.setViewAdapter(new NumericWheelAdapter(this, 1, 12, 22, "%02d"));
		hoursStart.setCyclic(true);
	
		final WheelView minsStart = (WheelView) findViewById(R.id.minsStart);
		//create time wheel listing :00 :15 :30 :45 :00
		ArrayWheelAdapter<String> minsStartAdapter =
            new ArrayWheelAdapter<String>(this, new String[] {"00", "10", "20", "30", "40", "50"});
		minsStartAdapter.setItemResource(R.layout.wheel_text_item);
		minsStartAdapter.setItemTextResource(R.id.text);
		minsStart.setViewAdapter(minsStartAdapter);
	
        final WheelView ampmStart = (WheelView) findViewById(R.id.ampmStart);
        //create wheel listing "AM" and "PM" - (you can change font size in wheel_text_item.xml)
        ArrayWheelAdapter<String> ampmStartAdapter =
            new ArrayWheelAdapter<String>(this, new String[] {"AM", "PM"});
        ampmStartAdapter.setItemResource(R.layout.wheel_text_item);
        ampmStartAdapter.setItemTextResource(R.id.text);
        ampmStart.setViewAdapter(ampmStartAdapter);
        
        
        
        
        //begin time wheel code
		final WheelView hoursEnd = (WheelView) findViewById(R.id.hoursEnd);
		//create time wheel listing numbers 1-12 with font size 24
		hoursEnd.setViewAdapter(new NumericWheelAdapter(this, 1, 12, 24, "%02d"));
		hoursEnd.setCyclic(true);
		
		final WheelView minsEnd = (WheelView) findViewById(R.id.minsEnd);
		//create time wheel listing :00 :15 :30 :45 :00
		ArrayWheelAdapter<String> minsEndAdapter =
            new ArrayWheelAdapter<String>(this, new String[] {"00", "10", "20", "30", "40", "50"});
		minsEndAdapter.setItemResource(R.layout.wheel_text_item);
		minsEndAdapter.setItemTextResource(R.id.text);
		minsEnd.setViewAdapter(minsEndAdapter);
	
        final WheelView ampmEnd = (WheelView) findViewById(R.id.ampmEnd);
        //create wheel listing "AM" and "PM" - (you can change font size in wheel_text_item.xml)
        ArrayWheelAdapter<String> ampmEndAdapter =
            new ArrayWheelAdapter<String>(this, new String[] {"AM", "PM"});
        ampmEndAdapter.setItemResource(R.layout.wheel_text_item);
        ampmEndAdapter.setItemTextResource(R.id.text);
        ampmEnd.setViewAdapter(ampmEndAdapter);

        
		
		// get current time
		int curHours = calendar.get(Calendar.HOUR_OF_DAY);
		int curMinutes = calendar.get(Calendar.MINUTE);
		int curAmpm = calendar.get(Calendar.AM_PM);	
		
		//set the start hour to the current hour (subtract 1 as 0 is first in list)
		hoursStart.setCurrentItem(curHours-1);
		//set the end hour to one hour from now (don't -1 as we're adding 1)
		hoursEnd.setCurrentItem(curHours);
		
		//set the current am / pm
		ampmStart.setCurrentItem(curAmpm);
		//if its 11, adding 1 hour will switch am to pm / pm to am
		if(curHours == 11)
		{
			//switch to opposite of current am/pm
			//we can always add 1 because the list uses % for the index (ie. 2 becomes 0)
			ampmEnd.setCurrentItem(curAmpm+1);
		}
		else
		{
			ampmEnd.setCurrentItem(curAmpm);
		}
		
		//set the start/end minutes to the most recently past 15 minute mark
		if(curMinutes < 10)
		{
			minsStart.setCurrentItem(0); // 00
			minsEnd.setCurrentItem(0); // 00
		}
		else if(curMinutes < 20)
		{
			minsStart.setCurrentItem(1); // 10
			minsEnd.setCurrentItem(1); // 10
		}
		else if(curMinutes < 30)
		{
			minsStart.setCurrentItem(2); // 20
			minsEnd.setCurrentItem(2); // 20
		}
		else if(curMinutes < 40)
		{
			minsStart.setCurrentItem(3); // 30
			minsEnd.setCurrentItem(3); // 30
		}
		else if(curMinutes < 50)
		{
			minsStart.setCurrentItem(4); // 40
			minsEnd.setCurrentItem(4); // 40
		}
		else if(curMinutes < 60)
		{
			minsStart.setCurrentItem(5); // 50
			minsEnd.setCurrentItem(5); // 50
		}
	
		
		// add listeners for when the wheel changes
		addChangingListener(minsStart, "min");
		addChangingListener(hoursStart, "hour");
	
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
		hoursStart.addChangingListener(wheelListener);
		minsStart.addChangingListener(wheelListener);
		
		//adds a listener to clicking on the wheel
		OnWheelClickedListener click = new OnWheelClickedListener() {
            public void onItemClicked(WheelView wheel, int itemIndex) {
                wheel.setCurrentItem(itemIndex, true);
            }
        };
        hoursStart.addClickingListener(click);
        minsStart.addClickingListener(click);

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
		
		hoursStart.addScrollingListener(scrollListener);
		minsStart.addScrollingListener(scrollListener);
		
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