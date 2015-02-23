/*
 * CoreHandlers.cpp
 *
 *  Created on: 22 ????. 2015 ?.
 *      Author: il
 */

#include <iostream>

#include "CoreHandlers.h"

#define CLASS_HANDLER					"nostalgia/Handler"
#define METHOD_HANDLER_FRAME			"frame"
#define METHOD_HANDLER_FRAME_SIG		"()V"
#define METHOD_HANDLER_RESIZE			"innerResize"
#define METHOD_HANDLER_RESIZE_SIG		"(II)V"
#define METHOD_HANDLER_MOUSEMOVE		"mouseMove"
#define METHOD_HANDLER_MOUSEMOVE_SIG	"(DD)V"
#define METHOD_HANDLER_MOUSEBUTTON		"innerMouseButton"
#define METHOD_HANDLER_MOUSEBUTTON_SIG	"(III)V"
#define METHOD_HANDLER_KEY				"innerKey"
#define METHOD_HANDLER_KEY_SIG			"(IIII)V"
#define METHOD_HANDLER_CHARACTER		"innerCharacter"
#define METHOD_HANDLER_CHARACTER_SIG	"(CI)V"

CoreHandlers::CoreHandlers(JNIEnv* env, jobject handler)
{
	this->env = env;
	this->handler = env->NewGlobalRef(handler);

	jclass hc = env->FindClass(CLASS_HANDLER);
	if (hc == NULL) {
		std::cout << "JNI problem: can't find " << CLASS_HANDLER << " class";
	}
	handlerClass = (jclass)env->NewGlobalRef(hc);

	handlerMouseMoveMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_MOUSEMOVE, METHOD_HANDLER_MOUSEMOVE_SIG);
	if (handlerMouseMoveMethod == NULL) {
		std::cout << "JNI problem: can't find " << METHOD_HANDLER_MOUSEMOVE << " method with signature " << METHOD_HANDLER_MOUSEMOVE_SIG << " in class " << CLASS_HANDLER;
	}
	handlerMouseButtonMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_MOUSEBUTTON, METHOD_HANDLER_MOUSEBUTTON_SIG);
	if (handlerMouseButtonMethod == NULL) {
		std::cout << "JNI problem: can't find " << METHOD_HANDLER_MOUSEBUTTON << " method with signature " << METHOD_HANDLER_MOUSEBUTTON_SIG << " in class " << CLASS_HANDLER;
	}
	handlerKeyMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_KEY, METHOD_HANDLER_KEY_SIG);
	if (handlerKeyMethod == NULL) {
		std::cout << "JNI problem: can't find " << METHOD_HANDLER_KEY << " method with signature " << METHOD_HANDLER_KEY_SIG << " in class " << CLASS_HANDLER;
	}
	handlerCharacterMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_CHARACTER, METHOD_HANDLER_CHARACTER_SIG);
	if (handlerCharacterMethod == NULL) {
		std::cout << "JNI problem: can't find " << METHOD_HANDLER_CHARACTER << " method with signature " << METHOD_HANDLER_CHARACTER_SIG << " in class " << CLASS_HANDLER;
	}

	handlerFrameMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_FRAME, METHOD_HANDLER_FRAME_SIG);
	if (handlerFrameMethod == NULL) {
		std::cout << "JNI problem: can't find " << METHOD_HANDLER_FRAME << " method with signature " << METHOD_HANDLER_FRAME_SIG << " in class " << CLASS_HANDLER;
	}
	handlerResizeMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_RESIZE, METHOD_HANDLER_RESIZE_SIG);
	if (handlerResizeMethod == NULL) {
		std::cout << "JNI problem: can't find " << METHOD_HANDLER_RESIZE << " method with signature " << METHOD_HANDLER_RESIZE_SIG << " in class " << CLASS_HANDLER;
	}
}

CoreHandlers::~CoreHandlers() {
	env->DeleteGlobalRef(handler);
	env->DeleteGlobalRef(handlerClass);
}


bool CoreHandlers::mouseMoveHandler(double xPoints, double yPoints)
{
	env->CallVoidMethod(handler, handlerMouseMoveMethod, xPoints, yPoints);
	return env->ExceptionCheck() == JNI_FALSE;
}

bool CoreHandlers::mouseButtonHandler(int button, int action, int mods)
{
	env->CallVoidMethod(handler, handlerMouseButtonMethod, button, action, mods);
	return env->ExceptionCheck() == JNI_FALSE;
}

bool CoreHandlers::keyHandler(int key, int scancode, int action, int mods)
{
	env->CallVoidMethod(handler, handlerKeyMethod, key, scancode, action, mods);
	return env->ExceptionCheck() == JNI_FALSE;
}

bool CoreHandlers::characterHandler(unsigned int character, int mods)
{
	env->CallVoidMethod(handler, handlerCharacterMethod, character, mods);
	return env->ExceptionCheck() == JNI_FALSE;
}

bool CoreHandlers::resizeHandler(int pointsWidthCount, int pointsHeightCount)
{
	env->CallVoidMethod(handler, handlerResizeMethod, pointsWidthCount, pointsHeightCount);

	return env->ExceptionCheck() == JNI_FALSE;
}

bool CoreHandlers::frameHandler()
{
	env->CallVoidMethod(handler, handlerFrameMethod);

	return env->ExceptionCheck() == JNI_FALSE;
}

