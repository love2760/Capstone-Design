package ce.inu.ikta;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import static ce.inu.ikta.globalValue.bitimg;

public class resultActivity extends AppCompatActivity {
    Context ctx;
    ImageView imgView;
    String TAG = "resultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_result );

        setValue();
        imgView.setImageBitmap( bitimg );
    }
    private void setValue() {
        imgView = (ImageView) findViewById( R.id.resultimg );
        String dataPath = getExternalFilesDir(null).getAbsolutePath();
        ctx = this;
    }

}
