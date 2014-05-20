LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

include /home/simone/Programmi/Android/OpenCV-2.4.9-android-sdk/sdk/native/jni/OpenCV.mk

LOCAL_MODULE    := Earify
LOCAL_SRC_FILES := Earify.c, lbp.c

LOCAL_LDLIBS +=  -llog -ldl

include $(BUILD_SHARED_LIBRARY)
