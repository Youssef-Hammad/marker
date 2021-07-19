package com.example.marker.packagemanager;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

//public class MarkerPackage {
//    private String packageName;
//    private String description;
//    private ArrayList<String> packages;
//    private int id;
//
//    /**
//     * TODO: Might add parameters to this constructor
//     */
//    public MarkerPackage() {
//        packageName = "";
//        description = "";
//        packages = new ArrayList<>();
//        id = 0;
//    }
//
//    public String getPackageName() {
//        return packageName;
//    }
//
//    public void setPackageName(String packageName) {
//        this.packageName = packageName;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public ArrayList<String> getPackages() {
//        return packages;
//    }
//
//    public void setPackages(ArrayList<String> packages) {
//        this.packages = packages;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//}

public class MarkerPackage {
    private ArrayList<String> models;

    /**
     * initializes the ArrayList models and populates it
     * @param context
     */
    public MarkerPackage(Context context) {
        models = new ArrayList<>();
        Log.i("mPackage","Reached constructor");
        File cacheDir = new File(context.getExternalCacheDir().getPath());
        populateList(cacheDir.listFiles());
        for(String path : models) {
            Log.i("mPackage",path);
        }
    }

    /**
     * Gets called by the constructor, populates models with the paths of the 3d models belonging to the currently active package
     * @param filesList
     */
    private void populateList(File[] filesList) {
        Log.i("mPackage","FilesList: "+filesList.length);
        for(File file : filesList) {
            //Log.i("mPackage","Path: "+file);
            if(file.isDirectory()) {
                populateList(file.listFiles());
            }
            else {
                models.add(file.getPath());
                Log.i("mPackage","Path: "+file.getPath());
            }
        }
    }

    /**
     * getter for a specific path in models
     * @param idx
     * @return specified path
     */
    public String getModelPath(Integer idx) {
        return models.get(idx);
    }

    /**
     * @return size of models ArrayList
     */
    public Integer getSize() {
        return models.size();
    }
}