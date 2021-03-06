package kankan.wheel.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	globalVarsApp appState;
	 /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the display
        setContentView(R.layout.login_layout);
        appState = ((globalVarsApp)getApplicationContext());
    }
    
    /************************************ LOGIN ***********************************\
    | This function saves the user name and password to the global saved variable  |
    | and then calls the QR Code reader                                            |
    \******************************************************************************/
    public void login(View view) {
    	appState.username = ((EditText) findViewById(R.id.username)).getText().toString();
    	appState.password = ((EditText) findViewById(R.id.editText2)).getText().toString();
    	
    	// call the QR code reader
    	IntentIntegrator integrator = new IntentIntegrator(LoginActivity.this);
    	integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
    }
    
    /***************************** ON ACTIVITY RESULT *****************************\
    | This function handles the response of the QR Code activity and parses the    |
    | resulting string to decide what to do                                        |
    \******************************************************************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	IntentResult scanResult;
    	
    	  scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	
    	  
    	  if (scanResult != null) 
    	  {
    		  String contents = "";
    		  contents = scanResult.getContents();
    		  if (contents == null) {
    			  Toast.makeText(this, "Did not scan an Overmind QR code", Toast.LENGTH_LONG).show();
    			  return;
    		  }
    		  int qrVersion = 0;
    		  int i;
    		  i = contents.indexOf('#');
    		  if ( i != -1){
    			  contents = contents.substring(i+1);
    		  }
    		  else {
    			  return;
    		  }
    		  if (contents.substring(0,6).equals("OVRMND")) {
    			  i = contents.indexOf(':');
    			  if ( i != -1 ){
    				  qrVersion = Integer.parseInt(contents.substring(6, i));
    				  contents = contents.substring(i+1);
    				  switch (qrVersion) {
    				  case 1:
    					  parseQRVersionOne(contents);
    					  return;
    				  default:
    					  Toast.makeText(this, "This QR Code is a newer version then this application can handle", Toast.LENGTH_LONG).show();
    					  return;
    				  }
    			  }
    			  else {
    				  Toast.makeText(this, "This is not a valid Overmind QR code", Toast.LENGTH_LONG).show();
    				  return;
    			  }
    		  }
    		  else {
    			  Toast.makeText(this, "This is not a valid Overmind QR code", Toast.LENGTH_LONG).show();
    			  return;
    			  //program crashes
    		  }
    		  //contents = "QR Version: " + qrVersion ;
    		  //Toast.makeText(this, contents, Toast.LENGTH_LONG).show();
    	  }
    	  else
    	  {
    		  Toast.makeText(this, "something went wrong there pall", Toast.LENGTH_LONG).show();
    	  }
    } 
    
    /**************************** PARSE QR VERSION ONE ****************************\
    | This is the first generation of QR code parsing. It will parse the ipAddress |
    | the port number, the next activity to launch, and the extraData to send to   |
    | the server                                                                   |
    \******************************************************************************/
    public void parseQRVersionOne(String contents) {
    	// sample qr code text
	// http://projectovermind.com/getreader#OVRMND1:128.113.140.78:80:1:sage3202
    	// example of code passed to the function
    	// 128.113.140.78:80:1:sage3202
    	String ipAddress = "0.0.0.0";
    	int port = 0;
    	int displayStyle = 0;
    	String extraData = "";
    	int i = contents.indexOf(':');
    	if (i != -1){
    		ipAddress = contents.substring(0,i);
    		contents = contents.substring(i+1);
    	}
    	i = contents.indexOf(':');
    	if (i != -1){
    		port = Integer.parseInt(contents.substring(0,i));
    		contents = contents.substring(i+1);
    	}
    	i = contents.indexOf(':');
    	if (i != -1){
    		displayStyle = Integer.parseInt(contents.substring(0,i));
    		contents = contents.substring(i+1);
    	}
  		extraData = contents;
  		// set data to global variables
  		appState.ipAddress = ipAddress;
  		appState.extraData = extraData;
  		appState.portNumber = port;
  		
  		Toast.makeText(this, ipAddress + port ,Toast.LENGTH_LONG).show();
  		
  		switch (displayStyle){
  		case 1:
  			startActivity(new Intent(this, TabbingActivity.class));
  			break;
  		case 2:
  			startActivity(new Intent(this, SchedulerActivity.class));
  			break;
  		default:
  			Toast.makeText(this, "The QR code is malformed",Toast.LENGTH_LONG).show();
  		}
    }
	
}
