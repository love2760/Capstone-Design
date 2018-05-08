package ce.inu.ikta;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by NyuNyu on 2018-04-09.
 */

public class wolfData {
    public String input;
    public String answer;
    public String graph;
    public String solution;

    public wolfData(String input, String answer,String graph, String solution) {
        this.input = input;
        this.answer = answer;
        this.graph = graph;
        this.solution = solution;
    }

    public String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream );
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }


}