#define MAXDATASIZE 100
//#include "ashsockPP.h" // Basic Socket Functionality
#include "ashsockClass.h"
#include "ashHTMLPP.h" // HTML Parsing

#include <fstream>
#include <iostream>
#include <stdio.h>
#include <pthread.h> // Threading


#include "charprint.h"

using namespace std;


#define DEFAULTWEBPATH "./web"


void *diviceThread(void *threadid);
/*void *htmlThread(void *threadid);*/

/******************************************************************************\
| Expand is a simple function to expand the size of a string by padding it     |
| with spaces. This is used for when we are trying to neatly print out tables  |
\******************************************************************************/
string expand(string data, int size) {
  return data+string(size-data.size(),' ');
}

/************************************ MAIN ************************************\
| 
\******************************************************************************/

int main ()
{
  cout << "program starting" << endl;
  // Create Another Thread for handeling Android Connections
  pthread_t divice_thread;
  pthread_create(&divice_thread, NULL, diviceThread, NULL);
  /*// Create Another Thread for HTML Connections
  pthread_t html_thread;
  pthread_create(&html_thread,NULL,htmlThread,NULL);*/
  while(true);
}
/*
void * htmlThread (void*)
{
  // HTML SOCKET INFORMATION
  // Bind Web Socket
  string rec_web_data = "";
  string port = "80";
  
  socketPort connectPort;
  connectPort.bindPort(port);
  
  cout << "[INFO] Bound Web Port " << connectPort.getPort() << endl;
  // End Bind Websocket
  // Load Webpath
  ifstream webPathF;
  string webPath;
  webPathF.open("webPath");
  getline(webPathF,webPath);
  if (webPath == "")
  {
    cout << "[WARNING] No Web Path Found. Using Default Webpath" << endl;
    webPath = DEFAULTWEBPATH;
  }
  cout << "[INFO]Web Path Set as " << webPath << endl;
  // End Load Webpath
  
  // Begin Waiting For Connections
  while (true)
  {
    // wait for a good socket connection
    socketLink connection = connectPort.waitClient();
    // fork after a client connects so the main program can handle another client
    if (!fork())
    {
      // Close the bound socket, we dont need to listen to it in the fork
      ///close(web_sockfd); 
      // while data is not recieved, continue waiting for data
      rec_web_data = "";
      while (rec_web_data == "")
      {
        rec_web_data = connection.waitData ();
      }
      // after data is received create a new html object from the data
      html newPack = html(rec_web_data);
      // output the basic information of the connected client
      cout << "--: TYPE:"    << expand(newPack.type   ,5) << "--: REQUEST:" << expand(newPack.request,50) << "--: HOST:"    << expand(newPack.host,20) << "--: POST:" << newPack.post << endl;
      
      // PAGE REQUESTS //
      // Send the web-browser the info that it needs
      if (newPack.type == HTML_GET)
      {
        ifstream f;
        f.open(string(webPath+newPack.request).c_str());
        string file = "";
        string part = "";
        while (getline(f,part))
        {
          file += part +'\n';
        }
        int size = connection.sendData (file);
        if (!size)
        {
          perror("send");
        }
	    }
	    
	    // DATA REQUESTS //
	    // If the type is post, the program checks the POST to respond correctly
	    else if (newPack.type == HTML_POST) {
	      if (newPack.post[0] == 's') {
	        cout << "SERVER LIST" << endl;
	        while (!connection.sendData ("server1name,ip,num&server2name,ip,num&server3name,ip,num"));
	        cout << "SENT DATA" << endl;
	      }
	      else if (newPack.post[0] == 'p') {
	        cout << "PLUGIN LIST REQUEST" << endl;
	      }
	      else if (newPack.post[0] == 'l') {
	        cout << "LOAD PLUGIN REQUEST" << endl; 
	      }
	      else if (newPack.post[0] == 'd') {
	        cout << "DATA REQUEST" << endl;
	      }
	    }
	    else {
	      cout << "NOT POST OR GET!!!" << endl;
	    }
	    ///close(clientSockFD);
	    return 0;
	  }
	  ///close(clientSockFD);
	}
	// End Waiting for connections
}*/

/******************************** DIVICE THREAD *******************************\
| A thread function to handle the connections from the android divices
\******************************************************************************/
void *diviceThread(void *threadid)
{
  // Bind Divice Socket
  string rec_web_data = "";
  string port = "80";
  socketPort connectPort;
  connectPort.bindPort(port); // From ashsockPP.h
  cout << "[INFO] Bound Divice Port " << port << endl;
  // End Bind Divice Socket
  while (true)
  {
    // wait for a good socket connection, set it to clientSockFD
    socketLink connection = connectPort.waitClient();
    // fork after a client connects so the main program can handle another client
    if (!fork())
    {
      cout << "[DIVICE]: Opened Connection on " << port << endl;
      // Close the bound socket, we dont need to listen to it in the fork
      while (connection.isOpen()){  
        // while data is not recieved, continue waiting for data
        rec_web_data = "";
        while (rec_web_data == "")
        {
          rec_web_data = connection.waitData();
        }
        cout << "[DIVICE]: Received data on divice port [" << rec_web_data << "]" << endl;
      }
	    cout << "[DIVICE]: Closed connection" << endl;
	    return 0;
	  }
	}
	// End Waiting for connections
  return 0;
}
