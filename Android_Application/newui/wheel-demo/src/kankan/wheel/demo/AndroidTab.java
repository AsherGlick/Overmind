package kankan.wheel.demo;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AndroidTab extends ListActivity {
	
	globalVarsApp appState;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list_activity);

		appState = ((globalVarsApp)getApplicationContext());
		
		ArrayList<String> powerpoints = fillListAndroid();
        
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.file_row, powerpoints);
        setListAdapter(fileList);
		
		
	}
	
	/****************************** FILL LIST ANDROID *****************************\
    | This function fill the list of power-point functions with those that are     |
    | found on the SD card in the downloads folder of the user.                    |
    \******************************************************************************/
    public ArrayList<String> fillListAndroid()
    {
    	ArrayList<String> fileList = new ArrayList<String>();
    	String d = ((Environment.getExternalStorageDirectory().toString() ) + "/Download");
    	fileList.addAll( (new SDReader(d)).returnFiles() );
    	return fileList;
    }
    
    /***************************** ON LIST ITEM CLICK *****************************\
    | Listener function to detect when a file is selected                          |
    \******************************************************************************/
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id)
    {
    	
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		
		sendData("OPEN|"+item);
		
		//setContentView(R.layout.ppt_switch);
		startActivity(new Intent(this, PowerPointControlActivity.class));
	}
    /********************************** SEND DATA *********************************\
	| This function sends the data over the opened socket's data inputstream       |
	\******************************************************************************/
    public void sendData(String data) {
    	try 
    	{
    		appState.toServer.writeBytes(data);
    		appState.toServer.flush();
    	}
    	catch(IOException ex)
    	{
    		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
    	}
    	catch(Exception ex)
    	{
    		Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
    	}
    }
}