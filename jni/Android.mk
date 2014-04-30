LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

include C:\Users\Carlo\Documents\Android\OpenCV-2.4.8-android-sdk\sdk\native\jni\OpenCV.mk

LOCAL_MODULE    := Earify
LOCAL_SRC_FILES := Earify.cpp

include $(BUILD_SHARED_LIBRARY)
