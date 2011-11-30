package overmind.demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class DemoActivity extends Activity {
	private Socket socket;
	private Button leftButton;
	private Button rightButton;
	private Button fullScreen;
	private Button close;
	private Button connect;
	private Button qr;
	private EditText ip;
	private EditText read;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        leftButton = (Button) findViewById(R.id.button1);
        rightButton = (Button) findViewById(R.id.button2);
        fullScreen = (Button) findViewById(R.id.button3);
        connect = (Button) findViewById(R.id.button4);
        close = (Button) findViewById(R.id.button5);
        ip = (EditText) findViewById(R.id.editText1);
        read = (EditText) findViewById(R.id.editText2);
        qr = (Button) findViewById(R.id.button6);
    }
    //called when scan button is called, calls the zxing application
    public void qrread(View view)
    {
    	IntentIntegrator integrator = new IntentIntegrator(DemoActivity.this);
    	integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
    }
    // this function parses and handles the first version of the QR code
    public void parseQRVersionOne(String contents) {
    	
    }
    //handles the message from zxing
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	  if (scanResult != null) 
    	  {
    		  String contents = scanResult.getContents();
    		  int qrVersion = 0;
    		  // sample qr code text
    		  // http://projectovermind.com/getreader#OVRMND1:128.113.140.78:80:1:sage3202
    		  int i;
    		  i = contents.indexOf('#');
    		  if ( i != -1){
    			  contents = contents.substring(i+1);
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
    		  contents = "QR Version: " + qrVersion ;
    		  Toast.makeText(this, contents, Toast.LENGTH_LONG).show();
    	  }
    	  else
    	  {
    		  Toast.makeText(this, "something went wrong there pall", Toast.LENGTH_LONG).show();
    	  }
    	  } 
    //sends the message to progress slideshow left
    public void left(View view)
    {
    	try
    	{
    		toServer.writeBytes("PREV");
    		toServer.flush();
    	}
    	catch(IOException ex)
    	{
    		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
    	}
    }
        //sends the message to progress slideshow right
    public void right(View view)
    {
    	try
    	{
    		toServer.writeBytes("NEXT");
    		toServer.flush();
    	}
    	catch(IOException ex)
    	{
    		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
    	}
    }
        //sends the message to fullscreen
    public void fullscreen(View view)
    {
    	try
    	{
    		toServer.writeBytes("FULL");
    		toServer.flush();
    	}
    	catch(IOException ex)
    	{
    		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
    	}
    }
        //sends the message to close slideshow
    public void close(View view)
    {
    	try
    	{
    		toServer.writeBytes("SHUT");
    		toServer.flush();
    	}
    	catch(IOException ex)
    	{
    		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
    	}
    }
        //sends the message to connect to specified server
    public void connect(View view)
    {
    	try
        {
    		String iptext = ip.getText().toString();	
        	socket = new Socket( iptext, 8080 );     	
            toServer = new DataOutputStream( socket.getOutputStream() );
            Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
        }
        catch(IOException ex)
        {
        	Toast.makeText(this, "Failed ", Toast.LENGTH_LONG).show();
        	Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }	
    }
    
    public void getScheduleByRoom(String roomid)
    {
    	String temp="ROOMSCH"+roomid;
    	try
    	{
    		toServer.writeBytes(temp);
    		toServer.flush();
    	}
    	catch(IOException ex)
    	{
    		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
    	}
    }
    
    public void getScheudleByDate(String roomid, String date)
    {
    	try
    	{
    		toServer.writeBytes("");
    		toServer.flush();
    	}
    	catch(IOException ex)
    	{
    		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
    	}
    }
    
    public void getMySchedules(String userid)
    {
    	try
    	{
    		toServer.writeBytes("");
    		toServer.flush();
    	}
    	catch(IOException ex)
    	{
    		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
    	}
    }
    
    public void reserveRoom(String roomid, String userid, String timeStart, String timeEnd)
    {
    	try
    	{
    		toServer.writeBytes("");
    		toServer.flush();
    	}
    	catch(IOException ex)
    	{
    		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
    	}
    }
    
    public void deleteReservation(String roomid, String userid, String timeStart, String timeEnd)
    {
    	try
    	{
    		toServer.writeBytes("");
    		toServer.flush();
    	}
    	catch(IOException ex)
    	{
    		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
    	}
    }
    
}