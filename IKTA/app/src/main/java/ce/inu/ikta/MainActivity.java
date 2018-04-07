package ce.inu.ikta;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static ce.inu.ikta.globalValue.bitimg;


public class MainActivity extends AppCompatActivity {

    //변수 및 객체 선언
    private static final String TAG = "MainActivity";
    Preview preview;
    Camera camera;
    Context ctx;
    OrientationEventListener orientEventListener;
    ImageButton cameraShtBtn;
    int orientation;
    private final static int PERMISSIONS_REQUEST_CODE = 100;
    private final static int CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_BACK;
    public AppCompatActivity mActivity;
    boolean flag;
    MainActivity aaa;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        mActivity = this;
        aaa = this;
        setValue();
        setListener();
        setPermissions();
        copyTrainData();
    }
    private void copyTrainData() {
        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream out = null;
        String ASSET_FILE_PATH = "tessdata/ikta.traineddata";
        String FILENAME = "ikta.traineddata";

        try {
            in = assetManager.open(ASSET_FILE_PATH);
            Log.v(TAG,"assets 파일 오픈 성공");
            Log.v(TAG,"폴더 이름 "+getExternalFilesDir(null));
            File outFile = new File(getExternalFilesDir(null), FILENAME);
            out = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int read;
            while((read = in.read(buffer)) != -1){
                out.write(buffer, 0, read);
            }
            Log.v(TAG,"파일 쓰기 성공");
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) try {in.close();}catch(IOException e){/*nothing*/}
            if(out != null) try {out.close();}catch(IOException e){/*nothing*/}
        }
    }


    void setValue() {
        ctx = this;
        cameraShtBtn= (ImageButton) findViewById( R.id.cameraShutterBtn );
        flag =false;
    }
    void setListener() {
        // 회전 리스너
        orientEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation2) {
                orientation=orientation2;
            }
        };
        if (orientEventListener.canDetectOrientation()) {
            orientEventListener.enable();
        } else {
            finish();
        }
        //카메라 버튼 클릭 리스너
        cameraShtBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraShtBtn.setEnabled(false);
                camera.autoFocus(myAutoFocusCallback);
                camera.takePicture( shutterCallback, rawCallback, bitmapCallback );
            }
        } );
    }
    //auto focus
    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
        }
    };

    void setPermissions() {
        if (getPackageManager().hasSystemFeature( PackageManager.FEATURE_CAMERA )) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //API 23 이상이면
                // 런타임 퍼미션 처리 필요

                int hasCameraPermission = ContextCompat.checkSelfPermission( this, Manifest.permission.CAMERA );
                int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int hasReadExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (hasCameraPermission == PackageManager.PERMISSION_GRANTED
                        && hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED
                        && hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
                    //이미 퍼미션을 가지고 있음
                } else {
                    //퍼미션 요청
                    ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE }, PERMISSIONS_REQUEST_CODE );
                }
            }


        } else {
            Toast.makeText( MainActivity.this, "Camera not supported",
                    Toast.LENGTH_LONG ).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (requestCode == PERMISSIONS_REQUEST_CODE && grandResults.length > 0) {

            int hasCameraPermission = ContextCompat.checkSelfPermission( this,
                    Manifest.permission.CAMERA );
            int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission( this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE );
            int hasReadExternalStoragePermission = ContextCompat.checkSelfPermission( this,
                    Manifest.permission.READ_EXTERNAL_STORAGE );

            if (hasCameraPermission == PackageManager.PERMISSION_GRANTED
                    && hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED
                    && hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {

                //이미 퍼미션을 가지고 있음
                doRestart( this );
            } else {
                checkPermissions();
            }
        }

    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        int hasCameraPermission = ContextCompat.checkSelfPermission( this,
                Manifest.permission.CAMERA );
        int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission( this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE );
        int hasReadExternalStoragePermission = ContextCompat.checkSelfPermission( this,
                Manifest.permission.READ_EXTERNAL_STORAGE );


        boolean cameraRationale = ActivityCompat.shouldShowRequestPermissionRationale( this,
                Manifest.permission.CAMERA );
        boolean writeExternalStorageRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean readExternalStorageRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((hasCameraPermission == PackageManager.PERMISSION_DENIED && cameraRationale)
                || (hasReadExternalStoragePermission== PackageManager.PERMISSION_DENIED
                && readExternalStorageRationale)
                || (hasWriteExternalStoragePermission== PackageManager.PERMISSION_DENIED
                && writeExternalStorageRationale))
            showDialogForPermission( "앱을 실행하려면 퍼미션을 허가하셔야합니다." );

        else if ((hasCameraPermission == PackageManager.PERMISSION_DENIED && !cameraRationale)
                || (hasReadExternalStoragePermission== PackageManager.PERMISSION_DENIED
                && !readExternalStorageRationale)
                || (hasWriteExternalStoragePermission== PackageManager.PERMISSION_DENIED
                && !writeExternalStorageRationale))
            showDialogForPermissionSetting( "퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다." );

        else if (hasCameraPermission == PackageManager.PERMISSION_GRANTED
                || hasWriteExternalStoragePermission== PackageManager.PERMISSION_GRANTED
                || hasReadExternalStoragePermission== PackageManager.PERMISSION_GRANTED ) {
            doRestart( this );
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this );
        builder.setTitle( "알림" );
        builder.setMessage( msg );
        builder.setCancelable( false );
        builder.setPositiveButton( "예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //퍼미션 요청
                ActivityCompat.requestPermissions( MainActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_CODE );
            }
        } );

        builder.setNegativeButton( "아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        } );
        builder.create().show();
    }

    private void showDialogForPermissionSetting(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this );
        builder.setTitle( "알림" );
        builder.setMessage( msg );
        builder.setCancelable( true );
        builder.setPositiveButton( "예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent myAppSettings = new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse( "package:" + mActivity.getPackageName() ) );
                myAppSettings.addCategory( Intent.CATEGORY_DEFAULT );
                myAppSettings.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                mActivity.startActivity( myAppSettings );
            }
        } );
        builder.setNegativeButton( "아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        } );
        builder.create().show();
    }




    //앱 재시작
    public static void doRestart(Context c) {
        //http://stackoverflow.com/a/22345538
        try {
            //주어진 context 확인
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        //create a pending intent so the application is restarted
                        // after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity( c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT );
                        AlarmManager mgr =
                                (AlarmManager) c.getSystemService( Context.ALARM_SERVICE );
                        mgr.set( AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent );
                        //kill the application
                        System.exit( 0 );
                    } else {
                        Log.e( TAG, "Was not able to restart application, " +
                                "mStartActivity null" );
                    }
                } else {
                    Log.e( TAG, "Was not able to restart application, PM null" );
                }
            } else {
                Log.e( TAG, "Was not able to restart application, Context null" );
            }
        } catch (Exception ex) {
            Log.e( TAG, "Was not able to restart application" );
        }
    }

    public void startCamera() {

        if (preview == null) {
            preview = new Preview( this, (SurfaceView) findViewById( R.id.cameraView) );
            preview.setLayoutParams( new LayoutParams( LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT ) );
            ((FrameLayout) findViewById( R.id.layout )).addView( preview );
            preview.setKeepScreenOn( true );

        }

        preview.setCamera( null );
        if (camera != null) {
            camera.release();
            camera = null;
        }

        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {

                camera = Camera.open( CAMERA_FACING );
                // camera orientation
                camera.setDisplayOrientation( 90 );
                // get Camera parameters
                Camera.Parameters params = camera.getParameters();
                // picture image orientation
                //params.setRotation( 90 );
                camera.startPreview();

            } catch (RuntimeException ex) {
                Toast.makeText( ctx, "camera_not_found " + ex.getMessage().toString(),
                        Toast.LENGTH_LONG ).show();
                Log.d( TAG, "camera_not_found " + ex.getMessage().toString() );
            }
        }

        preview.setCamera( camera );
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

        ((FrameLayout) findViewById( R.id.layout )).removeView( preview );
        preview = null;

    }

    private void resetCam() {
        startCamera();
        cameraShtBtn.setEnabled(true);
        Intent i = new Intent( MainActivity.this,resultActivity.class );
        startActivity( i );
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

    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d( TAG, "onPictureTaken - raw" );
        }
    };


    //참고 : http://stackoverflow.com/q/37135675
    PictureCallback bitmapCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //이미지의 너비와 높이 결정
            int w = camera.getParameters().getPictureSize().width;
            int h = camera.getParameters().getPictureSize().height;

            //byte array를 bitmap으로 변환
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitimg = BitmapFactory.decodeByteArray( data, 0, data.length, options );

            //이미지를 디바이스 방향으로 회전
            Matrix matrix = new Matrix();
            matrix.postRotate( checkDeviceOrientation( orientation ) );
            bitimg = Bitmap.createBitmap( bitimg, 0, 0, w, h, matrix, true );

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


}