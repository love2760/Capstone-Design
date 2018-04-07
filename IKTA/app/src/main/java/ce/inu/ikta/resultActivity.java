package ce.inu.ikta;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import static ce.inu.ikta.globalValue.bitimg;

public class resultActivity extends AppCompatActivity {
    Context ctx;
    imgprocessor imgproc;
    ImageView imgView;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_result );

        setValue();
        imgView.setImageBitmap( imgproc.imgfilter(bitimg) );
    }
    private void setValue() {
        imgView = (ImageView) findViewById( R.id.resultimg );
        ctx = this;
        imgproc = new imgprocessor();
    }
}
