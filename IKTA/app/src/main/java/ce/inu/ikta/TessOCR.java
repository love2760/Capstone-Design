package ce.inu.ikta;

        import android.graphics.Bitmap;
        import com.googlecode.tesseract.android.TessBaseAPI;
/**
 * Created by 김광현 on 2018-03-11.
 * https://tesseract.patagames.com/help/html/T_Patagames_Ocr_Enums_PageSegMode.htm
 * 위 주소는 tesseract segMode에 관하여 정리된 문서임.
 */

public class TessOCR {
    TessBaseAPI mtess;
    public TessOCR(String datapath) {

        mtess = new TessBaseAPI();
        mtess.init(datapath,"ikta");
        mtess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);

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
