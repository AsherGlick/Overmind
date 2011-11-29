/*
 * PowerPointApp.cpp
 * Implements the functions of the PowerPointApp class.
 */

//Emulates a F5 button being pushed.
void PowerPointApp::full_screen()
{
	keybd_event(VK_F5, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
	keybd_event(VK_F5, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
}

//Emulates a Left arrow being pushed.
void PowerPointApp::next_slide()
{
	keybd_event(VK_LEFT, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
	keybd_event(VK_LEFT, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
}

//Emulates a right arrow being pushed.
void PowerPointApp::prev_slide()
{
	keybd_event(VK_RIGHT, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
	keybd_event(VK_RIGHT, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);	
}

//Emulates ESC key being pushed then Alt+F4
void close()
{
	keybd_event(VK_ESCAPE, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
	keybd_event(VK_ESCAPE, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
	keybd_event(VK_MENU, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
	keybd_event(VK_F4, 0x45, KEYEVENTF_EXTENDEDKEY | 0, 0);
	keybd_event(VK_F4, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
	keybd_event(VK_MENU, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, 0);
}

//Combines each of the functions together.
void PowerPointApp::action(char* command)
{
	if (strncmp(command, "FULL",4)==0)
	{
		this.full_screen();
	}
	if (strncmp(command, "NEXT",4)==0)
	{
		this.next_slide();
	}
	if(strncmp(command, "PREV",4)==0)
	{
		this.prev_slide();
	}
	if(strncmp(command, "SHUT",4)==0)
	{
		this.close();
	}
}
