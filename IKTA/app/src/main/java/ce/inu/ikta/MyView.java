package ce.inu.ikta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by NyuNyu on 2018-04-16.
 */

public class MyView extends View {
    private static final String TAG = "MyView.java";
    float leftx, rightx, topy, bottomy;
    static int dep = 50;

    MainActivity mainActivity;
    Bitmap bitimg;
    Paint paint;
    float width, height;
    Context context;

    public MyView(Context context, float wid, float hei) {
        super(context);
        width = wid;
        height = hei;
        leftx = width / 10;
        rightx = width * 9 / 10;
        topy = height*(7/2) / 10;
        bottomy = height * (13/2) / 10;
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
    boolean bleftx, btopy, brightx, bbottomy;
    boolean boo = false;

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


                    //하나라도 선택시 boo값 변경
                    if (bleftx || brightx || btopy || bbottomy) boo = false;


                    Log.d(TAG,"들어오나1 "+ motionEvent.getAction()+" x값 "+oldx+" y값 "+oldy);

                    return true;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    if (bleftx) { leftx = x;  rightx = width - x; }
                    if (brightx) { rightx = x;    leftx = width - x; }
                    if (btopy) { topy = y;    bottomy = height - y; }
                    if (bbottomy) { bottomy = y;  topy = height - y; }

                    //사각형의 각 선분이 중앙을 넘지 않도록 처리
                    if (leftx >= width*4/10) {
                        leftx = width*4/10;
                    }

                    if (rightx <= width*6/10) {
                        rightx = width*6/10;
                    }

                    if (topy >= height*4/10) {
                        topy = height*4/10;
                    }

                    if (bottomy <= height*6/10) {
                        bottomy = height*6/10;
                    }

                    //화면 밖으로 나가지 않게 처리
                    if (leftx <= 0) leftx = 0;

                    if (rightx >= width) rightx = width;

                    if (topy <= 0) topy = 0;

                    if (bottomy >= height) bottomy = height;

                    invalidate(); // 움직일 때 다시 그림
                    oldx=x;
                    oldy=y;
                    Log.d(TAG,"들어오나2 "+ motionEvent.getAction()+"x값 "+oldx+" y값 "+oldy);

                    return true;
                }

                //action_up 이면 그리기 종료
                if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                    bleftx = brightx = btopy = bbottomy = boo = false;
                    return true;
                }
                return false;
            }
            return false;
        }
    };

    //선택된 사각형 이미지 저장
    public Bitmap imgsave(Bitmap bitimg) {
        Bitmap bitmap = Bitmap.createBitmap( bitimg, (int)leftx,(int)topy, (int)(rightx-leftx),(int)(bottomy-topy) );
        return bitmap;
    }

    /*
    public View activity(Activity activity) {
        return activity.findViewById( R.id.cameraView );
    }*/



}
