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
#include "lbp.c"

extern "C" {

JNIEXPORT jvoid JNICALL Java_it_unisa_earify_algorithms_lbp_LBPNativeLibrary_extractFeatures(JNIEnv* env, jobject obj, jstring pPath, jstring pDestinationPath) {
	jfloatArray result;
	int resultSize;
	jboolean r = 0;

	const char *path = env->GetStringUTFChars(pPath, &r);
	const char *destPath = env->GetStringUTFChars(pDestinationPath, &r);

	__android_log_write(ANDROID_LOG_VERBOSE,"Message",path);

	//Carica l'immagine
	IplImage* img = cvLoadImage(const char* filename);
	struct blBioKey biokey;
	blBuildBioKey(img, &biokey, img->nChannels);

	blSaveBioKey(destPath, &biokey);

	//****************************************************************
	resultSize = 1000000;

	result = env->NewFloatArray(resultSize);

	jfloat tempStructure[resultSize];
	//Porzione di codice che riempie l'array temporaneo
	env->SetFloatArrayRegion(result, 0, resultSize, tempStructure);
	//****************************************************************

	env->ReleaseStringUTFChars(pPath, path);
    return result;
	}
}
