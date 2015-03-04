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
	bool buffersAllocated;
	GLuint vertexBuffer, colorBuffer;

	GLfloat* vertexData;
	GLfloat* vertexColorData;
	int verticesCount;
	int pointsWidthCount, pointsHeightCount;
	int screenWidth, screenHeight;
	bool hasAlpha;

	glm::mat4 screenMatrix;

	bool constructed;
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
	float *r, *g, *b, *a;

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
