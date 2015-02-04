#ifndef MAIN_HPP_
#define MAIN_HPP_

typedef void frame_handler(float* r, float* g, float* b, void* custom);
typedef void resize_handler(int pointsWidthCount, int pointsHeightCount, void* custom);
typedef void mouse_move_handler(double xPoints, double yPoints, void* custom);
typedef void mouse_button_handler(int button, int action, int mods, void* custom);

bool createWindow(const char* title, int windowWidth, int windowHeight, int pixelsPerPoint);
bool mainLoop(frame_handler* frameHandler, resize_handler* resizeHandler, mouse_move_handler* mouseMoveHandler, mouse_button_handler* mouseButtonHandler, void* custom);

void setCursorVisibility(bool visible);


#endif
