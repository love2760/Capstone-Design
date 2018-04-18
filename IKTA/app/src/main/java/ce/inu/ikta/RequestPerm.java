package ce.inu.ikta;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by NyuNyu on 2018-04-17.
 */

public class RequestPerm {
    private final static int PERMISSIONS_REQUEST_CODE = 100;
    Context ctx;
    Activity activity;
    public RequestPerm(Context ctx, Activity activity) {
        this.ctx = ctx;
        this.activity = activity;
    }
    int getPermissionsRequestCode() {
        return PERMISSIONS_REQUEST_CODE;
    }
    void setPermissions() {
        if (ctx.getPackageManager().hasSystemFeature( PackageManager.FEATURE_CAMERA )) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //API 23 이상이면
                // 런타임 퍼미션 처리 필요

                //현재 권한 상태 넣음
                int hasCameraPermission = ContextCompat.checkSelfPermission( ctx, Manifest.permission.CAMERA );
                int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int hasReadExternalStoragePermission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE);

                if (hasCameraPermission == PackageManager.PERMISSION_GRANTED
                        && hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED
                        && hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
                    //이미 퍼미션을 가지고 있음
                } else {
                    //퍼미션 요청
                    ActivityCompat.requestPermissions( activity, new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE }, PERMISSIONS_REQUEST_CODE );
                }
            }


        } else {
            /*nothing*/
        }
    }



    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermissions() {
        //권한 상태 저장
        int hasCameraPermission = ContextCompat.checkSelfPermission( ctx,
                Manifest.permission.CAMERA );
        int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission( ctx,
                Manifest.permission.WRITE_EXTERNAL_STORAGE );
        int hasReadExternalStoragePermission = ContextCompat.checkSelfPermission( ctx,
                Manifest.permission.READ_EXTERNAL_STORAGE );

        //Manifest에 선언됐는지 확인
        boolean cameraRationale = ActivityCompat.shouldShowRequestPermissionRationale( activity,
                Manifest.permission.CAMERA );
        boolean writeExternalStorageRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean readExternalStorageRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((hasCameraPermission == PackageManager.PERMISSION_DENIED && cameraRationale)
                || (hasReadExternalStoragePermission== PackageManager.PERMISSION_DENIED
                && readExternalStorageRationale)
                || (hasWriteExternalStoragePermission== PackageManager.PERMISSION_DENIED
                && writeExternalStorageRationale))
            showDialogForPermission( "앱을 실행하려면 퍼미션을 허가하셔야합니다." );

        else if ((hasCameraPermission == PackageManager.PERMISSION_DENIED && !cameraRationale)
                || (hasReadExternalStoragePermission== PackageManager.PERMISSION_DENIED
                && !readExternalStorageRationale)
                || (hasWriteExternalStoragePermission== PackageManager.PERMISSION_DENIED
                && !writeExternalStorageRationale))
            showDialogForPermissionSetting( "퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다." );

        else if (hasCameraPermission == PackageManager.PERMISSION_GRANTED
                || hasWriteExternalStoragePermission== PackageManager.PERMISSION_GRANTED
                || hasReadExternalStoragePermission== PackageManager.PERMISSION_GRANTED ) {
            doRestart( activity );
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder( activity );
        builder.setTitle( "알림" );
        builder.setMessage( msg );
        builder.setCancelable( false );
        builder.setPositiveButton( "예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //퍼미션 요청
                ActivityCompat.requestPermissions( activity,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_CODE );
            }
        } );

        builder.setNegativeButton( "아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               activity.finish();
            }
        } );
        builder.create().show();
    }

    private void showDialogForPermissionSetting(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder( activity );
        builder.setTitle( "알림" );
        builder.setMessage( msg );
        builder.setCancelable( true );
        builder.setPositiveButton( "예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent myAppSettings = new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse( "package:" + activity.getPackageName() ) );
                myAppSettings.addCategory( Intent.CATEGORY_DEFAULT );
                myAppSettings.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                activity.startActivity( myAppSettings );
            }
        } );
        builder.setNegativeButton( "아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               activity.finish();
            }
        } );
        builder.create().show();
    }

    //앱 재시작
    public static void doRestart(Context c) {
        //http://stackoverflow.com/a/22345538
        try {
            //주어진 context 확인
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        //
                        mStartActivity.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        //create a pending intent so the application is restarted
                        // after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity( c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT );
                        AlarmManager mgr =
                                (AlarmManager) c.getSystemService( Context.ALARM_SERVICE );
                        mgr.set( AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent );
                        //kill the application
                        System.exit( 0 );
                    } else {
                        Log.e( "RequestPerm", "Was not able to restart application, " +
                                "mStartActivity null" );
                    }
                } else {
                    Log.e( "RequestPerm", "Was not able to restart application, PM null" );
                }
            } else {
                Log.e( "RequestPerm", "Was not able to restart application, Context null" );
            }
        } catch (Exception ex) {
            Log.e( "RequestPerm", "Was not able to restart application" );
        }
    }



}
