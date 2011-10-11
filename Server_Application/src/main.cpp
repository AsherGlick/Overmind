#define MAXDATASIZE 100
#include "ashsockPP.h"   // Basic Socket Functionality
#include "ashHTMLPP.h"
#include <fstream>
#include <iostream>
#include <stdio.h>

//threads
#include <pthread.h>


#include "charprint.h"

using namespace std;


#define DEFAULTWEBPATH "./web"


void *waitoffWeb(void *threadid);

/******************************************************************************\
| Expand is a simple function to expand the size of a string by padding it     |
| with spaces. This is used for when we are trying to neatly print out tables  |
\******************************************************************************/
string expand(string data, int size) {
  return data+string(size-data.size(),' ');
}


int main () {
  // Create Another Divice Thread
  pthread_t divice_thread;
  pthread_create(&divice_thread, NULL, waitoffWeb, NULL);
  // Bind Web Socket
  string rec_web_data = "";
  int web_sockfd;
  int clientSockFD;
  string port = "80";
  bindPort (web_sockfd, port); // From ashsockPP.h
  cout << "[INFO] Bound Web Port " << port << endl;
  // End Bind Websocket
  // Load Webpath
  ifstream webPathF;
  string webPath;
  webPathF.open("webPath");
  getline(webPathF,webPath);
  if (webPath == "") {
    cout << "[WARNING] No Web Path Found. Using Default Webpath" << endl;
    webPath = DEFAULTWEBPATH;
  }
  cout << "[INFO]Web Path Set as " << webPath << endl;
  // End Load Webpath
  
  // Begin Waiting For Connections
  while (true)
  {
    // wait for a good socket connection
    if (waitSelf(clientSockFD, web_sockfd) == -1) continue;
    // fork after a client connects so the main program can handle another client
    if (!fork())
    {
      // Close the bound socket, we dont need to listen to it in the fork
      close(web_sockfd); 
      // while data is not recieved, continue waiting for data
      rec_web_data = "";
      while (rec_web_data == "")
      {
        rec_web_data = waitData (clientSockFD);
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
        int size = sendData (clientSockFD, file);
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
	        while (!sendData (clientSockFD, "server1name,ip,num&server2name,ip,num&server3name,ip,num"));
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
	    close(clientSockFD);
	    return 0;
	  }
	  close(clientSockFD);
	}
	// End Waiting for connections
}

/* the program that is threaded in the begining */
void *waitoffWeb(void *threadid) {
  // Bind Divice Socket
  string rec_web_data = "";
  int web_sockfd;
  int clientSockFD;
  string port = "8080";
  bindPort (web_sockfd, port); // From ashsockPP.h
  cout << "[INFO] Bound Divice Port " << port << endl;
  // End Bind Divice Socket
  while (true)
  {
    // wait for a good socket connection, set it to clientSockFD
    if (waitSelf(clientSockFD, web_sockfd) == -1) continue;
    // fork after a client connects so the main program can handle another client
    if (!fork())
    {
      cout << "[DIVICE]: Opened Connection on 8080" << endl;
      // Close the bound socket, we dont need to listen to it in the fork
      close(web_sockfd);
      while (true){  
        // while data is not recieved, continue waiting for data
        rec_web_data = "";
        while (rec_web_data == "")
        {
          rec_web_data = waitData (clientSockFD);
        }
        cout << "[DIVICE]: Received data on divice port [" << rec_web_data << "]" << endl;
        cout << clientSockFD << endl;
      }
	    close(clientSockFD);
	    cout << "[DIVICE]: Closed connection" << endl;
	    return 0;
	  }
	  close(clientSockFD);
	}
	// End Waiting for connections
  return 0;
}
