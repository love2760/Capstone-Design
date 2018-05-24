package ce.inu.ikta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;

import static ce.inu.ikta.globalValue.bitimg;
import static ce.inu.ikta.globalValue.postImg;

/**
 * Created by NyuNyu on 2018-05-15.
 */

public class CutImg {

    Context context;
    Activity activity;
    ImageButton imageButton;
    float width, height;
    float imgx,imgy;
    private static String OCRResultStirng;

    public CutImg(Context context, Activity activity, ImageButton imageButton, float[] imgviewSize) {
        this.context = context;
        this.activity = activity;
        this.imageButton = imageButton;
        imgx = imgviewSize[0];imgy = imgviewSize[1];
        this.width = imgviewSize[2];
        this.height = imgviewSize[3];
    }

    public void cutImage( float topy,float bottomy,float leftx, float rightx) {
        int[] imgData = sizeXY( topy, bottomy, leftx, rightx );
        //bitimg = Bitmap.createBitmap( bitimg, imgData[0], imgData[1], imgData[2], imgData[3], null, true);
        //bitimg = Bitmap.createBitmap( bitimg, 0, 0, bitimg.getWidth(), bitimg.getHeight(), null, true );
        Log.d( getClass().getName(),imgData[0] +" "+imgData[1] +" "+imgData[2] +" "+imgData[3] +" bit"+bitimg.getWidth() +" "+bitimg.getHeight()+" " );
        postImg = Bitmap.createBitmap( bitimg, imgData[0], imgData[1], imgData[2], imgData[3], null, true );
        showCheckForEquationAlertdialog();
    }


    private int[] sizeXY(float topy,float bottomy,float leftx, float rightx) {
        float viewWidth = width;
        float viewHeight = height;
        Log.d( getClass().getName(),"img x \timg y\n"+imgx+"\t"+imgy+"\n"+topy+ " "+bottomy+ " "+leftx+ " "+rightx+ " "+width+ " "+height+ " " );
        float leftXratio = (leftx- imgx) / viewWidth;
        float rightXratio = (rightx -imgx )/ viewWidth;
        float topYratio = (topy-imgy) / viewHeight;
        float bottomYratio = (bottomy - imgy )/ viewHeight;
        Log.d (getClass().getName(), leftXratio+" "+rightXratio+" "+topYratio+" "+bottomYratio+" ");
        float bitimgWidth = bitimg.getWidth();
        float bitimgHeight = bitimg.getHeight();

        // 좌 우를 거꾸로 매칭하는 이유는 레이아웃의 x는 왼쪽부터지만 이미지의 x는 오른쪽 부터이기 때문이다.
        float imgLeftX = bitimgWidth * rightXratio;
        float imgRightX = bitimgWidth * leftXratio;
        float imgTopY = bitimgHeight * topYratio;
        float imgBottomY = bitimgHeight * bottomYratio;

        int imgX = (int) imgRightX;
        int imgY = (int) imgTopY;
        int imgWidth = (int) Math.abs( imgLeftX - imgRightX );
        int imgHeight = (int) Math.abs( imgBottomY - imgTopY );

        int[] imgData = {imgX, imgY, imgWidth, imgHeight};

        return imgData;
    }

    private ProgressDialog progDialog;

    private void showCheckForEquationAlertdialog() {
        AsyncTask progTask = new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                progDialog.dismiss();
                // 식이 정상적인지 확인하는 dialog
                AlertDialog.Builder alert = new AlertDialog.Builder( context );
                String onlyPrint;
                if(OCRResultStirng == null) {
                    onlyPrint = "문자 인식에 실패하였습니다.";
                } else {
                    onlyPrint =OCRResultStirng;
                }
                alert.setTitle( "다음의 식이 맞습니까?" ).setMessage( onlyPrint ).setCancelable( false ).setPositiveButton( "확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(OCRResultStirng != null) {
                                    Intent i = new Intent( context, resultActivity.class );
                                    i.putExtra( "ocrString", OCRResultStirng );
                                    imageButton.setEnabled( true );
                                    context.startActivity( i );
                                } else {
                                    imageButton.setEnabled( true );
                                }
                            }
                        } ).setNegativeButton( "취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                imageButton.setEnabled( true );
                                return;
                            }
                        } );
                alert.show();
                super.onPostExecute( o );
            }

            @Override
            protected void onPreExecute() {
                progDialog = new ProgressDialog( context );
                progDialog.setProgressStyle( ProgressDialog.STYLE_SPINNER );
                progDialog.setMessage( "사진을 구름에 담아 보내는 중..." );
                progDialog.setCancelable( false );
                progDialog.setCanceledOnTouchOutside( false );
                progDialog.show();
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                OCRResultStirng = "Failed";
                CloudVisionAPI cloudVisionAPI = CloudVisionAPI.Initializer();
                OCRResultStirng = cloudVisionAPI.request( postImg );
                return null;
            }
        };
        progTask.execute();

    }

}
