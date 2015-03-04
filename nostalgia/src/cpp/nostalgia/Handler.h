#ifndef HANDLER_H_
#define HANDLER_H_

#include <jni.h>

#include "../main.hpp"

#define CLASS_HANDLER						"nostalgia/Handler"
#define FIELD_HANDLER_NATIVE_ADDRESS		"nativeAddress"
#define FIELD_HANDLER_NATIVE_ADDRESS_SIG	"J"
#define METHOD_HANDLER_FRAME				"frame"
#define METHOD_HANDLER_FRAME_SIG			"()V"
#define METHOD_HANDLER_RESIZE				"innerResize"
#define METHOD_HANDLER_RESIZE_SIG			"(II)V"
#define METHOD_HANDLER_MOUSEMOVE			"mouseMove"
#define METHOD_HANDLER_MOUSEMOVE_SIG		"(DD)V"
#define METHOD_HANDLER_MOUSEBUTTON			"innerMouseButton"
#define METHOD_HANDLER_MOUSEBUTTON_SIG		"(III)V"
#define METHOD_HANDLER_KEY					"innerKey"
#define METHOD_HANDLER_KEY_SIG				"(IIII)V"
#define METHOD_HANDLER_CHARACTER			"innerCharacter"
#define METHOD_HANDLER_CHARACTER_SIG		"(CI)V"

namespace nostalgia {

	class Handler : public Handlers {
	private:
		JNIEnv* env;
		jclass handlerClass;
		jmethodID handlerMouseMoveMethod;
		jmethodID handlerMouseButtonMethod;
		jmethodID handlerKeyMethod;
		jmethodID handlerCharacterMethod;
		jmethodID handlerFrameMethod;
		jmethodID handlerResizeMethod;

		jobject handler;
	public:
		Handler(JNIEnv* env, jobject handler);
		virtual ~Handler();

		virtual bool resizeHandler(int pointsWidthCount, int pointsHeightCount);
		virtual bool frameHandler();
		virtual bool mouseMoveHandler(double xPoints, double yPoints);
		virtual bool mouseButtonHandler(int button, int action, int mods);
		virtual bool keyHandler(int key, int scancode, int action, int mods);
		virtual bool characterHandler(unsigned int character, int mods);	};

	extern "C" {
		JNIEXPORT jlong JNICALL Java_nostalgia_Handler_createNative(JNIEnv* env, jobject obj);
		JNIEXPORT void JNICALL Java_nostalgia_Handler_destroyNative(JNIEnv* env, jobject obj, jlong address);
	}

} /* namespace nostalgia */

#endif /* HANDLER_H_ */
