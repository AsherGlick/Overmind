package kankan.wheel.demo;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class SecondTab extends ListActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list_activity);

		ArrayList<String> powerpoints = fillFileList();
        
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.file_row, powerpoints);
        setListAdapter(fileList);
		
		
	}
	
	private ArrayList<String> fillFileList()
    {
    	//fill compPowerpoints with an ArrayList of file names on the computer
    	ArrayList<String> comp = getPptsOnComputer();
    	
        return comp;
    }

	private ArrayList<String> getPptsOnComputer()
    {
		ArrayList<String> fakeList = new ArrayList<String>();	
		fakeList.add("derpSauce.ppt");
		fakeList.add("alrightyThen.ppt");
		return fakeList;
    }
}