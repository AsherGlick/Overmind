package kankan.wheel.demo;

import java.io.DataOutputStream;
import java.net.Socket;

import android.app.Application;

/******************************* GLOBAL VARS APP ******************************\
| The globalVarsApp is a class that we implemented to allow the different      |
| activities to communicate certain data with each other. At the beginning of  |
| each Activity class in the onCreate() function,                              |
|    appState = ((globalVarsApp)getApplicationContext());                      |
| where appState is a variable within the class that can be accessed by any    |
| function within the class                                                    |
\******************************************************************************/

public class globalVarsApp extends Application {
	// USER INFO
	public String username;
	public String password;
	
	// QR INFO
	public String ipAddress;
	public int portNumber;
	public String extraData;
	
	// Socket INFO
	public Socket socket;
	public DataOutputStream toServer;
}
