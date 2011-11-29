/*
 * Application.h
 * Header file for the PowerPointApp.
 * It implements the functions as well.
 * PowerPointApp extends Application.
 */
#ifndef PowerPointApp_H
#define PowerPointApp_H
#include <string.h>
#include "Application.h"
class PowerPointApp : public Application
{
private:
/* 
 * full_screen()
 * Emulates the key 'F5' button being pushed.
 */
	void full_screen()
	{
		keybd_event(VK_F5, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
		keybd_event(VK_F5, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
	}
	
/*
 * prev_slide()
 * Emulates the key 'Left Arrow' being pushed.
 */
	void prev_slide()
	{
		keybd_event(VK_LEFT, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
		keybd_event(VK_LEFT, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);	
	}

/*
 * next_slide()
 * Emulates the key 'Right Arrow' being pushed.
 */
	void next_slide()
	{
		keybd_event(VK_RIGHT, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
		keybd_event(VK_RIGHT, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
	}
/*
 * close()
 * Emulates the key 'ESC' being pushed.
 */
	void close()
	{
		keybd_event(VK_ESCAPE, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
		keybd_event(VK_ESCAPE, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
		keybd_event(VK_MENU, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
		keybd_event(VK_F4, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
		keybd_event(VK_F4, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
		keybd_event(VK_MENU, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
	}
	
public:
/*
 * action(char* command)
 * Takes in a string and runs the appropriate function.
 */
	void action(char* command)
	{
		if (strncmp(command, "FULL",4)==0)
			full_screen();
		if (strncmp(command, "NEXT",4)==0)
			next_slide();
		if(strncmp(command, "PREV",4)==0)
			prev_slide();
		if(strncmp(command, "SHUT",4)==0)
			close();
	}
};

#endif