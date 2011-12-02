/*
 * Application.h
 * Header file for arbitrary Applications.
 * Applications must have a function action which acts on 4 character long strings.
 */
#ifndef Application_H
#define Application_H
class Application
{
public:
	virtual void action(char* command) {}
};

#endif