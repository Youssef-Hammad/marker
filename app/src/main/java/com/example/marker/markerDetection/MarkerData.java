package com.example.marker.markerDetection;

import org.opencv.core.Mat;

/**
 * Marker Data class, has all the data of the detected marker.
 */
public class MarkerData {
    private String TAG = "MarkerInfo";
    private Mat rvec , tvec;
    private Mat corners;
    private int id;

    /**
     * This constructor defines all member variables
     * and sets the id to -1.
     */
    public MarkerData()
    {
        rvec = new Mat();
        tvec = new Mat();
        corners = new Mat();
        id = -1;
    }

    /**
     * This constructor sets all the member variables.
     * @param rvec
     * @param tvec
     * @param corners
     * @param id
     */
    public MarkerData(Mat rvec, Mat tvec, Mat corners, int id)
    {
        this.rvec = rvec;
        this.tvec = tvec;
        this.corners = corners;
        this.id = id;
    }

    public Mat getRvec()
    {
        return rvec;
    }

    public Mat getTvec()
    {
        return tvec;
    }

    public Mat getCorners()
    {
        return corners;
    }

    public int getId() {
        return id;
    }
}
