#include <iostream>
#include <string.h>
#include <list>
#include "room.h"

using namespace std;

room::room(string newID, int newStart, int newEnd)
{
	roomID = newID;
	timeStart = newStart;
	timeEnd = newEnd;
}
int room::getStart()
{
	return timeStart;
}
int room::getEnd()
{
	return timeEnd;
}

void room::viewSchedule(int month, int day, int year)
{
	list<reserve>::iterator itr;
	for(itr=schedule.begin(); itr != schedule.end(); itr++)
	{
		if((month == itr->getMonth())&&(day == itr->getDay())&&(year == itr->getYear()))
		{
			cout<<itr->getName()<<" has reserved this room for "<<itr->getReserveTime()<<endl;
		}
	}
}

//reserve a room given a date, a person and a time range
void room::reserveRoom(int month, int day, int year, int id, string name, int startTime, int endTime)
{
	if(startTime < timeStart)
	{
		cout<<"Sorry, "<<name<<", but the room you want opens at"<<timeStart<<" not "<<startTime<<endl;
		return;
	}
	if(endTime > timeEnd)
	{
		cout<<"Sorry, "<<name<<", but the room you want closes at"<<timeEnd<<endl;
		return;
	}
	list<reserve>::iterator itr;
	for(itr=schedule.begin(); itr != schedule.end(); itr++)
	{
		if((month == itr->getMonth())&&(day == itr->getDay())&&(year == itr->getYear())&&((itr->getReserveTime()>=startTime)&&(itr->getReserveTime()<endTime)))
		{
			cout<<"Sorry, "<<name<<", but "<<itr->getName()<<" has reserved the room for this time"<<endl;
			return;
		}
	}
	for(int i=startTime; i!=endTime; i=i+100)
	{
		reserve temp(i, day, month, year, id, name);
		schedule.push_back(temp);
	}
}
void room::unreserveRoom(int month, int day, int year, int id, int startTime, int endTime)
{
	list<reserve>::iterator itr;
	for(itr=schedule.begin(); itr != schedule.end(); itr++)
	{
		if((month == itr->getMonth())&&(day == itr->getDay())&&(year == itr->getYear())&&((itr->getReserveTime()>=startTime)&&(itr->getReserveTime()<endTime)) && id == itr->getId())
		{
			itr = schedule.erase(itr);
			itr--;
		}
	}
}
