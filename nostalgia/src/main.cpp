#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include <vector>
#include <string>
#include <sstream>
#include <algorithm>
#include <iostream>

#include <glm/glm.hpp>
#include <glm/ext.hpp>

#include <sys/time.h>
#include <unistd.h>

#define GLFW_INCLUDE_GLU
#define GLFW_INCLUDE_GLCOREARB
#include <GLFW/glfw3.h>

class TimeMeasurer
{
private:
	long start;
	char* tag;
public:
	TimeMeasurer(char* tag)
	{
		this->tag = tag;

		struct timeval  tv;
		gettimeofday(&tv, NULL);

		start = (tv.tv_sec) * 1000 + (tv.tv_usec) / 1000;
	}

	void count()
	{
		struct timeval  tv;
		gettimeofday(&tv, NULL);

		long end = (tv.tv_sec) * 1000 + (tv.tv_usec) / 1000;
		//printf("%s: %ld msec\n", tag, end - start);
	}
};

void init();
void display();
void reshape(GLFWwindow* window, int w, int h);
void key_callback(GLFWwindow* window, int key, int scancode, int action, int mods);
void mouse_button_callback(GLFWwindow* window, int button, int action, int mods);
void cursor_position_callback(GLFWwindow* window, double x, double y);

static const int VERTEX_INDEX = 0;
static const int COLOR_INDEX = 1;
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
};

using namespace std;

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

GLuint shaderProgram;

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
    printf("Compiling vertex shader...\n");
    GLuint vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexShaderID, 1, &vertexSource, NULL);
    glCompileShader(vertexShaderID);

    // Check Vertex Shader
    glGetShaderiv(vertexShaderID, GL_COMPILE_STATUS, &result);
    glGetShaderiv(vertexShaderID, GL_INFO_LOG_LENGTH, &infoLogLength);
    std::vector<char> vertexShaderErrorMessage(infoLogLength);
    glGetShaderInfoLog(vertexShaderID, infoLogLength, NULL, &vertexShaderErrorMessage[0]);
    fprintf(stdout, "%s\n", &vertexShaderErrorMessage[0]);

    // Compile Fragment Shader
    printf("Compiling fragment shader...\n");
    GLuint fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fragmentShaderID, 1, &fragmentSourcePointer , NULL);
    glCompileShader(fragmentShaderID);

    // Check Fragment Shader
    glGetShaderiv(fragmentShaderID, GL_COMPILE_STATUS, &result);
    glGetShaderiv(fragmentShaderID, GL_INFO_LOG_LENGTH, &infoLogLength);
    std::vector<char> FragmentShaderErrorMessage(infoLogLength);
    glGetShaderInfoLog(fragmentShaderID, infoLogLength, NULL, &FragmentShaderErrorMessage[0]);
    fprintf(stdout, "%s\n", &FragmentShaderErrorMessage[0]);

    // Link the program
    fprintf(stdout, "Linking shader program...\n");
    GLuint programID = glCreateProgram();
    glAttachShader(programID, vertexShaderID);
    glAttachShader(programID, fragmentShaderID);
    glLinkProgram(programID);

    // Check the program
    glGetProgramiv(programID, GL_LINK_STATUS, &result);
    glGetProgramiv(programID, GL_INFO_LOG_LENGTH, &infoLogLength);
    std::vector<char> ProgramErrorMessage(std::max(infoLogLength, int(1)));
    glGetProgramInfoLog(programID, infoLogLength, NULL, &ProgramErrorMessage[0]);
    fprintf(stdout, "%s\n", &ProgramErrorMessage[0]);

    glDeleteShader(vertexShaderID);
    glDeleteShader(fragmentShaderID);

    shaderProgram = programID;
}

GLuint vertexArray;
GLuint vertexBuffer, colorBuffer;
glm::mat4 proportional;
int pixelsPerPoint = 5;
int pointsW = 0, pointsH = 0;

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

GLfloat* vertexColors = NULL;
GLfloat* vertices = NULL;
int verticesCount = 0;

void makeModel()
{
	TimeMeasurer tm("makeModel");

	int cubeVertexDataLength = sizeof(cubeVertexData) / sizeof(GLfloat);

	GLfloat* vertexIter = vertices;
	GLfloat* vertexColorIter = vertexColors;
	for (int i = 0; i < pointsW; i++)
	{
		for (int j = 0; j < pointsH; j++)
		{
			// Geometry
			GLfloat pixelGeometry[cubeVertexDataLength];
			memcpy(pixelGeometry, cubeVertexData, cubeVertexDataLength * sizeof(GLfloat));

			glm::mat4 trans = glm::translate(glm::vec3(-pointsW / 2 + i + 0.5f, pointsH / 2 - j - 0.5f, 0.0f));

			applyMatrix(&pixelGeometry[0], cubeVertexDataLength / 3, proportional * trans);
			memcpy(vertexIter, pixelGeometry, cubeVertexDataLength * sizeof(GLfloat));
			vertexIter += cubeVertexDataLength;

			// Colors
			//GLfloat pixelColor[cubeVertexDataLength];
			float r = (float)rand() / RAND_MAX;
			float g = (float)rand() / RAND_MAX;
			float b = (float)rand() / RAND_MAX;
			for (int k = 0; k < cubeVertexDataLength / 3; k++)
			{
				*vertexColorIter++ = r;
				*vertexColorIter++ = g;
				*vertexColorIter++ = b;
			}
		}
	}
	tm.count();
}

void display()
{
	TimeMeasurer tm("display");
	//assert(vertices.size() == vertexColors.size());

	glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
	glBufferData(GL_ARRAY_BUFFER, verticesCount * sizeof(GLfloat), &vertices[0], GL_STATIC_DRAW);

	glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
	glBufferData(GL_ARRAY_BUFFER, verticesCount * sizeof(GLfloat), &vertexColors[0], GL_STATIC_DRAW);

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
	tm.count();
}

void reshape(GLFWwindow* window, int w, int h)
{
	proportional = glm::scale(glm::vec3((float)pixelsPerPoint / w, (float)pixelsPerPoint / h, 1.0f));
	pointsW = (float)w * 2 / pixelsPerPoint;
	pointsH = (float)h * 2 / pixelsPerPoint;
	int cubeVertexDataLength = sizeof(cubeVertexData) / sizeof(GLfloat);
	if (vertices != NULL)
	{
		delete [] vertices;
	}
	if (vertexColors != NULL)
	{
		delete [] vertexColors;
	}
	verticesCount = cubeVertexDataLength * pointsW * pointsH;
	vertices = new GLfloat[verticesCount];
	vertexColors = new GLfloat[verticesCount];
}

void key_callback(GLFWwindow* window, int key, int scancode, int action, int mods)
{
	if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
	{
		glfwSetWindowShouldClose(window, GL_TRUE);
	}
}

void mouse_button_callback(GLFWwindow* window, int button, int action, int mods)
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

void cursor_position_callback(GLFWwindow* window, double x, double y)
{

}

void cleanup()
{
	glDeleteBuffers(1, &vertexBuffer);
	glDeleteVertexArrays(1, &vertexArray);
	glDeleteProgram(shaderProgram);
}

int main(void)
{
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

	GLFWwindow* window = glfwCreateWindow(800, 600, "Nostalgia", NULL, NULL);
	if (!window)
	{
		printf("Error: can't create a window");
		glfwTerminate();
		return EXIT_FAILURE;
	}

	printf("shader lang: %s\n", glGetString(GL_SHADING_LANGUAGE_VERSION));

	glfwSetFramebufferSizeCallback(window, reshape);
	glfwSetKeyCallback(window, key_callback);
	glfwSetMouseButtonCallback(window, mouse_button_callback);
	glfwSetCursorPosCallback(window, cursor_position_callback);

	glfwMakeContextCurrent(window);
	glfwSwapInterval(1);

	int width, height;
	glfwGetFramebufferSize(window, &width, &height);

	glfwSetTime(0.0);

	reshape(window, width, height);
	init();

	double t, t_old, dt;
	bool finish = false;

	while (!finish)
	{
		TimeMeasurer tm("loop");
		t = glfwGetTime();
		dt = t - t_old;
		t_old = t;

		makeModel();
		display();

		glfwSwapBuffers(window);
		glfwPollEvents();

		if (glfwWindowShouldClose(window))
		{
			finish = true;
		}
		tm.count();
	}
	cleanup();


	glfwTerminate();
	return EXIT_SUCCESS;
}

