#undef __cplusplus

#include <jni.h>
#include <opencv/cv.h>
#include <opencv/highgui.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <math.h>
#include <float.h>
#include <limits.h>
#include <time.h>
#include <ctype.h>
#include <android/log.h>
#include "lbp.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jintArray JNICALL Java_it_unisa_earify_algorithms_lbp_LBPNativeLibrary_extractFeatures
	(JNIEnv* env, jobject obj, jstring pPath, jint pRows, jint pCols) {
	__android_log_write(ANDROID_LOG_VERBOSE,"Message","Ok, initialized JNI library");
	jintArray result;
	jboolean* r = 0;
	int resultSize;
	int i;

	int rows = (int)pRows;
	int cols = (int)pCols;
	__android_log_write(ANDROID_LOG_VERBOSE,"Message","Ok, initialized JNI library");

	const char *path;
	path = (*env)->GetStringUTFChars(env, pPath, r);

	__android_log_write(ANDROID_LOG_VERBOSE,"Message",path);

	//Carica l'immagine
	IplImage* img = cvLoadImage(path, CV_LOAD_IMAGE_GRAYSCALE);

	resultSize = rows * cols * MAXBYTE;
	jint tempStructure[resultSize];
	result = (*env)->NewIntArray(env, (jint)resultSize);

	int* vector = lbp(img, rows, cols);
	for (i = 0; i < resultSize; i++) {
		tempStructure[i] = vector[i];
	}

	(*env)->SetIntArrayRegion(env, result, 0, resultSize, tempStructure);

	(*env)->ReleaseStringUTFChars(env, pPath, path);
    return result;
}

#ifdef __cplusplus
}
#endif
