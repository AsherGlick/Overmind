package overmind.androidapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import java.io.*;
import java.net.*;
import android.util.Log;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.widget.FrameLayout;

public class AndroidClientActivity extends Activity {
    private EditText text;
    private Button button1;
	private Socket socket;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	private static final String TAG = "CameraLog";
	Preview preview;
	Camera camera;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	//Does all the initiations
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toast.makeText(this, "Attempting to connect to server", Toast.LENGTH_LONG).show();
        text = (EditText) findViewById(R.id.editText1);
        text.setText("eeyup");
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				preview.camera.takePicture(shutterCallback, rawCallback,
						jpegCallback);
			}
		});
        preview = new Preview(this);
		((FrameLayout) findViewById(R.id.preview)).addView(preview);
        try
        {
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
        catch(IOException ex)
        {
        	Toast.makeText(this, "Failed ", Toast.LENGTH_LONG).show();
        	Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
    //sends information to the server and attempts to retrieve the schedule for a particular room
    public void getSchedule()
    {
    	
    }
    //sends information to server attempts to block out a time for a particular room
    public void blockTime()
    {
    	
    }
    /* does nothing. creates log about shutter*/ 
	ShutterCallback shutterCallback = new ShutterCallback() 
	{
		public void onShutter() {
			Log.d(TAG, "onShutter'd");
		}
	};

	/** Handles data for raw picture */
	PictureCallback rawCallback = new PictureCallback() 
	{
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - raw");
		}
	};

	/* Handles data for jpeg picture */
	PictureCallback jpegCallback = new PictureCallback() 
	{
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				// write to local sandbox file system
				// outStream =
				// CameraDemo.this.openFileOutput(String.format("%d.jpg",
				// System.currentTimeMillis()), 0);
				// Or write to sdcard
				outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
				outStream.write(data);
				outStream.close();
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};
    
}