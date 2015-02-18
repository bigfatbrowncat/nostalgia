#ifndef CORE_HPP_
#define CORE_HPP_

#include "jni.h"

#include "main.hpp"

// Core handler implementation
class CoreHandlers : public handlers
{
private:
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
	jfieldID handlerPointsWidthCountField;
	jfieldID handlerPointsHeightCountField;

	jobject handler;
public:
	CoreHandlers(JNIEnv* env, jobject handler);
	virtual ~CoreHandlers();

	virtual bool resizeHandler(int pointsWidthCount, int pointsHeightCount);
	virtual bool frameHandler(float* r, float* g, float* b);
	virtual bool mouseMoveHandler(double xPoints, double yPoints);
	virtual bool mouseButtonHandler(int button, int action, int mods);
	virtual bool keyHandler(int key, int scancode, int action, int mods);
	virtual bool characterHandler(unsigned int character, int mods);
};


extern "C"
{
	JNIEXPORT void JNICALL Java_nostalgia_Core_setHandler(JNIEnv* env, jclass clz, jobject handler);
	JNIEXPORT jboolean JNICALL Java_nostalgia_Core_run(JNIEnv* env, jclass clz);
	JNIEXPORT jboolean JNICALL Java_nostalgia_Core_open(JNIEnv* env, jclass clz, jstring title, jint windowWidth, jint windowHeight, jint pixelsPerPoint);
	JNIEXPORT void JNICALL Java_nostalgia_Core_setCursorVisibility(JNIEnv* env, jclass clz, jboolean visible);
	JNIEXPORT void JNICALL Java_nostalgia_Core_close(JNIEnv* env, jclass clz);
}

#endif /* CORE_HPP_ */
