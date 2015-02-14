#include <iostream>

#include "jni.h"

#include "Core.hpp"

extern "C"
{
	JNIEXPORT jlong JNICALL Java_nostalgia_Handler_createNative(JNIEnv* env, jobject obj)
	{
		CoreHandlers* handlers = new CoreHandlers(env, obj);
		std::cout << "CoreHandlers created: " << handlers << std::endl;
		return (jlong)handlers;
	}

	JNIEXPORT void JNICALL Java_nostalgia_Handler_destroyNative(JNIEnv* env, jobject obj, jlong address)
	{
		CoreHandlers* handlers = (CoreHandlers*)address;
		std::cout << "CoreHandlers destroyed: " << handlers << std::endl;
		delete handlers;
	}
}
