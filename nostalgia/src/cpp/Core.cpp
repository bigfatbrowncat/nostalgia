#include <iostream>

#include "jni.h"

#include "Core.hpp"

#define CLASS_HANDLER					"nostalgia/Handler"
#define FIELD_HANDLER_NATIVE_ADDRESS		"nativeAddress"
#define FIELD_HANDLER_NATIVE_ADDRESS_SIG	"J"
#define METHOD_HANDLER_MOUSEMOVE		"mouseMove"
#define METHOD_HANDLER_MOUSEMOVE_SIG	"(DD)V"
#define METHOD_HANDLER_MOUSEBUTTON		"innerMouseButton"
#define METHOD_HANDLER_MOUSEBUTTON_SIG	"(III)V"
#define METHOD_HANDLER_KEY				"innerKey"
#define METHOD_HANDLER_KEY_SIG			"(IIII)V"
#define METHOD_HANDLER_CHARACTER		"innerCharacter"
#define METHOD_HANDLER_CHARACTER_SIG	"(CI)V"

#define CLASS_GROUP_HANDLER					"nostalgia/GroupHandler"
#define METHOD_GROUP_HANDLER_FRAME			"frame"
#define METHOD_GROUP_HANDLER_FRAME_SIG		"()V"
#define METHOD_GROUP_HANDLER_RESIZE			"innerResize"
#define METHOD_GROUP_HANDLER_RESIZE_SIG		"(II)V"
#define FIELD_GROUP_HANDLER_R					"r"
#define FIELD_GROUP_HANDLER_R_SIG				"[F"
#define FIELD_GROUP_HANDLER_G					"g"
#define FIELD_GROUP_HANDLER_G_SIG				"[F"
#define FIELD_GROUP_HANDLER_B					"b"
#define FIELD_GROUP_HANDLER_B_SIG				"[F"
#define FIELD_GROUP_HANDLER_POINTS_WIDTH_COUNT		"pointsWidthCount"
#define FIELD_GROUP_HANDLER_POINTS_WIDTH_COUNT_SIG	"I"
#define FIELD_GROUP_HANDLER_POINTS_HEIGHT_COUNT		"pointsHeightCount"
#define FIELD_GROUP_HANDLER_POINTS_HEIGHT_COUNT_SIG	"I"


CoreHandlers::CoreHandlers(JNIEnv* env, jobject handler)
{
	this->env = env;
	this->handler = env->NewGlobalRef(handler);

	jclass hc = env->FindClass(CLASS_HANDLER);
	if (hc == NULL) {
		std::cout << "JNI problem: can't find " << CLASS_HANDLER << " class";
	}
	handlerClass = (jclass)env->NewGlobalRef(hc);

	handlerMouseMoveMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_MOUSEMOVE, METHOD_HANDLER_MOUSEMOVE_SIG);
	if (handlerMouseMoveMethod == NULL) {
		std::cout << "JNI problem: can't find " << METHOD_HANDLER_MOUSEMOVE << " method with signature " << METHOD_HANDLER_MOUSEMOVE_SIG << " in class " << CLASS_HANDLER;
	}
	handlerMouseButtonMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_MOUSEBUTTON, METHOD_HANDLER_MOUSEBUTTON_SIG);
	if (handlerMouseButtonMethod == NULL) {
		std::cout << "JNI problem: can't find " << METHOD_HANDLER_MOUSEBUTTON << " method with signature " << METHOD_HANDLER_MOUSEBUTTON_SIG << " in class " << CLASS_HANDLER;
	}
	handlerKeyMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_KEY, METHOD_HANDLER_KEY_SIG);
	if (handlerKeyMethod == NULL) {
		std::cout << "JNI problem: can't find " << METHOD_HANDLER_KEY << " method with signature " << METHOD_HANDLER_KEY_SIG << " in class " << CLASS_HANDLER;
	}
	handlerCharacterMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_CHARACTER, METHOD_HANDLER_CHARACTER_SIG);
	if (handlerCharacterMethod == NULL) {
		std::cout << "JNI problem: can't find " << METHOD_HANDLER_CHARACTER << " method with signature " << METHOD_HANDLER_CHARACTER_SIG << " in class " << CLASS_HANDLER;
	}
}

CoreHandlers::~CoreHandlers() {
	env->DeleteGlobalRef(handler);
	env->DeleteGlobalRef(handlerClass);
}


bool CoreHandlers::mouseMoveHandler(double xPoints, double yPoints)
{
	env->CallVoidMethod(handler, handlerMouseMoveMethod, xPoints, yPoints);
	return env->ExceptionCheck() == JNI_FALSE;
}

bool CoreHandlers::mouseButtonHandler(int button, int action, int mods)
{
	env->CallVoidMethod(handler, handlerMouseButtonMethod, button, action, mods);
	return env->ExceptionCheck() == JNI_FALSE;
}

bool CoreHandlers::keyHandler(int key, int scancode, int action, int mods)
{
	env->CallVoidMethod(handler, handlerKeyMethod, key, scancode, action, mods);
	return env->ExceptionCheck() == JNI_FALSE;
}

bool CoreHandlers::characterHandler(unsigned int character, int mods)
{
	env->CallVoidMethod(handler, handlerCharacterMethod, character, mods);
	return env->ExceptionCheck() == JNI_FALSE;
}

bool CoreGroupHandlers::resizeHandler(int pointsWidthCount, int pointsHeightCount)
{
	env->CallVoidMethod(group, groupResizeMethod, pointsWidthCount, pointsHeightCount);

	return env->ExceptionCheck() == JNI_FALSE;
}


bool CoreGroupHandlers::frameHandler(float* r, float* g, float* b)
{
	jfloatArray rArray = (jfloatArray)env->GetObjectField(group, groupRField);
	jfloatArray gArray = (jfloatArray)env->GetObjectField(group, groupGField);
	jfloatArray bArray = (jfloatArray)env->GetObjectField(group, groupBField);

	jint pointsWidthCount = env->GetIntField(group, groupPointsWidthCountField);
	jint pointsHeightCount = env->GetIntField(group, groupPointsHeightCountField);

	int size = pointsWidthCount * pointsHeightCount;

	env->CallVoidMethod(group, groupFrameMethod);

	env->GetFloatArrayRegion(rArray, 0, size, r);
	env->GetFloatArrayRegion(gArray, 0, size, g);
	env->GetFloatArrayRegion(bArray, 0, size, b);

	return env->ExceptionCheck() == JNI_FALSE;
}


extern "C"
{
	JNIEXPORT void JNICALL Java_nostalgia_Core_setHandler(JNIEnv* env, jclass clz, jobject handler)
	{
		jclass handlerClass = env->FindClass(CLASS_HANDLER);
		if (handlerClass == NULL) {
			std::cout << "JNI problem: can't find " << CLASS_HANDLER << " class";
		}

		jfieldID handlerNativeAddressField = env->GetFieldID(handlerClass, FIELD_HANDLER_NATIVE_ADDRESS, FIELD_HANDLER_NATIVE_ADDRESS_SIG);
		if (handlerNativeAddressField == NULL) {
			std::cout << "JNI problem: can't find " << FIELD_HANDLER_NATIVE_ADDRESS << " field with signature " << FIELD_HANDLER_NATIVE_ADDRESS_SIG << " in class " << CLASS_HANDLER;
		}

		jlong nativeAddress = env->GetLongField(handler, handlerNativeAddressField);
		CoreHandlers* coreHandlers = (CoreHandlers*)nativeAddress;
		setHandlers(coreHandlers);
	}

	JNIEXPORT jboolean JNICALL Java_nostalgia_Core_run(JNIEnv* env, jclass clz)
	{
		return mainLoop();
	}

	JNIEXPORT jboolean JNICALL Java_nostalgia_Core_open(JNIEnv* env, jclass clz, jstring title, jint windowWidth, jint windowHeight, jint pixelsPerPoint)
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

CoreGroupHandlers::CoreGroupHandlers(JNIEnv* env, jobject group)
{
	this->env = env;
	this->group = env->NewGlobalRef(group);

	jclass hc = env->FindClass(CLASS_GROUP_HANDLER);
	if (hc == NULL) {
		std::cout << "JNI problem: can't find " << CLASS_GROUP_HANDLER << " class";
	}
	groupClass = (jclass)env->NewGlobalRef(hc);

	groupFrameMethod = env->GetMethodID(groupClass, METHOD_GROUP_HANDLER_FRAME, METHOD_GROUP_HANDLER_FRAME_SIG);
	if (groupFrameMethod == NULL) {
		std::cout << "JNI problem: can't find " << METHOD_GROUP_HANDLER_FRAME << " method with signature " << METHOD_GROUP_HANDLER_FRAME_SIG << " in class " << CLASS_GROUP_HANDLER;
	}
	groupResizeMethod = env->GetMethodID(groupClass, METHOD_GROUP_HANDLER_RESIZE, METHOD_GROUP_HANDLER_RESIZE_SIG);
	if (groupResizeMethod == NULL) {
		std::cout << "JNI problem: can't find " << METHOD_GROUP_HANDLER_RESIZE << " method with signature " << METHOD_GROUP_HANDLER_RESIZE_SIG << " in class " << CLASS_GROUP_HANDLER;
	}

	groupRField = env->GetFieldID(groupClass, FIELD_GROUP_HANDLER_R, FIELD_GROUP_HANDLER_R_SIG);
	if (groupRField == NULL) {
		std::cout << "JNI problem: can't find " << FIELD_GROUP_HANDLER_R << " field with signature " << FIELD_GROUP_HANDLER_R_SIG << " in class " << CLASS_GROUP_HANDLER;
	}
	groupGField = env->GetFieldID(groupClass, FIELD_GROUP_HANDLER_G, FIELD_GROUP_HANDLER_G_SIG);
	if (groupGField == NULL) {
		std::cout << "JNI problem: can't find " << FIELD_GROUP_HANDLER_G << " field with signature " << FIELD_GROUP_HANDLER_G_SIG << " in class " << CLASS_GROUP_HANDLER;
	}
	groupBField = env->GetFieldID(groupClass, FIELD_GROUP_HANDLER_B, FIELD_GROUP_HANDLER_B_SIG);
	if (groupBField == NULL) {
		std::cout << "JNI problem: can't find " << FIELD_GROUP_HANDLER_B << " field with signature " << FIELD_GROUP_HANDLER_B_SIG << " in class " << CLASS_GROUP_HANDLER;
	}

	groupPointsWidthCountField = env->GetFieldID(groupClass, FIELD_GROUP_HANDLER_POINTS_WIDTH_COUNT, FIELD_GROUP_HANDLER_POINTS_WIDTH_COUNT_SIG);
	if (groupPointsWidthCountField == NULL) {
		std::cout << "JNI problem: can't find " << FIELD_GROUP_HANDLER_POINTS_WIDTH_COUNT << " field with signature " << FIELD_GROUP_HANDLER_POINTS_WIDTH_COUNT_SIG << " in class " << CLASS_GROUP_HANDLER;
	}
	groupPointsHeightCountField = env->GetFieldID(groupClass, FIELD_GROUP_HANDLER_POINTS_HEIGHT_COUNT, FIELD_GROUP_HANDLER_POINTS_HEIGHT_COUNT_SIG);
	if (groupPointsHeightCountField == NULL) {
		std::cout << "JNI problem: can't find " << FIELD_GROUP_HANDLER_POINTS_HEIGHT_COUNT << " field with signature " << FIELD_GROUP_HANDLER_POINTS_HEIGHT_COUNT_SIG << " in class " << CLASS_GROUP_HANDLER;
	}

}
