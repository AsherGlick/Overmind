/*
~ v3 of the Windows Client
-Receives fullscreen, left, right commands from phone
-moves powerpoint

next features
-GUI
-revise part of the code to make it object oriented

Notes
-our coding standard wants the comments of functions before the function. We didn't address anything about parts of the main function.
I feel like comments are needed in the main function right now.
*/

//Required to compile. Deals with certain SOCKET functions.
#define _WIN32_WINNT 0x501

#include <windows.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdlib.h>
#include <iostream>

//using namespace std;

// full_screen replicates the F5 key being hit.
void full_screen()
{
	keybd_event(VK_F5, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
	keybd_event(VK_F5, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
}

// move_backward replicates the left arrow being hit.
void move_backward()
{
	keybd_event(VK_LEFT, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
	keybd_event(VK_LEFT, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
}

// move_forward replicate the right arrow being hit
void move_forward()
{
	keybd_event(VK_RIGHT, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
	keybd_event(VK_RIGHT, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
}

int main(void)
{
	//Initialization of variables.
	char port[256] = "8080";
	
	WSADATA wsaData;
	//int test;
	
	SOCKET ListenSocket = INVALID_SOCKET;
	SOCKET ClientSocket = INVALID_SOCKET;
	
	struct addrinfo *listenS = NULL;
	struct addrinfo serverS;

	int buffer_len = 512;
	char buffer[buffer_len];
	
	//Initializing Winsock
	//test = WSAStartup(MAKEWORD(2,2), &wsaData);
	//if(test != 0)
	if (WSAStartup(MAKEWORD(2,2), &wsaData))
	{
		std::cerr << "WSAStartup failed" << std::endl;
		return 1;
	}
	
	ZeroMemory(&serverS, sizeof(serverS));
	serverS.ai_family = AF_INET;
	serverS.ai_socktype = SOCK_STREAM;
	serverS.ai_protocol = IPPROTO_TCP;
	serverS.ai_flags = AI_PASSIVE;
	
	//Set up the address and port.
	//test = getaddrinfo(NULL, port, &server, &listen);
	//if (test != 0)
	if (getaddrinfo(NULL, port, &serverS, &listenS))
	{
		std::cerr << "getaddrinfo failed" << std::endl;
		WSACleanup();
		return 1;
	}
	
	// Create a Socket to listen on
	ListenSocket = socket(listenS->ai_family, listenS->ai_socktype, listenS->ai_protocol);
	if (ListenSocket == INVALID_SOCKET)
	{
		std::cerr << "socket creation failed: " << WSAGetLastError() << std::endl;
		freeaddrinfo(listenS);
		WSACleanup();
		return 1;
	}
	
	// Setting up the socket
	//test = bind(ListenSocket, result->ai_addr, (int)result->ai_addrlen);
	//if (test = SOCKET_ERROR)
	if (bind(ListenSocket, listenS->ai_addr, (int)listenS->ai_addrlen))
	{
		std::cerr << "bind error: " << WSAGetLastError() << std::endl;
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
		std::cerr << "listen failed: " << WSAGetLastError() << std::endl;
		closesocket(ListenSocket);
		WSACleanup();
		return 1;
	}
	
	// Accept a client
	ClientSocket = accept(ListenSocket, NULL, NULL);
	if (ClientSocket == INVALID_SOCKET)
	{
		std::cerr << "accept failed: " << WSAGetLastError() << std::endl;
		closesocket(ListenSocket);
		WSACleanup();
		return 1;
	}
	closesocket(ListenSocket);
	
	// Keep doing the commands the phone sends.
	int received = 1;
	do
	{
		received = recv(ClientSocket, buffer, buffer_len, 0);
		if (received > 0)
		{
			if (!strcmp(buffer, "FULLSCREEN"))
			{
				full_screen();
			}
			else if (!strcmp(buffer, "RIGHT"))
			{
				move_forward();
			}
			else if(!strcmp(buffer, "LEFT"))
			{
				move_backward();
			}
		}
		else if(received == 0)
		{
			std::cout << "Connection closed." << std::endl;
		}
		else
		{
			std::cerr << "recv failed: " << WSAGetLastError() << std::endl;
			closesocket(ClientSocket);
			WSACleanup();
			return 1;
		}
	}
	while (received > 0);
	
	// Close socket
	if (shutdown(ClientSocket, SD_SEND) == SOCKET_ERROR)
	{
		std::cerr << "shutdown failed: " << WSAGetLastError() << std::endl;
		closesocket(ClientSocket);
		WSACleanup();
		return 1;
	}
	closesocket(ClientSocket);
	WSACleanup();

	return 0;
}