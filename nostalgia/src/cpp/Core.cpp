#include <iostream>

#include "jni.h"

#include "Core.hpp"
#include "CoreGroup.h"
#include "CoreHandlers.h"

#define CLASS_HANDLER						"nostalgia/Handler"
#define FIELD_HANDLER_NATIVE_ADDRESS		"nativeAddress"
#define FIELD_HANDLER_NATIVE_ADDRESS_SIG	"J"

#define CLASS_GROUP							"nostalgia/Group"
#define FIELD_GROUP_NATIVE_ADDRESS			"nativeAddress"
#define FIELD_GROUP_NATIVE_ADDRESS_SIG		"J"

extern "C"
{

	CoreGroup* extractCoreGroupFromJavaGroup(JNIEnv* env, jobject group) {
		jclass groupClass = env->FindClass(CLASS_GROUP);
		if (groupClass == NULL) {
			std::cout << "JNI problem: can't find " << CLASS_GROUP << " class";
		}

		jfieldID groupNativeAddressField = env->GetFieldID(groupClass, FIELD_GROUP_NATIVE_ADDRESS, FIELD_GROUP_NATIVE_ADDRESS_SIG);
		if (groupNativeAddressField == NULL) {
			std::cout << "JNI problem: can't find " << FIELD_GROUP_NATIVE_ADDRESS << " field with signature " << FIELD_GROUP_NATIVE_ADDRESS_SIG << " in class " << CLASS_GROUP;
		}

		jlong nativeAddress = env->GetLongField(group, groupNativeAddressField);
		CoreGroup* coreGroup = (CoreGroup*)nativeAddress;
		return coreGroup;
	}

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

	JNIEXPORT jlong JNICALL Java_nostalgia_Group_createNative(JNIEnv* env, jobject obj)
	{
		CoreGroup* group = new CoreGroup(env, obj);
		std::cout << "CoreGroup created: " << group << std::endl;
		return (jlong)group;
	}

	JNIEXPORT void JNICALL Java_nostalgia_Group_destroyNative(JNIEnv* env, jobject obj, jlong address)
	{
		CoreGroup* group = (CoreGroup*)address;
		std::cout << "CoreGroup destroyed: " << group << std::endl;
		delete group;
	}

	JNIEXPORT void JNICALL Java_nostalgia_Group_innerResize(JNIEnv* env, jobject group, int width, int height)
	{
		extractCoreGroupFromJavaGroup(env, group)->resize(width, height);
	}

	JNIEXPORT void JNICALL Java_nostalgia_Group_display(JNIEnv* env, jobject group)
	{
		extractCoreGroupFromJavaGroup(env, group)->display();
	}

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
