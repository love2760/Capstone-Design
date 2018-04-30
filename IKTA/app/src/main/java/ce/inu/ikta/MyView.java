package ce.inu.ikta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by NyuNyu on 2018-04-16.
 */

public class MyView extends View {
    private static final String TAG = "MyView.java";
    float leftx, rightx, topy, bottomy;
    static int dep = 30;

    MainActivity mainActivity;
    Bitmap bitimg;
    float width;
    float height;
    Paint paint;
    Context context;

    public MyView(Context context, float[] info) {
        super(context);
        width=info[0];
        height=info[1];
        leftx = width / 5;
        rightx = width * 4 / 5;
        topy = height / 5;
        bottomy = height * 4 / 5;
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
    float dx = 0, dy = 0;
    float oldx, oldy;
    boolean bleftx, bsy, bex, bey;
    boolean boo = false;

    public void FixImage(Bitmap bitmap) {
        //비트맵 크기 조절(메모리 문제로 1/2 크기로)
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        Log.d( TAG, "width" + width + "heightㅇㅁㄹㅇㄹ" + height );
        bitimg = Bitmap.createScaledBitmap( bitmap, (int) width, (int) height, false );
        Log.e( TAG, "" + bitimg.getHeight() * bitimg.getWidth() );
    }
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
                            bex = true;
                            Log.d(TAG,"right");
                        }
                    }

                    //위에 선 체크
                    if ((y > topy - dep) && (y < topy + dep)) {
                        if((x > leftx) && x < rightx) {
                            bsy = true;
                            Log.d(TAG,"bottom");
                        }
                    } // 아래 선 체크
                    else if ((y > bottomy - dep) && (y < bottomy + dep)) {
                        if((x > leftx) && x < rightx) {
                            bey = true;
                            Log.d(TAG,"top");
                        }
                    }


                    //하나라도 선택시 boo값 변경
                    if (bleftx || bex || bsy || bey) boo = false;


                    Log.d(TAG,"들어오나1 "+ motionEvent.getAction()+" x값 "+oldx+" y값 "+oldy);

                    return true;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    if (bleftx) leftx = x;
                    if (bex) rightx = x;
                    if (bsy) topy = y;
                    if (bey) bottomy = y;

                    //사각형의 시작 라인보다 끝라인이 크지않게 처리
                    if (rightx <= leftx + dep) {
                        rightx = leftx + dep;
                        return true;
                    }
                    if (bottomy <= topy + dep) {
                        bottomy = topy + dep;
                        return true;
                    }

                    //움직인 거리 구해서 적용
                    if (boo) {
                        dx = oldx - x;
                        dy = oldy - y;

                        leftx -= dx;
                        rightx -= dx;
                        topy -= dy;
                        bottomy -= dy;

                        //화면 밖으로 나가지 않게 처리
                        if (leftx <= 0) leftx = 0;
                        if (rightx >= width) rightx = width - 1;

                        if (topy <= 0) topy = 0;
                        if (bottomy >= height) bottomy = height - 1;
                    }

                    invalidate(); // 움직일 때 다시 그림
                    oldx=x;
                    oldy=y;
                    Log.d(TAG,"들어오나2 "+ motionEvent.getAction()+"x값 "+oldx+" y값 "+oldy);

                    return true;
                }

                //action_up 이면 그리기 종료
                if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                    bleftx = bex = bsy = bey = boo = false;
                    return true;
                }
                return false;
            }
            return false;
        }
    };


    /*
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        Log.d(TAG,"x값 " +x+" y 값 "+y+" df ");

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            oldx = x;
            oldy = y;


            //눌린 곳이 선 근처인지 확인
            if ((x > leftx - dep) && (x < leftx + dep)) bleftx = true;
            else if ((x > rightx - dep) && (x < rightx + dep)) bex = true;

            if ((y > topy - dep) && (y < topy + dep)) bsy = true;
            else if ((y > bottomy - dep) && (y < topy + dep)) bey = true;

            //하나라도 선택시 boo값 변경
            if (bleftx || bex || bsy || bey) boo = false;
            else if (((x > leftx + dep) && (x < rightx - dep)) && ((y > topy + dep) && (y < bottomy - dep)))
                boo = true;

            Log.d(TAG,"들어오나1 "+ motionEvent.getAction()+" x값 "+oldx+" y값 "+oldy);

            return false;
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            if (bleftx) leftx = x;
            if (bex) rightx = x;
            if (bsy) topy = y;
            if (bey) bottomy = y;

            //사각형의 시작 라인보다 끝라인이 크지않게 처리
            if (rightx <= leftx + dep) {
                rightx = leftx + dep;
                return true;
            }
            if (bottomy <= topy + dep) {
                bottomy = topy + dep;
                return true;
            }

            //움직인 거리 구해서 적용
            if (boo) {
                dx = oldx - x;
                dy = oldy - y;

                leftx -= dx;
                rightx -= dx;
                topy -= dy;
                bottomy -= dy;

                //화면 밖으로 나가지 않게 처리
                if (leftx <= 0) leftx = 0;
                if (rightx >= width) rightx = width - 1;

                if (topy <= 0) topy = 0;
                if (bottomy >= height) bottomy = height - 1;
            }

            invalidate(); // 움직일 때 다시 그림
            oldx=x;
            oldy=y;
            Log.d(TAG,"들어오나2 "+ motionEvent.getAction()+"x값 "+oldx+" y값 "+oldy);

            return true;
        }

        //action_up 이면 그리기 종료
        if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
            bleftx = bex = bsy = bey = boo = false;

            Log.d(TAG,"들어오나3 "+ motionEvent.getAction());
            return true;
        }
        return false;
    }*/

    //선택된 사각형 이미지 저장
    public Bitmap save(Bitmap bitimg) {
        Bitmap bitmap = Bitmap.createBitmap( bitimg, (int)leftx,(int)topy, (int)(rightx-leftx),(int)(bottomy-topy) );
        return bitmap;
    }

    /*
    public View activity(Activity activity) {
        return activity.findViewById( R.id.cameraView );
    }*/



}
