//
// Created by 김광현 on 2018-03-25.
//

#ifndef IKTA_OPENCV_MODULE_H
#include <vector>
#include <iostream>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/opencv.hpp>
#define IKTA_OPENCV_MODULE_H
#endif
/************************************************************************
작성일 18-03-25
작성자 김광현
테서렉트 트레이닝 및 카메라 촬영 후에 쓰이는 이미지 처리 함수입니다.
*************************************************************************/

using namespace cv;
using namespace std;


void imgFilter(Mat inputImg,Mat outimg);
vector<Rect> rectCheck(Mat& image, vector< vector<Point> > contours_poly);
