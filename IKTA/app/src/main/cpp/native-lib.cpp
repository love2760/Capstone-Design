#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>

extern "C" {

JNIEXPORT jstring JNICALL
Java_ce_inu_iknowtheanswer_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT void JNICALL
Java_ce_inu_iknowtheanswer_MainActivity_fillMatrixColor(
        JNIEnv* env,
        jobject,
        jlong addrMat,
        jint red,
        jint green,
        jint blue
) {
    using namespace cv;

    Mat &mat = *(Mat*)addrMat;
    mat = Scalar(red, green, blue);

    return;
}

}