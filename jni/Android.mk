LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

OPENCV_INSTALL_MODULES:=on
OPENCV_CAMERA_MODULES:=off
OPENCV_LIB_TYPE:=SHARED
include H:\OpenCV-2.4.6-android-sdk-r2\OpenCV-2.4.6-android-sdk\sdk\native\jni\OpenCV.mk

LOCAL_SRC_FILES  := sample.cpp
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include
LOCAL_LDLIBS     += -llog -ldl

LOCAL_MODULE     := my_lib

include $(BUILD_SHARED_LIBRARY)