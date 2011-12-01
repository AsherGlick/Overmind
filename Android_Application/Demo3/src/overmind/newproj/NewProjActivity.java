package overmind.newproj;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
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

public class NewProjActivity extends Activity {
	private Socket socket;	//socket for server communication
	private Socket socket2;	//socket for client communication
	private Button leftButton;
	private Button rightButton;
	private Button fullScreen;
	private Button close;
	private Button connect;
	private Button qr;
	private EditText ip;
	//private EditText read;
	private DataOutputStream toServer;
	private BufferedReader fromServer;
	private DataOutputStream toClient;
	private List<Schedule> schedules;
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
        //read = (EditText) findViewById(R.id.editText2);
        qr = (Button) findViewById(R.id.button6);
    }
    //called when scan button is called, calls the zxing application
    public void qrread(View view)
    {
    	IntentIntegrator integrator = new IntentIntegrator(NewProjActivity.this);
    	integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
    }
    // this function parses and handles the first version of the QR code
    public void parseQRVersionOne(String contents) {
    	// sample qr code text
		// http://projectovermind.com/getreader#OVRMND1:128.113.140.78:80:1:sage3202
    	// example of code passed to the function
    	// 128.113.140.78:80:1:sage3202
    	String ipAddress = "0.0.0.0";
    	int port = 0;
    	String displayStyle = "0";
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
    		displayStyle = contents.substring(0,i);
    		contents = contents.substring(i+1);
    	}
  		extraData = contents;
  		String outputMessage = "Connecting to " + ipAddress + ":" + port;
  		Toast.makeText(this, outputMessage, Toast.LENGTH_LONG).show();
  		try
        {	
        	createConnection(ipAddress,port);
            Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
        }
        catch(IOException ex)
        {
        	Toast.makeText(this, "Failed ", Toast.LENGTH_LONG).show();
        	Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        	return;
        }	
    	catch(Exception ex)
    	{
    		Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
    		return;
    	}
  		// send version number as message
  		sendData("VER"+1+"VER");
  		// send extraData as message
  		sendData(extraData);
  		// get results, call function on results
  		
  		// switch views
  		
    }
    //Send data to server
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
    public String getData()
    {
    	try
    	{
    		return fromServer.readLine();
    	}
    	catch(IOException ex)
    	{
    		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
    	}
    	catch(Exception ex)
    	{
    		Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
    	}
    	return "";
    }
    //Send data to client
    public void sendData2(String data)
    {
    	try 
    	{
    		toClient.writeBytes(data);
    		toClient.flush();
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
    
    //handles the message from zxing
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	  if (scanResult != null) 
    	  {
    		  String contents = scanResult.getContents();
    		  int qrVersion = 0;
    		  
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
    		  //contents = "QR Version: " + qrVersion ;
    		  //Toast.makeText(this, contents, Toast.LENGTH_LONG).show();
    	  }
    	  else
    	  {
    		  Toast.makeText(this, "something went wrong there pall", Toast.LENGTH_LONG).show();
    	  }
    } 
    //sends the message to progress slideshow left
    public void left(View view)
    {
    	sendData2("PREV");
    }
        //sends the message to progress slideshow right
    public void right(View view)
    {
    	sendData2("NEXT");
    }
        //sends the message to fullscreen
    public void fullscreen(View view)
    {
    	sendData2("FULL");
    }
        //sends the message to close slideshow
    public void close(View view)
    {
    	sendData2("SHUT");
    }
    //Create a connection to the server
    public void createConnection (String ip, int port) throws IOException {
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
    //create a connection to the client
    public boolean createClientConnection(String ip, int port) throws IOException
    {
    	try {
    		socket2 = new Socket (ip,port);
    		toClient = new DataOutputStream ( socket2.getOutputStream() );
    	}
    	catch (IOException ex) {
    		Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
    		return false;
    	}
    	catch(Exception ex)
    	{
    		Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
    		return false;
    	}
    	return true;
    }
    	//sends the message to connect to specified server
    public void connect(View view)
    {
    	try
        {
    		String iptext = ip.getText().toString();	
        	createConnection(iptext,8080);
            Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
        }
        catch(IOException ex)
        {
        	Toast.makeText(this, "Failed ", Toast.LENGTH_LONG).show();
        	Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }	
    	catch(Exception ex)
    	{
    		Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
    	}
    }
    //Sends the message to get the full room schedule
    public void getScheduleByRoom()
    {
    	//construct the message to send
    	String temp;
    	String[] recievedMessage;
    	//send message
    	sendData("ROOMSCH");
    	//receive message
    	//message is formatted as such roomid,yy-mm-dd hh-mm-ss,yy-mm-dd hh-mm-ss
    	//room_id, time_start, time_end
    	try
    	{
    		while(true)
    		{
    			temp = fromServer.readLine();
    			if(temp.equals("Done"))
    			{
    				break;
    			}
    			//parse this and store in schedules list
    			recievedMessage = temp.split(",");
    			schedules.add(new Schedule(recievedMessage[0], recievedMessage[1], recievedMessage[2]));
    		}
    	}
    	catch(IOException e)
    	{		
    		printSchedules();
    	}
    }
    //Sends the message to get the room schedule from a particular date
    public void getScheudleByDate(String date)
    {
    	String temp;
    	String[] recievedMessage;
    	//send message
    	sendData("ROOMDTE:"+ date);
    	//receive message
    	try
    	{
    		while(true)
    		{
    			temp = fromServer.readLine();
    			if(temp.equals("Done"))
    			{
    				break;
    			}
    			recievedMessage = temp.split(",");
    			schedules.add(new Schedule(recievedMessage[0], recievedMessage[1], recievedMessage[2]));
    		}
    	}
    	catch(IOException e)
    	{
    		printSchedules();
    	}
    }
    //Sends the message to the the room schedule for this user
    public void getMySchedules()
    {
    	String temp;
    	String[] recievedMessage;
    	//send message
    	sendData("MYSCH");
    	//receive message
    	try
    	{
    		while(true)
    		{
    			temp = fromServer.readLine();
    			if(temp.equals("Done"))
    			{
    				break;
    			}
    			recievedMessage = temp.split(",");
    			schedules.add(new Schedule(recievedMessage[0], recievedMessage[1], recievedMessage[2]));
    		}
    	}
    	catch(IOException e)
    	{
    		printSchedules();
    	}
    }
    //sends the message to reserve a room R
    public void reserveRoom(String timeStart, String timeEnd)
    {
    	String temp;
    	String[] recievedMessage;
    	sendData("RESERVE:"+ timeStart + ":" + timeEnd);
    	try
    	{
    		while(true)
    		{
    			temp = fromServer.readLine();
    			//parse this for pass/fail
    			recievedMessage = temp.split(",");
    			if(recievedMessage[0].compareTo("Win")==0)
    			{
    				Toast.makeText(this, "Congratulations, Reservation made", Toast.LENGTH_LONG).show();
    			}
    			else if(recievedMessage[0].compareTo("Fail")==0)
    			{
    				Toast.makeText(this, "Sorry, something went wrong", Toast.LENGTH_LONG).show();
    				Toast.makeText(this, temp, Toast.LENGTH_LONG).show();
    			}
    			else
    			{
    				Toast.makeText(this, temp, Toast.LENGTH_LONG).show();
    			}
    		}
    	}
    	catch(IOException e){}
    }
    //sends a message to un-reserve a room
    public void deleteReservation(String timeStart, String timeEnd)
    {
    	String temp;
    	String[] recievedMessage;
    	sendData("DELETE:"+ timeStart + ":" + timeEnd);
    	try
    	{
    		while(true)
    		{
    			temp = fromServer.readLine();
    			//parse this for pass/fail
    			recievedMessage = temp.split(",");
    			if(recievedMessage[0].compareTo("Win")==0)
    			{
    				Toast.makeText(this, "Congratulations, Reservation canceled", Toast.LENGTH_LONG).show();
    			}
    			else if(recievedMessage[0].compareTo("Fail")==0)
    			{
    				Toast.makeText(this, "Sorry, something went wrong", Toast.LENGTH_LONG).show();
    				Toast.makeText(this, temp, Toast.LENGTH_LONG).show();
    			}
    			else
    			{
    				Toast.makeText(this, temp, Toast.LENGTH_LONG).show();
    			}
    		}
    	}
    	catch(IOException e){}
    }
    
    public void attemptConnectionToComputer(String roomid)
    {
    	String ip = getComputerIP(roomid);
    
    	if(ip.equals("0.0.0.0"))
    	{
    		return;
    	}
    	else
    	{
    		try 
    		{
				if(createClientConnection(ip,8080))	//connection is fine tell the server this
				{
					sendData("YES");
				}
				else	//connection is no go try to pipe it through
				{
					sendData("FAIL");
				}
			} 
    		catch (IOException e) 
    		{
    			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			}
    	}
    }
    
    public String getComputerIP(String roomid)
    {
    	String temp;
    	sendData("GETIP");
    	try
    	{
    		temp = fromServer.readLine();
    		if(temp.compareTo("NSC")==0)
    		{
			Toast.makeText(this, "This room doesn't have a computer!", Toast.LENGTH_LONG).show();
			return "0.0.0.0";
    		}
    		else 
    		{
    			return temp;
    		}
    	}
    	catch(IOException ex)
    	{
 		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();   	
    	}
    	return "0.0.0.0";
    }
    
    public void printSchedules()
    {
    	ArrayList<String> combinedList = new ArrayList<String>();
    	for (Schedule t : schedules)
    	{
    		combinedList.add(t.getDate()+" "+t.getStartTime()+" "+t.getEndTime());
    	}
 		//my current xml doesnt have this stuff so uncomment when we do.
    	//fileList = new ArrayAdapter<String>(this, R.layout.file_row, combinedList);
    	//setListAdapter(fileList);
    }
    
}