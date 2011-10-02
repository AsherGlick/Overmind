//Victor Huang
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <arpa/inet.h>

#define BUFFER_SIZE 1024
#define MAX_CLIENTS 100

char *msg = "ack";		//client waits on server response for push

void giveSchedule(char* out)
{

}

int main(int argc, char* argv[])
{
	
	//initialize the server

	//connection stuff mostly copied and pasted from notes
	unsigned short port = 8080;
//	printf("Listening on port %d\n", port);
	char buffer[ BUFFER_SIZE ];
	int sock, newsock, len, fromlen, n;
	fd_set readfds;
	int client_sockets[ MAX_CLIENTS ]; /* client socket fd list */
	int client_socket_index = 0;  /* next free spot */
	 /* socket structures from /usr/include/sys/socket.h */
	struct sockaddr_in server;
	struct sockaddr_in client;
	 /* Create the listener socket as TCP socket */
	 /*   (use SOCK_DGRAM for UDP)               */
	sock = socket( PF_INET, SOCK_STREAM, 0 );

	if ( sock < 0 )
	{
		perror( "socket()" );
		exit(1);
	}

	server.sin_family = PF_INET;
	server.sin_addr.s_addr = INADDR_ANY;
	 /* htons() is host-to-network-short for marshalling */
	 /* Internet is "big endian"; Intel is "little endian" */
	server.sin_port = htons( port );
	len = sizeof( server );

	if ( bind( sock, (struct sockaddr *)&server, len ) < 0 ) 
	{
		perror( "bind()" );
		exit( 1 );
 	}

	fromlen = sizeof( client );
	listen( sock, 5 );  /* 5 is number of backlogged waiting clients */
	//end initialization

	//business end
	while ( 1 )
	{
		int q;
		struct timeval timeout;
		timeout.tv_sec = 3; 
		//accept clients
		FD_ZERO( &readfds );
		FD_SET( sock, &readfds );  /* <==== incoming new client connections */
		for ( q = 0 ; q < client_socket_index ; q++ ) 
		{
			FD_SET( client_sockets[ q ], &readfds );
		}
		q = select( FD_SETSIZE, &readfds, NULL, NULL, &timeout );
		if ( FD_ISSET( sock, &readfds ) )
		{
			newsock = accept( sock, (struct sockaddr *)&client, &fromlen );
		//	printf( "Accepted client connection\n" );
			client_sockets[ client_socket_index++ ] = newsock;
		}		
		//	receiving data from the clients
		for ( q = 0 ; q < client_socket_index ; q++ ) 
		{
			int fd = client_sockets[ q ];
			if ( FD_ISSET( fd, &readfds ) ) 
			{
				//get message
				n = recv( fd, buffer, BUFFER_SIZE - 1, 0 );
				if ( n == 0 ) 
				{
					int k;
					for ( k = 0 ; k < client_socket_index ; k++ ) 
					{
						if ( fd == client_sockets[ k ] ) 
						{
							int m;
							for ( m = k ; m < client_socket_index - 1 ; m++ ) 
							{
								client_sockets[ m ] = client_sockets[ m + 1 ];
							}
							client_socket_index--;
	             					break;  /* all done */
						}
					}
				}
				else if ( n < 0 ) 
				{
					perror( "recv()" );
				} 
				else 
				{
					buffer[n] = '\0';
					if(strncmp(buffer, "ack", 3)==0)
					{
						n = send( fd, msg, strlen( msg ), 0 );
						if ( n < strlen( msg ) ) 
						{
							perror( "send()" );
						}
					}
					if(strncmp(buffer,"Schedule", 8)==0){
						char * tempmsg="nope";
						giveSchedule(tempmsg);
						n = send( fd, tempmsg, strlen( msg ), 0 );
						if ( n < strlen( msg ) ) 
						{
							perror( "send()" );
						}
					}
					else
					{
						printf("Unrecognized command!\n");
						n = send( fd, msg, strlen( msg ), 0 );
						if ( n < strlen( msg ) ) 
						{
							perror("send()");
						}
					}
				}
			}
		}
	}	

 return 0; /* we never get here */


}
