package ce.inu.ikta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import static ce.inu.ikta.globalValue.bitimg;

public class resultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_result );
        ImageView aa = (ImageView) findViewById( R.id.resultimg );
        aa.setImageBitmap( bitimg );

    }
}
