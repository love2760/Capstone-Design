package ce.inu.ikta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by NyuNyu on 2018-04-16.
 */

public class MyView extends View {
    private static final String TAG = "MyView.java";
    float leftX, rightX, topY, bottomY;
    static int dep = 70; // 150 했더니 터치 범위가 말도안되게 변해서 고침.

    Paint paint;
    float linearWidth, linearHeight, SurfaceWidth, SurfaceHeight;

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
        linearWidth = size[0];
        linearHeight = size[1];
        SurfaceWidth = size[2];
        SurfaceHeight =size[3];
        leftX = linearWidth / 10;
        rightX = linearWidth * 9 / 10;
        topY = linearHeight*4.5f / 10;
        bottomY = linearHeight * 5.5f/ 10;
        setOnTouchListener(onTouchListener);
    }

    protected void onDraw(Canvas canvas) {

        paint = new Paint();
        paint.setColor( Color.WHITE );
        paint.setStrokeWidth( 5 );

        //사각형 선 그리기
        canvas.drawLine( leftX, topY, rightX, topY, paint );
        canvas.drawLine( rightX, topY, rightX, bottomY, paint );
        canvas.drawLine( leftX, topY, leftX, bottomY, paint );
        canvas.drawLine( leftX, bottomY, rightX, bottomY, paint );

        super.onDraw( canvas );
    }

    //이벤트 처리, 현재의 그리기 모드에 따른 점의 위치 조정
    float oldX, oldY;
    boolean boolLeftX, boolTopY, boolRightX, boolBottomY;

    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view.getId() == getId()) {

                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    oldX = x;
                    oldY = y;

                    //눌린 곳이 선 근처인지 확인
                    //왼쪽 선 체크
                    if ((x > leftX - dep) && (x < leftX + dep) ) {
                        if((y > topY) &&( y < bottomY)) {
                            boolLeftX = true;
                        }
                    }//오른쪽선 체크
                    else if ((x > rightX - dep) && (x < rightX + dep)) {
                        if((y > topY) &&( y < bottomY)) {
                            boolRightX = true;
                        }
                    }

                    //위에 선 체크
                    if ((y > topY - dep) && (y < topY + dep)) {
                        if((x > leftX) && (x < rightX)) {
                            boolTopY = true;
                        }
                    } // 아래 선 체크
                    else if ((y > bottomY - dep) && (y < bottomY + dep)) {
                        if((x > leftX) && (x < rightX)) {
                            boolBottomY = true;
                        }
                    }

                    return true;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    // 사각형 움직임 처리
                    if (boolLeftX) { leftX = x;  rightX = linearWidth - x; } // 왼쪽 변 선택시 이동 처리
                    if (boolRightX) { rightX = x;    leftX = linearWidth - x; } // 우측 변 선택시 이동 처리
                    if (boolTopY) { topY = y;    bottomY = linearHeight - y; } // 위쪽 변 선택시 이동 처리
                    if (boolBottomY) { bottomY = y;  topY = linearHeight - y; } // 아래쪽 변 선택시 이동 처리

                    //사각형의 최소크기 지정
                    if (leftX >= linearWidth*4/10)       leftX = linearWidth*4/10; // 왼쪽 변 최소 크기
                    if (rightX <= linearWidth*6/10)      rightX = linearWidth*6/10; // 우측 변 최소 크기
                    if (topY >= linearHeight*4.5 / 10)   topY = linearHeight*4.5f / 10; // 위쪽 변 최소 크기
                    if (bottomY <= linearHeight*5.5/10)  bottomY = linearHeight*5.5f / 10; // 아래쪽 변 최소 크기

                    //화면 밖으로 나가지 않게 처리
                    if (leftX <= 0) leftX = 0;
                    if (rightX >= linearWidth) rightX = linearWidth;
                    if (topY <= 0) topY = 0;
                    if (bottomY >= linearHeight) bottomY = linearHeight;
                    invalidate(); // 움직일 때 다시 그림

                    return true;
                }

                if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                    boolLeftX = boolRightX = boolTopY = boolBottomY = false;
                    return true;
                }
                return false;
            }
            return false;
        }
    };
}
