#ifndef CORE_HPP_
#define CORE_HPP_

#include "jni.h"

#include "Core.hpp"

extern "C"
{
	JNIEXPORT jlong JNICALL Java_nostalgia_Handler_createNative(JNIEnv* env, jobject obj);
	JNIEXPORT void JNICALL Java_nostalgia_Handler_destroyNative(JNIEnv* env, jobject obj, jlong address);
}

#endif /* CORE_HPP_ */
