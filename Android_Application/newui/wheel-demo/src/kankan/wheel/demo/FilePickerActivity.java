package kankan.wheel.demo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import kankan.wheel.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FilePickerActivity extends ListActivity {
	
	ArrayList<String> combinedList = new ArrayList<String>();
	ArrayList<String> compPowerpoints = new ArrayList<String>();
	ArrayList<String> sdPowerpoints = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	
	globalVarsApp appState;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_pick);

        appState = ((globalVarsApp)getApplicationContext());
        
        combinedList.clear();

        adapter = new ArrayAdapter<String>(this,
			R.layout.file_row, combinedList);
		setListAdapter(adapter);
        
		combinedList.addAll(getPptsOnComputer());        
    }
     //////////////////////////////////////////////////////////////////////////////
    /////////////////// SOCKET COMMUNICATION FUNCTIONS (SHARED) //////////////////
   //////////////////////////////////////////////////////////////////////////////
	private DataOutputStream toServer;
	private BufferedReader fromServer;
	private Socket socket;
	/********************************** SEND DATA *********************************\
	| This function sends the data 
	\******************************************************************************/
    public void sendData(String data) {
    	try 
    	{
    		toServer.writeBytes(data);
    		toServer.flush();
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
    /****************************** CREATE CONNECTION *****************************\
    | This function creates a new connection to a socket                           |
    \******************************************************************************/
    public void createConnection (String ip, int port) {
    	try {
    		socket = new Socket (ip,port);
    		toServer = new DataOutputStream ( socket.getOutputStream() );
    	}
    	catch (IOException ex) {
    		Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
    	}
    	catch(Exception ex)
    	{
    		Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
    	}
    }
     //////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// PICK FILE FUNCTIONS ////////////////////////////
   //////////////////////////////////////////////////////////////////////////////
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id)
    {
    	
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		
		createConnection(appState.ipAddress,appState.portNumber);
		
		setContentView(R.layout.ppt_switch);
	}
    
    public void fillListComputer(View v)
    {
    	combinedList.clear();
    	combinedList.addAll(getPptsOnComputer());
    	adapter.notifyDataSetChanged();
    	    	
    	return;    	
    }
    /****************************** FILL LIST ANDROID *****************************\
    | This function fill the list of power-point functions with those that are     |
    | found on the SD card in the downloads folder of the user.                    |
    \******************************************************************************/
    public void fillListAndroid(View v)
    {
    	combinedList.clear();
    	String d = ((Environment.getExternalStorageDirectory().toString() ) + "/Download");
    	combinedList.addAll( (new SDReader(d)).returnFiles() );
    	adapter.notifyDataSetChanged();
    	
    	return;
    }
    public void removeList(ArrayList<String> rem)
    {
    	combinedList.removeAll(rem);
    }
	public ArrayList<String> getPptsOnComputer()
    {
		ArrayList<String> fakeList = new ArrayList<String>();	
		fakeList.add("windowsPresentation.ppt");
		fakeList.add("presentationOkay.ppt");
		return fakeList;
    }
	  //////////////////////////////////////////////////////////////////////////////
	 ///////////////////////// CONTROL SLIDESHOW FUNCTIONS ////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	public void prevSlide(View view)
    {
    	sendData("PREV");
    }
        //sends the message to progress slideshow right
    public void nextSlide(View view)
    {
    	sendData("NEXT");
    }
        //sends the message to fullscreen
    public void fullScreen(View view)
    {
    	sendData("FULL");
    }
        //sends the message to close slideshow
    public void closePowerpoint(View view)
    {
    	sendData("SHUT");
    }
}