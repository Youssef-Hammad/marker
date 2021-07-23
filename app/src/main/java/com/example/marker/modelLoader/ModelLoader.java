package com.example.marker.modelLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.marker.packagemanager.MarkerModel;
import com.example.marker.packagemanager.MarkerPackage;
import com.example.marker.packagemanager.MarkerPackageManager;
import com.google.zxing.common.BitArray;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import gl.scenegraph.Shape;
import gl.textures.TextureManager;
import gl.textures.TexturedRenderData;
import util.IO;
import util.Vec;

public class ModelLoader {
    private final static String TAG = "Loader";
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Shape loadObj(int modelId) {
        Shape objShape = new Shape(gl.Color.white());
        TexturedRenderData data = new TexturedRenderData();
        HashMap<String, Bitmap> textures = null;

        MarkerPackageManager pkgManager = MarkerPackageManager.getInstance();
        MarkerPackage markerPackage = pkgManager.getActive();
        MarkerModel markerModel = markerPackage.getModel(modelId);
        String path = markerModel.getObj();

        try {
            BufferedReader buffer = new BufferedReader(new FileReader(path));

            ArrayList<Vec> vertexList = new ArrayList<>();
            ArrayList<int[][]> faces = new ArrayList<>();
            ArrayList<Vec> textureList = new ArrayList<>();

            vertexList.add(new Vec(0, 0, 0));
            textureList.add(new Vec(0, 0, 0));

            String line;

            while ((line = buffer.readLine()) != null) {
                StringTokenizer lineElements = new StringTokenizer(line, " ");

                if (lineElements.countTokens() == 0)
                    continue;

                String type = lineElements.nextToken();

                if (type.equals("v")) {
                    Vec vertex = new Vec();
                    vertex.x = Float.parseFloat(lineElements.nextToken());
                    vertex.y = Float.parseFloat(lineElements.nextToken());
                    vertex.z = Float.parseFloat(lineElements.nextToken());
                    vertexList.add(vertex);
                } else if (type.equals("f")) {
                    int verticesCount = lineElements.countTokens();
                    int[][] f = new int[2][verticesCount];
                    for (int i = 0; i < verticesCount; i++) {
                        String[] face = lineElements.nextToken().split("/");
                        f[0][i] = Integer.parseInt(face[0]);
                        if (face.length > 1 && face[1].length() != 0)
                            f[1][i] = Integer.parseInt(face[1]);
                    }
                    faces.add(f);
                } else if (type.equals("vt")) {
                    int coordsCount = lineElements.countTokens();
                    float[] c = new float[3];
                    for (int i = 0; i < coordsCount; ++i)
                        c[i] = Float.parseFloat(lineElements.nextToken());
                    c[1] = 1-c[1];
                    textureList.add(new Vec(c[0], c[1], c[2]));
                } else if (type.equals("mtllib")) {
                    textures = loadMtl(markerModel);
                }
            }

            objShape.setMyRenderData(data);

            ArrayList<Vec> texturePositions = new ArrayList<>();
            int facesCnt = 0;
            for (int[][] face : faces) {
                if (face[0].length == 3) {
                    Log.i(TAG,"Face #"+String.valueOf(facesCnt++)+": ");
                    for (int i = 0; i < 3; ++i) {
                        Vec v = vertexList.get(face[0][i]);
                        Vec t = textureList.get(face[1][i]);

                        objShape.add(v);
                        texturePositions.add(t);
                        Log.i(TAG,String.valueOf(v)+"/"+String.valueOf(t));
                    }
                } else if (face[0].length == 4) {
                    for (int i = 0; i < 2; ++i) {
                        Vec v1 = vertexList.get(face[0][0]);
                        Vec t1 = textureList.get(face[1][0]);
                        objShape.add(v1);
                        texturePositions.add(t1);

                        Vec v2 = vertexList.get(face[0][i + 1]);
                        Vec t2 = textureList.get(face[1][i + 1]);
                        objShape.add(v2);
                        texturePositions.add(t2);

                        Vec v3 = vertexList.get(face[0][i + 2]);
                        Vec t3 = textureList.get(face[1][i + 2]);
                        objShape.add(v3);
                        texturePositions.add(t3);
                        Log.i(TAG,"Face #"+String.valueOf(facesCnt++)+": "+String.valueOf(v1)+"/"+String.valueOf(t1)+" "+String.valueOf(v2)+"/"+String.valueOf(t2)+" "+String.valueOf(v3)+"/"+String.valueOf(t3));
                    }
                }
            }

            data.updateTextureBuffer(texturePositions);

            if (textures != null) {
                Log.d("Loader", "loaded mtl successfully");
                for (Map.Entry<String, Bitmap> texture : textures.entrySet()) {
                    TextureManager.getInstance().addTexture(data, texture.getValue(), texture.getKey());
                    Log.d(TAG, String.valueOf(texture.getValue()));
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Loader", "loaded successfully");
        return objShape;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static HashMap<String, Bitmap> loadMtl(MarkerModel markerModel) {
        HashMap<String, Bitmap> textures = new HashMap<>();

        String path = markerModel.getMtl();

        try {
            BufferedReader buffer = new BufferedReader(new FileReader(path));
            String line;
            String currentTexture = "";
            float[] Kd = new float[3];
            for (int i = 0; i < 3; ++i) Kd[i] = 1;
            while ((line = buffer.readLine()) != null) {
                Log.d(TAG, line);
                String[] parts = line.split(" ");
                if (parts.length == 0)
                    continue;
                String type = parts[0];
                if (type.equals("newmtl")) {
                    if (parts.length > 1) {
                        currentTexture = parts[1];
                    }
                } else if (type.equals("map_Kd")) {
                    if (parts.length > 1) {
                        String textureName = parts[1];
                        Log.d(TAG, parts[1]);
                        Bitmap image = BitmapFactory.decodeFile(markerModel.getTexture(textureName));
                        textures.put(currentTexture, image);
                    }
                } else if (type.equals("Kd")) {
                    if (parts.length == 4) {
                        for (int i = 0; i < 3; ++i)
                            Kd[i] = Float.parseFloat(parts[i + 1]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textures;
    }
}
