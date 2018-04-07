package ce.inu.ikta;

        import android.graphics.Bitmap;
        import com.googlecode.tesseract.android.TessBaseAPI;


/**
 * Created by 김광현 on 2018-03-11.
 */

public class TessOCR {
    TessBaseAPI mtess;
    TessOCR(String datapath) {

        mtess = new TessBaseAPI();
        mtess.init(datapath,"ikta");
        mtess.setPageSegMode(TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED);

        //추천 글자 설정
        // mtess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "01234");
        // 비추천 글자 설정
        //mtess.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-[]}{;:'\"\\|~`,./<>?");

    }

    public String requestOCR (Bitmap input) {
        //mtess.setImage(byte[] imagedata, int width, int height, int bpp, int bpl);
        mtess.setImage(input);
        String output = mtess.getUTF8Text();
        return output;
    }
}
