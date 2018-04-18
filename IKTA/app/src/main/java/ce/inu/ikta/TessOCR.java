package ce.inu.ikta;

        import android.content.Context;
        import android.content.res.AssetManager;
        import android.graphics.Bitmap;
        import android.util.Log;

        import com.googlecode.tesseract.android.TessBaseAPI;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;

/**
 * Created by 김광현 on 2018-03-11.
 * https://tesseract.patagames.com/help/html/T_Patagames_Ocr_Enums_PageSegMode.htm
 * 위 주소는 tesseract segMode에 관하여 정리된 문서임.
 */

public class TessOCR {
    TessBaseAPI mtess;
    final String TAG = "TessOCR.java";
    Context ctx;
    String datapath;
    public TessOCR(Context ctx) {
        this.ctx = ctx;
        copyTrainData();
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
    private void copyTrainData() {
        AssetManager assetManager = ctx.getAssets();
        InputStream in = null;
        OutputStream out = null;
        String ASSET_FILE_PATH = "tessdata/ikta.traineddata";
        String FILENAME = "tessdata/ikta.traineddata";
        // TESSDATAPATH = getFilesDir().getAbsolutePath()+"/tessdata";
        try {
            in = assetManager.open(ASSET_FILE_PATH);
            Log.v(TAG,"assets 파일 오픈 성공");
            Log.v(TAG,"폴더 이름 "+ctx.getFilesDir().getAbsolutePath());
            File tessDataFolder = new File(ctx.getFilesDir().getAbsolutePath(),"tessdata");
            if(!tessDataFolder.exists()) {
                tessDataFolder.mkdirs();
            }
            File outFile = new File(ctx.getFilesDir(), FILENAME);
            out = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int read;
            while((read = in.read(buffer)) != -1){
                out.write(buffer, 0, read);
            }
            Log.v(TAG,"파일 쓰기 성공");
            datapath =  ctx.getFilesDir().getAbsolutePath();
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) try {in.close();}catch(IOException e){/*nothing*/}
            if(out != null) try {out.close();}catch(IOException e){/*nothing*/}
        }
    }
}
