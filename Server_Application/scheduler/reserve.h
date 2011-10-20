#include <string>
using namespace std;

class reserve
{
	private:
		int reserveTime;
		int month;
		int day;
		int year;
		int id;
		string name;
	public:
		reserve(int reserveTime, int nmonth, int nday, int nyear, int nid, string nname);
		int getMonth();
		int getDay();
		int getYear();
		int getId();
		int getReserveTime();
		string getName();
};
