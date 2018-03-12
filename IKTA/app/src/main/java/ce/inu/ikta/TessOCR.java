package ce.inu.ikta;

        import android.graphics.Bitmap;

        import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Created by 김광현 on 2018-03-11.
 */

public class TessOCR {
    TessBaseAPI mtess;

    TessOCR(String path, String lang) {
        mtess = new TessBaseAPI();
        //mtess.init(path, lang);
    }
}
