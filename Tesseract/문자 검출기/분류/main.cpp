/*
	작성일 2018년 3월 25일
	작성자 김광현
	이 프로그램은 tesseract 교육용 이미지를 보다 편하게 추출하기 위하여
	제작된 프로그램입니다. 1.jpg라는 파일을 기반으로 이미지를 처리한 후
	최대한 글자 단위로 추출합니다. output 폴더가 존재하지 않으면 저장되지
	않습니다. 해당 main.cpp 코드는 imgpreprocess.h 를 필요로 합니다.
*/
#include <Windows.h>


#include "imgpreprocess.h"
/*
   ConvertWCtoC는 wchar_t 타입의 스트링을
   char 타입의 스트링으로 변환시켜주는 함수입니다.
*/
char * ConvertWCtoC(wchar_t* str) {
	char* pStr;
	int strSize = WideCharToMultiByte(CP_ACP, 0, str, -1, NULL, 0, NULL, NULL);
	pStr = new char[strSize];
	WideCharToMultiByte(CP_ACP, 0, str, -1, pStr, strSize, 0, 0);
	return pStr;
}

/* 처리된 이미지에서 글자들을 저장하는 함수입니다.*/
void extractContours(Mat& image, vector< Rect > Rect_poly) {
	Mat extractPic;
	image.copyTo(extractPic, image);
	for (int i = 0; i < Rect_poly.size(); i++) {
		Mat resizedPic = extractPic(Rect_poly[i]);
		stringstream outputFile;
		wchar_t curdir[256] = L"";
		GetCurrentDirectory(256, curdir);
		char* asd = ConvertWCtoC(curdir);
		outputFile << asd << "\\output\\" << "a" << i << ".jpg";
		cout << outputFile.str() << endl;
		imwrite(outputFile.str(), resizedPic);
	}
}



int main(int argc, char** argv)
{

	Mat img_original, img_gray,img_gray2;
	CvMemStorage *storage = cvCreateMemStorage(0);
	vector<vector<Point>> contours;
	vector<Vec4i> hierarchy;


	Rect rect, temp_rect;

	double ratio, delta_x, delta_y, gradient;
	int select, width, count, friend_count = 0, refinery_count = 0;



	//이미지파일을 로드하여 image에 저장  
	img_original = imread("1.jpg", IMREAD_COLOR);
	if (img_original.empty())
	{
		cout << "Could not open or find the image" << std::endl;
		return -1;
	}

	imgFilter(img_original, img_gray);
	imgFilter(img_original, img_gray2);
	
	findContours(img_gray, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_NONE, Point(0,0));
	vector<vector<Point> > contours_poly(contours.size());
	vector<Rect> boundRect(contours.size());
	
	//Get poly contours
	for (int i = 0; i < contours.size(); i++) {
		approxPolyDP(Mat(contours[i]), contours_poly[i], 3, true);
	}


	//Get only important contours, merge contours that are within another

	vector<vector<Point> > validContours;
	for (int i = 0; i<contours_poly.size(); i++) {
		Rect r = boundingRect(Mat(contours_poly[i]));
		
		if (r.area() < 150) continue;
			bool inside = false;
			for (int j = 0; j < contours_poly.size(); j++) {
				if (j == i)continue;

				Rect r2 = boundingRect(Mat(contours_poly[j]));
				if (r2.area() < 150 || r2.area() < r.area())continue;
				if (r.x > r2.x&&r.x + r.width<r2.x + r2.width&&
					r.y>r2.y&&r.y + r.height < r2.y + r2.height) {

					inside = true;
				}
			}
			cout << i << " " << r.area() << endl;
			if (inside)continue;
			validContours.push_back(contours_poly[i]);
		
	}

	//Get bounding rects
	vector<Rect> v(rectCheck(img_gray, validContours));
	for (int i = 0; i<v.size(); i++) {
		//boundRect[i] = boundingRect(Mat(validContours[i]));
		rectangle(img_gray, v[i].tl(), v[i].br(), Scalar(75), 3, 8, 0);
		
	}
	
	//윈도우 생성  
	namedWindow("original image", WINDOW_AUTOSIZE);




	//윈도우에 출력  
	imshow("original image", img_original);
	imshow("gray image", img_gray);
	imshow("aa", img_gray2);
	//키보드 입력이 될때까지 대기  
	//waitKey(0);

	//디스크에 저장  
	extractContours(img_gray2, v);
	imwrite("img_gray.jpg", img_gray);
	return 0;
}