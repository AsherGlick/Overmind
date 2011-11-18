#include <iostream>
#include <stdlib.h>
#include <fstream>
#include <vector>
#include <string>
#include "room.h"
using namespace std;

void printRooms(vector<room> roomList)
{
	for(int i=0; i<roomList.size(); i++)
	{
		cout<<"room "<<roomList[i].getRoomID()<<" opens at "<<roomList[i].getStart()<<" and closes at "<<roomList[i].getEnd()<<endl;
	}
}

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
	//set up the room layouts, soon to be replace by Database
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
	while(1)	//meat of the function, should be broken up, will be done later
	{
		getline(cin, temp);
		cout<<temp<<endl;
		//exit condition
		if (temp.compare("exit")==0)
		{
			break;
		}
		//returns list of all rooms
		else if (temp.compare("viewall")==0)
		{
			printRooms(roomList);
		}
		//views a particular schedule for a specific room and date
		else if (temp.compare(0,12, "viewSchedule")==0)
		{
			cout<<"Viewing Schedule for room"<<endl;
			char* toktemp, *p;
			string commands[5];
			strcpy(p, temp.c_str());
			toktemp = strtok(p, " ");
			for(int i=0 ; i<5 ;i++)
			{
				if(toktemp != NULL)
				{
					commands[i]=string(toktemp);
					toktemp = strtok(NULL, " ");
				}
				else
				{
					cout<<"Too few arguments"<<endl;
					break;
				}
			}
			toktemp = strtok(NULL, " ");
			if(toktemp != NULL)
			{
				cout<<"Too many arguments"<<endl;
			}
			for(int i=0; i<roomList.size(); i++)
			{
				//cout<<"Room "<<roomList[i].getRoomID()<<" comp "<<commands[1]<<endl;
				if(roomList[i].getRoomID().compare(commands[1])==0)
				{
					//cout<<"Found your room! Heres the schedule"<<endl;
					roomList[i].viewSchedule(atoi(commands[2].c_str()), atoi(commands[3].c_str()), atoi(commands[4].c_str()));
					break;	
				}
			}
		}
		//reserve a room on a specific date
		else if (temp.compare(0,7,"reserve")==0)
		{
			cout<<"You want to reserve a room!"<<endl;
			char *toktemp, *p;
			p = new char [temp.size()+1];
			string commands[9];
			strcpy(p, temp.c_str());
			toktemp = strtok(p, " ");
			for(int i=0 ; i<9 ;i++)
			{
				if(toktemp != NULL)
				{
					commands[i]=string(toktemp);
					toktemp = strtok(NULL, " ");
				}
				else
				{
					cout<<"Too few arguments: reserve roomId userId name month day year timeStart timeEnd"<<endl;
					break;
				}
			}
		
			toktemp = strtok(NULL, " ");
			if(toktemp != NULL)
			{
				cout<<"Too many arguments: reserve roomId userId name month day year timeStart timeEnd"<<endl;
			}
			cout<<"Processed your request!"<<endl;
			//0reserve 1roomid 2userid 3name 4month 5 day 6year 7start 8end
			for(int i=0; i<roomList.size(); i++)
			{
				if(roomList[i].getRoomID().compare(commands[1])==0)
				{
					roomList[i].reserveRoom(atoi(commands[4].c_str()), atoi(commands[5].c_str()), atoi(commands[6].c_str()), atoi(commands[2].c_str()), commands[3], atoi(commands[7].c_str()), atoi(commands[8].c_str()));
					break;
				}
			}
		}
		//cancel the reservation for a specific room for a specific date
		else if (temp.compare(0,9,"unreserve")==0)
		{
			cout<<"You want to unreserve a room!"<<endl;
			char* toktemp, *p;
			p = new char [temp.size()+1];
			string commands[8];
			strcpy(p, temp.c_str());
			toktemp = strtok(p, " ");
			for(int i=0; i<8 ; i++)
			{
				if(toktemp != NULL)
				{
					commands[i]=toktemp;
					toktemp = strtok(NULL, " ");
				}
				else
				{
					cout<<"Too few arguments: unreserve roomId userId month day year timeStart timeEnd"<<endl;
					break;
				}
			}
			toktemp = strtok(NULL, " ");
			if(toktemp != NULL)
			{
				cout<<"Too many arguments: reserve roomId userId month day year timeStart timeEnd"<<endl;
			}
			cout<<"Processed your request!"<<endl;
			//0reserve 1roomid 2userid 3month 4day 5year 6start 7end
			for(int i=0; i<roomList.size(); i++)
			{
				if(roomList[i].getRoomID().compare(commands[1])==0)
				{
					roomList[i].unreserveRoom(atoi(commands[3].c_str()), atoi(commands[4].c_str()), atoi(commands[5].c_str()), atoi(commands[2].c_str()), atoi(commands[6].c_str()), atoi(commands[7].c_str()));
					break;
				}
			}			
		}
		else
		{
			cout<<"Unrecognized command"<<endl;
		}	
	}
	//we never get here unless exit is called
	return 0;
}
