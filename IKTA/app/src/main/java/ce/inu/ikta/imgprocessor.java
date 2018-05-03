package ce.inu.ikta;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.android.Utils;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static ce.inu.ikta.globalValue.bitimg;

/**
 * Created by 김광현 on 2018-04-07.
 */

public class imgprocessor {
    // loadLibrary...

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }

    // native methods...
    private native Mat imgFiltering(long matAddrInput);
    private native void imgFiltering2(long matAddrInput);

    private List<MatOfPoint> contours = new ArrayList<>();
    private List<MatOfPoint2f> contours_poly = new ArrayList<>();

    // java methods...
    public Vector<Bitmap> imgfilter(Bitmap src)
    {
        //src 를 Mat로 변환하기 위한 객체 선언
        Mat tmp = new Mat (src.getWidth(), src.getHeight(), CvType.CV_8UC1);
        Vector<Bitmap> result = new Vector<>( );
        Utils.bitmapToMat(src, tmp);
        Mat asd = imgFiltering(tmp.getNativeObjAddr());

         //   Log.d("imgprocessor", "asd는 " +asd.length);

     //   Log.d("imgprocessor","width : " + asd[0].width() + "height : " + asd[0].height() );
       // for(int i = 0 ; i < asd.length ; i++) {
            Bitmap bmp = Bitmap.createBitmap(asd.width(),asd.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(asd, bmp);
            result.add(bmp);
//        }


        return result;
    }
    public void imgfilter2() {
        Mat tmp = new Mat (bitimg.getWidth(), bitimg.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(bitimg, tmp);
        Imgproc.cvtColor(tmp,tmp,Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur( tmp,tmp, new Size(3,3) , 1,1 );
        //Imgproc.threshold( tmp,tmp,0,255,Imgproc.THRESH_BINARY+ Imgproc.THRESH_OTSU);
       Imgproc.adaptiveThreshold( tmp,tmp,255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C , Imgproc.THRESH_BINARY, 75, 10);
        Core.bitwise_not(tmp,tmp);

        drawcontours(tmp, contours);
        Utils.matToBitmap(tmp, bitimg);
    }

    public void drawcontours(Mat img, List<MatOfPoint> contours)
    {
        Mat hierarchy = new Mat();
        contours.clear();
        List<Rect> rects = new ArrayList<>(contours.size());
        Imgproc.findContours(img, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_TC89_KCOS);

        MatOfPoint2f approxCurve = new MatOfPoint2f();

        Imgproc.cvtColor(img,img,Imgproc.COLOR_GRAY2RGB);


        for (int i = 0; i< contours.size();i++)
        {
             MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            if(approxDistance > 1)
            {
            Imgproc.approxPolyDP(contour2f, approxCurve,approxDistance, true);
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());
            rects.add(Imgproc.boundingRect(points));
            }

        }

        for (int i=0; i<contours.size();i++)
        {
            Imgproc.drawContours(img, contours, i, new Scalar(0,0,0), 1, 8, hierarchy, 0, new Point());
           if(rects.get(i).width>10 && rects.get(i).height>10)
           {
            Imgproc.rectangle(img, rects.get(i).tl(), rects.get(i).br(), new Scalar(0,255,0), 1, 8 ,0);
            }

        }

    }

}
