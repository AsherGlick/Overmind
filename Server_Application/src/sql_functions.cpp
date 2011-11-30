#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <libpq-fe.h>

/* for ntohl/htonl */
#include <netinet/in.h>
#include <arpa/inet.h>

// Takes string temp and parses it. It does not alter temp and puts the parsed string in temp0.
// The parsing removes all '(', ')', and '"' from the string because java's strtok can not take more than one token.
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
static void getRoomSchedule(char* roomid, PGresult *res, PGconn *conn)
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
		
		//Replace with code to send to phone.
		printf("%s\n", temp0);
	}
	PQclear(res);
}

// Does same as getRoomSchedule but takes in an additional date parameter.
static void getRoomScheduleDate(char* roomid, char* date, PGresult *res, PGconn *conn)
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
		
		//Replace with code to send to phone.
		printf("%s\n", temp0);
	}
	PQclear(res);
}

// Does same as getRoomSchedule but takes in additional start and end parameters.
static void getRoomScheduleDate(char* roomid, char* start, char* end, PGresult *res, PGconn *conn)
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
	
	for(int i = 0; i < j; i++)
	{
		char* row;
		char temp0[256] = "";
		
		row = PQgetvalue(res, i, 0);
		parseString(row, temp0);
		
		//Replace with code to send to phone.
		printf("%s\n", temp0);
	}
	PQclear(res);
}

// Does same as getRoomSchedule but takes in userid instead.
static void getUser(char* userid, PGresult *res, PGconn *conn)
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
		
		//Replace with code to send to phone.
		printf("%s\n", temp0);
	}
	PQclear(res);
}

static void reserve(char* userid, char* roomid, char* start, char* end, PGresult *res, PGconn *conn)
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
	res = PQexecParams(conn, temp, 1, NULL, paramValues, NULL, paramFormat, 0);
	
	PQclear(res);
}

static void unreserve(char* userid, char* roomid, char* start, char* end, PGresult *res, PGconn *conn)
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
	res = PQexecParams(conn, temp, 1, NULL, paramValues, NULL, paramFormat, 0);
	
	PQclear(res);
}

// Quits the connection to the database.
static void exit_nicely(PGconn *conn)
{
    PQfinish(conn);
    exit(1);
}

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
     */
    if (argc > 1)
        conninfo = argv[1];
    else
        conninfo = "host=server.projectovermind.com port=5432 dbname=roommanager user=overmind password=chexmix";

    /* Make a connection to the database */
    conn = PQconnectdb(conninfo);

    /* Check to see that the backend connection was successfully made */
    if (PQstatus(conn) != CONNECTION_OK)
    {
        fprintf(stderr, "Connection to database failed: %s",
                PQerrorMessage(conn));
        exit_nicely(conn);
    }

    pgetRoomSchedule("123",res,conn);
    /* close the connection to the database and cleanup */
    PQfinish(conn);

    return 0;
}