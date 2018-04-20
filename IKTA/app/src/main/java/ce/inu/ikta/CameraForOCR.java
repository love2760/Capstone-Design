package ce.inu.ikta;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import static ce.inu.ikta.globalValue.bitimg;

/**
 * Created by 김광현 on 2018-04-18.
 */

public class CameraForOCR {
    Preview preview;
    Context ctx;
    Activity mactivity;
    Camera camera;
    TessOCR mtessOCR;
    int orientation;
    private final static int CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_BACK;
    final String TAG = "CameraForOCR.java";
    public CameraForOCR(Context ctx, Activity mactivity) {
        this.preview = null;
        this.ctx = ctx;
        this.mactivity = mactivity;
        mtessOCR = new TessOCR(ctx);
    }

    public void start() {
        //프리뷰 초기화
        if (preview == null) {
            preview = new Preview( ctx, (SurfaceView) mactivity.findViewById( R.id.cameraView) );
            preview.setLayoutParams( new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT ) );  //프리뷰 크기 정함
            ((RelativeLayout) mactivity.findViewById( R.id.layout )).addView( preview );
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
                Log.d( TAG, "camera_not_found " + ex.getMessage().toString() );
            }
        }

        preview.setCamera( camera );    //프리뷰의 카메라 설정
    }
    public void pause() {
        if (camera != null) {
            // Call stopPreview() to stop updating the preview surface
            camera.stopPreview();
            preview.setCamera( null );
            camera.release();
            camera = null;
        }

        ((RelativeLayout) mactivity.findViewById( R.id.layout )).removeView( preview );
        preview = null;
    }
    public void reset() {
        start();
        showCheckForEquationAlertdialog();
    }
    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.d( TAG, "onShutter'd" );
        }
    };
    Camera.PictureCallback bitmapCallback = new Camera.PictureCallback() {
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

            bitimg = Bitmap.createBitmap( bitimg, 0, 0, w, h, matrix, true );

            Log.d( TAG,"bitmapCallback close" );
            reset();
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
    public void takePicture( ) {
        camera.autoFocus(myAutoFocusCallback);
        camera.takePicture(shutterCallback, null, bitmapCallback);
    }
    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {/*nothing*/}
    };


    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    //인식한 결과가 맞는지 확인하기 위해 dialog 창을 띄움
    private void showCheckForEquationAlertdialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        imgprocessor a = new imgprocessor();

        alert.setTitle("다음의 식이 맞습니까?").setMessage(mtessOCR.requestOCR( bitimg )).setCancelable( false ).setPositiveButton( "확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent( ctx,resultActivity.class );
                        ((ImageButton)mactivity.findViewById(R.id.cameraShutterBtn)).setEnabled(true);
                        ctx.startActivity( i );
                    }
                } ).setNegativeButton( "취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((ImageButton)mactivity.findViewById(R.id.cameraShutterBtn)).setEnabled(true);
                        return;
                    }
                } );
        alert.show();
    }

}
