package ce.inu.ikta;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //변수 및 객체 선언
    private static final String TAG = "MainActivity";
    Context ctx;
    ImageButton cameraShtBtn;
    OrientationEventListener orientEventListener;
    public AppCompatActivity mActivity;
    RequestPerm requestPerm;
    CameraForOCR camera;
    MyView myView;
    LinearLayout linearLayout;
    SurfaceView surfaceView;
    float[] size;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        setValue();
        setListener();
        requestPerm.setPermissions();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        linearLayout = (LinearLayout) this.findViewById(R.id.cameraTopView);
        surfaceView = (SurfaceView) this. findViewById( R.id.cameraView );
        size = new float[4];
        size[0] = linearLayout.getWidth();
        size[1] = linearLayout.getHeight();
        size[2] = surfaceView.getWidth();
        size[3] = surfaceView.getHeight();
        myView = MyView.create( ctx, size );
        linearLayout.removeAllViews();
        linearLayout.addView( myView, new RelativeLayout.LayoutParams
                ( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        );
    }

    //각종 value 설정
    void setValue() {
        mActivity = this;
        ctx = this;
        linearLayout = (LinearLayout) findViewById(R.id.cameraTopView);
        cameraShtBtn= (ImageButton) findViewById( R.id.cameraShutterBtn );  //카메라 버튼 id 매칭
        requestPerm = new RequestPerm( this,this );
        camera = new CameraForOCR(ctx,mActivity);
    }
    //각종 리스너 선언
    void setListener() {
        // 회전 리스너
        orientEventListener = new OrientationEventListener(ctx,SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                camera.setOrientation(orientation);
            }
        };
        if (orientEventListener.canDetectOrientation()) {
            orientEventListener.enable();   //방향 감지 가능 > 활성화
        } else {
            finish();
        }

        //카메라 버튼 클릭 리스너
        cameraShtBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.cameraShutterBtn) {
                    cameraShtBtn.setEnabled( false ); //버튼이 눌렸을 때 버튼을 비활성화
                    camera.takePicture();
                }
                else {
                }
            }
        } );
    }

    //권한 확인
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
        requestPerm.monRequestPermissionsResult(requestCode,permissions,grandResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.pause();
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
        mediaScanIntent.setData( Uri.fromFile( file ) );
        sendBroadcast( mediaScanIntent );
    }

}