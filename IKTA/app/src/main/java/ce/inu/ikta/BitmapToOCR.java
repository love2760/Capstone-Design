package ce.inu.ikta;

import android.graphics.Bitmap;

import java.util.Vector;

/**
 * Created by NyuNyu on 2018-05-01.
 */

public class BitmapToOCR {
    Vector<Bitmap> vecBmp;
    TessOCR tessOCR;
    public BitmapToOCR(Vector<Bitmap> vecBmp, TessOCR tessOCR) {
        this.vecBmp = vecBmp;
        this.tessOCR = tessOCR;
    }
    public String excuteOCR() {
        String result = "";
        for(int i = 0 ; i < vecBmp.size() ; i++) {
            result = result + tessOCR.requestOCR(vecBmp.get(i));
        }
        return result;
    }
}
