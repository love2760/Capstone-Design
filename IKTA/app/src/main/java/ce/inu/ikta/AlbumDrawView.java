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

public class AlbumDrawView extends View {
    private static final String TAG = "AlbumDrawView.java";
    float leftX, rightX, topY, bottomY;
    static int dep = 70; // 150 했더니 터치 범위가 말도안되게 변해서 고침.

    Paint paint;
    float linearWidth, linearHeight, ViewWidth, ViewHeight;

    static AlbumDrawView albumDrawView = null;

    public static AlbumDrawView create(Context context, float[] size) {
        if(albumDrawView == null) {
            albumDrawView = new AlbumDrawView(context,size);
        }
        return albumDrawView;
    }

    public static AlbumDrawView getAlbumDrawView() {
        return albumDrawView;
    }

    private AlbumDrawView(Context context, float[] size) {
        super(context);
        linearWidth = size[0];
        linearHeight = size[1];
        ViewWidth = size[2];
        ViewHeight =size[3];
        leftX = linearWidth *0.1f;
        rightX = linearWidth * 0.9f;
        topY = linearHeight*0.45f;
        bottomY = linearHeight * 0.55f;
        setOnTouchListener(onTouchListener);
    }

    protected void onDraw(Canvas canvas) {

        paint = new Paint();
        paint.setColor( Color.BLACK );
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
    float drawX = 0, drawY = 0;
    boolean boolLeftX, boolTopY, boolRightX, boolBottomY;
    boolean bBoxMove = false;

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

                    //상자 움직이기
                    if(boolBottomY||boolLeftX||boolRightX||boolTopY) bBoxMove = false;
                    else
                        if( ((x > leftX-dep) && (x < rightX+dep))
                                && ((y > topY-dep) && (y < bottomY)) ) bBoxMove = true;

                    return true;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    // 사각형 변 움직임 처리
                    if (boolLeftX) leftX = x;  // 왼쪽 변 선택시 이동 처리
                    if (boolRightX) rightX = x;// 우측 변 선택시 이동 처리
                    if (boolTopY) topY = y; // 위쪽 변 선택시 이동 처리
                    if (boolBottomY) bottomY = y; // 아래쪽 변 선택시 이동 처리

                    //사각형의 최소크기 지정
                    if (rightX <= leftX+dep)      rightX = leftX+dep;
                    if (bottomY <= topY+dep)  bottomY = topY+dep;

                    //상자 움직인 거리 구해서 적용
                    if(bBoxMove) {
                        drawX = oldX - x*0.01f;
                        drawY = oldY - y*0.01f;

                        leftX -= drawX;
                        rightX -= drawX;
                        topY -= drawY;
                        bottomY -= drawY;

                        if(leftX <= 0) {leftX = 0;}
                        if(rightX >= ViewWidth) {rightX = ViewWidth;}
                        if(topY <= 0) {topY = 0;}
                        if(bottomY >= ViewHeight) {bottomY = ViewHeight;}

                        return true;
                    }

                    invalidate(); // 움직일 때 다시 그림

                    return true;
                }

                if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                    boolLeftX = boolRightX = boolTopY = boolBottomY = bBoxMove = false;
                    return true;
                }
                return false;
            }
            return false;
        }
    };
}
