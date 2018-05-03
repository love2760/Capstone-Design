
#include <jni.h>
#include <string>
#include "opencv_module.h"
#include<android/log.h>

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG  , "libnav", __VA_ARGS__)

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



class comparator {
public:
    bool operator()(vector<Point> c1, vector<Point>c2) {

        return boundingRect(Mat(c1)).x<boundingRect(Mat(c2)).x;

    }

};

class openCV {
public:
    Mat imageFiltering(Mat src,  vector<vector<Point> > &contours) {
        double ratio, width, height;
        vector<Vec4i> hierarchy;
        Rect rect;


        /*Mat &src = *(Mat *) matAddrInput;
        Mat &matResult = *(Mat *) matAddrResult;
        */

        width = src.cols;
        height = src.rows;
        ratio = width / height;

        cv::Size size(3, 3);
        cvtColor(src, src, CV_BGR2GRAY);

        //이미지 크기 확대.
        resize(src, src, Size(ratio * 300, 300), 0, 0, CV_INTER_LINEAR);

        GaussianBlur(src, src, size, 0);
        //adaptiveThreshold(src,src, 255, ADAPTIVE_THRESH_GAUSSIAN_C, CV_THRESH_BINARY, 75, 10);
        threshold(src, src, 0, 255, THRESH_BINARY + THRESH_OTSU);
        //향상된 이진화 처리------------ 18.4.13
        bitwise_not(src, src);

        findContours(src, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_TC89_KCOS,
                     Point());

        vector<vector<Point> > contours_poly(contours.size());
        vector<Rect> boundRect(contours.size());
        vector<Rect> boundRect2(contours.size());

        for (int i = 0; i < contours.size(); i++)
        {
            approxPolyDP(Mat(contours[i]), contours_poly[i], 4, true);
            //다소 울퉁불퉁한 외곽선을 근사법을 통해 좀 더 부드럽게 잡아줌.

            boundRect[i] = boundingRect(Mat(contours_poly[i]));
            //외곽선에 사각형을 할당하는 함수.
        }

        cvtColor(src, src, CV_GRAY2RGB);
        //이미지를 그레이스케일로 불러왔기 때문에 외곽선이나 사각형도 흑백으로 보이는 현상이 발생.
        //방지하기 위해 원색으로 되돌려줌.




        //외곽선과 사각형을 그려주는 작업. 현재 외곽선은 굳이 그릴 필요가 없다고 생각해 주석처리.
        for (int i = 0; i < contours.size(); i++)
        {
            /*drawContours(img_adaptive, contours, i, Scalar(0, 0, 0), 1, 8, hierarchy, 0, Point());*/
            rectangle(src, boundRect[i].tl(), boundRect[i].br(), Scalar(0, 255, 0), 1, 8, 0);
            contours[i]=contours_poly[i];
        }


        return src;

    }


    void extractContours(Mat &image, vector<vector<Point> > &contours_poly , vector<Mat>& images) {
        vector<Mat> temp(contours_poly.size());

        //외곽선들을 x 좌표 기준으로 왼쪽에서부터 오른쪽으로 정렬.
        sort(contours_poly.begin(), contours_poly.end(), comparator());

        int j=0;
        //모든 외곽선들에 대해 추출을
        for (int i = 0; i < contours_poly.size(); i++)
        {

            Rect r = boundingRect(Mat(contours_poly[i]));
            Mat mask = Mat::zeros(image.size(), CV_8UC1);
            //마스크에 외곽선들을 그려줍니다.
            drawContours(mask, contours_poly, i, Scalar(255), CV_FILLED);

            //등호를 하나의 기호로 합쳐주는 부분.
            if (i + 1 < contours_poly.size()) {
                Rect r2 = boundingRect(Mat(contours_poly[i + 1]));
                if (abs(r2.x - r.x) < 20) {
                    //Draw mask onto image
                    drawContours(mask, contours_poly, i + 1, Scalar(255), CV_FILLED);
                    i++;
                    j--;
                    int minX = min(r.x, r2.x);
                    int minY = min(r.y, r2.y);
                    int maxX = max(r.x + r.width, r2.x + r2.width);
                    int maxY = max(r.y + r.height, r2.y + r2.height);
                    r = Rect(minX, minY, maxX - minX, maxY - minY);

                }
            }
            //복사
            Mat extractPic;
            image.copyTo(extractPic, mask);
            Mat resizedPic = extractPic(r);

            //높이 75픽셀로 리사이즈
            double ratio, width = (double) resizedPic.cols, height = (double) resizedPic.rows;
            ratio = width / height;
            resize(resizedPic, resizedPic, Size(75 * ratio, 75), 0, 0, CV_INTER_LINEAR);
            temp[i] = resizedPic;
            j++;
        }
        images.resize(j);
        for (int i = 0; i < j; i++)
        {
            images[i] = temp[i];
        }
    }
};


extern "C"
JNIEXPORT Mat JNICALL
Java_ce_inu_ikta_imgprocessor_imgFiltering(JNIEnv *env, jobject instance, jlong matAddrInput) {
    // TODO
    Mat src = *(Mat *) matAddrInput;
    vector<Mat> images;
    vector<vector<Point> > contours_;
    openCV opencv;

    LOGD("start Filter");
    src= opencv.imageFiltering(src, contours_);
    LOGD("finish Filter start Contour");
    opencv.extractContours(src, contours_,images);
    LOGD("finish method");

    jclass matclass = env->FindClass("org/opencv/core/Mat");
    jmethodID jMatCons = env->GetMethodID(matclass,"<init>","()V");
    jmethodID getPtrMethod = env->GetMethodID(matclass, "getNativeObjAddr", "()J");

    // Call back constructor to allocate a new instance
    jobjectArray newMatArr = env->NewObjectArray(images.size(), matclass, 0);

    for (int i=0; i< images.size(); i++){
        jobject jMat = env->NewObject(matclass, jMatCons);
        Mat & native_image= *(Mat*)env->CallLongMethod(jMat, getPtrMethod);
        native_image=images[i];
        env->SetObjectArrayElement(newMatArr, i, jMat);
    }

    LOGD("finish");
    for( int i = 0 ; i < images.size() ; i++) {
        LOGD("images 개수 보자");
    }

    return src;
}

extern "C"
JNIEXPORT void JNICALL
Java_ce_inu_ikta_imgprocessor_imgFiltering2(JNIEnv *env, jobject instance, jlong matAddrInput) {

    Mat src = *(Mat *) matAddrInput;
    vector<vector<Point> > contours_;
    openCV opencv;
    src= opencv.imageFiltering(src, contours_);

}