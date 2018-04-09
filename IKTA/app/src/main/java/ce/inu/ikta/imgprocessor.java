package ce.inu.ikta;

import android.graphics.Bitmap;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.android.Utils;
import org.opencv.imgproc.Imgproc;

/**
 * Created by 김광현 on 2018-04-07.
 */

public class imgProcessor {
    // loadLibrary...

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }

    // native methods...
    private native void imgFiltering(long matAddrInput);

    // java methods...
    public Bitmap imgfilter(Bitmap src) {
        Mat tmp = new Mat (src.getWidth(), src.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(src, tmp);
        imgFiltering(tmp.getNativeObjAddr());
        Bitmap bmp = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(tmp, bmp);
        return bmp;
    }
}
