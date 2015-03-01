/*
 * Group.h
 *
 *  Created on: 19 ????. 2015 ?.
 *      Author: il
 */

#ifndef GROUP_H_
#define GROUP_H_

#include <stddef.h>

#include <GL3/gl3w.h>

#include <glm/glm.hpp>

#include "Transform.h"

class Group {
private:
	GLuint shaderProgram;
	GLuint vertexArray;
	bool buffersAllocated = false;
	GLuint vertexBuffer, colorBuffer;

	GLfloat* vertexData = NULL;
	GLfloat* vertexColorData = NULL;
	int verticesCount = 0;
	int pointsWidthCount = 0, pointsHeightCount = 0;
	int screenWidth, screenHeight;
	bool hasAlpha;

	glm::mat4 screenMatrix;
	glm::mat4 transformMatrix;

	bool constructed = false;
	void lazyConstruct();

	void createShaderProgram();
	void allocateBuffers();
	void freeBuffers();
	inline void applyMatrix(GLfloat vertices[], size_t verticesCount, const glm::mat4& mat)
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
protected:
	float *r = NULL, *g = NULL, *b = NULL, *a = NULL;

	void makeModel();

public:
	Group(bool hasAlpha);

	void resize(int pointsWidthCount, int pointsHeightCount);
	virtual void draw() = 0;
	void display(const Transform& transform);
	void setScreenMatrix(const glm::mat4& screenMatrix) { this->screenMatrix = screenMatrix; }
	void setScreenSize(int width, int height) {
		this->screenWidth = width;
		this->screenHeight = height;
	}
	bool getHasAlpha() { return hasAlpha; }

	virtual ~Group();
};

#endif /* GROUP_H_ */
