#include <jni.h>
#include <android/log.h>
#include <stdlib.h>
#include <string.h>
#include "com_fast_framework_ndk_SampleHelper.h"

#ifndef LOG_TAG
#define LOG_TAG "System.out"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#endif

const char *EXAMPLE =
        "308201dd30820146020101300d06092a864886f70d010105050030373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b3009060355040613025553301e170d3136303732323038323135305a170d3436303731353038323135305a30373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b300906035504061302555330819f300d06092a864886f70d010101050003818d0030818902818100993d08675f97fcc6f2a4119c1b1fa6672027a37099648b87fb54b98ce0dd3c39140366af396a093f60e656f6fa70f4707098bf56566787643f36a7d5771896911936a12b26ae621eb33b3d1725853095fa25c123696d70e5d009085e54c1caa250f562ed65ee70182886d1f92c5031cba37429e3f787b2e114af86e962c011a30203010001300d06092a864886f70d010105050003818100604663b5d15d8018677063428963c01fef2f099187355ed91381097605dc2e2974875db7f7ea9cde293569ac2a34def496bcd6d96b9ab8af62ca1ea88b7cc99a1398775363a3be7e94f0b331c80bb53515f11cb9d80063775093d6f552fbec57e9bd1a495db6c86a789aabda0dc09097179a6ef1ee8fa239175aa122fb2a0f6b";

// jstring转char*
char *jstringTostring(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("utf-8");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte *ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    return rtn;
}

// 获取application
jobject getApplication(JNIEnv *env) {
    jclass localClass = env->FindClass("android/app/ActivityThread");
    if (localClass != NULL) {
        LOGI("class have find");
        jmethodID getapplication = env->GetStaticMethodID(localClass, "currentApplication",
                                                          "()Landroid/app/Application;");
        if (getapplication != NULL) {
            jobject application = env->CallStaticObjectMethod(localClass, getapplication);
            return application;
        }
        return NULL;
    }
    return NULL;
}

int verifySign(JNIEnv *env) {
    jobject context = getApplication(env);
    jclass activity = env->GetObjectClass(context);
    // 得到 getPackageManager 方法的 ID
    jmethodID methodID_func = env->GetMethodID(activity, "getPackageManager", "()Landroid/content/pm/PackageManager;");
    // 获得PackageManager对象
    jobject packageManager = env->CallObjectMethod(context, methodID_func);
    jclass packageManagerclass = env->GetObjectClass(packageManager);
    //得到 getPackageName 方法的 ID
    jmethodID methodID_pack = env->GetMethodID(activity, "getPackageName", "()Ljava/lang/String;");
    //获取包名
    jstring name_str = static_cast<jstring>(env->CallObjectMethod(context, methodID_pack));
    // 得到 getPackageInfo 方法的 ID
    jmethodID methodID_pm = env->GetMethodID(packageManagerclass, "getPackageInfo",
                                             "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    // 获得应用包的信息
    jobject package_info = env->CallObjectMethod(packageManager, methodID_pm, name_str, 64);
    // 获得 PackageInfo 类
    jclass package_infoclass = env->GetObjectClass(package_info);
    // 获得签名数组属性的 ID
    jfieldID fieldID_signatures = env->GetFieldID(package_infoclass, "signatures", "[Landroid/content/pm/Signature;");
    // 得到签名数组，待修改
    jobject signatur = env->GetObjectField(package_info, fieldID_signatures);
    jobjectArray signatures = reinterpret_cast<jobjectArray>(signatur);
    // 得到签名
    jobject signature = env->GetObjectArrayElement(signatures, 0);
    // 获得 Signature 类，待修改
    jclass signature_clazz = env->GetObjectClass(signature);
    //获取sign
    jmethodID toCharString = env->GetMethodID(signature_clazz, "toCharsString", "()Ljava/lang/String;");
    //获取签名字符；或者其他进行验证操作
    jstring signstr = static_cast<jstring>(env->CallObjectMethod(signature, toCharString));
    char *ch = jstringTostring(env, signstr);
    //输入签名字符串，这里可以进行相关验证
    // LOGI("|||(~_~).zZ -> ########## sign is :%s", ch);
    return strcmp(EXAMPLE, ch);
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {

    LOGI("|||(~_~).zZ -> ########## JNI_OnLoad");

    JNIEnv *env = NULL;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    if (verifySign(env) != 0) {
        LOGI("|||(~_~).zZ -> ########## SIGNERROR");
        return -1;
    }
    //成功
    LOGI("|||(~_~).zZ -> ########## SIGNOK");
    return JNI_VERSION_1_6;
}

JNIEXPORT jstring JNICALL Java_com_fast_framework_ndk_SampleHelper_getHelloJni
        (JNIEnv *env, jclass jclass1) {
    return env->NewStringUTF("hello jni");
}
