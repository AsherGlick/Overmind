#define MAXDATASIZE 100
//#include "ashsockPP.h" // Basic Socket Functionality
#include "ashsockClass.h"
#include "ashHTMLPP.h" // HTML Parsing

#include <fstream>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h> // Threading
#include <sys/types.h>

#include "charprint.h"

//SQL libraries
#include <libpq-fe.h>
#include <netinet/in.h>
#include <arpa/inet.h>



using namespace std;


#define DEFAULTWEBPATH "./web"


void *deviceThread(void *threadid);
static void parseString(char* temp, char* temp0, socketLink connection);
static void getRoomSchedule(char* roomid, PGresult *res, PGconn *conn, socketLink connection);
static void getRoomScheduleDate(char* roomid, char* date, PGresult *res, PGconn *conn, socketLink connection);
static bool getRoomScheduleDate(char* roomid, char* start, char* end, PGresult *res, PGconn *conn);
static void getUserSchedule(char* userid, PGresult *res, PGconn *conn, socketLink connection);
static void reserve(char* userid, char* roomid, char* start, char* end, PGresult *res, PGconn *conn, socketLink connection);
static void unreserve(char* userid, char* roomid, char* start, char* end, PGresult *res, PGconn *conn, socketLink connection);
static void exit_nicely(PGconn *conn);
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
  pthread_t device_thread;
  pthread_create(&device_thread, NULL, deviceThread, NULL);
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

/******************************** DEVICE THREAD *******************************\
| A thread function to handle the connections from the android devices
\******************************************************************************/
void *deviceThread(void *threadid)
{
  // Bind Device Socket
  string rec_web_data = "";
  string port = "80";
  
  // store info from device
  string username = "";
  string roomid = "";
  string type = "";
  int rec_num=0;
  
  //DB connection
  const char *conninfo;
  PGconn     *conn;
  PGresult   *res;
  
  //connect to DB
  conninfo = "host=server.projectovermind.com port=8000 dbname=roommanager user=overmind password=chexmix";
  conn = PQconnectdb(conninfo);
  //check connection, fail if connection fails
  if (PQstatus(conn) != CONNECTION_OK)
  {
       fprintf(stderr, "Connection to database failed: %s", PQerrorMessage(conn));
       exit_nicely(conn);
  }
  
  
  socketPort connectPort;
  connectPort.bindPort(port); // From ashsockPP.h
  cout << "[INFO] Bound Device Port " << port << endl;
  // End Bind Device Socket
  while (true)
  {
    // wait for a good socket connection, set it to clientSockFD
    socketLink connection = connectPort.waitClient();
    // fork after a client connects so the main program can handle another client
    if (!fork())
    {
      cout << "[DEVICE]: Opened Connection on " << port << endl;
      // Close the bound socket, we dont need to listen to it in the fork
      while (connection.isOpen()){  
        // while data is not recieved, continue waiting for data
        rec_web_data = "";
        //int count = 0;
        while (rec_web_data == "")
        {
          //cout << "\r" << count;
          //dcount ++;
          rec_web_data = connection.waitData();
          
        }
        cout << "[DEVICE]: Received data on device port [" << rec_web_data << "]" << endl;
        //first thing recieved from device is version number, ignore
        //2nd thing recieved from device is the username, save
        //3rd thing recieved is the type: ie: computer room etc
        //4th thing recieved is the room number, save
        //anything more is extra data
        //extradata will be in the format of 
        //RESERVE:timestart:timeend
        //DELETE:timestart:timeend
        //ROOMDTE:date
        //MYSCH
        //ROOMSCH
        
	//Conditions for getting the user's schedules here:
	
	/*
		HUEHUEHUEHUEHUEHUEHUE
	*/
        
        if (rec_web_data.compare(0,3,"VER")==0)//This is the version number, print for lulz
        {
        	cout<<rec_web_data;
        	rec_num=1;
        }
        else if (rec_num==1) // This is the 2nd thing recieved from the phone after version, the user name, save this
        {
        	username = rec_web_data;
        	rec_num++;
        }
        else if (rec_num==2) // This is the 3rd thing recieved from the phone, this is the type of QR scanned, computer or room for now, save
        {
        	type = rec_web_data;
        	rec_num++;
        }
        else if (rec_num==3) // this is the 4th thing recieved from the phone, this is the room number, save
        {
        	roomid = rec_web_data;
        	rec_num++;
        }
        else //else this is extradata territory, a command to do something, parse and work magic
        {
        	if(rec_web_data.compare(0,6,"ROOMSCH")==0)	//I WANT THE ENTIRE ROOM SCHEDULE FROM NOW TO FOREVER
        	{
        		getRoomSchedule(roomid.c_str(), res, conn, connection);
        	}
        	else if(rec_web_data.compare(0,6,"ROOMDTE")==0)	//I WANT THE ROOM SCHEDULE FOR JUST THIS DATE PLZ
        	{
        		//parse
        		char* temp;
        		char* pch;
        		char* date[2];
        		strcpy(temp, rec_web_data.c_str());
        		pch = strtok(temp, ":");
        		for(int i=0; i<2; i++)
        		{
				date[i] = pch;
				pch = strtok (NULL, ": ");
			}
        		//send request
        		getRoomScheduleDate(roomid.c_str(), date[1], res, conn, connection);
        	}
        	else if(rec_web_data.compare(0,6,"RESERVE")==0)	//I WANT TO RESERVE A ROOM
        	{
        		//parse
        		char* temp;
        		char* pch;
        		char* date[3];
        		strcpy(temp, rec_web_data.c_str());
        		pch = strtok(temp, ":");
        		for(int i=0; i<3; i++)
        		{
				date[i] = pch;
				pch = strtok (NULL, ": ");
			}
        		//send request
        		reserve(username.c_str(), roomid.c_str(), date[1], date[2], res, conn, connection);
        	}
        	else if(rec_web_data.compare(0,5,"DELETE")==0)	//I WANT TO DELETE A RESERVATION
        	{
        		//parse
        		char* temp;
        		char* pch;
        		char* date[3];
        		strcpy(temp, rec_web_data.c_str());
        		pch = strtok(temp, ":");
        		for(int i=0; i<3; i++)
        		{
				date[i] = pch;
				pch = strtok (NULL, ": ");
			}
        		//send request
        		unreserve(username.c_str(), roomid.c_str(), date[1], date[2], res, conn, connection)
        	}
        	else if(rec_web_data.compare(0,4,"GETIP")==0)	//GIVE ME THE IP TO THE COMPUTER RAWR
        	{
        		getIP(roomid.c_str(), res, conn, connection);
        		rec_web_data = connection.waitData();
        		if(rec_web_data.compare(0,3, "YES")==0)
        		{
        			//connection is fine, do nothing
        		}
        		else
        		{
        			//connection failed, connect to computer and pipe thru
        			
        		}
        	}
        }
        
      }
      PQfinish(conn);
      cout << "[DEVICE]: Closed connection" << endl;
      return 0;
    }
  }
	// End Waiting for connections
  return 0;
}

static void parseString(char* temp, char* temp0)
{
	char* pch;
	pch = strtok(temp, "()\"");
	while(pch != NULL)
	{
		strcat(temp0, pch);
		pch = strtok(NULL, "()\"");	
	}
}

// getRoomSchedule:
// Calls getRoomSchedule on the database.
// Parses the return values and sends them to the phone.
static void getRoomSchedule(char* roomid, PGresult *res, PGconn *conn, socketLink connection)
{
	char* temp = "SELECT getRoomSchedule($1);";
	const char* paramValues[1];
	paramValues[0] = roomid;
	int paramFormat[1];
	paramFormat[0] = 0;
	res = PQexecParams(conn, temp, 1, NULL, paramValues, NULL, paramFormat, 0);
	
	int j = PQntuples(res);
	
	for(int i = 0; i < j; i++)
	{
		char* row;
		char temp0[256] = "";

		row = PQgetvalue(res, i, 0);
		parseString(row, temp0);
		string message(temp0);
		//Replace with code to send to phone.
		connection.sendData(message);
		//printf("%s\n", temp0);
	}
	string message("Done");
	connection.sendData(message);
	PQclear(res);
}

// Does same as getRoomSchedule but takes in an additional date parameter.
static void getRoomScheduleDate(char* roomid, char* date, PGresult *res, PGconn *conn, socketLink connection)
{
	char* temp = "SELECT getRoomScheduleDate($1, $2);";
	const char* paramValues[2];
	paramValues[0] = roomid;
	paramValues[1] = date;
	int paramFormat[2];
	paramFormat[0] = 0;
	paramFormat[1] = 0;
	res = PQexecParams(conn, temp, 2, NULL, paramValues, NULL, paramFormat, 0); 
	
	int j = PQntuples(res);
	
	for(int i = 0; i < j; i++)
	{
		char* row;
		char temp0[256] = "";
		
		row = PQgetvalue(res, i, 0);
		parseString(row, temp0);
		string message(temp0);
		//Replace with code to send to phone.
		connection.sendData(message);
		//printf("%s\n", temp0);
	}
	string message("Done");
	connection.sendData(message);
	PQclear(res);
}

// used as a check for reserving rooms
static bool getRoomScheduleDate(char* roomid, char* start, char* end, PGresult *res, PGconn *conn)
{
	char* temp = "SELECT getRoomScheduleTime($1, $2, $3);";
	const char* paramValues[3];
	paramValues[0] = roomid;
	paramValues[1] = start;
	paramValues[2] = end;
	int paramFormat[3];
	paramFormat[0] = 0;
	paramFormat[1] = 0;
	paramFormat[2] = 0;
	res = PQexecParams(conn, temp, 3, NULL, paramValues, NULL, paramFormat, 0); 
	
	int j = PQntuples(res);
	PQclear(res);
	if (j==0)
		return true;
	else
		return false;
	
}

// Does same as getRoomSchedule but takes in userid instead.
static void getUserSchedule(char* userid, PGresult *res, PGconn *conn, socketLink connection)
{
	char* temp = "SELECT getRoomScheduleTime($1);";
	const char* paramValues[1];
	paramValues[0] = roomid;
	int paramFormat[1];
	paramFormat[0] = 0;
	res = PQexecParams(conn, temp, 1, NULL, paramValues, NULL, paramFormat, 0); 
	
	int j = PQntuples(res);
	
	for(int i = 0; i < j; i++)
	{
		char* row;
		char temp0[256] = "";
		
		row = PQgetvalue(res, i, 0);
		parseString(row, temp0);
		string message(temp0);
		//Replace with code to send to phone.
		connection.sendData(message);
		//printf("%s\n", temp0);
	}
	string message("Done");
	connection.sendData(message);
	PQclear(res);
}

static void reserve(char* userid, char* roomid, char* start, char* end, PGresult *res, PGconn *conn, socketLink connection)
{
	if(getRoomScheduleDate(roomid, start, end, res, conn))
	{
		char* temp = "INSERT INTO reservation($1, $2, $3, $4);";
		const char* paramValues[4];
		paramValues[0] = userid;
		paramValues[1] = roomid;
		paramValues[2] = start;
		paramValues[3] = end;
		int paramFormat[4];
		paramFormat[0] = 0;
		paramFormat[1] = 0;
		paramFormat[2] = 0;
		paramFormat[3] = 0;
		res = PQexecParams(conn, temp, 4, NULL, paramValues, NULL, paramFormat, 0);
		PQclear(res);
		string message("Win");
		connection.sendData(message);
		return;
	}
	else
	{
		string message("Fail,Time slot is already taken");
		connection.sendData(message);
		return;
	}
}

static void unreserve(char* userid, char* roomid, char* start, char* end, PGresult *res, PGconn *conn, socketLink connection)
{
	if(getRoomScheduleDate(roomid, start, end, res, conn))
	{
		string message("Fail,no such reservation");
		connection.sendData(message);
		return;
	}
	else
	{
		char* temp = "DELETE FROM reservation WHERE reservation.user_id = $1 AND reservation.room_id = $2 AND reservation.reserve_start = $3 AND reservation.reserve_ned = $4;";
		const char* paramValues[4];
		paramValues[0] = userid;
		paramValues[1] = roomid;
		paramValues[2] = start;
		paramValues[3] = end;
		int paramFormat[4];
		paramFormat[0] = 0;
		paramFormat[1] = 0;
		paramFormat[2] = 0;
		paramFormat[3] = 0;
		res = PQexecParams(conn, temp, 4, NULL, paramValues, NULL, paramFormat, 0);
		PQclear(res);
		string message("Win");
		connection.sendData(message);
		return;
	}
	
}

static void getIP(char* roomid, PGresult *res, PGconn* conn, socketLink connection)
{
	char* temp = "SELECT room.computer_ip FROM room WHERE room.room_id = $1";
	const char* paramValues[1];
	paramValues[0] = roomid;
	int paramFormat[1];
	paramFormat[0] = 0;
	res = PQexecParams(conn, temp, 1, NULL, paramValues, NULL, paramFormat, 0);
	if (PQntuples(res)==0)
	{
		string message("NSC");
		connection.sendData(message);
	}
	else
	{
		char* row;
		row = PQgetvalue(res, 0, 0);
		string message(row);
		connection.sendData(message);
	}
}

// Quits the connection to the database.
static void exit_nicely(PGconn *conn)
{
    PQfinish(conn);
    exit(1);
}

/*
 * This function prints a query result that is a binary-format fetch from
 * a table defined as in the comment above.  We split it out because the
 * main() function uses it twice.
 */

/*
int
main(int argc, char **argv)
{
    const char *conninfo;
    PGconn     *conn;
    PGresult   *res;

    /*
     * If the user supplies a parameter on the command line, use it as the
     * conninfo string; otherwise default to setting dbname=postgres and using
     * environment variables or defaults for all other connection parameters.
     
    if (argc > 1)
        conninfo = argv[1];
    else
        conninfo = "host=server.projectovermind.com port=5432 dbname=roommanager user=overmind password=chexmix";

    /* Make a connection to the database 
    conn = PQconnectdb(conninfo);

    /* Check to see that the backend connection was successfully made 
    if (PQstatus(conn) != CONNECTION_OK)
    {
        fprintf(stderr, "Connection to database failed: %s",
                PQerrorMessage(conn));
        exit_nicely(conn);
    }

    getRoomSchedule("123",res,conn);
    /* close the connection to the database and cleanup 
    PQfinish(conn);

    return 0;
}
*/
