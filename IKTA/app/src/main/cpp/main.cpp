#include <opencv2/highgui.hpp>  
#include <opencv2/imgproc.hpp> 
#include <opencv2/opencv.hpp>
#include <iostream>  
#include <vector>
#include <string>

using namespace cv;
using namespace std;


int main(int argc, char** argv)
{
	
	Mat img_original, img_gray, img_adaptive;
	//openCV에서 이미지를 처라히는 자료형.

	Size size(5,5);
	//가우시안 필터를 적용하기 위한 변수. 숫자가 클수록 이미지가 뿌옇게 처리된다.
	vector<vector<Point>> contours; //외곽선을 저장하기 위한 변수
	vector<Vec4i> hierarchy;		//외곽선끼리의 관계(계층)를 저장하는 변수. 
	Rect rect;						//사각형을 표현하기 위한 클래스.



	char filename[256];
	cout << "파일이름 입력(확장자 포함) : ";
	cin >> filename;



	//이미지파일을 로드하여 img_original 에 저장.
	img_original = imread(filename, IMREAD_GRAYSCALE); //adaptiveThreshold를 적용시키기 위해 이미지를 그레이스케일로 불러옴. 
	if (img_original.empty())
	{
		cout << "Could not open or find the image" << std::endl;
		return -1;
	}

	
	GaussianBlur(img_original, img_gray, size, 0); //가우시안 필터 적용.
	adaptiveThreshold(img_gray, img_adaptive, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, 75, 10);
	//이미지의 이진화를 적용하는 함수.
	bitwise_not(img_adaptive, img_adaptive);
	//adaptiveThreshold 후, 글자가 검은색 배경이 흰색이므로 이를 보기 편하게 색반전 시켜줌.

	findContours(img_adaptive, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_TC89_KCOS, Point());
	//이미지에서 숫자의 외곽선을 찾아 contours에 넣는 메소드.




	vector<vector<Point>> contours_poly(contours.size());
	vector<Rect> boundRect(contours.size());
	vector<Rect> boundRect2(contours.size());



	for (int i = 0; i < contours.size(); i++)
	{
		
		approxPolyDP(Mat(contours[i]), contours_poly[i], 3, true);
		//다소 울퉁불퉁한 외곽선을 근사법을 통해 좀 더 부드럽게 잡아줌.

		
		boundRect[i] = boundingRect(Mat(contours_poly[i]));
		//외곽선에 사각형을 할당하는 함수.


	}

	
	cvtColor(img_adaptive, img_adaptive, CV_GRAY2RGB);
	//이미지를 그레이스케일로 불러왔기 때문에 외곽선이나 사각형도 흑백으로 보이는 현상이 발생.
	//방지하기 위해 원색으로 되돌려줌.




	//외곽선과 사각형을 그려주는 작업. 현재 외곽선은 굳이 그릴 필요가 없다고 생각해 주석처리.
	for (int i = 0; i < contours.size(); i++)
	{
		/*drawContours(img_adaptive, contours, i, Scalar(0, 0, 0), 1, 8, hierarchy, 0, Point());*/
		rectangle(img_adaptive, boundRect[i].tl(), boundRect[i].br(), Scalar(0, 255, 0), 1, 8, 0);
			
	}

	

	//윈도우 생성  
	namedWindow("original image", WINDOW_AUTOSIZE);
	namedWindow("gray image", WINDOW_AUTOSIZE);




	//윈도우에 출력  
	imshow("original image", img_original);
	imshow("gray image", img_adaptive);

	//키보드 입력이 될때까지 대기  
	/*waitKey(0);*/

	//디스크에 저장  
	string savename(filename);
	imwrite(savename+"_con.tiff", img_adaptive);

	return 0;
}