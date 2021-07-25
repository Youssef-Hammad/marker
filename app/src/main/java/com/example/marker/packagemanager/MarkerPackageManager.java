package com.example.marker.packagemanager;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.ArrayList;

public class MarkerPackageManager {
    private static final String TAG = "PackageManagerActivity";

    private MarkerPackage currentActivePackage;
    private PackageHandler pkgHandler;


    private static MarkerPackageManager markerPackageManager;

    private MarkerPackageManager() {
        currentActivePackage = null;
        pkgHandler = new PackageHandler();
        Log.i(TAG, "Camera Calibration Activity instantiated");
    }

    public static MarkerPackageManager getInstance() {
        if (markerPackageManager == null)
            markerPackageManager = new MarkerPackageManager();
        return markerPackageManager;
    }

    /**
     * This method calls the downloadPackage method in pkgHandler
     * and returns whether or not the package was downloaded successfully
     * @param context is the activity that should be passed when calling this function
     * @param url is the link to the API to download a specific package
     * @param PackageName: Name of the package
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public Boolean downloadPackage(Context context, String url, String PackageName) {
        return pkgHandler.downloadPackage(context,url,PackageName);
    }

    /**
     * This method receives a package name on the local memory
     * and sets it as the global Active Package
     * @param context is the activity that should be passed when calling this function
     * @param PackageName: Name of the package
     */
    //TODO: Might redesign the communication between this and pkgHandler.setActive
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Boolean setActive(Context context, String PackageName) throws IOException {
        Boolean success = pkgHandler.setActive(context, PackageName);
        if(success) {
            currentActivePackage = new MarkerPackage(context); //Constructor will populate the ArrayList with the models
            currentActivePackage.setPackageName(PackageName);
        }
        return success;
    }

    /**
     * This method returns the name of the currently active package
     */
    public MarkerPackage getActive() {
        return currentActivePackage;
    }

    /**
     * This method calls the deletePackage method in pkgHandler
     * and returns whether or not the package was deleted successfully
     * @param context is the activity that should be passed when calling this function
     * @param PackageName: Name of the package
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Boolean deletePackage(Context context, String PackageName) {
        //TODO
        //Check if the package to delete is the active package.
        Boolean deleteSuccess = pkgHandler.deletePackage(context,PackageName);
        if(deleteSuccess) {
            if(currentActivePackage!=null && PackageName.equals(currentActivePackage.getPackageName()))
                currentActivePackage=null;
        }
        return deleteSuccess;
    }

    /**
     * This method returns an ArrayList of strings
     * each string denoting the path of each package stored on the local memory
     */
    public ArrayList<String> getPackages(Context context) {
        return pkgHandler.getPackages(context);
    }
}