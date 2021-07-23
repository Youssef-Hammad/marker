package com.example.marker.markerDetection;

import android.opengl.Matrix;

import com.example.marker.modelLoader.ModelLoader;
import com.example.marker.packagemanager.MarkerPackage;
import com.example.marker.packagemanager.MarkerPackageManager;

import gl.GLCamera;
import gl.MarkerObject;
import gl.scenegraph.MeshComponent;
import gl.scenegraph.Shape;
import util.Vec;

public class MarkerData implements MarkerObject {
    private String TAG = "MarkerInfo";

    private float[] invertedCameraMatrix = new float[16];
    private float[] resultPosVec = { 0, 0, 0, 1 };
    private float[] antiCameraMarkerRotMatrix = new float[16];

    private int id;
    private Shape model;
    private GLCamera camera;
    protected MeshComponent mesh;
    public boolean isVisible = false;

    public MarkerData(int id, MeshComponent mesh, GLCamera camera) {
        this.id = id;
        this.mesh = mesh;
        this.camera = camera;

        MarkerPackageManager manager = MarkerPackageManager.getInstance();
        MarkerPackage markerPackage = manager.getActive();
        if (id < markerPackage.getSize())
            model = ModelLoader.loadObj(id);
        else
            model = new Shape();
    }

    @Override
    public int getMyId() {
        return id;
    }

    @Override
    public void OnMarkerPositionRecognized(float[] rotMatrix, int start, int end) {
        Matrix.invertM(invertedCameraMatrix, 0, camera.getRotationMatrix(), 0);

        float[] markerCenterPosVec = { rotMatrix[start + 12],
                rotMatrix[start + 13],
                rotMatrix[start + 14], 1 };
        Matrix.multiplyMV(resultPosVec, 0, invertedCameraMatrix, 0,
                markerCenterPosVec, 0);

        Vec camPos = camera.getPosition();
        setObjectPos(new Vec(resultPosVec[0] + camPos.x, resultPosVec[1]
                + camPos.y, resultPosVec[2] + camPos.z));

        Matrix.multiplyMM(antiCameraMarkerRotMatrix, 0, invertedCameraMatrix,
                0, rotMatrix, start);

        float[] rotOffset = new float[16];
        rotOffset[0]=1;
        rotOffset[6]=-1;
        rotOffset[9]=1;
        rotOffset[15]=1;

        // clear the translation values:
        antiCameraMarkerRotMatrix[12] = 0;
        antiCameraMarkerRotMatrix[13] = 0;
        antiCameraMarkerRotMatrix[14] = 0;

        Matrix.multiplyMM(antiCameraMarkerRotMatrix,0,antiCameraMarkerRotMatrix,0,rotOffset,0);
        Matrix.multiplyMM(antiCameraMarkerRotMatrix,0,antiCameraMarkerRotMatrix,0,rotOffset,0);
        Matrix.multiplyMM(antiCameraMarkerRotMatrix,0,antiCameraMarkerRotMatrix,0,rotOffset,0);


        setObjRotation(antiCameraMarkerRotMatrix);
    }

    @Override
    public void setVisible(boolean isVisible) {
        if (this.isVisible == isVisible)
            return;
        this.isVisible = isVisible;
        updateMesh();
    }

    public synchronized void updateMesh() {
        if (isVisible) {
            mesh.addChild(model);
        } else {
            mesh.removeAllChildren();
        }
    }

    public void setObjRotation(float[] rotMatrix) {
        mesh.setRotationMatrix(rotMatrix);
    }

    public void setObjectPos(Vec positionVec) {
        mesh.setPosition(positionVec);
    }

}
