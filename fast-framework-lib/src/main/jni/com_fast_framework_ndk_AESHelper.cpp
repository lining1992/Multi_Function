//
// Created by lishicong on 2017/3/9.
//

#include <jni.h> 
#include <android/log.h> 
#include "com_fast_framework_ndk_AESHelper.h"  

#ifndef LOG_TAG
#define LOG_TAG "System.out" 
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#endif  

const char *aes_key = "1234567890abcdef";

JNIEXPORT jstring JNICALL Java_com_fast_framework_ndk_AESHelper_getAESKey(
        JNIEnv *env, jclass jclass1) {

    return env->NewStringUTF(aes_key);
}