#ifndef NGROUP_H_
#define NGROUP_H_

#include <jni.h>

#include "../main.hpp"
#include "Transform.h"

#define CLASS_GROUP							"nostalgia/Group"
#define FIELD_GROUP_R						"r"
#define FIELD_GROUP_R_SIG					"[F"
#define FIELD_GROUP_G						"g"
#define FIELD_GROUP_G_SIG					"[F"
#define FIELD_GROUP_B						"b"
#define FIELD_GROUP_B_SIG					"[F"
#define FIELD_GROUP_A						"a"
#define FIELD_GROUP_A_SIG					"[F"
#define FIELD_GROUP_WIDTH					"width"
#define FIELD_GROUP_WIDTH_SIG				"I"
#define FIELD_GROUP_HEIGHT					"height"
#define FIELD_GROUP_HEIGHT_SIG				"I"
#define METHOD_GROUP_DRAW					"innerDraw"
#define METHOD_GROUP_DRAW_SIG				"()Z"
#define FIELD_GROUP_NATIVE_ADDRESS			"nativeAddress"
#define FIELD_GROUP_NATIVE_ADDRESS_SIG		"J"

namespace nostalgia {

	class Group : public ::Group {
	private:
		JNIEnv* env;
		jclass groupClass;
		jfieldID groupRField;
		jfieldID groupGField;
		jfieldID groupBField;
		jfieldID groupAField;
		jfieldID groupPointsWidthCountField;
		jfieldID groupPointsHeightCountField;

		jmethodID groupDrawMethod;
		jobject group;

	public:
		Group(JNIEnv* env, jobject group, bool hasAlpha);
		bool updateRGB();
		virtual void draw() {
			if (env->CallBooleanMethod(group, groupDrawMethod) == JNI_TRUE) {
				updateRGB();
			}
		}

		virtual ~Group();
	};

	extern "C"
	{
		JNIEXPORT jlong JNICALL Java_nostalgia_Group_createNative(JNIEnv* env, jobject obj, bool hasAlpha);
		JNIEXPORT void JNICALL Java_nostalgia_Group_destroyNative(JNIEnv* env, jobject obj, jlong address);
		JNIEXPORT void JNICALL Java_nostalgia_Group_innerResize(JNIEnv* env, jobject group, int width, int height);
		JNIEXPORT void JNICALL Java_nostalgia_Group_display(JNIEnv* env, jobject group, jobject transform);
	}

} /* namespace nostalgia */

#endif /* NGROUP_H_ */
