#ifndef CORE_H_
#define CORE_H_

#include <jni.h>

namespace nostalgia {

	extern "C" {
		JNIEXPORT void JNICALL Java_nostalgia_Core_setHandler(JNIEnv* env, jclass clz, jobject handler);
		JNIEXPORT jboolean JNICALL Java_nostalgia_Core_run(JNIEnv* env, jclass clz);
		JNIEXPORT jboolean JNICALL Java_nostalgia_Core_innerOpen(JNIEnv* env, jclass clz, jstring title, jint windowWidth, jint windowHeight, jint pixelsPerPoint);
		JNIEXPORT void JNICALL Java_nostalgia_Core_setCursorVisibility(JNIEnv* env, jclass clz, jboolean visible);
		JNIEXPORT void JNICALL Java_nostalgia_Core_close(JNIEnv* env, jclass clz);
	}

} /* namespace nostalgia */

#endif /* CORE_H_ */
