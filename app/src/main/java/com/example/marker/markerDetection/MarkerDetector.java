package com.example.marker.markerDetection;

import android.util.Log;

import org.opencv.aruco.Dictionary;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.aruco.Aruco;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Marker Detection class, handles all the detection logic.
 */
public class MarkerDetector {
    private static final String TAG = "DetectorClass";

    private Mat cameraMatrix;
    private Mat distortionCoef;

    /**
     * The list of corners of the detected markers.
     */
    private List<Mat> corners;

    /**
     * The ids of the detected markers.
     */
    private Mat ids;

    /**
     * The rvecs and tvecs of the detected markers.
     */
    private Mat rvecs, tvecs;

    /**
     * Length of the marker to be detected.
     */
    private float markerLength;

    /**
     * Dictionary that contains the markers to be scanned for and detected.
     */
    private Dictionary markerDictionary;

    /**
     * A list of detected markers data.
     */
    private List<MarkerData> detectedMarkers;

    /**
     * This constructor defines all member variables,
     * defines cameraMatrix as a 3x3 CV_64FC1 Matrix as well as the distortionCoef as a
     * 5x1 CV_64FC1 Matrix.
     */
    public MarkerDetector() {
        cameraMatrix = new Mat();
        distortionCoef = new Mat();
        ids = new Mat();
        corners = new ArrayList<>();
        rvecs = new Mat();
        tvecs = new Mat();
        markerDictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250);
        Mat.eye(3, 3, CvType.CV_64FC1).copyTo(cameraMatrix);
        Mat.zeros(5, 1, CvType.CV_64FC1).copyTo(distortionCoef);
        markerLength = (float)0.126;
        detectedMarkers = new ArrayList<>();

        /** Hard Coded Camera Matrix and Distortion Coef **/
        double dataMatrix[] =  {1005.379076113438, 0, 640, 0, 1005.379076113438, 360, 0, 0, 1};
        double dataDistortion[] = {0.1027157926764224, -0.2658898222368876, 0, 0, 0};

        cameraMatrix.put(0, 0, dataMatrix);
        distortionCoef.put(0, 0, dataDistortion);
        Log.i(TAG, "hard coded matrix " + cameraMatrix.dump());
        Log.i(TAG, "hard coded distortion " + distortionCoef.dump());
    }

    /**
     * This method takes a gray frame, detects the markers
     * and stores their corners, rvecs, tvecs and ids,
     * then updates the detectedMarkers list with the new markers detected.
     * @param frameGray
     */
    public void markerDetection(Mat frameGray)
    {
        ids = new Mat();
        corners = new ArrayList<>();
        Aruco.detectMarkers(frameGray, markerDictionary, corners, ids);

        List<MarkerData> newDetectedMarkers = new ArrayList<>();

        if(ids.size().height > 0)
        {
            rvecs = new Mat();
            tvecs = new Mat();
            Aruco.estimatePoseSingleMarkers(corners, markerLength, cameraMatrix, distortionCoef, rvecs, tvecs);

            for(int i=0; i<ids.size().height; i++)
            {
                double[] rvecArray = new double[]{
                        rvecs.get(i , 0)[0],
                        rvecs.get(i , 0)[1],
                        rvecs.get(i , 0)[2]};
                double[] tvecArray = new double[]{
                        tvecs.get(i , 0)[0],
                        tvecs.get(i , 0)[1],
                        tvecs.get(i , 0)[2]};

                MarkerData newMarker = new MarkerData(rvecArray, tvecArray, corners.get(i), (int)ids.get(i, 0)[0]);
                newDetectedMarkers.add(newMarker);
            }
        }

        updateDetectedMarkers(newDetectedMarkers);
    }

    /**
     * This method takes a gray frame
     * and updates the detectedMarkers list with the new markers detected.
     * @param detectedMarkers
     */
    private void updateDetectedMarkers(List<MarkerData> detectedMarkers)
    {
        this.detectedMarkers = detectedMarkers;
    }

    public List<MarkerData> getDetectedMarkers()
    {
        return detectedMarkers;
    }

    public int detectedMarkersSize() { return detectedMarkers.size(); }

    /**
     * This method takes a gray frame,
     * draws a square around the detected markers
     * and writes the id on top of each marker.
     * @param frameGray
     * @return
     */
    public Mat drawDetectedMarkers(Mat frameGray)
    {
        if(detectedMarkersSize() == 0)
            return frameGray;

        Aruco.drawDetectedMarkers(frameGray, corners, ids);

        return frameGray;
    }

    /**
     * This method takes a gray frame,
     * draws x, y and z axis on top of each marker
     * and calls drawDetectedMarkers method.
     * @param frameGray
     * @return
     */
    public Mat drawAxis(Mat frameGray)
    {
        if(detectedMarkersSize() == 0)
            return frameGray;

        frameGray = drawDetectedMarkers(frameGray);

        for(int i=0; i<ids.size().height; i++)
            Aruco.drawAxis(frameGray, cameraMatrix, distortionCoef, rvecs.row(i), tvecs.row(i), (float)0.1);

        return frameGray;
    }
}
