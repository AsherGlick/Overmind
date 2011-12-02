package overmind.android.application;

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
	public String username;
	public String password;
	public String ipAddress;
	public int portNumber;
	public String extraData;
}
