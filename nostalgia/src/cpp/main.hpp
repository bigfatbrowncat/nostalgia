#ifndef MAIN_HPP_
#define MAIN_HPP_


struct Handlers
{
	virtual bool mouseMoveHandler(double xPoints, double yPoints) = 0;
	virtual bool mouseButtonHandler(int button, int action, int mods) = 0;
	virtual bool keyHandler(int key, int scancode, int action, int mods) = 0;
	virtual bool characterHandler(unsigned int character, int mods) = 0;

	virtual ~Handlers() { }
};

bool createWindow(const char* title, int windowWidth, int windowHeight, int pixelsPerPoint);
void setHandlers(Handlers* handlers);
bool mainLoop();
void closeWindow();
void setCursorVisibility(bool visible);


#endif
