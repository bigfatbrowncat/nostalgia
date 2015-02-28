/*
 * Group.cpp
 *
 *  Created on: 19 ????. 2015 ?.
 *      Author: il
 */

#include <iostream>
#include <sstream>
#include <vector>

#include <GL3/gl3w.h>
#include <glm/glm.hpp>
#include <glm/ext.hpp>

#include "main.hpp"
#include "Group.h"

using namespace std;

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

void Group::lazyConstruct() {
	if (!constructed) {
		glGenVertexArrays(1, &vertexArray);
		glBindVertexArray(vertexArray);

		createShaderProgram();
		constructed = true;
	}
}

Group::Group() {
	addGroup(this);
}

void Group::createShaderProgram()
{
	stringstream ss;
    ss << "#version 330 core\n";
    ss << "uniform mat4 globalTrans;\n";
    ss << "layout(location = " << VERTEX_INDEX << ") in vec3 vertex;\n";
    ss << "layout(location = " << COLOR_INDEX << ") in vec3 vertexColor;\n";
    ss << "out vec3 fragmentColor;\n";
    ss << "void main() {\n";
    ss << "    gl_Position = globalTrans * vec4(vertex, 1.0);\n";
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
    std::cout << "Compiling vertex shader..." << std::endl;
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

void Group::makeModel()
{
	lazyConstruct();
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

			glm::mat4 trans = glm::translate(glm::vec3(-(float)screenWidth / 2 + i + 0.5f, (float)screenHeight / 2 - j - 0.5f, 0.0f));

			applyMatrix(&pixelGeometry[0], cubeVertexDataLength / 3, trans);
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
}

void Group::display(float x, float y)
{
	if (buffersAllocated)
	{
		glm::mat4 globalTrans = globalMatrix * glm::translate(glm::vec3(x, -y, 0.0f));

		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
		glBufferData(GL_ARRAY_BUFFER, verticesCount * sizeof(GLfloat), &vertexData[0], GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
		glBufferData(GL_ARRAY_BUFFER, verticesCount * sizeof(GLfloat), &vertexColorData[0], GL_STATIC_DRAW);

		glUseProgram(shaderProgram);
		glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "globalTrans"), 1, GL_FALSE, glm::value_ptr(globalTrans));
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

		glClear(GL_DEPTH_BUFFER_BIT);
		glDrawArrays(GL_TRIANGLES, 0, verticesCount);

		glDisableVertexAttribArray(VERTEX_INDEX);
		glDisableVertexAttribArray(COLOR_INDEX);
	}
	else
	{
		cerr << "Buffers aren't allocated! Call resize() on the group at least once" << endl;
	}
}

void Group::allocateBuffers()
{
	glGenBuffers(1, &vertexBuffer);
	glGenBuffers(1, &colorBuffer);
	buffersAllocated = true;
}

void Group::freeBuffers()
{
	if (buffersAllocated)
	{
		glDeleteBuffers(1, &vertexBuffer);
		glDeleteBuffers(1, &colorBuffer);
		buffersAllocated = false;
	}
}

void Group::resize(int pointsWidthCount, int pointsHeightCount)
{
	freeBuffers();

	this->pointsWidthCount = pointsWidthCount;
	this->pointsHeightCount = pointsHeightCount;

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

	allocateBuffers();

}

Group::~Group() {
	removeGroup(this);

	freeBuffers();

	glDeleteVertexArrays(1, &vertexArray);
	glDeleteProgram(shaderProgram);
}

