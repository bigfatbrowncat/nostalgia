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
	jfieldID groupPointsWidthCountField;
	jfieldID groupPointsHeightCountField;

	jobject group;

public:
	CoreGroup(JNIEnv* env, jobject group);
	bool updateRGB();

	virtual ~CoreGroup();
};


#endif /* COREGROUP_H_ */
