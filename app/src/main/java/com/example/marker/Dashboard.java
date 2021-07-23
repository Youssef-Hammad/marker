package com.example.marker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.marker.cameracalibration.CameraCalibrationActivity;
import com.example.marker.codescanner.CodeScannerActivity;
import com.example.marker.markerDetection.MarkerDetectionActivity;
import com.example.marker.packagemanager.MarkerPackageManager;
import com.example.marker.packagemanager.PackageManagerActivity;

import java.io.File;

import system.ArActivity;

public class Dashboard extends AppCompatActivity {
    //private ImageView calibrateCameraBtn;
    private CardView renderBtn;
    private CardView scanQRCodeBtn;
    private CardView viewPackagesBtn;
    private CardView calibrateCameraBtn;


    private static final int REQUEST_CAMERA=102;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        calibrateCameraBtn = findViewById(R.id.calibrate_camera);
        renderBtn = findViewById(R.id.render_btn);
        viewPackagesBtn = findViewById(R.id.browse_pkg_btn);
        scanQRCodeBtn = findViewById(R.id.scan_qr_btn);

        calibrateCameraBtn.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        renderBtn.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        viewPackagesBtn.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        scanQRCodeBtn.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

        File PackageDirectory = new File(getExternalFilesDir(null),"Packages");
        File Test = new File(Dashboard.this.getExternalFilesDirs(null)[0],"Packages");
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
                requestCameraPermission();
                if(ContextCompat.checkSelfPermission(Dashboard.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("activity", "Launched Camera Calibration Activity");
                    Intent intent = new Intent(Dashboard.this, CameraCalibrationActivity.class);
                    startActivity(intent);
                } else {
                    Log.i("error","Permission Denied, Could not launch activity");
                }
            }
        });

        renderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
                if(ContextCompat.checkSelfPermission(Dashboard.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("activity", "Launched View Markers Activity");
                    MarkerPackageManager packageManager = MarkerPackageManager.getInstance();
                    if(packageManager.getActive()!=null)
                        ArActivity.startWithSetup(Dashboard.this, new MarkerDetectionActivity());
                    else
                        Toast.makeText(Dashboard.this,"No package is currently active!",Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("error","Permission Denied. Could not launch activity");
                }
            }
        });

        viewPackagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, PackageManagerActivity.class);
                startActivity(intent);
            }
        });

        scanQRCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
                if(ContextCompat.checkSelfPermission(Dashboard.this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED) {
                    Log.i("activity", "Launched View Packages Activity");
                    Intent intent = new Intent(Dashboard.this, CodeScannerActivity.class);
                    startActivity(intent);
                } else {
                    Log.i("error","Permission Denied. Could not launch activity");
                }
            }
        });
    }

    public void requestCameraPermission() {
        if(ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    Dashboard.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    REQUEST_CAMERA
            );
        }
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack (true);
    }
}