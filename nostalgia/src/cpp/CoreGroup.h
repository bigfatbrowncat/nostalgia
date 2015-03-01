/*
 * CoreGroup.h
 *
 *  Created on: 22 ????. 2015 ?.
 *      Author: il
 */

#ifndef COREGROUP_H_
#define COREGROUP_H_

#include "Group.h"

class CoreGroup : public Group
{
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
	CoreGroup(JNIEnv* env, jobject group, bool hasAlpha);
	bool updateRGB();
	virtual void draw() {
		if (env->CallBooleanMethod(group, groupDrawMethod) == JNI_TRUE) {
			updateRGB();
		}
	}

	virtual ~CoreGroup();
};


#endif /* COREGROUP_H_ */
