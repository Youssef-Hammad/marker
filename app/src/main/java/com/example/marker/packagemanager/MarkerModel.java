package com.example.marker.packagemanager;

import android.util.Log;

import java.util.HashMap;

public class MarkerModel {
    private String obj;
    private String mtl;
    private HashMap<String, String> texturesMap;

    public MarkerModel() {
        obj = "";
        mtl = "";
        texturesMap = new HashMap<>();
    }

    public String getMtl() {
        return mtl;
    }

    public void setMtl(String mtl) {
        this.mtl = mtl;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getTexture(String textureName) {
        return texturesMap.get(textureName);
    }

    public void addTexture(String path) {
        Log.d("MarkerModel", path);
        String[] parts = path.split("/");
        String name = parts[parts.length - 1];
        texturesMap.put(name, path);
    }
}
