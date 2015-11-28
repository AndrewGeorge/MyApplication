package com.example.administrator.mycamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 自定义相机功能
 * Created by Administrator on 2015/11/28.
 */
public class CustomCameraAvtivity extends Activity implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File file = new File("/sdcard/temp.jpg");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(data);
                fos.close();
                Log.i("CustomCameraAvtivity", "path" + file.getAbsolutePath());
                Intent intent = new Intent(CustomCameraAvtivity.this, ResultActivity.class);
                intent.putExtra("picPath", file.getAbsolutePath());
                startActivity(intent);
                Log.d("CustomCameraAvtivity", "startActivity");
                CustomCameraAvtivity.this.finish();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom);
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        Log.i("CustomCameraAvtivity", "initView");
        mSurfaceView = (SurfaceView) findViewById(R.id.preview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            //预览时点击屏幕时自动对焦
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });
    }

    public void capture(View view) {
        Log.i("CustomCameraAvtivity", "capture");
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPreviewSize(800, 400);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        Log.i("CustomCameraAvtivity", "setFocusMode");
        mCamera.takePicture(null, null, mPictureCallback);
//        mCamera.autoFocus(new Camera.AutoFocusCallback() {
//            @Override
//            public void onAutoFocus(boolean success, Camera camera) {
//
//                    Log.i("CustomCameraAvtivity", "take photo");
//                    mCamera.takePicture(null, null, mPictureCallback);
//
//            }
//        });
    }

    /**
     * 获取carera 对象
     *
     * @return
     */
    private Camera getCamera() {
        Camera camera = null;
        Log.i("CustomCameraAvtivity", "getCamera");
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (camera != null) {
            return camera;
        }
        return null;
    }

    /**
     * 开始预览相机内容
     */
    private void setStartPreview(Camera camera, SurfaceHolder surfaceHolder) {
        Log.i("CustomCameraAvtivity", "setStartPreview");
        try {
            //将相机绑定到SurfaceViewHolder
            camera.setPreviewDisplay(surfaceHolder);
            //相机默认位横屏
            camera.setDisplayOrientation(90);
            //开始将相机中的内容实时显示在surfaceview中
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放相机资源
     */
    private void releseCamera() {
        Log.i("CustomCameraAvtivity", "releseCamera");
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera();
            if (mSurfaceHolder != null) {
                Log.i("CustomCameraAvtivity", "onResume");
                setStartPreview(mCamera, mSurfaceHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releseCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera, mSurfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        setStartPreview(mCamera, mSurfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releseCamera();
    }
}
