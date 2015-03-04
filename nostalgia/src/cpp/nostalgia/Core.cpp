#include <iostream>

#include <jni.h>

#include "Handler.h"
#include "Core.h"

namespace nostalgia {

	extern "C" {

		JNIEXPORT void JNICALL Java_nostalgia_Core_setHandler(JNIEnv* env, jclass clz, jobject jhandler)
		{
			jclass handlerClass = env->FindClass(CLASS_HANDLER);
			if (handlerClass == NULL) {
				std::cerr << "JNI problem: can't find " << CLASS_HANDLER << " class";
			}

			jfieldID handlerNativeAddressField = env->GetFieldID(handlerClass, FIELD_HANDLER_NATIVE_ADDRESS, FIELD_HANDLER_NATIVE_ADDRESS_SIG);
			if (handlerNativeAddressField == NULL) {
				std::cerr << "JNI problem: can't find " << FIELD_HANDLER_NATIVE_ADDRESS << " field with signature " << FIELD_HANDLER_NATIVE_ADDRESS_SIG << " in class " << CLASS_HANDLER;
			}

			jlong nativeAddress = env->GetLongField(jhandler, handlerNativeAddressField);
			Handler* handler = (Handler*)nativeAddress;
			setHandlers(handler);
		}


		JNIEXPORT jboolean JNICALL Java_nostalgia_Core_innerRun(JNIEnv* env, jclass clz)
		{
			return mainLoop();
		}

		JNIEXPORT jboolean JNICALL Java_nostalgia_Core_innerOpen(JNIEnv* env, jclass clz, jstring title, jint windowWidth, jint windowHeight, jint pixelsPerPoint)
		{
			const char* titleChars = env->GetStringUTFChars(title, NULL);
			bool res = createWindow(titleChars, windowWidth, windowHeight, pixelsPerPoint);
			env->ReleaseStringUTFChars(title, titleChars);
			return res;
		}

		JNIEXPORT void JNICALL Java_nostalgia_Core_setCursorVisibility(JNIEnv* env, jclass clz, jboolean visible)
		{
			setCursorVisibility(visible);
		}

		JNIEXPORT void JNICALL Java_nostalgia_Core_close(JNIEnv* env, jclass clz)
		{
			closeWindow();
		}

	}

} /* namespace nostalgia */
