package kankan.wheel.demo;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PowerPointControlActivity extends Activity {
	globalVarsApp appState;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		 super.onCreate(savedInstanceState);
	     //set the display
	     setContentView(R.layout.ppt_switch);
	     appState = ((globalVarsApp)getApplicationContext());
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
