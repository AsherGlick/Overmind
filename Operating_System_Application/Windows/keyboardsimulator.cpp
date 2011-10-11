/*
This program the does the most basic commands on a powerpoint presentation (full screening it, go to previous slide, go to next slide)
by replicating keyboard inputs.
It loops on indefinitely waiting for commands from the phone.
------------
I am currently trying to learn to open powerpoint files and using its own api to do these functions or forcing the keyboard inputs to only effect the powerpoint window.
The latter is a option if the former becomes too difficult or messy to do. It may be easier to search the currently opened windows and grabbing the powerpoint window.
There are other pro/cons.
Having trouble actually opening the files as a slideshow (opening the file itself is not what I need) because microsoft did not publish any documentation related to how to do this in c++
although I found documentation on how to control the powerpoint after being able to open and manage it.
------------
Will start working on server/phone interactions when we finalize how we want to send commands/information back and forth so I know how to parse the information.
*/
#include <windows.h>
#include <string>
#include <unistd.h>
#include <iostream>

using namespace std;

// full_screen replicates the F5 key being hit which is the shortcut for full screening the powerpoint.
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

int main()
{
	string input = "RIGHT";
	while(true)
	{
		// Read/wait for input from phone here
		sleep(5);
		if(!input.compare("RIGHT"))
		{
			move_forward();
		}
		else if(!input.compare("LEFT"))
		{
			move_backward();
		}
		else if(!input.compare("FULLSCREEN"))
		{
			full_screen();
		}
	}
	return 0;
}
