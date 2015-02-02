#ifndef MAIN_HPP_
#define MAIN_HPP_

typedef void frame_handler(float* r, float* g, float* b, void* custom);
typedef void resize_handler(int pointsWidthCount, int pointsHeightCount, void* custom);

int mainLoop(const char* title,
             int windowWidth, int windowHeight, int pixelsPerPoint,
             frame_handler* frameHandler, resize_handler* resizeHandler, void* custom);

#endif
