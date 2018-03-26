/************************************************************************
�ۼ��� 18-03-25
�ۼ��� �豤��
�׼���Ʈ Ʈ���̴� �� ī�޶� �Կ� �Ŀ� ���̴� �̹��� ó�� �Լ��Դϴ�.
*************************************************************************/


#pragma once
#include <vector>
#include <iostream>  
#include <opencv2/highgui.hpp>  
#include <opencv2/imgproc.hpp> 
#include <opencv2/opencv.hpp>
using namespace cv;
using namespace std;

class comparator {
public:
	bool operator()(vector<Point> c1, vector<Point>c2) {

		return boundingRect(Mat(c1)).x<boundingRect(Mat(c2)).x;

	}

};

void imgFilter(Mat inputImg,Mat &outimg);
vector<Rect> rectCheck(Mat& image, vector<vector<Point>> contours_poly);