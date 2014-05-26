LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

include /home/simone/Programmi/Android/OpenCV-2.4.9-android-sdk/sdk/native/jni/OpenCV.mk
#include /Users/Matt/Development/OpenCV-2.4.9-android-sdk/sdk/native/jni/OpenCV.mk
#include C:\Users\Carlo\Documents\Android\OpenCV-2.4.8-android-sdk\sdk\native\jni\OpenCV.mk

LOCAL_MODULE    := libEarify
LOCAL_SRC_FILES := lbp.c Earify.c

LOCAL_LDLIBS += -ldl
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog 
include $(BUILD_SHARED_LIBRARY)