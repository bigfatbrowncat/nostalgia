/*
 * Group.cpp
 *
 *  Created on: 04 ????? 2015 ?.
 *      Author: il
 */

#include <iostream>

#include "Group.h"

namespace nostalgia {

	Group::Group(JNIEnv* env, jobject group, bool hasAlpha) : ::Group(hasAlpha)
	{
		this->env = env;
		this->group = env->NewGlobalRef(group);

		jclass hc = env->FindClass(CLASS_GROUP);
		if (hc == NULL) {
			std::cerr << "JNI problem: can't find " << CLASS_GROUP << " class";
		}
		groupClass = (jclass)env->NewGlobalRef(hc);

		groupRField = env->GetFieldID(groupClass, FIELD_GROUP_R, FIELD_GROUP_R_SIG);
		if (groupRField == NULL) {
			std::cerr << "JNI problem: can't find " << FIELD_GROUP_R << " field with signature " << FIELD_GROUP_R_SIG << " in class " << CLASS_GROUP;
		}
		groupGField = env->GetFieldID(groupClass, FIELD_GROUP_G, FIELD_GROUP_G_SIG);
		if (groupGField == NULL) {
			std::cerr << "JNI problem: can't find " << FIELD_GROUP_G << " field with signature " << FIELD_GROUP_G_SIG << " in class " << CLASS_GROUP;
		}
		groupBField = env->GetFieldID(groupClass, FIELD_GROUP_B, FIELD_GROUP_B_SIG);
		if (groupBField == NULL) {
			std::cerr << "JNI problem: can't find " << FIELD_GROUP_B << " field with signature " << FIELD_GROUP_B_SIG << " in class " << CLASS_GROUP;
		}
		groupAField = env->GetFieldID(groupClass, FIELD_GROUP_A, FIELD_GROUP_A_SIG);
		if (groupAField == NULL) {
			std::cerr << "JNI problem: can't find " << FIELD_GROUP_A << " field with signature " << FIELD_GROUP_A_SIG << " in class " << CLASS_GROUP;
		}

		groupPointsWidthCountField = env->GetFieldID(groupClass, FIELD_GROUP_WIDTH, FIELD_GROUP_WIDTH_SIG);
		if (groupPointsWidthCountField == NULL) {
			std::cerr << "JNI problem: can't find " << FIELD_GROUP_WIDTH << " field with signature " << FIELD_GROUP_WIDTH_SIG << " in class " << CLASS_GROUP;
		}
		groupPointsHeightCountField = env->GetFieldID(groupClass, FIELD_GROUP_HEIGHT, FIELD_GROUP_HEIGHT_SIG);
		if (groupPointsHeightCountField == NULL) {
			std::cerr << "JNI problem: can't find " << FIELD_GROUP_HEIGHT << " field with signature " << FIELD_GROUP_HEIGHT_SIG << " in class " << CLASS_GROUP;
		}

		groupDrawMethod = env->GetMethodID(groupClass, METHOD_GROUP_DRAW, METHOD_GROUP_DRAW_SIG);
		if (groupDrawMethod == NULL) {
			std::cerr << "JNI problem: can't find " << METHOD_GROUP_DRAW << " method with signature " << METHOD_GROUP_DRAW_SIG << " in class " << CLASS_GROUP;
		}
	}

	bool Group::updateRGB()
	{
		jfloatArray rArray = (jfloatArray)env->GetObjectField(group, groupRField);
		jfloatArray gArray = (jfloatArray)env->GetObjectField(group, groupGField);
		jfloatArray bArray = (jfloatArray)env->GetObjectField(group, groupBField);
		jfloatArray aArray = (jfloatArray)env->GetObjectField(group, groupAField);

		jint pointsWidthCount = env->GetIntField(group, groupPointsWidthCountField);
		jint pointsHeightCount = env->GetIntField(group, groupPointsHeightCountField);

		int size = pointsWidthCount * pointsHeightCount;

		env->GetFloatArrayRegion(rArray, 0, size, r);
		env->GetFloatArrayRegion(gArray, 0, size, g);
		env->GetFloatArrayRegion(bArray, 0, size, b);
		if (getHasAlpha()) {
			env->GetFloatArrayRegion(aArray, 0, size, a);
		}

		makeModel();

		return env->ExceptionCheck() == JNI_FALSE;
	}

	Group::~Group()
	{

	}

	Group* extractGroupFromJavaGroup(JNIEnv* env, jobject jgroup) {
		jclass groupClass = env->FindClass(CLASS_GROUP);
		if (groupClass == NULL) {
			std::cout << "JNI problem: can't find " << CLASS_GROUP << " class";
		}

		jfieldID groupNativeAddressField = env->GetFieldID(groupClass, FIELD_GROUP_NATIVE_ADDRESS, FIELD_GROUP_NATIVE_ADDRESS_SIG);
		if (groupNativeAddressField == NULL) {
			std::cout << "JNI problem: can't find " << FIELD_GROUP_NATIVE_ADDRESS << " field with signature " << FIELD_GROUP_NATIVE_ADDRESS_SIG << " in class " << CLASS_GROUP;
		}

		jlong nativeAddress = env->GetLongField(jgroup, groupNativeAddressField);
		Group* group = (Group*)nativeAddress;
		return group;
	}

	extern "C" {

		JNIEXPORT jlong JNICALL Java_nostalgia_Group_createNative(JNIEnv* env, jobject obj, bool hasAlpha)
		{
			Group* group = new Group(env, obj, hasAlpha);
			std::cout << "nostalgia::Group created: " << group << std::endl;
			return (jlong)group;
		}

		JNIEXPORT void JNICALL Java_nostalgia_Group_destroyNative(JNIEnv* env, jobject obj, jlong address)
		{
			Group* group = (Group*)address;
			std::cout << "nostalgia::Group destroyed: " << group << std::endl;
			delete group;
		}

		JNIEXPORT void JNICALL Java_nostalgia_Group_innerResize(JNIEnv* env, jobject group, int width, int height)
		{
			extractGroupFromJavaGroup(env, group)->resize(width, height);
		}

		JNIEXPORT void JNICALL Java_nostalgia_Group_display(JNIEnv* env, jobject group, jobject transform)
		{
			const ::Transform& trans = getNativeTransformFromJava(env, transform);
			extractGroupFromJavaGroup(env, group)->display(trans);
		}

	}

} /* namespace nostalgia */
