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

    public wolfData(String input, String answer,String graph) {
        this.input = input;
        this.answer = answer;
        this.graph = graph;
    }

}