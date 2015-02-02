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
		jclass frameHandlerClass;
		jmethodID frameHandlerFrameMethod;
		jfieldID frameHandlerRField;
		jfieldID frameHandlerGField;
		jfieldID frameHandlerBField;

		jobject frameHandler;

		frameHandlerJNICustom(JNIEnv* env, jobject frameHandler)
		{
			this->env = env;
			this->frameHandler = frameHandler;

			frameHandlerClass = env->FindClass(CLASS_FRAMEHANDLER);
			if (frameHandlerClass == NULL) {
				std::cout << "JNI problem: can't find " << CLASS_FRAMEHANDLER << " class";
			}
			frameHandlerFrameMethod = env->GetMethodID(frameHandlerClass, METHOD_FRAMEHANDLER_FRAME, METHOD_FRAMEHANDLER_FRAME_SIG);
			if (frameHandlerFrameMethod == NULL) {
				std::cout << "JNI problem: can't find " << METHOD_FRAMEHANDLER_FRAME << " method with signature " << METHOD_FRAMEHANDLER_FRAME_SIG << " in class " << CLASS_FRAMEHANDLER;
			}
			frameHandlerRField = env->GetFieldID(frameHandlerClass, FIELD_FRAMEHANDLER_R, FIELD_FRAMEHANDLER_R_SIG);
			if (frameHandlerRField == NULL) {
				std::cout << "JNI problem: can't find " << FIELD_FRAMEHANDLER_R << " field with signature " << FIELD_FRAMEHANDLER_R_SIG << " in class " << CLASS_FRAMEHANDLER;
			}
			frameHandlerGField = env->GetFieldID(frameHandlerClass, FIELD_FRAMEHANDLER_G, FIELD_FRAMEHANDLER_G_SIG);
			if (frameHandlerGField == NULL) {
				std::cout << "JNI problem: can't find " << FIELD_FRAMEHANDLER_G << " field with signature " << FIELD_FRAMEHANDLER_G_SIG << " in class " << CLASS_FRAMEHANDLER;
			}
			frameHandlerBField = env->GetFieldID(frameHandlerClass, FIELD_FRAMEHANDLER_B, FIELD_FRAMEHANDLER_B_SIG);
			if (frameHandlerBField == NULL) {
				std::cout << "JNI problem: can't find " << FIELD_FRAMEHANDLER_B << " field with signature " << FIELD_FRAMEHANDLER_B_SIG << " in class " << CLASS_FRAMEHANDLER;
			}

		}
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
		frameHandlerJNICustom* fHCustom = (frameHandlerJNICustom*)custom;
		JNIEnv* env = fHCustom->env;

		jfloatArray rArray = (jfloatArray)env->GetObjectField(fHCustom->frameHandler, fHCustom->frameHandlerRField);
		jfloatArray gArray = (jfloatArray)env->GetObjectField(fHCustom->frameHandler, fHCustom->frameHandlerGField);
		jfloatArray bArray = (jfloatArray)env->GetObjectField(fHCustom->frameHandler, fHCustom->frameHandlerBField);

		int rSize = env->GetArrayLength(rArray);
		int gSize = env->GetArrayLength(gArray);
		int bSize = env->GetArrayLength(bArray);

		env->CallVoidMethod(fHCustom->frameHandler, fHCustom->frameHandlerFrameMethod);

		env->GetFloatArrayRegion(rArray, 0, rSize, r);
		env->GetFloatArrayRegion(gArray, 0, gSize, g);
		env->GetFloatArrayRegion(bArray, 0, bSize, b);
	}


	JNIEXPORT jint JNICALL Java_nostalgia_JNILayer_mainLoop(JNIEnv* env, jclass clz, jstring title, jint windowWidth, jint windowHeight, jint pixelsPerPoint, jobject frameHandler)
	{
		const char* titleChars = env->GetStringUTFChars(title, NULL);

		frameHandlerJNICustom custom(env, frameHandler);

		int res = mainLoop(titleChars, windowWidth, windowHeight, pixelsPerPoint, &frame_handler_callback, &resize_handler_callback, &custom);

		env->ReleaseStringUTFChars(title, titleChars);

		return res;
	}
}
