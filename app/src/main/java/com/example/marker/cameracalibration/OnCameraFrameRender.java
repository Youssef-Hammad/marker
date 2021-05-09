package com.example.marker.cameracalibration;

import android.content.res.Resources;

import com.example.marker.R;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * This class renders calibration frames
 */
class CalibrationFrameRender {
    private CameraCalibrator mCalibrator;

    public CalibrationFrameRender(CameraCalibrator pCamCalibrator) {
        mCalibrator = pCamCalibrator;
    }

    public Mat render(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgbaFrame = inputFrame.rgba();
        Mat grayFrame = inputFrame.gray();
        mCalibrator.processFrame(grayFrame, rgbaFrame);
        return rgbaFrame;
    }
}