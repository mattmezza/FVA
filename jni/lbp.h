#include <opencv/cv.h>
#include <opencv/highgui.h>
#include <jni.h>
#include <android/log.h>
#include <math.h>
#include <string.h>


#define MAXBYTE 256
#define GRIDSIZE 3
typedef unsigned char PIXEL;
typedef unsigned char BINARY_PATH;

int* lbp(IplImage*, int, int);
void myDEBUG_STRING(char*);
void myDEBUG_INT(int);
void myDEBUG_ARRINT(int, int*);
