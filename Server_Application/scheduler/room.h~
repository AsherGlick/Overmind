#include <string.h>
#include <list>
#include "reserve.h"
using namespace std;

class room
{
	private:
		string roomID;
		int timeStart;
		int timeEnd;
		int reserveTime;
		list<reserve> schedule;
	public:
		room(string newID, int NewStart, int NewEnd);
		int getStart();
		int getEnd();
		void viewSchedule(int month, int day, int year);
		void reserveRoom(int month, int day, int year, int id, string name, int startTime, int endTime);
		void unreserveRoom(int month, int day, int year, int id, int startTime, int endTime);
};
