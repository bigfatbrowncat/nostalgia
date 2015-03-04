/*
 * Transform.cpp
 *
 *  Created on: 04 ????? 2015 ?.
 *      Author: il
 */

#include "Transform.h"

namespace nostalgia {

	const Transform& getNativeTransformFromJava(JNIEnv* env, jobject transform) {
		jclass transformClass = env->FindClass(CLASS_TRANSFORM);
		jfieldID nativePointerField = env->GetFieldID(transformClass, FIELD_TRANSFORM_NATIVE, FIELD_TRANSFORM_NATIVE_SIG);
		jlong nativePointer = env->GetLongField(transform, nativePointerField);
		return *(Transform*)nativePointer;
	}

	extern "C" {

		JNIEXPORT jlong JNICALL Java_nostalgia_Transform_createNativeRotate(JNIEnv* env, jclass clz, float angle, float vx, float vy, float vz) {
			return (jlong)(new Transform(Transform::rotate(angle, vx, vy, vz)));
		}

		JNIEXPORT jlong JNICALL Java_nostalgia_Transform_createNativeScale(JNIEnv* env, jclass clz, float sx, float sy, float sz) {
			return (jlong)(new Transform(Transform::scale(sx, sy, sz)));
		}

		JNIEXPORT jlong JNICALL Java_nostalgia_Transform_createNativeTranslate(JNIEnv* env, jclass clz, float dx, float dy, float dz) {
			return (jlong)(new Transform(Transform::translate(dx, dy, dz)));
		}

		JNIEXPORT jlong JNICALL Java_nostalgia_Transform_createNativeMultiply(JNIEnv* env, jclass clz, jlong ptr1, jlong ptr2) {
			Transform& t1 = *(Transform*)ptr1;
			Transform& t2 = *(Transform*)ptr2;
			return (jlong)(new Transform(Transform::multiply(t1, t2)));
		}

		JNIEXPORT void JNICALL Java_nostalgia_Transform_destroyNative(JNIEnv* env, jclass clz, long pointer) {
			delete (Transform*)pointer;
		}
	}

} /* namespace nostalgia */
