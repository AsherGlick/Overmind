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

/******************************************************************************\
| Expand is a simple function to expand the size of a string by padding it     |
| with spaces. This is used for when we are trying to neatly print out tables  |
\******************************************************************************/
string expand(string data, int size) {
  return data+string(size-data.size(),' ');
}


int main () {
  string data = "";
  int sockfd;
  int clientSockFD;
  string port = "80";
  // Bind Socket
  bindPort (sockfd, port); // From
  cout << "[INFO] Bound Port " << port << endl;
  
  //load webpath
  ifstream webPathF;
  string webPath;
  webPathF.open("webPath");
  getline(webPathF,webPath);
  if (webPath == "") {
    cout << "[WARNING] No Web Path Found. Using Default Webpath" << endl;
    webPath = "./web";
  }
  cout << "[INFO]Web Path Set as " << webPath << endl;
 
  while (true) {
    if (waitSelf(clientSockFD, sockfd) == -1) continue;
    if (!fork()) {
      close(sockfd);
      data = "";
      while (data == "") {
        data = waitData (clientSockFD);
      }
      html newPack = html(data);
      //cout << "---- RAW ----" << endl;
      //charprint(newPack.raw);
      //cout << "-- END RAW --" << endl;
      cout << "--: TYPE:"    << expand(newPack.type   ,5) << "--: REQUEST:" << expand(newPack.request,50) << "--: HOST:"    << expand(newPack.host,20) << "--: POST:" << newPack.post << endl;
      
        ///////////////////
       // PAGE REQUESTS //
      ///////////////////
      if (newPack.type == HTML_GET) {
        ifstream f;
        
        f.open(string(webPath+newPack.request).c_str());
        string file = "";
        string part = "";
        while (getline(f,part)) {
          file += part +'\n';
        }
        int size = sendData (clientSockFD, file);
        if (!size) {
          perror("send");
        }
	    }
	    
	      ///////////////////
	     // DATA REQUESTS //
	    ///////////////////
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
}
