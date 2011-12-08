package kankan.wheel.demo;
//import java.io.BufferedReader;//to implement later
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import kankan.wheel.demo.R;
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
	
	String selectedFileName;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ppt_switch);

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
	//private BufferedReader fromServer;// to implement later
	private Socket socket;
	/********************************** SEND DATA *********************************\
	| This function sends the data over the opened socket's data inputstream       |
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
    
    /***************************** FILL LIST COMPUTER *****************************\
    | this fills the list of files from the computer                               |
    \******************************************************************************/
    public void fillListComputer(View v)
    {
    	combinedList.clear();
    	combinedList.addAll(getPptsOnComputer());
    	adapter.notifyDataSetChanged();
    	    	
    	return;    	
    }
    
    /********************************* REMOVE LIST ********************************\
    | clears the entire list                                                       |
    \******************************************************************************/
    public void removeList(ArrayList<String> rem)
    {
    	combinedList.removeAll(rem);
    }
    /************************* GET POWERPOINTS ON COMPUTER ************************\
    | F	inds the list of powerpoints that are on the computer, currently this       |
    | function does not communicate with the computer so it fills the list with    |
    | fake files                                                                   |
    \******************************************************************************/
	public ArrayList<String> getPptsOnComputer()
    {
		ArrayList<String> fakeList = new ArrayList<String>();	
		fakeList.add("test.pptx");
		fakeList.add("placeholder.ppt");
		return fakeList;
    }
	
}
