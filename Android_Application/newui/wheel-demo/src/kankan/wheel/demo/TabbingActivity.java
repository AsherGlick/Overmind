package kankan.wheel.demo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class TabbingActivity extends TabActivity {
	
	globalVarsApp appState;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		
		// Get Global variables
		appState = ((globalVarsApp)getApplicationContext());
		
		
		/** TabHost will have Tabs */
		TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
		tabHost.setup();
		
		Intent intent1 = new Intent().setClass(this, ComputerTab.class);
		Intent intent2 = new Intent().setClass(this, AndroidTab.class);
		
		Resources res = getResources();
		
		TabSpec tspec1 = tabHost.newTabSpec("First Tab");
		tspec1.setIndicator("Computer",res.getDrawable(R.drawable.ic_tab_computer)).setContent(intent1);
		tabHost.addTab(tspec1);
		TabSpec tspec2 = tabHost.newTabSpec("Second Tab");
		tspec2.setIndicator("Android",res.getDrawable(R.drawable.ic_tab_android)).setContent(intent2);
		tabHost.addTab(tspec2);
		
		tabHost.setCurrentTab(2);
		
		createConnection (appState.ipAddress, appState.portNumber);
	}
	  //////////////////////////////////////////////////////////////////////////////
    /////////////////// SOCKET COMMUNICATION FUNCTIONS (SHARED) //////////////////
   //////////////////////////////////////////////////////////////////////////////
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
    /****************************** CREATE CONNECTION *****************************\
    | This function creates a new connection to a socket                           |
    \******************************************************************************/
    public void createConnection (String ip, int port) {
    	try {
    		appState.socket = new Socket (ip,port);
    		appState.toServer = new DataOutputStream (appState.socket.getOutputStream() );
    	}
    	catch (IOException ex) {
    		Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
    	}
    	catch(Exception ex)
    	{
    		Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
    	}
    }
}