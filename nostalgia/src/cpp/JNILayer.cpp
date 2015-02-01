/*
 * JNILayer.cpp
 *
 *  Created on: 02 ????. 2015 ?.
 *      Author: il
 */

#include "JNILayer.hpp"

extern "C"
{
	JNIEXPORT jint JNICALL Java_nostalgia_JNILayer_mainLoop(JNIEnv* env, jclass clz, jstring title, jint windowWidth, jint windowHeight, jint pixelsPerPoint)
	{
		const char* titleChars = env->GetStringUTFChars(title, NULL);

		int res = mainLoop(titleChars, windowWidth, windowHeight, pixelsPerPoint);

		env->ReleaseStringUTFChars(title, titleChars);

		return res;
	}
}
