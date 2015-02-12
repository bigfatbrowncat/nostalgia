#include <iostream>

#include "jni.h"

#include "Core.hpp"

#define CLASS_HANDLER					"nostalgia/Core$Handler"
#define METHOD_HANDLER_FRAME			"frame"
#define METHOD_HANDLER_FRAME_SIG		"()V"
#define METHOD_HANDLER_SETSIZE			"setSize"
#define METHOD_HANDLER_SETSIZE_SIG		"(II)V"
#define METHOD_HANDLER_MOUSEMOVE		"mouseMove"
#define METHOD_HANDLER_MOUSEMOVE_SIG	"(DD)V"
#define METHOD_HANDLER_MOUSEBUTTON		"innerMouseButton"
#define METHOD_HANDLER_MOUSEBUTTON_SIG	"(III)V"
#define METHOD_HANDLER_KEY				"innerKey"
#define METHOD_HANDLER_KEY_SIG			"(IIII)V"
#define METHOD_HANDLER_CHARACTER		"innerCharacter"
#define METHOD_HANDLER_CHARACTER_SIG	"(CI)V"
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
		jmethodID handlerResizeMethod;
		jmethodID handlerMouseMoveMethod;
		jmethodID handlerMouseButtonMethod;
		jmethodID handlerKeyMethod;
		jmethodID handlerCharacterMethod;
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
			handlerResizeMethod = env->GetMethodID(handlerClass, METHOD_HANDLER_SETSIZE, METHOD_HANDLER_SETSIZE_SIG);
			if (handlerResizeMethod == NULL) {
				std::cout << "JNI problem: can't find " << METHOD_HANDLER_SETSIZE << " method with signature " << METHOD_HANDLER_SETSIZE_SIG << " in class " << CLASS_HANDLER;
			}
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
		handlerJNICustom* fHCustom = (handlerJNICustom*)custom;
		JNIEnv* env = fHCustom->env;

		jclass handlerClass = env->FindClass(CLASS_HANDLER);
		if (handlerClass == NULL) {
			std::cout << "JNI problem: can't find " << CLASS_HANDLER << " class";
		}

		env->CallVoidMethod(fHCustom->handler, fHCustom->handlerResizeMethod, pointsWidthCount, pointsHeightCount);
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

	void mouse_button_handler_callback(int button, int action, int mods, void* custom)
	{
		handlerJNICustom* fHCustom = (handlerJNICustom*)custom;
		JNIEnv* env = fHCustom->env;

		env->CallVoidMethod(fHCustom->handler, fHCustom->handlerMouseButtonMethod, button, action, mods);
	}

	void key_handler_callback(int key, int scancode, int action, int mods, void* custom)
	{
		handlerJNICustom* fHCustom = (handlerJNICustom*)custom;
		JNIEnv* env = fHCustom->env;

		env->CallVoidMethod(fHCustom->handler, fHCustom->handlerKeyMethod, key, scancode, action, mods);
	}

	void character_handler_callback(unsigned int character, int mods, void* custom)
	{
		handlerJNICustom* fHCustom = (handlerJNICustom*)custom;
		JNIEnv* env = fHCustom->env;

		env->CallVoidMethod(fHCustom->handler, fHCustom->handlerCharacterMethod, character, mods);
	}

	JNIEXPORT jboolean JNICALL Java_nostalgia_Core_run(JNIEnv* env, jclass clz, jobject handler)
	{
		handlerJNICustom* custom = new handlerJNICustom(env, handler);

		handlers* h = new handlers();
		h->frameHandler = frame_handler_callback;
		h->resizeHandler = resize_handler_callback;
		h->mouseMoveHandler = mouse_move_handler_callback;
		h->mouseButtonHandler = mouse_button_handler_callback;
		h->keyHandler = key_handler_callback;
		h->characterHandler = character_handler_callback;
		h->custom = custom;

		return mainLoop(h);
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
