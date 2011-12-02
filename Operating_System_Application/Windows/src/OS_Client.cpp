/*
 * OS_Client.
 * Compile with -L"C:\Program Files\Microsoft SDKs\Windows\v7.1\Lib" and -lw2_32
*/

//Required to compile. Deals with certain SOCKET functions.
#define _WIN32_WINNT 0x501

#include <windows.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdlib.h>
#include <iostream>
#include <fstream>
#include <string>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include "PowerPointApp.h"

using namespace std;

/*
 * main
 * starts the OS client on the background.
 * Due to the length of the main function, there will be comments within the function.
 */
int main(void)
{
	//Initialization of variables.
	char port[256];
	
	ifstream config;
	config.open("config.txt");
	char in[256] = "8080";
	if(config.is_open() && !config.eof())
		config >> in;
	cout << in << endl;
	strcpy(port, in);
	config.close();
	
	while(true)
	{
		//variable initiations;
		WSADATA wsaData;
		SOCKET ListenSocket = INVALID_SOCKET;
		SOCKET ClientSocket = INVALID_SOCKET;
	
		struct addrinfo *listenS = NULL;
		struct addrinfo serverS;

		int buffer_len = 4096;
		char buffer[buffer_len];
	
		//Initializing Winsock
		//test = WSAStartup(MAKEWORD(2,2), &wsaData);
		//if(test != 0)
		if (WSAStartup(MAKEWORD(2,2), &wsaData))
		{
			cerr << "WSAStartup failed" << endl;
			return 1;
		}
	
		ZeroMemory(&serverS, sizeof(serverS));
		serverS.ai_family = AF_INET;
		serverS.ai_socktype = SOCK_STREAM;
		serverS.ai_protocol = IPPROTO_TCP;
		serverS.ai_flags = AI_PASSIVE;
		
		//Set up the address and port.
		if (getaddrinfo(NULL, port, &serverS, &listenS))
		{
			cerr << "getaddrinfo failed" << endl;
			WSACleanup();
			return 1;
		}
	
	// Create a Socket to listen on
		ListenSocket = socket(listenS->ai_family, listenS->ai_socktype, listenS->ai_protocol);
		if (ListenSocket == INVALID_SOCKET)
		{
			cerr << "socket creation failed: " << WSAGetLastError() << endl;
			freeaddrinfo(listenS);
			WSACleanup();
			return 1;
		}
		
		// Setting up the socket
		//test = bind(ListenSocket, result->ai_addr, (int)result->ai_addrlen);
		//if (test = SOCKET_ERROR)
		if (bind(ListenSocket, listenS->ai_addr, (int)listenS->ai_addrlen))
		{
			cerr << "bind error: " << WSAGetLastError() << endl;
			freeaddrinfo(listenS);
			closesocket(ListenSocket);
			WSACleanup();
			return 1;
		}
		freeaddrinfo(listenS);
		
		// Start listening for connections
		// Currently assumes the phone is authorized to use the computer.
		int test = listen(ListenSocket, SOMAXCONN);
		if (test == SOCKET_ERROR)
		{
			cerr << "listen failed: " << WSAGetLastError() << endl;
			closesocket(ListenSocket);
			WSACleanup();
			return 1;
		}
	// Accept a client
		ClientSocket = accept(ListenSocket, NULL, NULL);
		if (ClientSocket == INVALID_SOCKET)
		{
			cerr << "accept failed: " << WSAGetLastError() << endl;
			closesocket(ListenSocket);
			WSACleanup();
			return 1;
		}
		closesocket(ListenSocket);
		// Keep doing the commands the phone sends.
		Application* app = new PowerPointApp();
		char filename[buffer_len];
		char filesz[buffer_len];
		int received = 1;
		do 	//this while connection is not closed
		{
			received = recv(ClientSocket, buffer, buffer_len, 0);
			if (received > 0)
			{
				printf("%s\n", buffer);
				//act upon recieved messaged
				if(strncmp(buffer, "OPEN", 4)==0)
				{
					//Code to display powerpoints
					//send(ClientSocket, "test.pptx", buffer_len, 0);
					char* tx = strtok(buffer, "|");
					tx = strtok(NULL, "|");
					strcpy(filename, tx);
					pid_t pid = fork();
					if (pid < 0)
					{
						perror( "fork failed\n");
					}
					else if( pid == 0)
					{
						execlp("C:/Program Files (x86)/Microsoft Office/Office12/POWERPNT.exe", "POWERPNT.exe", "/N", filename, NULL);
					}
				}
				//reacts to a file
				else if(strncmp(buffer, "FILE", 4)==0)
				{
					received = recv(ClientSocket, buffer, buffer_len, 0);
					strcpy(filename, "t");
					char* tx = strtok(buffer, "|");
					strcat(filename, tx);
						
					tx = strtok(NULL, "|");
					int filesize = atoi(tx);
						
					int output = open(filename, O_WRONLY | O_TRUNC | O_CREAT, 0600);
					int cursize = 0;
					while (cursize < filesize)
					{
						received = recv(ClientSocket, buffer, buffer_len, 0);
						cursize = cursize+received;
						write(output, buffer, received);
					}
					pid_t pid = fork();
					if (pid < 0)
					{
						perror( "fork failed\n");
					}
					else if( pid == 0)
					{
						execlp("C:/Program Files (x86)/Microsoft Office/Office12/POWERPNT.exe", "POWERPNT.exe", "/N", filename, NULL);
					}
				}
				else
				{
					app->action(buffer);
				}
			}
			else if(received == 0)
			{
				cout << "Connection closed." << endl;
			}
			else
			{
				cerr << "recv failed: " << WSAGetLastError() << endl;
				closesocket(ClientSocket);
				WSACleanup();
				return 1;
			}
		}
		while (received > 0);

	// Close socket
		if (shutdown(ClientSocket, SD_SEND) == SOCKET_ERROR)
		{
			cerr << "shutdown failed: " << WSAGetLastError() << endl;
			closesocket(ClientSocket);
			WSACleanup();
			return 1;
		}
		closesocket(ClientSocket);
		WSACleanup();
	}
	return 0;
}
