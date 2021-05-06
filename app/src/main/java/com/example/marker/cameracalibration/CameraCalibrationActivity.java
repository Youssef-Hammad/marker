package com.example.marker.cameracalibration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.example.marker.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.core.Mat;

public class CameraCalibrationActivity extends AppCompatActivity implements View.OnTouchListener, CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "CamCalibrationActivity";

    private CameraBridgeViewBase mCameraView;

    private int mWidth;
    private int mHeight;

    public CameraCalibrationActivity() {
        Log.i(TAG, "Camera Calibration Activity instantiated");
    }

    /**
     *
     * Callback function that is used to commit the status of OpenCV loading.
     * @param Application Context, i.e. `this`
     */
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        /**
         * This method receives the status of loading openCV and enables the camera view
         * if loading OpenCV was successful.
         * @param status a status code reflecting the status of loading openCV
         */
        @Override
        public void onManagerConnected(int status) {
            switch(status) {
                case LoaderCallbackInterface
                        .SUCCESS:
                {
                    Log.i(TAG, "OpenCV Loaded Successfully");
                    mCameraView.enableView();
                    mCameraView.setOnTouchListener(CameraCalibrationActivity.this);
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    /**
     * This method sets the activity's content view to the CameraCalibrationActivity, then
     * instantiates the openCvCameraView (mCameraView) and makes it visible.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate called");
        super.onCreate(savedInstanceState);

        // don't let screen turn off
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera_calibration);

        mCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_calibration_java_camera_view);
        mCameraView.setVisibility(SurfaceView.VISIBLE);
        mCameraView.setCvCameraViewListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    /**
     * Inflates the calibration menu
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.calibration, menu);
        return true;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return null;
    }
}