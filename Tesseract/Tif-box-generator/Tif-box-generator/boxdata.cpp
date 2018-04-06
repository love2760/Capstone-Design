#include <iostream>

class boxdata {
public :
	int x;
	int y;
	int width;
	int height;
	std::string latter;

	boxdata(int x,int y, int width, int height, std::string latter) {
		this->x = x;
		this->y = y;
		this->width = width;
		this->height = height;
		this->latter = latter;
	}
};