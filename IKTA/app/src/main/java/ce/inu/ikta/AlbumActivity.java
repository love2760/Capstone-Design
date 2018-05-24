package ce.inu.ikta;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.FileNotFoundException;
import java.io.IOException;

import static ce.inu.ikta.globalValue.bitimg;

public class AlbumActivity extends AppCompatActivity {

    private static final String TAG = "AlbumActivity";
    Context context;
    LinearLayout linearLayout;
    ImageView albumView;
    AlbumDrawView albumDrawView;
    CutImg cutImg;
    public AppCompatActivity activity;
    ImageButton cut_ok;
    float[] size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_album );

        context = AlbumActivity.this;
        albumView = (ImageView) findViewById( R.id.album );

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra( "uri" );
        try {
            bitimg = MediaStore.Images.Media.getBitmap( getContentResolver(), uri );
            albumView.setImageBitmap( bitimg );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        linearLayout = (LinearLayout) this.findViewById( R.id.top );
        size = new float[4];
        size[0] = albumView.getX();
        size[1] = albumView.getY();
        size[2] = albumView.getWidth();
        size[3] = albumView.getHeight();
        linearLayout.removeAllViews();
        albumDrawView.albumDrawView = null;
        albumDrawView = AlbumDrawView.create( context, size );
        linearLayout.addView( albumDrawView, new RelativeLayout.LayoutParams
                ( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT )
        );

        cut_ok = (ImageButton) findViewById( R.id.cut_button );
        cut_ok.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.cut_button) {
                    cutImg = new CutImg( context, activity, cut_ok,size );
                    cut_ok.setEnabled( false );
                    cutImg.cutImage(albumDrawView.topY,albumDrawView.bottomY,albumDrawView.leftX,albumDrawView.rightX);
                } else {
                }
            }
        } );
    }

}
