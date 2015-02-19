/*
 * Group.h
 *
 *  Created on: 19 ????. 2015 ?.
 *      Author: il
 */

#ifndef GROUP_H_
#define GROUP_H_

#include <stddef.h>

struct GroupHandlers
{
	virtual bool frameHandler(float* r, float* g, float* b) = 0;
	virtual bool resizeHandler(int pointsWidthCount, int pointsHeightCount) = 0;
};

class Group {
private:
	GLuint shaderProgram;
	GLuint vertexArray;
	bool buffersAllocated = false;
	GLuint vertexBuffer, colorBuffer;
	glm::mat4 proportional;

	GLfloat* vertexData = NULL;
	GLfloat* vertexColorData = NULL;
	int verticesCount = 0;
	float *r = NULL, *g = NULL, *b = NULL;

	GroupHandlers* handlers;
protected:
	void create_shader_program();
	void makeModel();
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
public:
	Group();

	void resize(int w, int h);
	void display();
	void setHandlers(GroupHandlers* Handlers);

	virtual ~Group();
};

#endif /* GROUP_H_ */
