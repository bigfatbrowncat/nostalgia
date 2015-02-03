#ifndef JNILAYER_HPP_
#define JNILAYER_HPP_

#include "jni.h"

#include "main.hpp"


extern "C"
{
	JNIEXPORT jboolean JNICALL Java_nostalgia_JNILayer_mainLoop(JNIEnv* env, jclass clz, jobject handler);
	JNIEXPORT jboolean JNICALL Java_nostalgia_JNILayer_createWindow(JNIEnv* env, jclass clz, jstring title, jint windowWidth, jint windowHeight, jint pixelsPerPoint);
	JNIEXPORT void JNICALL Java_nostalgia_JNILayer_setCursorVisibility(JNIEnv* env, jclass clz, jboolean visible);
}

#endif /* JNILAYER_HPP_ */
