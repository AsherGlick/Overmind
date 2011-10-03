package overmind.androidapp;

import android.app.Activity;
import android.os.Bundle;
//import android.view.View;
import android.widget.Toast;
//import android.widget.RadioButton;
import android.widget.EditText;
import java.io.*;
import java.net.*;

public class AndroidClientActivity extends Activity {
    private EditText text;
	private Socket socket;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toast.makeText(this, "Attempting to connect to server", Toast.LENGTH_LONG).show();
        text = (EditText) findViewById(R.id.editText1);
        text.setText("eeyup");
        try{
        	socket = new Socket( "server.overmind.com", 8080 );
        	 // Create an input stream to receive data from the server
            fromServer = new DataInputStream( socket.getInputStream() );
            // Create an output stream to send data to the server
            toServer = new DataOutputStream( socket.getOutputStream() );
            toServer.writeChars("ack");
            toServer.flush();
            char ack = fromServer.readChar();
            Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
            text.setText(ack);
        }
        catch(IOException ex){
        	Toast.makeText(this, "Failed ", Toast.LENGTH_LONG).show();
        	Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }
 		
    }
}