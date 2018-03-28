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
	//openCV���� �̹����� ó������ �ڷ���.

	Size size(5,5);
	//����þ� ���͸� �����ϱ� ���� ����. ���ڰ� Ŭ���� �̹����� �ѿ��� ó���ȴ�.
	vector<vector<Point>> contours; //�ܰ����� �����ϱ� ���� ����
	vector<Vec4i> hierarchy;		//�ܰ��������� ����(����)�� �����ϴ� ����. 
	Rect rect;						//�簢���� ǥ���ϱ� ���� Ŭ����.



	char filename[256];
	cout << "�����̸� �Է�(Ȯ���� ����) : ";
	cin >> filename;



	//�̹��������� �ε��Ͽ� img_original �� ����.
	img_original = imread(filename, IMREAD_GRAYSCALE); //adaptiveThreshold�� �����Ű�� ���� �̹����� �׷��̽����Ϸ� �ҷ���. 
	if (img_original.empty())
	{
		cout << "Could not open or find the image" << std::endl;
		return -1;
	}

	
	GaussianBlur(img_original, img_gray, size, 0); //����þ� ���� ����.
	adaptiveThreshold(img_gray, img_adaptive, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, 75, 10);
	//�̹����� ����ȭ�� �����ϴ� �Լ�.
	bitwise_not(img_adaptive, img_adaptive);
	//adaptiveThreshold ��, ���ڰ� ������ ����� ����̹Ƿ� �̸� ���� ���ϰ� ������ ������.

	findContours(img_adaptive, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_TC89_KCOS, Point());
	//�̹������� ������ �ܰ����� ã�� contours�� �ִ� �޼ҵ�.




	vector<vector<Point>> contours_poly(contours.size());
	vector<Rect> boundRect(contours.size());
	vector<Rect> boundRect2(contours.size());



	for (int i = 0; i < contours.size(); i++)
	{
		
		approxPolyDP(Mat(contours[i]), contours_poly[i], 3, true);
		//�ټ� ���������� �ܰ����� �ٻ���� ���� �� �� �ε巴�� �����.

		
		boundRect[i] = boundingRect(Mat(contours_poly[i]));
		//�ܰ����� �簢���� �Ҵ��ϴ� �Լ�.


	}

	
	cvtColor(img_adaptive, img_adaptive, CV_GRAY2RGB);
	//�̹����� �׷��̽����Ϸ� �ҷ��Ա� ������ �ܰ����̳� �簢���� ������� ���̴� ������ �߻�.
	//�����ϱ� ���� �������� �ǵ�����.




	//�ܰ����� �簢���� �׷��ִ� �۾�. ���� �ܰ����� ���� �׸� �ʿ䰡 ���ٰ� ������ �ּ�ó��.
	for (int i = 0; i < contours.size(); i++)
	{
		/*drawContours(img_adaptive, contours, i, Scalar(0, 0, 0), 1, 8, hierarchy, 0, Point());*/
		rectangle(img_adaptive, boundRect[i].tl(), boundRect[i].br(), Scalar(0, 255, 0), 1, 8, 0);
			
	}

	

	//������ ����  
	namedWindow("original image", WINDOW_AUTOSIZE);
	namedWindow("gray image", WINDOW_AUTOSIZE);




	//�����쿡 ���  
	imshow("original image", img_original);
	imshow("gray image", img_adaptive);

	//Ű���� �Է��� �ɶ����� ���  
	/*waitKey(0);*/

	//��ũ�� ����  
	string savename(filename);
	imwrite(savename+"_con.tiff", img_adaptive);

	return 0;
}