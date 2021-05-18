package com.example.marker.markerDetection;

import org.opencv.core.Mat;

/**
 * Marker Data class, has all the data of the detected marker.
 */
public class MarkerData {
    private String TAG = "MarkerInfo";
    private double[] rvec , tvec;
    private Mat corners;
    private int id;

    /**
     * This constructor defines all member variables
     * and sets the id to -1.
     */
    public MarkerData()
    {
        rvec = new double[3];
        tvec = new double[3];
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
    public MarkerData(double[] rvec, double[] tvec, Mat corners, int id)
    {
        this.rvec = rvec;
        this.tvec = tvec;
        this.corners = corners;
        this.id = id;
    }

    public double[] getRvec()
    {
        return rvec;
    }

    public double[] getTvec()
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
