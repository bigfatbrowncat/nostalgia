#ifndef MAIN_HPP_
#define MAIN_HPP_

#include "Group.h"

struct Handlers
{
	virtual bool resizeHandler(int pointsWidthCount, int pointsHeightCount) = 0;
	virtual bool frameHandler() = 0;
	virtual bool mouseMoveHandler(double xPoints, double yPoints) = 0;
	virtual bool mouseButtonHandler(int button, int action, int mods) = 0;
	virtual bool keyHandler(int key, int scancode, int action, int mods) = 0;
	virtual bool characterHandler(unsigned int character, int mods) = 0;

	virtual ~Handlers() { }
};

bool createWindow(const char* title, int windowWidth, int windowHeight, int pixelsPerPoint);
void setGroup(Group* group);
void setHandlers(Handlers* handlers);
bool mainLoop();
void closeWindow();
void setCursorVisibility(bool visible);


#endif
