package ce.inu.ikta;

/**
 * Created by NyuNyu on 2018-03-22.
 */

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;


class Preview extends ViewGroup implements SurfaceHolder.Callback {
    private final String TAG = "Preview";

    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    Size mPreviewSize;
    Size mPictureSize;
    List<Size> mSupportedPreviewSizes;
    List<Size> mSupportedPictureSizes;
    Camera mCamera;

    Preview(Context context, SurfaceView sv ) {
        super(context);

        mSurfaceView = sv;
//        addView(mSurfaceView);

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    public void setCamera(Camera camera) {
        if (mCamera != null) {
            //카메라에 기존 값이 들어있으면 일단 프리뷰를 멈춤
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();
            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            mCamera.release();
            mCamera = null;
        }

        mCamera = camera;
        if (mCamera != null) {
            List<Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
            List<Size> localPictureSizes = mCamera.getParameters().getSupportedPictureSizes();
            mSupportedPreviewSizes = localSizes;
            mSupportedPictureSizes = localPictureSizes;
            requestLayout();

            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Important: Call startPreview() to start updating the preview
            // surface. Preview must be started before you can take a picture.
            mCamera.startPreview();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = r - l;
            final int height = b - t;

            int previewWidth = width;
            int previewHeight = height;
            if (mPreviewSize != null) {
                previewWidth = mPreviewSize.width;
                previewHeight = mPreviewSize.height;
            }

            // Center the child SurfaceView within the parent.
            if (width * previewHeight > height * previewWidth) {
                final int scaledChildWidth = previewWidth * height / previewHeight;
                child.layout((width - scaledChildWidth) / 2, 0,
                        (width + scaledChildWidth) / 2, height);
            } else {
                final int scaledChildHeight = previewHeight * width / previewWidth;
                child.layout(0, (height - scaledChildHeight) / 2,
                        width, (height + scaledChildHeight) / 2);
            }
        }

    }

    public void surfaceCreated(SurfaceHolder holder) {
        //Toast.makeText(getContext(), "surfaceCreated", Toast.LENGTH_LONG).show();
        Log.d( "@@@", "surfaceCreated" );

        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    //w,h 값을 받아서 가장 근접한 해상도와 비를 가지는 해상도를 list에서 선택해 가져옴
    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        setOptimalPictureSize(mCamera.getParameters());
        return optimalSize;
    }

    private void setOptimalPictureSize(Camera.Parameters parameters) {
        Size currentPreviewSize = parameters.getPreviewSize();
        List<Size>  allPictureSize = parameters.getSupportedPictureSizes();
        float previewAspectRatio = (float)currentPreviewSize.width / (float)currentPreviewSize.height;
        Size OptimalPictureSize = null;
        for(int i = 0 ; i < allPictureSize.size() ; i++) {
            float tmpPictureAspectRatio = (float)allPictureSize.get(i).width / (float)allPictureSize.get(i).height;
            if( tmpPictureAspectRatio == previewAspectRatio) {
                if(OptimalPictureSize == null) OptimalPictureSize = allPictureSize.get(i);
                else {
                    if(OptimalPictureSize.width*OptimalPictureSize.height
                            < allPictureSize.get(i).width*allPictureSize.get(i).height)
                        OptimalPictureSize = allPictureSize.get(i);
                }
            }
        }
        parameters.setPictureSize(OptimalPictureSize.width,OptimalPictureSize.height);
        mPictureSize = OptimalPictureSize;
        mCamera.setParameters(parameters);
    }



    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if ( mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            List<Size> allSizes = parameters.getSupportedPreviewSizes();
            Camera.Size size = allSizes.get(0); // get top size
            for (int i = 0; i < allSizes.size(); i++) {
                if (allSizes.get(i).width > size.width)
                    size = allSizes.get(i);
            }
            //set max Preview Size
            parameters.setPreviewSize(size.width, size.height);
            // Important: Call startPreview() to start updating the preview surface.
            // Preview must be started before you can take a picture.
            mCamera.startPreview();
        }

    }

}