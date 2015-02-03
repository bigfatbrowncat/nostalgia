/*
 * JNILayer.cpp
 *
 *  Created on: 02 ????. 2015 ?.
 *      Author: il
 */

#include <iostream>

#include "jni.h"

#include "JNILayer.hpp"

#define CLASS_HANDLER					"nostalgia/JNILayer$Handler"
#define METHOD_HANDLER_FRAME			"frame"
#define METHOD_HANDLER_FRAME_SIG		"()V"
#define METHOD_HANDLER_SETSIZE			"setSize"
#define METHOD_HANDLER_SETSIZE_SIG		"(II)V"
#define METHOD_HANDLER_MOUSEMOVE		"mouseMove"
#define METHOD_HANDLER_MOUSEMOVE_SIG	"(DD)V"
#define FIELD_HANDLER_R					"r"
#define FIELD_HANDLER_R_SIG				"[F"
#define FIELD_HANDLER_G					"g"
#define FIELD_HANDLER_G_SIG				"[F"
#define FIELD_HANDLER_B					"b"
#define FIELD_HANDLER_B_SIG				"[F"

#define RESULT_ERROR					1

extern "C"
{
	// handler implementation
	struct handlerJNICustom
	{
		JNIEnv* env;
		jclass handlerClass;
		jmethodID handlerFrameMethod;
		jmethodID handlerMouseMoveMethod;
		jfieldID handlerRField;
		jfieldID handlerGField;
		jfieldID handlerBField;

		jobject handler;

		handlerJNICustom(JNIEnv* env, jobject handler)
		{
			this->env = env;
			this->handler = handler;

			handlerClass = env->FindClass(CLASS_HANDLER);
			if (handlerClass == NULL) {
				std::cout << "JNI problem: can't find " << CLASS_HANDLER << " class";
			}

			handlerFrameMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_FRAME, METHOD_HANDLER_FRAME_SIG);
			if (handlerFrameMethod == NULL) {
				std::cout << "JNI problem: can't find " << METHOD_HANDLER_FRAME << " method with signature " << METHOD_HANDLER_FRAME_SIG << " in class " << CLASS_HANDLER;
			}
			handlerMouseMoveMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_MOUSEMOVE, METHOD_HANDLER_MOUSEMOVE_SIG);
			if (handlerMouseMoveMethod == NULL) {
				std::cout << "JNI problem: can't find " << METHOD_HANDLER_MOUSEMOVE << " method with signature " << METHOD_HANDLER_MOUSEMOVE_SIG << " in class " << CLASS_HANDLER;
			}

			handlerRField = env->GetFieldID(handlerClass, FIELD_HANDLER_R, FIELD_HANDLER_R_SIG);
			if (handlerRField == NULL) {
				std::cout << "JNI problem: can't find " << FIELD_HANDLER_R << " field with signature " << FIELD_HANDLER_R_SIG << " in class " << CLASS_HANDLER;
			}
			handlerGField = env->GetFieldID(handlerClass, FIELD_HANDLER_G, FIELD_HANDLER_G_SIG);
			if (handlerGField == NULL) {
				std::cout << "JNI problem: can't find " << FIELD_HANDLER_G << " field with signature " << FIELD_HANDLER_G_SIG << " in class " << CLASS_HANDLER;
			}
			handlerBField = env->GetFieldID(handlerClass, FIELD_HANDLER_B, FIELD_HANDLER_B_SIG);
			if (handlerBField == NULL) {
				std::cout << "JNI problem: can't find " << FIELD_HANDLER_B << " field with signature " << FIELD_HANDLER_B_SIG << " in class " << CLASS_HANDLER;
			}

		}
	};

	void resize_handler_callback(int pointsWidthCount, int pointsHeightCount, void* custom)
	{
		JNIEnv* env = ((handlerJNICustom*)custom)->env;

		jclass handlerClass = env->FindClass(CLASS_HANDLER);
		if (handlerClass == NULL) {
			std::cout << "JNI problem: can't find " << CLASS_HANDLER << " class";
		}
		jmethodID handlerSetSizeMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_SETSIZE, METHOD_HANDLER_SETSIZE_SIG);
		if (handlerSetSizeMethod == NULL) {
			std::cout << "JNI problem: can't find " << METHOD_HANDLER_SETSIZE << " method with signature " << METHOD_HANDLER_SETSIZE_SIG << " in class " << CLASS_HANDLER;
		}

		jobject handler = ((handlerJNICustom*)custom)->handler;
		env->CallVoidMethod(handler, handlerSetSizeMethod, pointsWidthCount, pointsHeightCount);
	}


	void frame_handler_callback(float* r, float* g, float* b, void* custom)
	{
		handlerJNICustom* fHCustom = (handlerJNICustom*)custom;
		JNIEnv* env = fHCustom->env;

		jfloatArray rArray = (jfloatArray)env->GetObjectField(fHCustom->handler, fHCustom->handlerRField);
		jfloatArray gArray = (jfloatArray)env->GetObjectField(fHCustom->handler, fHCustom->handlerGField);
		jfloatArray bArray = (jfloatArray)env->GetObjectField(fHCustom->handler, fHCustom->handlerBField);

		int rSize = env->GetArrayLength(rArray);
		int gSize = env->GetArrayLength(gArray);
		int bSize = env->GetArrayLength(bArray);

		env->CallVoidMethod(fHCustom->handler, fHCustom->handlerFrameMethod);

		env->GetFloatArrayRegion(rArray, 0, rSize, r);
		env->GetFloatArrayRegion(gArray, 0, gSize, g);
		env->GetFloatArrayRegion(bArray, 0, bSize, b);
	}

	void mouse_move_handler_callback(double xPoints, double yPoints, void* custom)
	{
		handlerJNICustom* fHCustom = (handlerJNICustom*)custom;
		JNIEnv* env = fHCustom->env;

		env->CallVoidMethod(fHCustom->handler, fHCustom->handlerMouseMoveMethod, xPoints, yPoints);
	}


	JNIEXPORT jint JNICALL Java_nostalgia_JNILayer_mainLoop(JNIEnv* env, jclass clz, jstring title, jint windowWidth, jint windowHeight, jint pixelsPerPoint, jobject handler)
	{
		const char* titleChars = env->GetStringUTFChars(title, NULL);

		handlerJNICustom custom(env, handler);

		int res = mainLoop(titleChars, windowWidth, windowHeight, pixelsPerPoint,
		                   &frame_handler_callback, &resize_handler_callback, &mouse_move_handler_callback, &custom);

		env->ReleaseStringUTFChars(title, titleChars);

		return res;
	}
}
