#include <stdlib.h>
#include <math.h>

#include <vector>
#include <string>
#include <sstream>
#include <algorithm>
#include <iostream>

#include <glm/glm.hpp>
#include <glm/ext.hpp>

//#define GLFW_INCLUDE_GLU
#define GLFW_INCLUDE_GLCOREARB
#include <GLFW/glfw3.h>

#include "TimeMeasurer.hpp"
#include "main.hpp"

static const int VERTEX_INDEX = 0;
static const int COLOR_INDEX = 1;


static const GLfloat cubeVertexData[] = {
	0.5f, 0.5f,0.0f, // triangle 2 : begin
	-0.5f,-0.5f,0.0f,
	-0.5f, 0.5f,0.0f, // triangle 2 : end
	0.5f, 0.5f,0.0f,
	0.5f,-0.5f,0.0f,
	-0.5f,-0.5f,0.0f,
};

/*
static const GLfloat cubeVertexData[] = {
    -0.5f,-0.5f,-0.5f, // triangle 1 : begin
    -0.5f,-0.5f, 0.5f,
    -0.5f, 0.5f, 0.5f, // triangle 1 : end
    0.5f, 0.5f,-0.5f, // triangle 2 : begin
    -0.5f,-0.5f,-0.5f,
    -0.5f, 0.5f,-0.5f, // triangle 2 : end

    0.5f,-0.5f, 0.5f,
    -0.5f,-0.5f,-0.5f,
    0.5f,-0.5f,-0.5f,
    0.5f, 0.5f,-0.5f,
    0.5f,-0.5f,-0.5f,
    -0.5f,-0.5f,-0.5f,

    -0.5f,-0.5f,-0.5f,
    -0.5f, 0.5f, 0.5f,
    -0.5f, 0.5f,-0.5f,
    0.5f,-0.5f, 0.5f,
    -0.5f,-0.5f, 0.5f,
    -0.5f,-0.5f,-0.5f,

    -0.5f, 0.5f, 0.5f,
    -0.5f,-0.5f, 0.5f,
    0.5f,-0.5f, 0.5f,
    0.5f, 0.5f, 0.5f,
    0.5f,-0.5f,-0.5f,
    0.5f, 0.5f,-0.5f,

    0.5f,-0.5f,-0.5f,
    0.5f, 0.5f, 0.5f,
    0.5f,-0.5f, 0.5f,
    0.5f, 0.5f, 0.5f,
    0.5f, 0.5f,-0.5f,
    -0.5f, 0.5f,-0.5f,

    0.5f, 0.5f, 0.5f,
    -0.5f, 0.5f,-0.5f,
    -0.5f, 0.5f, 0.5f,
    0.5f, 0.5f, 0.5f,
    -0.5f, 0.5f, 0.5f,
    0.5f,-0.5f, 0.5f
};*/

using namespace std;

GLuint shaderProgram;
GLuint vertexArray;
GLuint vertexBuffer, colorBuffer;
glm::mat4 proportional;
int pixelsPerPoint = 5;
int pointsWidthCount = 0, pointsHeightCount = 0;
int windowWidth, windowHeight;

GLfloat* vertexData = NULL;
GLfloat* vertexColorData = NULL;
int verticesCount = 0;

resize_handler* resizeHandler;
mouse_move_handler* mouseMoveHandler;
void* custom;

float *r = NULL, *g = NULL, *b = NULL;

void create_shader_program()
{
	stringstream ss;
    ss << "#version 330 core\n";
    ss << "layout(location = " << VERTEX_INDEX << ") in vec3 vertex;\n";
    ss << "layout(location = " << COLOR_INDEX << ") in vec3 vertexColor;\n";
    ss << "out vec3 fragmentColor;\n";
    ss << "void main() {\n";
    ss << "    gl_Position.xyz = vertex;\n";
    ss << "    gl_Position.w = 1.0;\n";
    ss << "    fragmentColor = vertexColor;\n";
    ss << "}\n";
    char const * vertexSource = strdup(ss.str().c_str());

    ss.str(string());
    ss << "#version 330 core\n";
    ss << "in vec3 fragmentColor;\n";
    ss << "out vec3 color;\n";
    ss << "void main() {\n";
    ss << "    color = fragmentColor;\n";
    ss << "}\n";
    char const * fragmentSourcePointer = strdup(ss.str().c_str());

    GLint result = GL_FALSE;
    int infoLogLength;

    // Compile Vertex Shader
    cout << "Compiling vertex shader..." << std::endl;
    GLuint vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexShaderID, 1, &vertexSource, NULL);
    glCompileShader(vertexShaderID);

    // Check Vertex Shader
    glGetShaderiv(vertexShaderID, GL_COMPILE_STATUS, &result);
    glGetShaderiv(vertexShaderID, GL_INFO_LOG_LENGTH, &infoLogLength);
    std::vector<char> vertexShaderErrorMessage(infoLogLength);
    glGetShaderInfoLog(vertexShaderID, infoLogLength, NULL, (GLchar*)&vertexShaderErrorMessage[0]);
    if (vertexShaderErrorMessage.size() > 0)
    {
    	cout << &vertexShaderErrorMessage[0] << std::endl;
    }

    // Compile Fragment Shader
    cout << "Compiling fragment shader..." << std::endl;
    GLuint fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fragmentShaderID, 1, &fragmentSourcePointer , NULL);
    glCompileShader(fragmentShaderID);

    // Check Fragment Shader
    glGetShaderiv(fragmentShaderID, GL_COMPILE_STATUS, &result);
    glGetShaderiv(fragmentShaderID, GL_INFO_LOG_LENGTH, &infoLogLength);
    std::vector<char> fragmentShaderErrorMessage(infoLogLength);
    glGetShaderInfoLog(fragmentShaderID, infoLogLength, NULL, (GLchar*)&fragmentShaderErrorMessage[0]);
    if (fragmentShaderErrorMessage.size() > 0)
    {
    	cout << &fragmentShaderErrorMessage[0] << std::endl;
    }

    // Link the program
    cout << "Linking shader program..." << std::endl;
    GLuint programID = glCreateProgram();
    glAttachShader(programID, vertexShaderID);
    glAttachShader(programID, fragmentShaderID);
    glLinkProgram(programID);

    // Check the program
    glGetProgramiv(programID, GL_LINK_STATUS, &result);
    glGetProgramiv(programID, GL_INFO_LOG_LENGTH, &infoLogLength);
    std::vector<char> programErrorMessage(std::max(infoLogLength, int(1)));
    glGetProgramInfoLog(programID, infoLogLength, NULL, (GLchar*)&programErrorMessage[0]);
    if (fragmentShaderErrorMessage.size() > 0)
    {
    	cout << &programErrorMessage[0] << std::endl;
	}

    glDeleteShader(vertexShaderID);
    glDeleteShader(fragmentShaderID);

    shaderProgram = programID;
}

void init()
{
	glGenVertexArrays(1, &vertexArray);
	glBindVertexArray(vertexArray);

	glGenBuffers(1, &vertexBuffer);
	glGenBuffers(1, &colorBuffer);

	// Starting drawing
	glClearColor(0.0f, 0.0f, 0.0f, 0.f);

	create_shader_program();
}

void applyMatrix(GLfloat vertices[], size_t verticesCount, const glm::mat4& mat)
{
	for (size_t i = 0; i < verticesCount; i++)
	{
		glm::vec4 v(vertices[i * 3], vertices[i * 3 + 1], vertices[i * 3 + 2], 1.0f);
		v = mat * v;
		vertices[i * 3] = v.x;
		vertices[i * 3 + 1] = v.y;
		vertices[i * 3 + 2] = v.z;
	}
}

void makeModel(float r[], float g[], float b[])
{
	TimeMeasurer tm("makeModel");

	int cubeVertexDataLength = sizeof(cubeVertexData) / sizeof(GLfloat);

	GLfloat* vertexIter = vertexData;
	GLfloat* vertexColorIter = vertexColorData;
	float *rIter = r, *gIter = g, *bIter = b;
	for (int j = 0; j < pointsHeightCount; j++)
	{
		for (int i = 0; i < pointsWidthCount; i++)
		{
			// Geometry
			GLfloat pixelGeometry[cubeVertexDataLength];
			memcpy(pixelGeometry, cubeVertexData, cubeVertexDataLength * sizeof(GLfloat));

			glm::mat4 trans = glm::translate(glm::vec3(-(float)pointsWidthCount / 2 + i + 0.5f, (float)pointsHeightCount / 2 - j - 0.5f, 0.0f));

			applyMatrix(&pixelGeometry[0], cubeVertexDataLength / 3, proportional * trans);
			memcpy(vertexIter, pixelGeometry, cubeVertexDataLength * sizeof(GLfloat));
			vertexIter += cubeVertexDataLength;

			// Colors
			for (int k = 0; k < cubeVertexDataLength / 3; k++)
			{
				*vertexColorIter++ = *rIter;
				*vertexColorIter++ = *gIter;
				*vertexColorIter++ = *bIter;
			}
			rIter++; gIter++; bIter++;
		}
	}
	tm.measureAndReport();
}

void display()
{
	TimeMeasurer tm("display");
	//assert(vertices.size() == vertexColors.size());

	glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
	glBufferData(GL_ARRAY_BUFFER, verticesCount * sizeof(GLfloat), &vertexData[0], GL_STATIC_DRAW);

	glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
	glBufferData(GL_ARRAY_BUFFER, verticesCount * sizeof(GLfloat), &vertexColorData[0], GL_STATIC_DRAW);

	glEnableVertexAttribArray(VERTEX_INDEX);
	glEnableVertexAttribArray(COLOR_INDEX);

	{
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
		glVertexAttribPointer(
				VERTEX_INDEX,		// attribute 0. No particular reason for 0, but must match the layout in the shader.
				3,                  // size
				GL_FLOAT,           // type
				GL_FALSE,           // normalized?
				0,                  // stride
				(void*) 0			// array buffer offset
		);
	}

	{
		glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
		glVertexAttribPointer(
				COLOR_INDEX,		// attribute 0. No particular reason for 0, but must match the layout in the shader.
				3,                  // size
				GL_FLOAT,           // type
				GL_FALSE,           // normalized?
				0,                  // stride
				(void*) 0			// array buffer offset
		);
	}

	glUseProgram(shaderProgram);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glDrawArrays(GL_TRIANGLES, 0, verticesCount);

	glDisableVertexAttribArray(VERTEX_INDEX);
	glDisableVertexAttribArray(COLOR_INDEX);
	tm.measureAndReport();
}

void reshape(GLFWwindow* window, int w, int h)
{
	windowWidth = w;
	windowHeight = h;

	proportional = glm::scale(glm::vec3(2.0f * pixelsPerPoint / w, 2.0f * pixelsPerPoint / h, 1.0f));

	int w_smaller = (w / pixelsPerPoint) * pixelsPerPoint;
	int h_smaller = (h / pixelsPerPoint) * pixelsPerPoint;

	pointsWidthCount = (int)((float)w_smaller / pixelsPerPoint);
	pointsHeightCount = (int)((float)h_smaller / pixelsPerPoint);
	int cubeVertexDataLength = sizeof(cubeVertexData) / sizeof(GLfloat);
	if (vertexData != NULL)
	{
		delete [] vertexData;
	}
	if (vertexColorData != NULL)
	{
		delete [] vertexColorData;
	}
	verticesCount = cubeVertexDataLength * pointsWidthCount * pointsHeightCount;
	vertexData = new GLfloat[verticesCount];
	vertexColorData = new GLfloat[verticesCount];

	if (r != NULL) { delete [] r; }
	if (g != NULL) { delete [] g; }
	if (b != NULL) { delete [] b; }
	r = new float[pointsWidthCount * pointsHeightCount];
	g = new float[pointsWidthCount * pointsHeightCount];
	b = new float[pointsWidthCount * pointsHeightCount];

	(*resizeHandler)(pointsWidthCount, pointsHeightCount, custom);
}

void cleanup()
{
	glDeleteBuffers(1, &vertexBuffer);
	glDeleteVertexArrays(1, &vertexArray);
	glDeleteProgram(shaderProgram);
}

void keyCallback(GLFWwindow* window, int key, int scancode, int action, int mods)
{
	if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
	{
		glfwSetWindowShouldClose(window, GL_TRUE);
	}
}

void mouseButtonCallback(GLFWwindow* window, int button, int action, int mods)
{
	if (button != GLFW_MOUSE_BUTTON_LEFT)
	{
		return;
	}

	if (action == GLFW_PRESS)
	{

	}
	else
	{

	}
}

void cursorPositionCallback(GLFWwindow* window, double x, double y)
{
	double xc = (double)windowWidth / 2, yc = (double)windowHeight / 2;
	double xl = xc - pointsWidthCount * pixelsPerPoint / 2,
	       yt = yc - pointsHeightCount * pixelsPerPoint / 2;
	double xpts = (x - xl) / pixelsPerPoint,
	       ypts = (y - yt) / pixelsPerPoint;

	(*mouseMoveHandler)(xpts, ypts, custom);
}

int mainLoop(const char* title,
             int windowWidth, int windowHeight, int pixelsPerPoint,
             frame_handler* frameHandler, resize_handler* resizeHandler, mouse_move_handler* mouseMoveHandler, void* custom)
{
	::pixelsPerPoint = pixelsPerPoint;
	::custom = custom;
	::resizeHandler = resizeHandler;
	::mouseMoveHandler = mouseMoveHandler;

	if (!glfwInit())
	{
		return EXIT_FAILURE;
	}

	glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	glfwWindowHint(GLFW_DEPTH_BITS, 16);
	glfwWindowHint(GLFW_SAMPLES, 3);

	GLFWwindow* window = glfwCreateWindow(windowWidth, windowHeight, title, NULL, NULL);
	if (!window)
	{
		cout << "Error: can't create a window" << std::endl;
		glfwTerminate();
		return EXIT_FAILURE;
	}

	cout << "GLSL version: " << glGetString(GL_SHADING_LANGUAGE_VERSION) << std::endl;

	glfwSetFramebufferSizeCallback(window, reshape);
	glfwSetKeyCallback(window, keyCallback);
	glfwSetMouseButtonCallback(window, mouseButtonCallback);
	glfwSetCursorPosCallback(window, cursorPositionCallback);

	glfwMakeContextCurrent(window);
	glfwSwapInterval(1);

	int width, height;
	glfwGetFramebufferSize(window, &width, &height);

	glfwSetTime(0.0);

	reshape(window, width, height);
	init();

	double t, tOld = 0, dt;
	bool finish = false;

	while (!finish)
	{
		TimeMeasurer tm("loop");
		t = glfwGetTime();
		dt = t - tOld;
		tOld = t;


		{
			TimeMeasurer tm("JNI callback");
			(*frameHandler)(r, g, b, custom);
			makeModel(r, g, b);
			tm.measureAndReport();
		}

		display();

		{
			TimeMeasurer tm("draw");
			glfwSwapBuffers(window);
			glfwPollEvents();
			tm.measureAndReport();
		}

		if (glfwWindowShouldClose(window))
		{
			finish = true;
		}
		tm.measureAndReport();
	}
	cleanup();


	glfwTerminate();
	return EXIT_SUCCESS;
}

