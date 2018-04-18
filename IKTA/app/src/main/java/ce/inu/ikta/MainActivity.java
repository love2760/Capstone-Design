package ce.inu.ikta;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;

import static ce.inu.ikta.globalValue.bitimg;

public class MainActivity extends AppCompatActivity {

    //변수 및 객체 선언
    private static final String TAG = "MainActivity";
    Context ctx;
    OrientationEventListener orientEventListener;
    ImageButton cameraShtBtn;
    public AppCompatActivity mActivity;
    RequestPerm requestPerm;
    CameraForOCR camera;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        setValue();
        setListener();
        requestPerm.setPermissions();
    }

    //각종 value 설정
    void setValue() {
        mActivity = this;
        ctx = this;
        cameraShtBtn= (ImageButton) findViewById( R.id.cameraShutterBtn );  //카메라 버튼 id 매칭
        requestPerm = new RequestPerm( this,this );
        camera = new CameraForOCR(ctx,mActivity);
    }
    //각종 리스너 선언
    void setListener() {
        // 회전 리스너
        orientEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
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
                cameraShtBtn.setEnabled(false); //버튼이 눌렸을 때 버튼을 비활성화
                camera.takePicture();
            }
        } );
    }

    //권한 확인
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (requestCode == requestPerm.getPermissionsRequestCode() && grandResults.length > 0) {

            //권한 상태 불러옴
            int hasCameraPermission = ContextCompat.checkSelfPermission( ctx,
                    Manifest.permission.CAMERA );
            int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission( ctx,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE );
            int hasReadExternalStoragePermission = ContextCompat.checkSelfPermission( ctx,
                    Manifest.permission.READ_EXTERNAL_STORAGE );

            if (hasCameraPermission == PackageManager.PERMISSION_GRANTED
                    && hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED
                    && hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {

                //이미 퍼미션을 가지고 있음
                requestPerm.doRestart( this );
            } else {
                requestPerm.checkPermissions();
            }
        }

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



    /*
    private int[] r_size() {
        final int R_int[] = new int[2];
        ViewTreeObserver viewTreeObserver_i = relativeLayout.getViewTreeObserver();
        viewTreeObserver_i.addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                relativeLayout.getViewTreeObserver().removeOnGlobalLayoutListener( this );
                R_int[0] = relativeLayout.getMeasuredWidth();
                R_int[1] = relativeLayout.getMeasuredHeight();
                Log.d(TAG,"R_width "+R_int[0]+" R_height "+R_int[1]);
            }
        } );
        return R_int;
    }

    private int[] s_size() {
        final int S_int[] = new int[2];
        ViewTreeObserver viewTreeObserver_s = surfaceView.getViewTreeObserver();
        viewTreeObserver_s.addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                surfaceView.getViewTreeObserver().removeOnGlobalLayoutListener( this );
                S_int[0] = surfaceView.getMeasuredWidth();
                S_int[1] = surfaceView.getMeasuredHeight();
                Log.d(TAG,"s_width "+S_int[0]+" s_height "+S_int[1]);
            }
        } );
        return S_int;
    }*/

}