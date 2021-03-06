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
#include <glm/gtx/transform.hpp>

#include "main.hpp"
#include "Group.h"

using namespace std;

static const GLfloat cubeVertexData[] = {
	0.5f, 0.5f,0.0f, // triangle 2 : begin
	-0.5f,-0.5f,0.0f,
	-0.5f, 0.5f,0.0f, // triangle 2 : end
	0.5f, 0.5f,0.0f,
	0.5f,-0.5f,0.0f,
	-0.5f,-0.5f,0.0f,
};


/*static const GLfloat cubeVertexData[] = {
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

Group::Group(bool hasAlpha) : buffersAllocated(false),
        vertexData(NULL), vertexColorData(NULL), verticesCount(0),
        pointsWidthCount(0), pointsHeightCount(0),
        constructed(false), r(NULL), g(NULL), b(NULL), a(NULL)
{
	this->hasAlpha = hasAlpha;
	addGroup(this);
}

void Group::createShaderProgram()
{
	stringstream ss;
    ss << "#version 330 core\n";
    ss << "uniform mat4 globalTrans;\n";
    ss << "in vec3 vertex;\n";
    ss << "in vec4 vertexColor;\n";
    ss << "out vec4 fragmentColor;\n";
    ss << "void main() {\n";
    ss << "    gl_Position = globalTrans * vec4(vertex, 1.0);\n";
    ss << "    gl_Position.w = 1.0;\n";
    ss << "    fragmentColor = vertexColor;\n";
    ss << "}\n";
    char const * vertexSource = strdup(ss.str().c_str());

    ss.str(string());
    ss << "#version 330 core\n";
    ss << "in vec4 fragmentColor;\n";
    ss << "out vec4 color;\n";
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
	float *rIter = r, *gIter = g, *bIter = b, *aIter;

	if (hasAlpha) aIter = a;
	for (int j = 0; j < pointsHeightCount; j++)
	{
		for (int i = 0; i < pointsWidthCount; i++)
		{
			// Geometry
			GLfloat pixelGeometry[cubeVertexDataLength];
			memcpy(pixelGeometry, cubeVertexData, cubeVertexDataLength * sizeof(GLfloat));

			glm::mat4 trans = glm::translate(glm::vec3(-(float)pointsWidthCount / 2 + i + 0.5f, (float)pointsHeightCount / 2 - j - 0.5f, 0.0f));

			applyMatrix(&pixelGeometry[0], cubeVertexDataLength / 3, trans);
			memcpy(vertexIter, pixelGeometry, cubeVertexDataLength * sizeof(GLfloat));
			vertexIter += cubeVertexDataLength;

			// Colors
			for (int k = 0; k < cubeVertexDataLength / 3; k++)
			{
				*vertexColorIter++ = *rIter;
				*vertexColorIter++ = *gIter;
				*vertexColorIter++ = *bIter;
				if (hasAlpha) {
					*vertexColorIter++ = *aIter;
				} else {
					*vertexColorIter++ = 1.0f;
				}
			}
			rIter++; gIter++; bIter++;
			if (hasAlpha) aIter++;
		}
	}
}

void Group::display(const Transform& transform)
{
	if (buffersAllocated)
	{
		//glEnable(GL_CULL_FACE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glClear(GL_DEPTH_BUFFER_BIT);


		GLint globalTransLocation = glGetUniformLocation(shaderProgram, "globalTrans");
		GLint vertexLocation = glGetAttribLocation(shaderProgram, "vertex");
		GLint vertexColorLocation = glGetAttribLocation(shaderProgram, "vertexColor");

		glm::mat4 trans = glm::translate(glm::vec3(-(float)screenWidth / 2 + (float)pointsWidthCount / 2 + 1, (float)screenHeight / 2 - (float)pointsHeightCount / 2 - 1, 0.0f));

		glm::mat4 globalTrans = screenMatrix * trans * transform.getMatrix();// *
				/*glm::rotate(glm::mat4(1.0f), (glm::mediump_float)3.14159 / 4, glm::vec3(0,0,1)) **/
				/*glm::translate(glm::vec3(x, -y, 0.0f))*/;

		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
		glBufferData(GL_ARRAY_BUFFER, verticesCount * sizeof(GLfloat) * 3, &vertexData[0], GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
		glBufferData(GL_ARRAY_BUFFER, verticesCount * sizeof(GLfloat) * 4, &vertexColorData[0], GL_STATIC_DRAW);

		glUseProgram(shaderProgram);
		glUniformMatrix4fv(globalTransLocation, 1, GL_FALSE, glm::value_ptr(globalTrans));
		glEnableVertexAttribArray(vertexLocation);
		glEnableVertexAttribArray(vertexColorLocation);

		{
			glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
			glVertexAttribPointer(
					vertexLocation,
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
					vertexColorLocation,
					4,                  // size
					GL_FLOAT,           // type
					GL_FALSE,           // normalized?
					0,                  // stride
					(void*) 0			// array buffer offset
			);
		}

		glDrawArrays(GL_TRIANGLES, 0, verticesCount);

		glDisableVertexAttribArray(vertexLocation);
		glDisableVertexAttribArray(vertexColorLocation);
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

	if (vertexData != NULL)
	{
		delete [] vertexData;
	}
	if (vertexColorData != NULL)
	{
		delete [] vertexColorData;
	}

	int cubeVerticesCount = sizeof(cubeVertexData) / sizeof(GLfloat) / 3;
	verticesCount = cubeVerticesCount * pointsWidthCount * pointsHeightCount;

	vertexData = new GLfloat[verticesCount * 3];
	vertexColorData = new GLfloat[verticesCount * 4];

	if (r != NULL) { delete [] r; r = NULL; }
	if (g != NULL) { delete [] g; g = NULL; }
	if (b != NULL) { delete [] b; b = NULL; }
	if (a != NULL) { delete [] a; a = NULL; }

	r = new float[pointsWidthCount * pointsHeightCount];
	g = new float[pointsWidthCount * pointsHeightCount];
	b = new float[pointsWidthCount * pointsHeightCount];
	if (hasAlpha) {
		a = new float[pointsWidthCount * pointsHeightCount];
	}

	allocateBuffers();

}

Group::~Group() {
	removeGroup(this);

	freeBuffers();

	glDeleteVertexArrays(1, &vertexArray);
	glDeleteProgram(shaderProgram);

	if (r != NULL) { delete [] r; r = NULL; }
	if (g != NULL) { delete [] g; g = NULL; }
	if (b != NULL) { delete [] b; b = NULL; }
	if (a != NULL) { delete [] a; a = NULL; }

}

