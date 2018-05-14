package ce.inu.ikta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.api.client.util.StringUtils;

import java.io.File;
import java.net.URI;
import java.security.Policy;

public class MainActivity extends AppCompatActivity {

    //변수 및 객체 선언
    private static final String TAG = "MainActivity";
    Context ctx;
    Activity activity;
    ImageButton cameraShtBtn;
    OrientationEventListener orientEventListener;
    public DataBasesOpen dataBasesOpen;
    public AppCompatActivity mActivity;
    RequestPerm requestPerm;
    CameraForOCR cameraForOCR;
    MyView myView;
    LinearLayout linearLayout;
    SurfaceView surfaceView;
    float[] size;
    final int REQ_CODE_SELECT_IMAGE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toast.makeText( getApplicationContext(), "카메라 버튼을 길게 누르면 초점이 잡힙니다", Toast.LENGTH_LONG ).show();
        setValue();
        setListener();
        requestPerm = new RequestPerm( ctx, activity );
        requestPerm.setPermissions();
        dataBasesOpen.DB();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        linearLayout = (LinearLayout) this.findViewById( R.id.cameraTopView );
        surfaceView = (SurfaceView) this.findViewById( R.id.cameraView );
        size = new float[4];
        size[0] = linearLayout.getWidth();
        size[1] = linearLayout.getHeight();
        size[2] = surfaceView.getWidth();
        size[3] = surfaceView.getHeight();
        myView = MyView.create( ctx, size );
        linearLayout.removeAllViews();
        linearLayout.addView( myView, new RelativeLayout.LayoutParams
                ( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT )
        );
    }

    //메뉴 버튼 그림
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_actionbar, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.GoSaveList) {
            Intent intent = new Intent( ctx, SaveActivity.class );
            startActivity( intent );
        } else if (menuItem.getItemId() == R.id.GoAlbum) {
            Intent intent = new Intent( Intent.ACTION_PICK );
            intent.setType( MediaStore.Images.Media.CONTENT_TYPE );
            intent.setData( MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
            startActivityForResult( intent, REQ_CODE_SELECT_IMAGE );
        }
        return super.onOptionsItemSelected( menuItem );
    }

    @Override   //갤러리 리스트에서 사진 데이터 가져오기
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                Intent intent = new Intent( ctx, AlbumActivity.class );
                intent.putExtra( "uri", uri );
                Log.d( TAG, "여기는 메인의 uri " + uri );
                ctx.startActivity( intent );
            }
        }
    }

    //각종 value 설정
    void setValue() {
        mActivity = this;
        ctx = this;
        linearLayout = (LinearLayout) findViewById( R.id.cameraTopView );
        cameraShtBtn = (ImageButton) findViewById( R.id.cameraShutterBtn );  //카메라 버튼 id 매칭
        requestPerm = new RequestPerm( this, this );
        cameraForOCR = new CameraForOCR( ctx, mActivity,1 );
        dataBasesOpen = new DataBasesOpen( MainActivity.this );
    }

    //각종 리스너 선언
    void setListener() {
        // 회전 리스너
        orientEventListener = new OrientationEventListener( ctx, SensorManager.SENSOR_DELAY_NORMAL ) {
            @Override
            public void onOrientationChanged(int orientation) {
                cameraForOCR.setOrientation( orientation );
            }
        };
        if (orientEventListener.canDetectOrientation()) {
            orientEventListener.enable();   //방향 감지 가능 > 활성화
        } else {
            finish();
        }

        //카메라 버튼 클릭 리스너
        cameraShtBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.cameraShutterBtn) {
                    cameraShtBtn.setEnabled( false ); //버튼이 눌렸을 때 버튼을 비활성화
                    cameraForOCR.takePicture();
                } else {
                }
            }
        } );

        //카메라 버튼 롱클릭 리스너(초점 잡아줌)
        cameraShtBtn.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                cameraForOCR.camera.autoFocus(cameraForOCR.myAutoFocusCallback);
                return true;
            }
        } );
    }

    //권한 확인
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
        requestPerm.monRequestPermissionsResult( requestCode, permissions, grandResults );
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraForOCR.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraForOCR.pause();
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
        mediaScanIntent.setData( Uri.fromFile( file ) );
        sendBroadcast( mediaScanIntent );
    }


}
