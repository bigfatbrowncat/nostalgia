#ifndef MAIN_HPP_
#define MAIN_HPP_


struct handlers
{
	void* custom;

	virtual void frameHandler(float* r, float* g, float* b) = 0;
	virtual void resizeHandler(int pointsWidthCount, int pointsHeightCount) = 0;
	virtual void mouseMoveHandler(double xPoints, double yPoints) = 0;
	virtual void mouseButtonHandler(int button, int action, int mods) = 0;
	virtual void keyHandler(int key, int scancode, int action, int mods) = 0;
	virtual void characterHandler(unsigned int character, int mods) = 0;

	virtual ~handlers() { }
};

bool createWindow(const char* title, int windowWidth, int windowHeight, int pixelsPerPoint);
bool mainLoop(handlers* hh);
void closeWindow();
void setCursorVisibility(bool visible);


#endif
