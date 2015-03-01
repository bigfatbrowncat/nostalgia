#ifndef CORETRANSFORM_H_
#define CORETRANSFORM_H_

#include <jni.h>

#include "Transform.h"

extern "C" {
	JNIEXPORT jlong JNICALL Java_nostalgia_Transform_createNativeRotate(JNIEnv* env, jclass clz, float angle, float vx, float vy, float vz);
	JNIEXPORT jlong JNICALL Java_nostalgia_Transform_createNativeScale(JNIEnv* env, jclass clz, float sx, float sy, float sz);
	JNIEXPORT jlong JNICALL Java_nostalgia_Transform_createNativeTranslate(JNIEnv* env, jclass clz, float dx, float dy, float dz);
	JNIEXPORT jlong JNICALL Java_nostalgia_Transform_createNativeMultiply(JNIEnv* env, jclass clz, jlong ptr1, jlong ptr2);
	JNIEXPORT void JNICALL Java_nostalgia_Transform_destroyNative(JNIEnv* env, jclass clz, long pointer);
}

#define CLASS_TRANSFORM					"nostalgia/Transform"
#define FIELD_TRANSFORM_NATIVE			"nativePointer"
#define FIELD_TRANSFORM_NATIVE_SIG		"J"

const Transform& getNativeTransformFromJava(JNIEnv* env, jobject transform);

#endif /* CORETRANSFORM_H_ */
