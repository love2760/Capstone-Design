package ce.inu.ikta;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static ce.inu.ikta.globalValue.bitimg;
import static ce.inu.ikta.globalValue.dataPath;


public class MainActivity extends AppCompatActivity {

    //변수 및 객체 선언
    private static final String TAG = "MainActivity";
    Preview preview;
    Camera camera;
    Context ctx;
    CutImage cutImage;
    OrientationEventListener orientEventListener;
    ImageButton cameraShtBtn;
    int orientation;    //방향전환을 위한 변수
    RelativeLayout relativeLayout;
    SurfaceView surfaceView;
    private final static int CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_BACK;
    public AppCompatActivity mActivity;
    RequestPerm requestPerm;
    TessOCR tessOCR;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //relativeLayout = (RelativeLayout) findViewById( R.id.box );
        //relativeLayout.setBackgroundResource( R.drawable.outline );
        //surfaceView = (SurfaceView) findViewById( R.id.cameraView );

        //wolframalpha wolf = new wolframalpha();
        //wolf.Wolfoutput("1+1");
        //Log.d("울프람울프람",wolf.Wolfoutput("1+1").answer);

        //cutImage = (CutImage) findViewById( R.id.showCustom );

        setValue();
        setListener();
        requestPerm.setPermissions();

    }

    /*
     * 2018 04 09 김광현
     * traineddata에 접근하기 위해서는 파일이 존재하는
     * 디렉토리의 절대경로를 알아야 하기 때문에 assets에 존재하는
     * traineddata를 외부 저장소에 옮겨서 해당 저장소 경로를 가르키도록
     * 하기위해 외부 저장소로 옮기는 작업을 하는 메소드
     */


    //각종 value 설정
    void setValue() {
        mActivity = this;
        ctx = this;
        cameraShtBtn= (ImageButton) findViewById( R.id.cameraShutterBtn );  //카메라 버튼 id 매칭
        requestPerm = new RequestPerm( this,this );
        tessOCR = new TessOCR(ctx);
    }
    //각종 리스너 선언
    void setListener() {
        // 회전 리스너
        orientEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation2) {
                orientation=orientation2;
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
                camera.autoFocus(myAutoFocusCallback);
                camera.takePicture( shutterCallback, null, bitmapCallback );
            }
        } );
    }

    //auto focus
    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
        }
    };

    //각종 권한을 갖고 있는지 확인
    //없다면 요청



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




    public void startCamera() {

        //프리뷰 초기화
        if (preview == null) {
            preview = new Preview( this, (SurfaceView) findViewById( R.id.cameraView) );
            preview.setLayoutParams( new LayoutParams( LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT ) );  //프리뷰 크기 정함
            ((RelativeLayout) findViewById( R.id.layout )).addView( preview );
            preview.setKeepScreenOn( true );    //화면을 켠 상태로 유지

        }

        //카메라 초기화
        preview.setCamera( null );
        if (camera != null) {
            camera.release();   //카메라 객체 비우기
            camera = null;      //null로 초기화
        }

        //카메라 개수 받아옴
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {

                camera = Camera.open( CAMERA_FACING );  //후면 카메라 불러옴
                camera.setDisplayOrientation( 90 ); //방향 고정
                List<Camera.Size> tmp = camera.getParameters().getSupportedPictureSizes();
                Camera.Size Max = tmp.get(0);
                for(int i = 0 ; i < tmp.size() ; i++) {
                    if( tmp.get( i ).width*tmp.get( i ).height > Max.width*Max.height)
                        Max =tmp.get(i);
                }
                camera.getParameters().setPictureSize( Max.width,Max.height );
                camera.startPreview();  //프리뷰 시작

            } catch (RuntimeException ex) {
                Toast.makeText( ctx, "camera_not_found " + ex.getMessage().toString(),
                        Toast.LENGTH_LONG ).show();
                Log.d( TAG, "camera_not_found " + ex.getMessage().toString() );
            }
        }

        preview.setCamera( camera );    //프리뷰의 카메라 설정
    }






    @Override
    protected void onResume() {
        super.onResume();

        startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Surface will be destroyed when we return, so stop the preview.
        if (camera != null) {
            // Call stopPreview() to stop updating the preview surface
            camera.stopPreview();
            preview.setCamera( null );
            camera.release();
            camera = null;
        }

        ((RelativeLayout) findViewById( R.id.layout )).removeView( preview );
        preview = null;

    }

    private void resetCam() {
        startCamera();
        showCheckForEquationAlertdialog();
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
        mediaScanIntent.setData( Uri.fromFile( file ) );
        sendBroadcast( mediaScanIntent );
    }

    ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            Log.d( TAG, "onShutter'd" );
        }
    };

    //원본 : jpegCallback 이었으나 bitmap으로 처리함
    //참고 : http://stackoverflow.com/q/37135675
    PictureCallback bitmapCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //이미지의 너비와 높이 결정
            int w= camera.getParameters().getPictureSize().width;
            int h = camera.getParameters().getPictureSize().height;

            //byte array를 bitmap으로 변환
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;    //디코딩 방식을 ARGB_8888로 설정
            bitimg = BitmapFactory.decodeByteArray( data, 0, data.length, options );    //디코딩

            //이미지를 디바이스 방향으로 회전
            Matrix matrix = new Matrix();
            matrix.postRotate( checkDeviceOrientation( orientation ) );

            bitimg = Bitmap.createBitmap( bitimg, 0, 0, 1000, 1000, matrix, true );

            Log.d( TAG,"bitmapCallback close" );
            resetCam();
        }
    };


    private int checkDeviceOrientation(int orientation) {
        Log.d( TAG,"방향확인" );
        if(orientation>=315 || orientation<45) {
            Log.d( TAG,"방향1" );
            return 90;
        }
        else if(orientation>=45 && orientation<135) {
            Log.d( TAG,"방향2" );
            return 180;
        }
        else if(orientation>=135 && orientation<225) {
            Log.d( TAG,"방향3" );
            return 270;
        }
        else if(orientation>=225 && orientation<315) {
            Log.d( TAG,"방향4" );
            return 0;
        } else { Log.d( TAG,"방향확인 실패" ); return 0; }
    }

    //인식한 결과가 맞는지 확인하기 위해 dialog 창을 띄움
    private void showCheckForEquationAlertdialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder( MainActivity.this );
        imgprocessor a = new imgprocessor();

        alert.setTitle("다음의 식이 맞습니까?").setMessage(tessOCR.requestOCR( bitimg )).setCancelable( false ).setPositiveButton( "확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent( MainActivity.this,resultActivity.class );
                        cameraShtBtn.setEnabled(true);
                        startActivity( i );
                    }
                } ).setNegativeButton( "취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cameraShtBtn.setEnabled(true);
                        return;
                    }
                } );
        alert.show();
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