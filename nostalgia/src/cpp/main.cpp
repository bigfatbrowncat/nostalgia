#include <stdlib.h>
#include <math.h>

#include <vector>
#include <string>
#include <sstream>
#include <set>
#include <algorithm>
#include <iostream>

#include <GL3/gl3w.h>

#include <glm/glm.hpp>
#include <glm/ext.hpp>

#define GLFW_INCLUDE_GLCOREARB
#include <GLFW/glfw3.h>

#include "TimeMeasurer.hpp"
#include "main.hpp"
#include "Group.h"

using namespace std;

bool glInitialized = false;
int pixelsPerPoint = 5;
int pointsWidthCount = 0, pointsHeightCount = 0;
int windowWidth, windowHeight;
glm::mat4 proportional;

Handlers* theHandlers = NULL;
set<Group*> groups;
GLFWwindow* window;
bool terminatedByException;

void getGlVersion(int *major, int *minor)
{
    const char *verstr = (const char *) glGetString(GL_VERSION);
    if ((verstr == NULL) || (sscanf(verstr,"%d.%d", major, minor) != 2))
    {
        *major = *minor = 0;
        fprintf(stderr, "Invalid GL_VERSION format!!!\n");
    }
}

void getGlslVersion(int *major, int *minor)
{
    int gl_major, gl_minor;
    getGlVersion(&gl_major, &gl_minor);

    *major = *minor = 0;
    if (gl_major == 1)
    {
        /* GL v1.x can only provide GLSL v1.00 as an extension */
        const char *extstr = (const char *) glGetString(GL_EXTENSIONS);
        if ((extstr != NULL) &&
            (strstr(extstr, "GL_ARB_shading_language_100") != NULL))
        {
            *major = 1;
            *minor = 0;
        }
    }
    else if (gl_major >= 2)
    {
        /* GL v2.0 and greater must parse the version string */
        const char *verstr =
            (const char *) glGetString(GL_SHADING_LANGUAGE_VERSION);

        if((verstr == NULL) ||
            (sscanf(verstr, "%d.%d", major, minor) != 2))
        {
            *major = *minor = 0;
            fprintf(stderr,
                "Invalid GL_SHADING_LANGUAGE_VERSION format!!!\n");
        }
    }
}

bool glInit()
{
    if (gl3wInit())
    {
        printf("Problem initializing OpenGL\n");
        return false;
    }

    int maj, min, slmaj, slmin;
    getGlVersion(&maj, &min);
    getGlslVersion(&slmaj, &slmin);

    printf("OpenGL version: %d.%d\n", maj, min);
    printf("GLSL version: %d.%d\n", slmaj, slmin);

    return true;
}

void init()
{
	// Starting drawing
	glClearColor(0.0f, 0.0f, 0.0f, 1.f);
}

void reshape(GLFWwindow* window, int w, int h)
{
	glViewport(0, 0, (GLsizei)w, (GLsizei)h);

	windowWidth = w;
	windowHeight = h;

	proportional = glm::scale(glm::vec3(2.0f * pixelsPerPoint / w, 2.0f * pixelsPerPoint / h, 1.0f));

	int w_smaller = (w / pixelsPerPoint) * pixelsPerPoint;
	int h_smaller = (h / pixelsPerPoint) * pixelsPerPoint;

	pointsWidthCount = (int)((float)w_smaller / pixelsPerPoint) + 2;
	pointsHeightCount = (int)((float)h_smaller / pixelsPerPoint) + 2;

	for (set<Group*>::iterator iter = groups.begin(); iter != groups.end(); iter++) {
		(*iter)->setGlobalMatrix(proportional);
		(*iter)->setScreenSize(pointsWidthCount, pointsHeightCount);
	}

	if (!(theHandlers->resizeHandler)(pointsWidthCount, pointsHeightCount)) {
		terminatedByException = true;
	}

	for (set<Group*>::iterator iter = groups.begin(); iter != groups.end(); iter++) {
		(*iter)->draw();
	}

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glClearColor(0.f, 0.f, 0.f, 1.f);

	if (!(theHandlers->frameHandler)()) {
		terminatedByException = true;
	}

	glfwSwapBuffers(window);
}

void cleanup()
{
	groups.clear();
}

void keyCallback(GLFWwindow* window, int key, int scancode, int action, int mods)
{
	if (!(theHandlers->keyHandler)(key, scancode, action, mods)) {
		terminatedByException = true;
	}
}

void characterCallback(GLFWwindow* window, unsigned int codepoint, int mods)
{
	if (!(theHandlers->characterHandler)(codepoint, mods)) {
		terminatedByException = true;
	}
}

void mouseButtonCallback(GLFWwindow* window, int button, int action, int mods)
{
	if (!(theHandlers->mouseButtonHandler)(button, action, mods)) {
		terminatedByException = true;
	}
}

void cursorPositionCallback(GLFWwindow* window, double x, double y)
{
	double xc = (double)windowWidth / 2, yc = (double)windowHeight / 2;
	double xl = xc - pointsWidthCount * pixelsPerPoint / 2,
	       yt = yc - pointsHeightCount * pixelsPerPoint / 2;
	double xpts = (x - xl) / pixelsPerPoint,
	       ypts = (y - yt) / pixelsPerPoint;

	if (!(theHandlers->mouseMoveHandler)(xpts, ypts)) {
		terminatedByException = true;
	}
}

void setCursorVisibility(bool visible)
{
	if (visible)
	{
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}
	else
	{
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
	}
}

bool createWindow(const char* title, int windowWidth, int windowHeight, int pixelsPerPoint)
{
	::pixelsPerPoint = pixelsPerPoint;

    if (!glfwInit())
	{
		return false;
	}

	glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	glfwWindowHint(GLFW_DEPTH_BITS, 16);
	glfwWindowHint(GLFW_SAMPLES, 1);

	window = glfwCreateWindow(windowWidth, windowHeight, title, NULL, NULL);
	if (!window)
	{
		cout << "Error: can't create a window" << std::endl;
		glfwTerminate();
		return false;
	}

	glfwSetWindowSizeCallback(window, reshape);
	glfwSetKeyCallback(window, keyCallback);
	glfwSetCharModsCallback(window, characterCallback);
	glfwSetMouseButtonCallback(window, mouseButtonCallback);
	glfwSetCursorPosCallback(window, cursorPositionCallback);

	glfwMakeContextCurrent(window);
    if (!glInit())
    {
		cout << "Error: can't initialize GL3 API" << std::endl;
		glfwTerminate();
		return false;

    }
	glfwSwapInterval(1);

	glInitialized = true;
	return true;
}

void closeWindow()
{
	glfwSetWindowShouldClose(window, GL_TRUE);
}

void addGroup(Group* group)
{
	groups.insert(group);
}

void removeGroup(Group* group)
{
	groups.erase(group);
}

void setHandlers(Handlers* handlers)
{
	::theHandlers = handlers;
	int width, height;
	glfwGetFramebufferSize(window, &width, &height);
	reshape(window, width, height);
}

bool mainLoop()
{
	if (!glInitialized) {
		cerr << "OpenGL isn't initialized";
		return false;
	}
	if (theHandlers == NULL) {
		cerr << "Handler object isn't assigned";
		return false;
	}

	glfwSetTime(0.0);

	init();

	double t, tOld = 0, dt;
	bool finish = false;

	terminatedByException = false;

	while (!finish)
	{
		t = glfwGetTime();
		dt = t - tOld;
		tOld = t;

		for (set<Group*>::iterator iter = groups.begin(); iter != groups.end(); iter++) {
			(*iter)->draw();
		}

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.f, 0.f, 0.f, 1.f);

		if (!(theHandlers->frameHandler)()) {
			terminatedByException = true;
		}

		glfwSwapBuffers(window);
		glfwPollEvents();

		if (glfwWindowShouldClose(window))
		{
			finish = true;
		}

		if (terminatedByException)
		{
			finish = true;
		}
	}
	cleanup();

	glfwTerminate();
	return !terminatedByException;
}

