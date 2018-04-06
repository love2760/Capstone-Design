/*
	�� ���α׷��� tesseract ������ �̹����� ���� ���ϰ� �����ϱ� ���Ͽ�
	���۵� ���α׷��Դϴ�. 1.jpg��� ������ ������� �̹����� ó���� ��
	�ִ��� ���� ������ �����մϴ�. output ������ �������� ������ �������
	�ʽ��ϴ�. �ش� main.cpp �ڵ�� imgpreprocess.h �� �ʿ�� �մϴ�.
*/
#include <Windows.h>


#include "imgpreprocess.h"
/*
   ConvertWCtoC�� wchar_t Ÿ���� ��Ʈ����
   char Ÿ���� ��Ʈ������ ��ȯ�����ִ� �Լ��Դϴ�.
*/
char * ConvertWCtoC(wchar_t* str) {
	char* pStr;
	int strSize = WideCharToMultiByte(CP_ACP, 0, str, -1, NULL, 0, NULL, NULL);
	pStr = new char[strSize];
	WideCharToMultiByte(CP_ACP, 0, str, -1, pStr, strSize, 0, 0);
	return pStr;
}

/* ó���� �̹������� ���ڵ��� �����ϴ� �Լ��Դϴ�.*/
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



	//�̹��������� �ε��Ͽ� image�� ����  
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
		
		if (r.area() < 30) continue;
			bool inside = false;
			for (int j = 0; j < contours_poly.size(); j++) {
				if (j == i)continue;

				Rect r2 = boundingRect(Mat(contours_poly[j]));
				if (r2.area() < 30 || r2.area() < r.area())continue;
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
		boundRect[i] = boundingRect(Mat(validContours[i]));
		rectangle(img_gray, v[i].tl(), v[i].br(), Scalar(75), 1, 8, 0);
	}
	
	//������ ����  
	//namedWindow("original image", WINDOW_AUTOSIZE);




	//�����쿡 ���  
	//imshow("original image", img_original);
	//imshow("gray image", img_gray);
	//imshow("aa", img_gray2);
	//Ű���� �Է��� �ɶ����� ���  
	//waitKey(0);

	//��ũ�� ����  
	extractContours(img_gray2, v);
	imwrite("img_gray.jpg", img_gray);
	return 0;
}