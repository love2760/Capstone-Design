#include <iostream>
#include <fstream>
#include <windows.h>
#include <opencv2/highgui.hpp>  
#include <opencv2/imgproc.hpp> 
#include <opencv2/opencv.hpp>
#include "boxdata.cpp"
using namespace std;
using namespace cv;

#define TIFWIDTH 9000
#define TIFHEIGHT 30 


int numOfFiles(char* searchPath)
{
	WIN32_FIND_DATA	FindData;
	HANDLE		hFiles;
	LPTSTR		lptszFiles[100];
	UINT		nFileCount = 0;

	hFiles = FindFirstFile(searchPath, &FindData);

	if (hFiles == INVALID_HANDLE_VALUE)
		return 0;

	bool bFinished = false;
	while (!bFinished) {
		if (!(FindData.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)) {
			nFileCount++;
		}

		if (!FindNextFile(hFiles, &FindData)) {
			bFinished = true;
		}
	}
	FindClose(hFiles);
	return nFileCount;
}


Mat AutoExtendsTif(Mat src) {
	Mat tif2 = Mat::zeros(src.size().height*1.2, src.size().width, CV_8UC3);
	Mat tmp = tif2.colRange(0, src.size().width).rowRange(0, src.size().height);
	src.copyTo(tmp);
	src = NULL;
	return tif2;
}
void GenerateBox(vector<boxdata> boxes, string filename, int imgHeight) {
	ofstream box(filename + ".box");
	cout << "Generating box file..." << endl;
	int num = boxes.size();
	for (int i = 0; i < num; i++) {
		if (boxes[i].latter == "DIV") {
			box << "/" << " "
				<< boxes[i].x << " "
				<< imgHeight - (boxes[i].y + boxes[i].height) << " "
				<< boxes[i].x + boxes[i].width << " "
				<< imgHeight - boxes[i].y << endl;
		}
		else if (boxes[i].latter == "MUL") {
			box << "*" << " "
				<< boxes[i].x << " "
				<< imgHeight - (boxes[i].y + boxes[i].height) << " "
				<< boxes[i].x + boxes[i].width << " "
				<< imgHeight - boxes[i].y << endl;
		}
		else {
			box << boxes[i].latter << " "
				<< boxes[i].x << " "
				<< imgHeight - (boxes[i].y + boxes[i].height) << " "
				<< boxes[i].x + boxes[i].width << " "
				<< imgHeight - boxes[i].y << endl;
		}
		cout << "\r[" << i+1 << " / " << num << "]";
	}
	cout << " complete!" << endl;
}


void GenerateTif(vector<string> keys,vector<boxdata> boxes, string datasetD,string filename) {
	int x = 10;
	int y = 10;
	int maxy = 0;
	Mat tif = Mat::zeros(TIFHEIGHT,TIFWIDTH,CV_8UC3);
	Mat readImg;
	for (int i = 0; i < keys.size(); i++) {
		stringstream searchPath;
		stringstream latter;
		latter << keys[i];
		searchPath << datasetD << keys[i] << "\\*.jpg";
		int num = numOfFiles((char*)searchPath.str().c_str());
		cout << "number of Filles in " << keys[i] << " Directory : " << num << endl;
		for (int j = 0; j < num; j++) {
			stringstream imgPath;
			imgPath << datasetD<< keys[i] << "\\" << keys[i] << " (" << j+1 << ").jpg";
			readImg = imread(imgPath.str());
			//extends tif

			if (x + readImg.size().width + 20 > tif.size().width) {
				x = 10;
				y = maxy + 20;
			}
			if (y + readImg.size().height+20 > tif.size().height) {
				while (y + readImg.size().height+20 > tif.size().height) {
					tif = AutoExtendsTif(tif);
				}
			}
			boxdata box(x,y,readImg.size().width,readImg.size().height,keys[i]);
			boxes.push_back(box);
			Mat tmp = tif.colRange(x, x + readImg.size().width).rowRange(y, y + readImg.size().height);
			readImg.copyTo(tmp);
			x = x + readImg.size().width + 20;
			if (maxy < y + readImg.size().height) 
				maxy = y + readImg.size().height;	
			cout << "\r[" << j + 1 << "/" << num << "] ";
		}
		if(num !=0)cout << "complete!" << endl;
	}
	imwrite(filename+".tif", tif);
	GenerateBox(boxes, filename, tif.size().height);
}



int main(void) {

	string fileName;
	char cCurrentPath[256];
	stringstream datasetDirectory;
	vector<boxdata> boxes;

	cout << "Who wrote it ? >>  ";
	cin >> fileName;
	fileName = "ikta." + fileName + ".exp0";
	GetCurrentDirectory(sizeof(cCurrentPath), cCurrentPath);
	vector<string> keys = {"DIV","MUL","-","=","+","0","1","2","3","4","5","6","7","8","9"};
	datasetDirectory << cCurrentPath << "\\dataset\\";
	
	if (CreateDirectory("dataset", NULL) == 0) {
		cout << "dataset Directory is already exists." << endl;
	}
	else cout << "create dataset Directory!" << endl;

	GenerateTif(keys, boxes, datasetDirectory.str(), fileName);
	
	system("pause");
}