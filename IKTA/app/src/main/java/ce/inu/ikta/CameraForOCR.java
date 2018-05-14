package ce.inu.ikta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

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
    int orientation, i;
    private static String OCRResultStirng;


    private final static int CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_BACK;
    final String TAG = "CameraForOCR.java";

    public CameraForOCR(Context ctx, Activity mactivity, int i) {
        this.preview = null;
        this.ctx = ctx;
        this.mactivity = mactivity;
        mtessOCR = new TessOCR(ctx);
        if(i==1) this.i = 1;
        else if(i==0) this.i = 0;
    }

    public void start() {
        //프리뷰 초기화
        if (preview == null) {
            preview = new Preview(ctx, (SurfaceView) mactivity.findViewById(R.id.cameraView));
            preview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));  //프리뷰 크기 정함
            ((RelativeLayout) mactivity.findViewById(R.id.layout)).addView(preview);
            preview.setKeepScreenOn(true);    //화면을 켠 상태로 유지
        }

        //카메라 초기화
        preview.setCamera(null);
        if (camera != null) {
            camera.release();   //카메라 객체 비우기
            camera = null;      //null로 초기화
        }

        //카메라 개수 받아옴
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {

                camera = Camera.open(CAMERA_FACING);  //후면 카메라 불러옴
                camera.setDisplayOrientation(90); //방향 고정
                List<Camera.Size> tmp = camera.getParameters().getSupportedPictureSizes();
                Camera.Size Max = tmp.get(0);
                for (int i = 0; i < tmp.size(); i++) {
                    if (tmp.get(i).width * tmp.get(i).height > Max.width * Max.height)
                        Max = tmp.get(i);
                }
                //camera.getParameters().setPictureSize( Max.width,Max.height );
                camera.startPreview();  //프리뷰 시작

            } catch (RuntimeException ex) {
                Log.d(TAG, "camera_not_found " + ex.getMessage().toString());
            }
        }

        preview.setCamera(camera);    //프리뷰의 카메라 설정
    }

    public void pause() {
        if (camera != null) {
            // Call stopPreview() to stop updating the preview surface
            camera.stopPreview();
            preview.setCamera(null);
            camera.release();
            camera = null;
        }

        ((RelativeLayout) mactivity.findViewById(R.id.layout)).removeView(preview);
        preview = null;
    }

    public void reset() {
        start();
        showCheckForEquationAlertdialog();
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.d(TAG, "onShutter'd");
        }
    };
    Camera.PictureCallback bitmapCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //byte array를 bitmap으로 변환
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;    //디코딩 방식을 ARGB_8888로 설정
            options.inSampleSize = 4;
            bitimg = BitmapFactory.decodeByteArray(data, 0, data.length, options);    //디코딩

            cutImg();

            Log.d(TAG, "bitmapCallback close");
            reset();
        }
    };

    public void cutImg() {

        //이미지를 디바이스 방향으로 회전
        Matrix matrix = new Matrix();
        if(i == 1) {
            matrix.postRotate( checkDeviceOrientation( orientation ) );
        }
        else matrix = null;

        int[] imgData = sizeXY();
        bitimg = Bitmap.createBitmap(bitimg, imgData[0], imgData[1], imgData[2], imgData[3], null, true);
        bitimg = Bitmap.createBitmap(bitimg, 0, 0, bitimg.getWidth(), bitimg.getHeight(), matrix, true);

    }

    private int[] sizeXY() {
        Log.d(TAG,"i값은? "+i);

        float surfaceWidth = 0, surfaceHeight = 0,
                leftXratio = 0, rightXratio = 0, topYratio = 0, bottomYratio = 0;

        if(i == 1) {
            surfaceWidth = MyView.getMyView().SurfaceWidth;
            surfaceHeight = MyView.getMyView().SurfaceHeight;
            leftXratio = MyView.getMyView().topY / surfaceHeight;
            rightXratio = MyView.getMyView().bottomY / surfaceHeight;
            topYratio = MyView.getMyView().leftX / surfaceWidth;
            bottomYratio = MyView.getMyView().rightX / surfaceWidth;
        } else if(i == 0){
            surfaceWidth = AlbumDrawView.getAlbumDrawView().ViewWidth;
            surfaceHeight =AlbumDrawView.getAlbumDrawView().ViewHeight;
            leftXratio = AlbumDrawView.getAlbumDrawView().topY / surfaceHeight;
            rightXratio = AlbumDrawView.getAlbumDrawView().bottomY / surfaceHeight;
            topYratio = AlbumDrawView.getAlbumDrawView().leftX / surfaceWidth;
            bottomYratio = AlbumDrawView.getAlbumDrawView().rightX / surfaceWidth;
        }

        float bitimgWidth = bitimg.getWidth();
        float bitimgHeight = bitimg.getHeight();

        // 좌 우를 거꾸로 매칭하는 이유는 레이아웃의 x는 왼쪽부터지만 이미지의 x는 오른쪽 부터이기 때문이다.
        float imgLeftX = bitimgWidth * rightXratio;
        float imgRightX = bitimgWidth * leftXratio;
        float imgTopY = bitimgHeight * topYratio;
        float imgBottomY = bitimgHeight * bottomYratio;

        int imgX = (int) imgRightX;
        int imgY = (int) imgTopY;
        int imgWidth = (int) Math.abs(imgLeftX - imgRightX);
        int imgHeight = (int) Math.abs(imgBottomY - imgTopY);
        int[] imgData = {imgX, imgY, imgWidth, imgHeight};
        return imgData;
    }

    private int checkDeviceOrientation(int orientation) {
        if (orientation >= 315 || orientation < 45) {
            return 90;
        } else if (orientation >= 45 && orientation < 135) {
            return 180;
        } else if (orientation >= 135 && orientation < 225) {
            return 270;
        } else if (orientation >= 225 && orientation < 315) {
            return 0;
        } else {
            Log.d(TAG, "방향확인 실패");
            return 0;
        }
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void takePicture() {
        camera.autoFocus( myAutoFocusCallback );
        camera.takePicture(shutterCallback, null, bitmapCallback);
    }

    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {/*nothing*/}
    };

    //인식한 결과가 맞는지 확인하기 위해 dialog 창을 띄움
    private ProgressDialog progDialog;
    private void showCheckForEquationAlertdialog() {
        AsyncTask progTask = new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                progDialog.dismiss();
                // 식이 정상적인지 확인하는 dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                alert.setTitle("다음의 식이 맞습니까?").setMessage(OCRResultStirng).setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(ctx, resultActivity.class);
                                i.putExtra("ocrString", OCRResultStirng);
                                ((ImageButton) mactivity.findViewById(R.id.cameraShutterBtn)).setEnabled(true);
                                ctx.startActivity(i);
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((ImageButton) mactivity.findViewById(R.id.cameraShutterBtn)).setEnabled(true);
                                return;
                            }
                        });
                alert.show();
                super.onPostExecute( o );
            }

            @Override
            protected void onPreExecute() {
                progDialog = new ProgressDialog(ctx);
                progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDialog.setMessage("사진을 구름에 담아 보내는 중...");
                progDialog.setCancelable(false);
                progDialog.setCanceledOnTouchOutside(false);
                progDialog.show();
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                OCRResultStirng = "Failed";
                CloudVisionAPI cloudVisionAPI = CloudVisionAPI.Initializer();
                OCRResultStirng = cloudVisionAPI.request(bitimg);
                return null;
            }
        };
        progTask.execute(  );

    }

}
