#include <iostream>

#include "jni.h"

#include "Core.hpp"

#define CLASS_HANDLER						"nostalgia/Handler"
#define FIELD_HANDLER_NATIVE_ADDRESS		"nativeAddress"
#define FIELD_HANDLER_NATIVE_ADDRESS_SIG	"J"

extern "C"
{
	JNIEXPORT jlong JNICALL Java_nostalgia_Handler_createNative(JNIEnv* env, jobject obj)
	{
		CoreHandlers* handlers = new CoreHandlers(env, obj);
		return (jlong)handlers;
	}

	JNIEXPORT void JNICALL Java_nostalgia_Handler_destroyNative(JNIEnv* env, jobject obj, jlong address)
	{
		CoreHandlers* handlers = (CoreHandlers*)address;
		delete handlers;
	}
}
