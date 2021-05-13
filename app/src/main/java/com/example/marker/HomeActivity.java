package com.example.marker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.marker.cameracalibration.CameraCalibrationActivity;
import com.example.marker.markerDetection.MarkerDetectionActivity;

public class HomeActivity extends AppCompatActivity {

    private Button calibrateCameraBtn;
    private Button viewMarkersBtn;
    private Button scanQRCodeBtn;
    private Button viewPackagesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // initializing and setting the buttons button
        calibrateCameraBtn = (Button) findViewById(R.id.calibrateCameraBtn);
        viewMarkersBtn = (Button) findViewById(R.id.viewMarkersBtn);
        scanQRCodeBtn = (Button) findViewById(R.id.scanQRBtn);
        viewPackagesBtn = (Button) findViewById(R.id.viewPackagesBtn);

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
     * Launches the Camera Calibration Activity with the current context.
     */
    public void launchCalibrateCameraActivity() {
        Log.i("activity", "Launched Camera Calibration Activity");
        Intent intent = new Intent(this, CameraCalibrationActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the View Markers Activity with the current context.
     */
    public void launchViewMarkersActivity() {
        Log.i("activity", "Launched View Markers Activity");
        Intent intent = new Intent(this, MarkerDetectionActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the QR Code scan Activity with the current context.
     */
    public void launchQRCodeActivity() {
        Log.i("activity", "Launched QR Code Scanner Activity");
    }

    /**
     * Launches the View Packages Activity with the current context.
     */
    public void launchViewPackagesActivity() {
        Log.i("activity", "Launched View Packages Activity");
    }
}