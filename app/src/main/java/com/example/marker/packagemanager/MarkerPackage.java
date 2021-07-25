package com.example.marker.packagemanager;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

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
    private String packageName;
    private ArrayList<MarkerModel> models;


    static class ModelComparator implements Comparator<MarkerModel> {
        @Override
        public int compare(MarkerModel m1, MarkerModel m2) {
            if(m1==null&&m2==null)
                return 0;
            if(m1==null)
                return -1;
            if(m2==null)
                return 1;
            int ret = m1.getObj().compareTo(m2.getObj());
            return ret;
        }
    }


    /**
     * initializes the ArrayList models and populates it
     * @param context
     */
    public MarkerPackage(Context context) {
        models = new ArrayList<>();
        Log.i("mPackage","Reached constructor");
        File cacheDir = new File(context.getExternalCacheDir().getPath());
        populateList(cacheDir.listFiles());
        Collections.sort(models,new ModelComparator());
        //Collections.sort(models);
        for(MarkerModel path : models) {
            Log.i("mPackage", path.getObj());
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
                MarkerModel markerModel = new MarkerModel();
                Boolean containsObj = false;
                for(File modelFile : filesList) {
                    String[] extension = modelFile.getName().split("\\.");
                    if(extension[extension.length-1].equals("obj")) {
                        markerModel.setObj(modelFile.getPath());
                        containsObj = true;
                    }
                    else if(extension[extension.length-1].equals("mtl"))
                        markerModel.setMtl(modelFile.getPath());
                    else
                        markerModel.addTexture(modelFile.getPath());
                }
                if(containsObj)
                    models.add(markerModel);
                return;
                //models.add(file.getPath());
                //Log.i("mPackage","Path: "+file.getPath());
            }
        }
    }

    /**
     * getter for a specific model info (paths to texture and obj)
     * @param idx
     * @return model info
     */
    public MarkerModel getModel(Integer idx) {
        return models.get(idx);
    }

    /**
     * @return size of models ArrayList
     */
    public Integer getSize() {
        return models.size();
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}