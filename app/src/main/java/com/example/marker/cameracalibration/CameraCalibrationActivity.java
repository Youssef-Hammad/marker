package com.example.marker.cameracalibration;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.marker.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import com.example.marker.cameracalibration.FrameRender;

public class CameraCalibrationActivity extends AppCompatActivity implements View.OnTouchListener, CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "CamCalibrationActivity";

    private CameraBridgeViewBase mCameraView;
    private CameraCalibrator mCalibrator;
    private OnCameraFrameRender mOnCameraFrameRender;
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.preview_mode).setEnabled(true);
        if(!mCalibrator.isCalibrated())
            menu.findItem(R.id.preview_mode).setEnabled(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calibration:
                mOnCameraFrameRender =
                        new OnCameraFrameRender(new CalibrationFrameRender(mCalibrator));
                item.setChecked(true);
                return true;
            case R.id.undistortion:
                mOnCameraFrameRender =
                        new OnCameraFrameRender(new UndistortionFrameRender(mCalibrator));
                item.setChecked(true);
                return true;
            case R.id.comparison:
                mOnCameraFrameRender =
                        new OnCameraFrameRender(new ComparisonFrameRender(mCalibrator, mWidth, mHeight, getResources()));
                item.setChecked(true);
                return true;
            case R.id.calibrate:
                final Resources res = getResources();
                if (mCalibrator.getCornersBufferSize() < 2) {
                    (Toast.makeText(this, res.getString(R.string.more_samples), Toast.LENGTH_SHORT)).show();
                    return true;
                }

                mOnCameraFrameRender = new OnCameraFrameRender(new PreviewFrameRender());
                new AsyncTask<Void, Void, Void>() {
                    private ProgressDialog calibrationProgress;

                    @Override
                    protected void onPreExecute() {
                        calibrationProgress = new ProgressDialog(CameraCalibrationActivity.this);
                        calibrationProgress.setTitle(res.getString(R.string.calibrating));
                        calibrationProgress.setMessage(res.getString(R.string.please_wait));
                        calibrationProgress.setCancelable(false);
                        calibrationProgress.setIndeterminate(true);
                        calibrationProgress.show();
                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        mCalibrator.calibrate();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        calibrationProgress.dismiss();
                        mCalibrator.clearCorners();
                        mOnCameraFrameRender = new OnCameraFrameRender(new CalibrationFrameRender(mCalibrator));
                        String resultMessage = (mCalibrator.isCalibrated()) ?
                                res.getString(R.string.calibration_successful)  + " " + mCalibrator.getAvgReprojectionError() :
                                res.getString(R.string.calibration_unsuccessful);
                        (Toast.makeText(CameraCalibrationActivity.this, resultMessage, Toast.LENGTH_SHORT)).show();

                        if (mCalibrator.isCalibrated()) {
                            CalibrationResult.save(CameraCalibrationActivity.this,
                                    mCalibrator.getCameraMatrix(), mCalibrator.getDistortionCoefficients());
                        }
                    }
                }.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "onTouch invoked");

        mCalibrator.addCorners();
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mCameraView != null)
            mCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()) {
            Log.d(TAG, "No internal OpenCVLib was found, using openCV manager for initialization instead");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCVLib found internally.");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if(mCameraView != null)
            mCameraView.disableView();
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        if (mWidth != width || mHeight != height) {
            mWidth = width;
            mHeight = height;
            mCalibrator = new CameraCalibrator(mWidth, mHeight);
            if (CalibrationResult.tryLoad(this, mCalibrator.getCameraMatrix(), mCalibrator.getDistortionCoefficients())) {
                mCalibrator.setCalibrated();
            }

            mOnCameraFrameRender = new OnCameraFrameRender(new CalibrationFrameRender(mCalibrator));
        }
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return mOnCameraFrameRender.render(inputFrame);
    }
}