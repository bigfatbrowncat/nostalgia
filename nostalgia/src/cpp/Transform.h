/*
 * Transform.h
 *
 *  Created on: 02 ????? 2015 ?.
 *      Author: il
 */

#ifndef TRANSFORM_H_
#define TRANSFORM_H_

#include <glm/glm.hpp>
#include <glm/ext.hpp>

class Transform {
private:
	glm::mat4 matrix;

protected:
	Transform(const glm::mat4& matrix) : matrix(matrix) {}

public:
	Transform(const Transform& other) : matrix(other.matrix) {}

	static Transform rotate(float angle, float vx, float vy, float vz) {
		glm::mat4 res = glm::rotate(angle, glm::vec3(vx, vy, vz));
		return Transform(res);
	}

	static Transform scale(float sx, float sy, float sz) {
		glm::mat4 res = glm::scale(glm::vec3(sx, sy, sz));
		return Transform(res);
	}

	static Transform translate(float dx, float dy, float dz) {
		glm::mat4 res = glm::translate(glm::vec3(dx, dy, dz));
		return Transform(res);
	}

	static Transform multiply(const Transform& t1, const Transform& t2) {
		return Transform(t1.getMatrix() * t2.getMatrix());
	}

	const glm::mat4& getMatrix() const {
		return matrix;
	}

	virtual ~Transform();
};

#endif /* TRANSFORM_H_ */
