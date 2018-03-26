#include "imgpreprocess.h"

void imgFilter(Mat inputImg, Mat &outImg) {
	cv::Size size(3, 3);
	cvtColor(inputImg, outImg, CV_BGR2GRAY);
	GaussianBlur(outImg, outImg, size, 0);
	adaptiveThreshold(outImg, outImg, 255, ADAPTIVE_THRESH_GAUSSIAN_C, CV_THRESH_BINARY, 75, 10);
	//bitwise_not(outImg, outImg);
}

vector<Rect> rectCheck(Mat& image, vector<vector<Point>> contours_poly) {
	vector<Rect> boundedRect;
	sort(contours_poly.begin(), contours_poly.end(), comparator());
	//이미지를 처리하여 얻은 외곽선들을 이용하여 사각형 단위로 문자를
	//분할하는 과정입니다.
	//"="기호를 "-"기호가 두 개 존재하는 것을 확인하여 합쳐주는 작업을 진행합니다.
	for (int i = 0; i < contours_poly.size(); i++) {
		Rect r = boundingRect(Mat(contours_poly[i]));

		if (r.width / r.height > 3.0) {
			if (i + 1 < contours_poly.size()) {
				Rect r2 = boundingRect(Mat(contours_poly[i + 1]));
				if (abs(r2.x - r.x) < 20) {
					i++;
					int minX = min(r.x, r2.x);
					int minY = min(r.y, r2.y);
					int maxX = max(r.x + r.width, r2.x + r2.width);
					int maxY = max(r.y + r.height, r2.y + r2.height);
					r = Rect(minX, minY, maxX - minX, maxY - minY);
				}
			}
			// 의도치 않은 결합이 있을경우 해당 결합을 무효로 돌리고 원본을 다시 불러옵니다.
			if (r.width / r.height < 1.5) {
				i--;
				r = boundingRect(Mat(contours_poly[i]));
			}
		}
		boundedRect.push_back(r);
	}
	//정리된 사각형들을 반환
	return boundedRect;
}