package ce.inu.ikta;

        import android.content.Context;
        import android.graphics.Bitmap;

        import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Created by 김광현 on 2018-03-11.
 */

public class TessOCR {
    TessBaseAPI mtess;
    Context mCtx;
    TessOCR(Context context) {
        mCtx = context;
        TessDataManager.initTessTrainedData(mCtx);
        mtess = new TessBaseAPI();
        String path = TessDataManager.getTrainedDataPath();
        mtess.init(path,"ikta");
        //추천 글자 설정
        // mtess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        // 비추천 글자 설정
        //mtess.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-[]}{;:'\"\\|~`,./<>?");
        mtess.setPageSegMode(TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED);


    }

    public String requestOCR () {
        //mtess.setImage(byte[] imagedata, int width, int height, int bpp, int bpl);
        String output = mtess.getUTF8Text();
        return output;
    }
}
