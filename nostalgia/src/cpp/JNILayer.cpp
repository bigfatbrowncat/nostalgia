/*
 * JNILayer.cpp
 *
 *  Created on: 02 ????. 2015 ?.
 *      Author: il
 */

#include <iostream>

#include "jni.h"

#include "JNILayer.hpp"

#define CLASS_FRAMEHANDLER				"nostalgia/JNILayer$FrameHandler"
#define METHOD_FRAMEHANDLER_FRAME		"frame"
#define METHOD_FRAMEHANDLER_FRAME_SIG	"()V"
#define METHOD_FRAMEHANDLER_SETSIZE		"setSize"
#define METHOD_FRAMEHANDLER_SETSIZE_SIG	"(II)V"
#define FIELD_FRAMEHANDLER_R			"r"
#define FIELD_FRAMEHANDLER_R_SIG		"[F"
#define FIELD_FRAMEHANDLER_G			"g"
#define FIELD_FRAMEHANDLER_G_SIG		"[F"
#define FIELD_FRAMEHANDLER_B			"b"
#define FIELD_FRAMEHANDLER_B_SIG		"[F"

#define RESULT_ERROR					1

extern "C"
{
	// frame_handler implementation
	struct frameHandlerJNICustom
	{
		JNIEnv* env;
		jobject frameHandler;
	};

	void resize_handler_callback(int pointsWidthCount, int pointsHeightCount, void* custom)
	{
		JNIEnv* env = ((frameHandlerJNICustom*)custom)->env;

		jclass frameHandlerClass = env->FindClass(CLASS_FRAMEHANDLER);
		if (frameHandlerClass == NULL) {
			std::cout << "JNI problem: can't find " << CLASS_FRAMEHANDLER << " class";
		}
		jmethodID frameHandlerSetSizeMethod = env->GetMethodID(frameHandlerClass, METHOD_FRAMEHANDLER_SETSIZE, METHOD_FRAMEHANDLER_SETSIZE_SIG);
		if (frameHandlerSetSizeMethod == NULL) {
			std::cout << "JNI problem: can't find " << METHOD_FRAMEHANDLER_SETSIZE << " method with signature " << METHOD_FRAMEHANDLER_SETSIZE_SIG << " in class " << CLASS_FRAMEHANDLER;
		}

		jobject frameHandler = ((frameHandlerJNICustom*)custom)->frameHandler;
		env->CallVoidMethod(frameHandler, frameHandlerSetSizeMethod, pointsWidthCount, pointsHeightCount);
	}

	void frame_handler_callback(float* r, float* g, float* b, void* custom)
	{
		JNIEnv* env = ((frameHandlerJNICustom*)custom)->env;

		jclass frameHandlerClass = env->FindClass(CLASS_FRAMEHANDLER);
		if (frameHandlerClass == NULL) {
			std::cout << "JNI problem: can't find " << CLASS_FRAMEHANDLER << " class";
		}
		jmethodID frameHandlerFrameMethod = env->GetMethodID(frameHandlerClass, METHOD_FRAMEHANDLER_FRAME, METHOD_FRAMEHANDLER_FRAME_SIG);
		if (frameHandlerFrameMethod == NULL) {
			std::cout << "JNI problem: can't find " << METHOD_FRAMEHANDLER_FRAME << " method with signature " << METHOD_FRAMEHANDLER_FRAME_SIG << " in class " << CLASS_FRAMEHANDLER;
		}
		jfieldID frameHandlerRField = env->GetFieldID(frameHandlerClass, FIELD_FRAMEHANDLER_R, FIELD_FRAMEHANDLER_R_SIG);
		if (frameHandlerRField == NULL) {
			std::cout << "JNI problem: can't find " << FIELD_FRAMEHANDLER_R << " field with signature " << FIELD_FRAMEHANDLER_R_SIG << " in class " << CLASS_FRAMEHANDLER;
		}
		jfieldID frameHandlerGField = env->GetFieldID(frameHandlerClass, FIELD_FRAMEHANDLER_G, FIELD_FRAMEHANDLER_G_SIG);
		if (frameHandlerGField == NULL) {
			std::cout << "JNI problem: can't find " << FIELD_FRAMEHANDLER_G << " field with signature " << FIELD_FRAMEHANDLER_G_SIG << " in class " << CLASS_FRAMEHANDLER;
		}
		jfieldID frameHandlerBField = env->GetFieldID(frameHandlerClass, FIELD_FRAMEHANDLER_B, FIELD_FRAMEHANDLER_B_SIG);
		if (frameHandlerBField == NULL) {
			std::cout << "JNI problem: can't find " << FIELD_FRAMEHANDLER_B << " field with signature " << FIELD_FRAMEHANDLER_B_SIG << " in class " << CLASS_FRAMEHANDLER;
		}

		jobject frameHandler = ((frameHandlerJNICustom*)custom)->frameHandler;

		jfloatArray rArray = (jfloatArray)env->GetObjectField(frameHandler, frameHandlerRField);
		jfloatArray gArray = (jfloatArray)env->GetObjectField(frameHandler, frameHandlerGField);
		jfloatArray bArray = (jfloatArray)env->GetObjectField(frameHandler, frameHandlerBField);

		int rSize = env->GetArrayLength(rArray);
		int gSize = env->GetArrayLength(rArray);
		int bSize = env->GetArrayLength(rArray);

		env->CallVoidMethod(frameHandler, frameHandlerFrameMethod);

		env->GetFloatArrayRegion(rArray, 0, rSize, r);
		env->GetFloatArrayRegion(gArray, 0, gSize, g);
		env->GetFloatArrayRegion(bArray, 0, bSize, b);
	}


	JNIEXPORT jint JNICALL Java_nostalgia_JNILayer_mainLoop(JNIEnv* env, jclass clz, jstring title, jint windowWidth, jint windowHeight, jint pixelsPerPoint, jobject frameHandler)
	{
		const char* titleChars = env->GetStringUTFChars(title, NULL);

		/*jclass frameHandlerClass = env->FindClass(CLASS_FRAMEHANDLER);
		if (frameHandlerClass == NULL) {
			std::cout << "JNI problem: can't find " << CLASS_FRAMEHANDLER << " class";
			return RESULT_ERROR;
		}*/

		frameHandlerJNICustom custom;
		custom.env = env;
		custom.frameHandler = frameHandler;
		int res = mainLoop(titleChars, windowWidth, windowHeight, pixelsPerPoint, &frame_handler_callback, &resize_handler_callback, &custom);

		env->ReleaseStringUTFChars(title, titleChars);

		return res;
	}
}
