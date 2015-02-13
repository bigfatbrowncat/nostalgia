#ifndef CORE_HPP_
#define CORE_HPP_

#include "jni.h"

#include "main.hpp"

// Core handler implementation
struct CoreHandlers : public handlers
{
	JNIEnv* env;
	jclass handlerClass;
	jmethodID handlerFrameMethod;
	jmethodID handlerResizeMethod;
	jmethodID handlerMouseMoveMethod;
	jmethodID handlerMouseButtonMethod;
	jmethodID handlerKeyMethod;
	jmethodID handlerCharacterMethod;
	jfieldID handlerRField;
	jfieldID handlerGField;
	jfieldID handlerBField;

	jobject handler;

	CoreHandlers(JNIEnv* env, jobject handler);

	void resizeHandler(int pointsWidthCount, int pointsHeightCount);
	void frameHandler(float* r, float* g, float* b);
	void mouseMoveHandler(double xPoints, double yPoints);
	void mouseButtonHandler(int button, int action, int mods);
	void keyHandler(int key, int scancode, int action, int mods);
	void characterHandler(unsigned int character, int mods);
};


extern "C"
{
	JNIEXPORT jboolean JNICALL Java_nostalgia_Core_run(JNIEnv* env, jclass clz, jobject handler);
	JNIEXPORT jboolean JNICALL Java_nostalgia_Core_open(JNIEnv* env, jclass clz, jstring title, jint windowWidth, jint windowHeight, jint pixelsPerPoint);
	JNIEXPORT void JNICALL Java_nostalgia_Core_setCursorVisibility(JNIEnv* env, jclass clz, jboolean visible);
	JNIEXPORT void JNICALL Java_nostalgia_Core_close(JNIEnv* env, jclass clz);
}

#endif /* CORE_HPP_ */
