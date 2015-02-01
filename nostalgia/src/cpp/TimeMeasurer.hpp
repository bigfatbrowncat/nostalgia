#ifndef TIME_MEASURER_HPP_
#define TIME_MEASURER_HPP_

#include <sys/time.h>
#include <unistd.h>

class TimeMeasurer
{
private:
	long start;
	std::string tag;
public:
	TimeMeasurer(std::string tag)
	{
		this->tag = tag;

#ifdef MEASURE_TIMES
		struct timeval tv;
		gettimeofday(&tv, NULL);

		start = (tv.tv_sec) * 1000 + (tv.tv_usec) / 1000;
#else
		start = 0;
#endif
	}

	void measureAndReport()
	{
#ifdef MEASURE_TIMES
		struct timeval tv;
		gettimeofday(&tv, NULL);

		long end = (tv.tv_sec) * 1000 + (tv.tv_usec) / 1000;
		printf("%s: %ld msec\n", tag.c_str(), end - start);
#endif
	}
};

#endif
