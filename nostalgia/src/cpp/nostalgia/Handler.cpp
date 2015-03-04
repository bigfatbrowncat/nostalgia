#include <iostream>

#include <jni.h>

#include "Handler.h"

namespace nostalgia {

	Handler::Handler(JNIEnv* env, jobject handler)
	{
		this->env = env;
		this->handler = env->NewGlobalRef(handler);

		jclass hc = env->FindClass(CLASS_HANDLER);
		if (hc == NULL) {
			std::cerr << "JNI problem: can't find " << CLASS_HANDLER << " class";
		}
		handlerClass = (jclass)env->NewGlobalRef(hc);

		handlerMouseMoveMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_MOUSEMOVE, METHOD_HANDLER_MOUSEMOVE_SIG);
		if (handlerMouseMoveMethod == NULL) {
			std::cerr << "JNI problem: can't find " << METHOD_HANDLER_MOUSEMOVE << " method with signature " << METHOD_HANDLER_MOUSEMOVE_SIG << " in class " << CLASS_HANDLER;
		}
		handlerMouseButtonMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_MOUSEBUTTON, METHOD_HANDLER_MOUSEBUTTON_SIG);
		if (handlerMouseButtonMethod == NULL) {
			std::cerr << "JNI problem: can't find " << METHOD_HANDLER_MOUSEBUTTON << " method with signature " << METHOD_HANDLER_MOUSEBUTTON_SIG << " in class " << CLASS_HANDLER;
		}
		handlerKeyMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_KEY, METHOD_HANDLER_KEY_SIG);
		if (handlerKeyMethod == NULL) {
			std::cerr << "JNI problem: can't find " << METHOD_HANDLER_KEY << " method with signature " << METHOD_HANDLER_KEY_SIG << " in class " << CLASS_HANDLER;
		}
		handlerCharacterMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_CHARACTER, METHOD_HANDLER_CHARACTER_SIG);
		if (handlerCharacterMethod == NULL) {
			std::cerr << "JNI problem: can't find " << METHOD_HANDLER_CHARACTER << " method with signature " << METHOD_HANDLER_CHARACTER_SIG << " in class " << CLASS_HANDLER;
		}

		handlerFrameMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_FRAME, METHOD_HANDLER_FRAME_SIG);
		if (handlerFrameMethod == NULL) {
			std::cerr << "JNI problem: can't find " << METHOD_HANDLER_FRAME << " method with signature " << METHOD_HANDLER_FRAME_SIG << " in class " << CLASS_HANDLER;
		}
		handlerResizeMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_RESIZE, METHOD_HANDLER_RESIZE_SIG);
		if (handlerResizeMethod == NULL) {
			std::cerr << "JNI problem: can't find " << METHOD_HANDLER_RESIZE << " method with signature " << METHOD_HANDLER_RESIZE_SIG << " in class " << CLASS_HANDLER;
		}
	}

	Handler::~Handler() {
		env->DeleteGlobalRef(handler);
		env->DeleteGlobalRef(handlerClass);
	}


	bool Handler::mouseMoveHandler(double xPoints, double yPoints)
	{
		env->CallVoidMethod(handler, handlerMouseMoveMethod, xPoints, yPoints);
		return env->ExceptionCheck() == JNI_FALSE;
	}

	bool Handler::mouseButtonHandler(int button, int action, int mods)
	{
		env->CallVoidMethod(handler, handlerMouseButtonMethod, button, action, mods);
		return env->ExceptionCheck() == JNI_FALSE;
	}

	bool Handler::keyHandler(int key, int scancode, int action, int mods)
	{
		env->CallVoidMethod(handler, handlerKeyMethod, key, scancode, action, mods);
		return env->ExceptionCheck() == JNI_FALSE;
	}

	bool Handler::characterHandler(unsigned int character, int mods)
	{
		env->CallVoidMethod(handler, handlerCharacterMethod, character, mods);
		return env->ExceptionCheck() == JNI_FALSE;
	}

	bool Handler::resizeHandler(int pointsWidthCount, int pointsHeightCount)
	{
		env->CallVoidMethod(handler, handlerResizeMethod, pointsWidthCount, pointsHeightCount);
		return env->ExceptionCheck() == JNI_FALSE;
	}

	bool Handler::frameHandler()
	{
		env->CallVoidMethod(handler, handlerFrameMethod);
		return env->ExceptionCheck() == JNI_FALSE;
	}


	extern "C" {


		JNIEXPORT jlong JNICALL Java_nostalgia_Handler_createNative(JNIEnv* env, jobject obj)
		{
			Handler* handler = new Handler(env, obj);
			std::cout << "nostalgia::Handler created: " << handler << std::endl;
			return (jlong)handler;
		}

		JNIEXPORT void JNICALL Java_nostalgia_Handler_destroyNative(JNIEnv* env, jobject obj, jlong address)
		{
			Handler* handler = (Handler*)address;
			std::cout << "nostalgia::Handler destroyed: " << handler << std::endl;
			delete handler;
		}

	}

} /* namespace nostalgia */
