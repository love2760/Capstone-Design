package ce.inu.ikta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;

import static ce.inu.ikta.globalValue.bitimg;

/**
 * Created by NyuNyu on 2018-05-15.
 */

public class CutImg {

    Context context;
    Activity activity;
    ImageButton imageButton;
    private static String OCRResultStirng;

    public CutImg(Context context, Activity activity, ImageButton imageButton) {
        this.context = context;
        this.activity = activity;
        this.imageButton = imageButton;
    }

    public void cutImage() {
        int[] imgData = sizeXY();

        //bitimg = Bitmap.createBitmap( bitimg, imgData[0], imgData[1], imgData[2], imgData[3], null, true);
        //bitimg = Bitmap.createBitmap( bitimg, 0, 0, bitimg.getWidth(), bitimg.getHeight(), null, true );

        bitimg = Bitmap.createBitmap( bitimg, imgData[0], imgData[1], imgData[2], imgData[3], null, true );
        showCheckForEquationAlertdialog();
    }

    /*
    private int[] sizeXY() {
        float viewWidth = AlbumDrawView.getAlbumDrawView().ViewWidth;
        float viewHeight = AlbumDrawView.getAlbumDrawView().ViewHeight;

        float leftXratio = AlbumDrawView.getAlbumDrawView().topY / viewHeight;
        float rightXratio = AlbumDrawView.getAlbumDrawView().bottomY / viewHeight;
        float topYratio = AlbumDrawView.getAlbumDrawView().leftX / viewWidth;
        float bottomYratio = AlbumDrawView.getAlbumDrawView().rightX / viewWidth;

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
    }*/


    private int[] sizeXY() {
        //이미지 뷰의 너비와 높이
        float viewWidth = AlbumDrawView.getAlbumDrawView().ViewWidth;
        float viewHeight = AlbumDrawView.getAlbumDrawView().ViewHeight;

        //이미지의 너비와 높이
        float imgWidth = bitimg.getWidth();
        float imgHeight = bitimg.getHeight();

        //상자
        float top = AlbumDrawView.getAlbumDrawView().topY;
        float bottom = AlbumDrawView.getAlbumDrawView().bottomY;
        float left = AlbumDrawView.getAlbumDrawView().leftX;
        float right = AlbumDrawView.getAlbumDrawView().rightX;

        //시작점
        int startX = (int)(((viewWidth-right)*imgWidth)/viewWidth);
        int startY = (int)((top*imgHeight)/viewHeight);

        //상자의 너비와 높이
        int boxWidth = (int)((Math.abs(right-left)*imgWidth)/viewWidth);
        int boxHeight = (int)((Math.abs(bottom-top)*imgHeight)/viewHeight);

        int[] imgData = {startX, startY, boxWidth, boxHeight};


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
                alert.setTitle( "다음의 식이 맞습니까?" ).setMessage( OCRResultStirng ).setCancelable( false ).setPositiveButton( "확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent( context, resultActivity.class );
                                i.putExtra( "ocrString", OCRResultStirng );
                                imageButton.setEnabled( true );
                                context.startActivity( i );
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
                OCRResultStirng = cloudVisionAPI.request( bitimg );
                return null;
            }
        };
        progTask.execute();

    }

}
