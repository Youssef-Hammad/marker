package com.example.marker.markerDetection;
import com.example.marker.R;
import com.example.marker.modelLoader.ModelLoader;
import com.example.marker.packagemanager.MarkerPackage;
import com.example.marker.packagemanager.MarkerPackageManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import actions.Action;
import actions.ActionBufferedCameraAR;
import actions.ActionCalcRelativePos;
import actions.ActionMoveCameraBuffered;
import actions.ActionPlaceObject;
import actions.ActionRotateCameraBuffered;
import commands.Command;
import geo.GeoObj;
import gl.Color;
import gl.CustomGLSurfaceView;
import gl.GL1Renderer;
import gl.GLCamera;
import gl.GLFactory;
import gl.scenegraph.MeshComponent;
import gl.scenegraph.Shape;
import gui.GuiSetup;
import markerDetection.MarkerDetectionSetup;
import markerDetection.MarkerObjectMap;
import markerDetection.UnrecognizedMarkerListener;
import system.ArActivity;
import system.ErrorHandler;
import system.EventManager;
import system.Setup;
import util.Vec;
import util.Wrapper;
import worldData.Obj;
import worldData.SystemUpdater;
import worldData.World;

public class MarkerDetectionActivity extends MarkerDetectionSetup {
    private static final String TAG = "DetectionActivity";

    private GLCamera camera;
    private World world;
    private MeshComponent[] models;

    @Override
    public UnrecognizedMarkerListener _a2_getUnrecognizedMarkerListener() {
        return null;
    }

    @Override
    public void _a3_registerMarkerObjects(MarkerObjectMap markerObjectMap) {
        MarkerPackageManager manager = MarkerPackageManager.getInstance();
        MarkerPackage markerPackage = manager.getActive();
        if (markerPackage != null) {
            models = new MeshComponent[markerPackage.getSize()];
            for (int i = 0; i < markerPackage.getSize(); ++i) {
                models[i] = new Shape();
                markerObjectMap.put(new MarkerData(i, models[i], camera));
                world.add(models[i]);
            }
        }
    }

    @Override
    public void _a_initFieldsIfNecessary() {
        camera = new GLCamera(new Vec(0, 0, 10));
        world = new World(camera);
    }

    @Override
    public void _b_addWorldsToRenderer(GL1Renderer glRenderer, GLFactory objectFactory, GeoObj currentPosition) {
        glRenderer.addRenderElement(world);
    }

    @Override
    public void _c_addActionsToEvents(EventManager eventManager, CustomGLSurfaceView arView, SystemUpdater updater) {

    }

    @Override
    public void _d_addElementsToUpdateThread(SystemUpdater updater) {
        updater.addObjectToUpdateCycle(world);
    }

    @Override
    public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {

    }
}
