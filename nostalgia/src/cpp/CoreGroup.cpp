#include "jni.h"

#include <iostream>

#include "CoreGroup.h"

#define CLASS_GROUP							"nostalgia/Group"
#define FIELD_GROUP_R						"r"
#define FIELD_GROUP_R_SIG					"[F"
#define FIELD_GROUP_G						"g"
#define FIELD_GROUP_G_SIG					"[F"
#define FIELD_GROUP_B						"b"
#define FIELD_GROUP_B_SIG					"[F"
#define FIELD_GROUP_WIDTH		"width"
#define FIELD_GROUP_WIDTH_SIG	"I"
#define FIELD_GROUP_HEIGHT		"height"
#define FIELD_GROUP_HEIGHT_SIG	"I"

#define METHOD_GROUP_DRAW			"innerDraw"
#define METHOD_GROUP_DRAW_SIG		"()Z"



CoreGroup::CoreGroup(JNIEnv* env, jobject group)
{
	this->env = env;
	this->group = env->NewGlobalRef(group);

	jclass hc = env->FindClass(CLASS_GROUP);
	if (hc == NULL) {
		std::cout << "JNI problem: can't find " << CLASS_GROUP << " class";
	}
	groupClass = (jclass)env->NewGlobalRef(hc);

	groupRField = env->GetFieldID(groupClass, FIELD_GROUP_R, FIELD_GROUP_R_SIG);
	if (groupRField == NULL) {
		std::cout << "JNI problem: can't find " << FIELD_GROUP_R << " field with signature " << FIELD_GROUP_R_SIG << " in class " << CLASS_GROUP;
	}
	groupGField = env->GetFieldID(groupClass, FIELD_GROUP_G, FIELD_GROUP_G_SIG);
	if (groupGField == NULL) {
		std::cout << "JNI problem: can't find " << FIELD_GROUP_G << " field with signature " << FIELD_GROUP_G_SIG << " in class " << CLASS_GROUP;
	}
	groupBField = env->GetFieldID(groupClass, FIELD_GROUP_B, FIELD_GROUP_B_SIG);
	if (groupBField == NULL) {
		std::cout << "JNI problem: can't find " << FIELD_GROUP_B << " field with signature " << FIELD_GROUP_B_SIG << " in class " << CLASS_GROUP;
	}

	groupPointsWidthCountField = env->GetFieldID(groupClass, FIELD_GROUP_WIDTH, FIELD_GROUP_WIDTH_SIG);
	if (groupPointsWidthCountField == NULL) {
		std::cout << "JNI problem: can't find " << FIELD_GROUP_WIDTH << " field with signature " << FIELD_GROUP_WIDTH_SIG << " in class " << CLASS_GROUP;
	}
	groupPointsHeightCountField = env->GetFieldID(groupClass, FIELD_GROUP_HEIGHT, FIELD_GROUP_HEIGHT_SIG);
	if (groupPointsHeightCountField == NULL) {
		std::cout << "JNI problem: can't find " << FIELD_GROUP_HEIGHT << " field with signature " << FIELD_GROUP_HEIGHT_SIG << " in class " << CLASS_GROUP;
	}

	groupDrawMethod = env->GetMethodID(groupClass, METHOD_GROUP_DRAW, METHOD_GROUP_DRAW_SIG);
	if (groupDrawMethod == NULL) {
		std::cout << "JNI problem: can't find " << METHOD_GROUP_DRAW << " method with signature " << METHOD_GROUP_DRAW_SIG << " in class " << CLASS_GROUP;
	}
}

bool CoreGroup::updateRGB()
{
	jfloatArray rArray = (jfloatArray)env->GetObjectField(group, groupRField);
	jfloatArray gArray = (jfloatArray)env->GetObjectField(group, groupGField);
	jfloatArray bArray = (jfloatArray)env->GetObjectField(group, groupBField);

	jint pointsWidthCount = env->GetIntField(group, groupPointsWidthCountField);
	jint pointsHeightCount = env->GetIntField(group, groupPointsHeightCountField);

	int size = pointsWidthCount * pointsHeightCount;

	env->GetFloatArrayRegion(rArray, 0, size, r);
	env->GetFloatArrayRegion(gArray, 0, size, g);
	env->GetFloatArrayRegion(bArray, 0, size, b);

	makeModel();

	return env->ExceptionCheck() == JNI_FALSE;
}

CoreGroup::~CoreGroup()
{

}
