#ifndef CORE_HPP_
#define CORE_HPP_

#include "jni.h"

#include <glm/glm.hpp>
#include <glm/ext.hpp>

#include "Group.h"
#include "main.hpp"


extern "C"
{
	JNIEXPORT jlong JNICALL Java_nostalgia_Handler_createNative(JNIEnv* env, jobject obj);
	JNIEXPORT void JNICALL Java_nostalgia_Handler_destroyNative(JNIEnv* env, jobject obj, jlong address);
	JNIEXPORT jlong JNICALL Java_nostalgia_Group_createNative(JNIEnv* env, jobject obj);
	JNIEXPORT void JNICALL Java_nostalgia_Group_destroyNative(JNIEnv* env, jobject obj, jlong address);

	JNIEXPORT void JNICALL Java_nostalgia_Group_updateRGB(JNIEnv* env, jobject group);
	JNIEXPORT void JNICALL Java_nostalgia_Group_innerResize(JNIEnv* env, jobject group, int width, int height);
	JNIEXPORT void JNICALL Java_nostalgia_Group_display(JNIEnv* env, jobject group);

	JNIEXPORT void JNICALL Java_nostalgia_Core_setHandler(JNIEnv* env, jclass clz, jobject handler);
	JNIEXPORT jboolean JNICALL Java_nostalgia_Core_run(JNIEnv* env, jclass clz);
	JNIEXPORT jboolean JNICALL Java_nostalgia_Core_innerOpen(JNIEnv* env, jclass clz, jstring title, jint windowWidth, jint windowHeight, jint pixelsPerPoint);
	JNIEXPORT void JNICALL Java_nostalgia_Core_setCursorVisibility(JNIEnv* env, jclass clz, jboolean visible);
	JNIEXPORT void JNICALL Java_nostalgia_Core_close(JNIEnv* env, jclass clz);
}

#endif /* CORE_HPP_ */
