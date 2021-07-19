package com.example.marker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.marker.cameracalibration.CameraCalibrationActivity;

import com.example.marker.packagemanager.PackageManagerActivity;
import com.example.marker.codescanner.CodeScannerActivity;
import com.example.marker.markerDetection.MarkerDetectionActivity;

import java.io.File;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {

    private Button calibrateCameraBtn;
    private Button viewMarkersBtn;
    private Button scanQRCodeBtn;
    private Button viewPackagesBtn;

    private static final int REQUEST_CAMERA=102;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // initializing and setting the buttons button
        calibrateCameraBtn = (Button) findViewById(R.id.calibrateCameraBtn);
        viewMarkersBtn = (Button) findViewById(R.id.viewMarkersBtn);
        scanQRCodeBtn = (Button) findViewById(R.id.scanQRBtn);
        viewPackagesBtn = (Button) findViewById(R.id.viewPackagesBtn);

        File PackageDirectory = new File(getExternalFilesDir(null),"Packages");
        File Test = new File(HomeActivity.this.getExternalFilesDirs(null)[0],"Packages");
        Log.i("directory",Test.getPath());
        boolean dirCreated = false;
        if(!PackageDirectory.isDirectory())
            dirCreated = PackageDirectory.mkdirs();
        if(dirCreated)
            Log.i("directory","Directory \"Packages\" created successfully!");
        else
            Log.i("directory","Failed to create new directory");

        calibrateCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCalibrateCameraActivity();
            }
        });

        viewMarkersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchViewMarkersActivity();
            }
        });

        scanQRCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchQRCodeActivity();
            }
        });

        viewPackagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchViewPackagesActivity();
            }
        });
    }



    /**
     * This function should include all the necessary permissions needed for use throughout the application
     * */
    public void requestCameraPermission() {
        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    HomeActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    REQUEST_CAMERA
            );
        }
    }


    /**
     * Launches the View Markers Activity with the current context.
     */
    public void launchViewMarkersActivity() {
        requestCameraPermission();
        if(ContextCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.i("activity", "Launched View Markers Activity");
            Intent intent = new Intent(this, MarkerDetectionActivity.class);
            startActivity(intent);
        } else {
            Log.i("error","Permission Denied. Could not launch activity");
        }
    }

    /**
     * Launches the QR Code scan Activity with the current context.
     */
    public void launchQRCodeActivity() {
        requestCameraPermission();
        if(ContextCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED) {
            Log.i("activity", "Launched View Packages Activity");
            Intent intent = new Intent(this, CodeScannerActivity.class);
            startActivity(intent);
        } else {
            Log.i("error","Permission Denied. Could not launch activity");
        }
    }

    /**
     * Launches the View Packages Activity with the current context.
     */
    public void launchViewPackagesActivity() {
        Log.i("activity", "Launched View Packages Activity");
        Intent intent = new Intent(this, PackageManagerActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the Camera Calibration Activity with the current context.
     */
    public void launchCalibrateCameraActivity() {
        requestCameraPermission();
        if(ContextCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.i("activity", "Launched Camera Calibration Activity");
            Intent intent = new Intent(this, CameraCalibrationActivity.class);
            startActivity(intent);
        } else {
            Log.i("error","Permission Denied, Could not launch activity");
        }
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack (true);
    }
}