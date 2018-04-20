package ce.inu.ikta;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by NyuNyu on 2018-04-16.
 */

public class MyView extends View {
    private static final String TAG = "CUT_IMAGE";
    float sx, ex, sy, ey;
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
    }

    protected void onDraw(Canvas canvas) {

        sx = width / 5;
        ex = width * 4 / 5;
        sy = height / 5;
        ey = height * 4 / 5;

        paint = new Paint();
        paint.setColor( Color.WHITE );
        paint.setStrokeWidth( 5 );

        //canvas.drawLine( 20,50,100,150, paint );


        //사각형 선 그리기
        canvas.drawLine( sx, sy, ex, sy, paint );
        canvas.drawLine( ex, sy, ex, ey, paint );
        canvas.drawLine( sx, sy, sx, ey, paint );
        canvas.drawLine( sx, ey, ex, ey, paint );

        super.onDraw( canvas );
    }

    //이벤트 처리, 현재의 그리기 모드에 따른 점의 위치 조정
    float dx = 0, dy = 0;
    float oldx, oldy;
    boolean bsx, bsy, bex, bey;
    boolean boo = false;

    public void FixImage(Bitmap bitmap) {
        //비트맵 크기 조절(메모리 문제로 1/2 크기로)
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        Log.d( TAG, "width" + width + "heightㅇㅁㄹㅇㄹ" + height );
        bitimg = Bitmap.createScaledBitmap( bitmap, (int) width, (int) height, false );
        Log.e( TAG, "" + bitimg.getHeight() * bitimg.getWidth() );
    }
    /*
    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Log.d(TAG,"여기는 온터치");
            if (view.getId() == myView.getId()) {

                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                Log.d(TAG,"x값 " +x+" y 값 "+y+" df ");

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    oldx = x;
                    oldy = y;


                    //눌린 곳이 선 근처인지 확인
                    if ((x > sx - dep) && (x < sx + dep)) bsx = true;
                    else if ((x > ex - dep) && (x < ex + dep)) bex = true;

                    if ((y > sy - dep) && (y < sy + dep)) bsy = true;
                    else if ((y > ey - dep) && (y < sy + dep)) bey = true;

                    //하나라도 선택시 boo값 변경
                    if (bsx || bex || bsy || bey) boo = false;
                    else if (((x > sx + dep) && (x < ex - dep)) && ((y > sy + dep) && (y < ey - dep)))
                        boo = true;

                    Log.d(TAG,"들어오나1 "+ motionEvent.getAction()+" x값 "+oldx+" y값 "+oldy);

                    return true;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    if (bsx) sx = x;
                    if (bex) ex = x;
                    if (bsy) sy = y;
                    if (bey) ey = y;

                    //사각형의 시작 라인보다 끝라인이 크지않게 처리
                    if (ex <= sx + dep) {
                        ex = sx + dep;
                        return true;
                    }
                    if (ey <= sy + dep) {
                        ey = sy + dep;
                        return true;
                    }

                    //움직인 거리 구해서 적용
                    if (boo) {
                        dx = oldx - x;
                        dy = oldy - y;

                        sx -= dx;
                        ex -= dx;
                        sy -= dy;
                        ey -= dy;

                        //화면 밖으로 나가지 않게 처리
                        if (sx <= 0) sx = 0;
                        if (ex >= width) ex = width - 1;

                        if (sy <= 0) sy = 0;
                        if (ey >= height) ey = height - 1;
                    }

                    invalidate(); // 움직일 때 다시 그림
                    oldx=x;
                    oldy=y;
                    Log.d(TAG,"들어오나2 "+ motionEvent.getAction()+"x값 "+oldx+" y값 "+oldy);

                    return true;
                }

                //action_up 이면 그리기 종료
                if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                    bsx = bex = bsy = bey = boo = false;

                    Log.d(TAG,"들어오나3 "+ motionEvent.getAction());
                    return true;
                }
                return false;
            }
            return false;
        }
    };*/


    /*
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        Log.d(TAG,"x값 " +x+" y 값 "+y+" df ");

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            oldx = x;
            oldy = y;


            //눌린 곳이 선 근처인지 확인
            if ((x > sx - dep) && (x < sx + dep)) bsx = true;
            else if ((x > ex - dep) && (x < ex + dep)) bex = true;

            if ((y > sy - dep) && (y < sy + dep)) bsy = true;
            else if ((y > ey - dep) && (y < sy + dep)) bey = true;

            //하나라도 선택시 boo값 변경
            if (bsx || bex || bsy || bey) boo = false;
            else if (((x > sx + dep) && (x < ex - dep)) && ((y > sy + dep) && (y < ey - dep)))
                boo = true;

            Log.d(TAG,"들어오나1 "+ motionEvent.getAction()+" x값 "+oldx+" y값 "+oldy);

            return false;
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            if (bsx) sx = x;
            if (bex) ex = x;
            if (bsy) sy = y;
            if (bey) ey = y;

            //사각형의 시작 라인보다 끝라인이 크지않게 처리
            if (ex <= sx + dep) {
                ex = sx + dep;
                return true;
            }
            if (ey <= sy + dep) {
                ey = sy + dep;
                return true;
            }

            //움직인 거리 구해서 적용
            if (boo) {
                dx = oldx - x;
                dy = oldy - y;

                sx -= dx;
                ex -= dx;
                sy -= dy;
                ey -= dy;

                //화면 밖으로 나가지 않게 처리
                if (sx <= 0) sx = 0;
                if (ex >= width) ex = width - 1;

                if (sy <= 0) sy = 0;
                if (ey >= height) ey = height - 1;
            }

            invalidate(); // 움직일 때 다시 그림
            oldx=x;
            oldy=y;
            Log.d(TAG,"들어오나2 "+ motionEvent.getAction()+"x값 "+oldx+" y값 "+oldy);

            return true;
        }

        //action_up 이면 그리기 종료
        if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
            bsx = bex = bsy = bey = boo = false;

            Log.d(TAG,"들어오나3 "+ motionEvent.getAction());
            return true;
        }
        return false;
    }*/

    //선택된 사각형 이미지 저장
    public Bitmap save(Bitmap bitimg) {
        Bitmap bitmap = Bitmap.createBitmap( bitimg, (int)sx,(int)sy, (int)(ex-sx),(int)(ey-sy) );
        return bitmap;
    }

    /*
    public View activity(Activity activity) {
        return activity.findViewById( R.id.cameraView );
    }*/



}
