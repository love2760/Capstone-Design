package ce.inu.ikta;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.LinearLayout;

import static ce.inu.ikta.globalValue.bitimg;
import static okhttp3.internal.Internal.instance;

/**
 * Created by NyuNyu on 2018-04-16.
 */

public class MyView extends View {
    private static final String TAG = "MyView.java";
    float leftx, rightx, topy, bottomy;
    static int dep = 70; // 150 했더니 터치 범위가 말도안되게 변해서 고침.

    Paint paint;
    float Lwidth, Lheight, Swidth, Sheight;

    static MyView myView = null;

    public static MyView create(Context context, float[] size) {
        if(myView == null) {
            myView = new MyView(context,size);
        }
        return myView;
    }

    public static MyView getMyView() {
        return myView;
    }

    private MyView(Context context, float[] size) {
        super(context);
        Lwidth = size[0];
        Lheight = size[1];
        Swidth = size[2];
        Sheight =size[3];
        leftx = Lwidth / 10;
        rightx = Lwidth * 9 / 10;
        topy = Lheight*4.5f / 10;
        bottomy = Lheight * 5.5f/ 10;
        setOnTouchListener(onTouchListener);
    }

    protected void onDraw(Canvas canvas) {

        paint = new Paint();
        paint.setColor( Color.WHITE );
        paint.setStrokeWidth( 5 );


        //사각형 선 그리기
        canvas.drawLine( leftx, topy, rightx, topy, paint );
        canvas.drawLine( rightx, topy, rightx, bottomy, paint );
        canvas.drawLine( leftx, topy, leftx, bottomy, paint );
        canvas.drawLine( leftx, bottomy, rightx, bottomy, paint );

        super.onDraw( canvas );
    }

    //이벤트 처리, 현재의 그리기 모드에 따른 점의 위치 조정
    float oldx, oldy;
    boolean bleftx, btopy, brightx, bbottomy;

    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Log.d(TAG,"여기는 온터치");
            if (view.getId() == getId()) {

                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                Log.d(TAG,"x값 " +x+" y 값 "+y+" df ");

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    oldx = x;
                    oldy = y;

                    //눌린 곳이 선 근처인지 확인
                    //왼쪽 선 체크
                    if ((x > leftx - dep) && (x < leftx + dep) ) {
                        if((y > topy) &&( y < bottomy)) {
                            bleftx = true;
                            Log.d(TAG,"left");
                        }
                    }//오른쪽선 체크
                    else if ((x > rightx - dep) && (x < rightx + dep)) {
                        if((y > topy) &&( y < bottomy)) {
                            brightx = true;
                            Log.d(TAG,"right");
                        }
                    }

                    //위에 선 체크
                    if ((y > topy - dep) && (y < topy + dep)) {
                        if((x > leftx) && (x < rightx)) {
                            btopy = true;
                            Log.d(TAG,"bottom");
                        }
                    } // 아래 선 체크
                    else if ((y > bottomy - dep) && (y < bottomy + dep)) {
                        if((x > leftx) && (x < rightx)) {
                            bbottomy = true;
                            Log.d(TAG,"top");
                        }
                    }

                    return true;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    // 사각형 움직임 처리
                    if (bleftx) { leftx = x;  rightx = Lwidth - x; } // 왼쪽 변 선택시 이동 처리
                    if (brightx) { rightx = x;    leftx = Lwidth - x; } // 우측 변 선택시 이동 처리
                    if (btopy) { topy = y;    bottomy = Lheight - y; } // 위쪽 변 선택시 이동 처리
                    if (bbottomy) { bottomy = y;  topy = Lheight - y; } // 아래쪽 변 선택시 이동 처리

                    //사각형의 최소크기 지정
                    if (leftx >= Lwidth*4/10)       leftx = Lwidth*4/10; // 왼쪽 변 최소 크기
                    if (rightx <= Lwidth*6/10)      rightx = Lwidth*6/10; // 우측 변 최소 크기
                    if (topy >= Lheight*4.5 / 10)   topy = Lheight*4.5f / 10; // 위쪽 변 최소 크기
                    if (bottomy <= Lheight*5.5/10)  bottomy = Lheight*5.5f / 10; // 아래쪽 변 최소 크기

                    //화면 밖으로 나가지 않게 처리
                    if (leftx <= 0) leftx = 0;
                    if (rightx >= Lwidth) rightx = Lwidth;
                    if (topy <= 0) topy = 0;
                    if (bottomy >= Lheight) bottomy = Lheight;
                    invalidate(); // 움직일 때 다시 그림

                    return true;
                }

                if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                    bleftx = brightx = btopy = bbottomy = false;
                    return true;
                }
                return false;
            }
            return false;
        }
    };

}
