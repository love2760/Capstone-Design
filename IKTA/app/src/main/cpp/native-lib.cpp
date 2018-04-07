#include <jni.h>
#include <string>
#include "opencv_module.h"

using namespace cv;
extern "C"

JNIEXPORT jstring

JNICALL
Java_ce_inu_ikta_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_ce_inu_ikta_imgprocessor_imgFiltering(JNIEnv *env, jobject instance, jlong matAddrInput) {
    // TODO
    Mat &src = *(Mat *)matAddrInput;
    cv::Size size(3, 3);
    cvtColor(src,src, CV_BGR2GRAY);
    GaussianBlur(src,src, size, 0);
    adaptiveThreshold(src,src, 255, ADAPTIVE_THRESH_GAUSSIAN_C, CV_THRESH_BINARY, 75, 10);
    bitwise_not(src,src);
}