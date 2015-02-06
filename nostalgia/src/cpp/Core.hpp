#ifndef CORE_HPP_
#define CORE_HPP_

#include "jni.h"

#include "main.hpp"


extern "C"
{
	JNIEXPORT jboolean JNICALL Java_nostalgia_Core_run(JNIEnv* env, jclass clz, jobject handler);
	JNIEXPORT jboolean JNICALL Java_nostalgia_Core_open(JNIEnv* env, jclass clz, jstring title, jint windowWidth, jint windowHeight, jint pixelsPerPoint);
	JNIEXPORT void JNICALL Java_nostalgia_Core_setCursorVisibility(JNIEnv* env, jclass clz, jboolean visible);
}

#endif /* CORE_HPP_ */
