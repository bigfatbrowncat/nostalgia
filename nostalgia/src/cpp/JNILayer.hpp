#ifndef JNILAYER_HPP_
#define JNILAYER_HPP_

#include "jni.h"

#include "main.hpp"


extern "C"
{
	JNIEXPORT jint JNICALL Java_nostalgia_JNILayer_mainLoop(JNIEnv* env, jclass clz, jstring title, jint windowWidth, jint windowHeight, jint pixelsPerPoint);
}

#endif /* JNILAYER_HPP_ */
