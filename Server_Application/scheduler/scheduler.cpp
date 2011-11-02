#include <iostream>
#include <stdlib.h>
#include <fstream>
#include <vector>
#include <string.h>
#include "room.h"
using namespace std;

int main(int argc, char* argv[])
{
	if(argc != 2)
	{
		cout<<"Proper usage: ./scheduler file.config"<<endl;
		return 1;
	}
	//read in the config file
	ifstream inputFile(argv[1], ifstream::in);
	if (!inputFile) 
	{
	       cout<<"Can't read input file\n";
		return 1;
	}
	//set up the room layouts
	vector<room> roomList;
	string temp;
	while(inputFile>>temp)
	{
		if(temp.compare("end")==0)
		{
			break;
		}
		else
		string newID;
		string newID = temp;
		inputFile>>temp;
		int newStart = atoi(temp.c_str());
		inputFile>>temp;
		int newEnd = atoi(temp.c_str());
		room temproom(newID, newStart, newEnd);
		roomList.push_back(temproom);
	}
	inputFile.close();
	//loop forever
	while(1)
	{
		getline(cin, temp);
		if (temp.compare("exit")==0)
		{
			break;
		}
		cout<<temp<<endl;
		
	}
	//we never get here unless exit is called
	return 0;
}
