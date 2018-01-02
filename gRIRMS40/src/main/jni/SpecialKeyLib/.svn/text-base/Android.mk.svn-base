LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_SHARED_LIBRARIES := libutils 

LOCAL_MODULE:= libSpecialKey
LOCAL_MODULE_TAGS:= optional
LOCAL_SRC_FILES:= \
    SpecialKey.cpp
LOCAL_C_INCLUDES := $(JNI_H_INCLUDE)

include $(BUILD_SHARED_LIBRARY)
include $(call all-makefiles-under,$(LOCAL_PATH))

