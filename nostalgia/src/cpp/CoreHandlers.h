/*
 * CoreHandlers.h
 *
 *  Created on: 22 ????. 2015 ?.
 *      Author: il
 */

#ifndef COREHANDLERS_H_
#define COREHANDLERS_H_

#include "jni.h"

#include "main.hpp"

// Core handler implementation
class CoreHandlers : public Handlers
{
private:
	JNIEnv* env;
	jclass handlerClass;
	jmethodID handlerMouseMoveMethod;
	jmethodID handlerMouseButtonMethod;
	jmethodID handlerKeyMethod;
	jmethodID handlerCharacterMethod;
	jmethodID handlerFrameMethod;
	jmethodID handlerResizeMethod;

	jobject handler;
public:
	CoreHandlers(JNIEnv* env, jobject handler);
	virtual ~CoreHandlers();

	virtual bool resizeHandler(int pointsWidthCount, int pointsHeightCount);
	virtual bool frameHandler();
	virtual bool mouseMoveHandler(double xPoints, double yPoints);
	virtual bool mouseButtonHandler(int button, int action, int mods);
	virtual bool keyHandler(int key, int scancode, int action, int mods);
	virtual bool characterHandler(unsigned int character, int mods);
};

#endif /* COREHANDLERS_H_ */
