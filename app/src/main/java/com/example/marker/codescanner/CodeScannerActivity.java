package com.example.marker.codescanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.example.marker.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import static android.os.SystemClock.sleep;

public class CodeScannerActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private PreviewView mCameraView;
    private ListenableFuture<ProcessCameraProvider> mCameraProvider;
    private Preview mPreview;
    private CameraSelector mCamSelector;

    private ImageAnalysis mImage;
    private Button mQRCodeButton;
    private String mQRCode;


    public CodeScannerActivity() {
        Log.i("CodeScannerActivity", "QR Code Scanner Activity instantiated");
    }

    /**
     * This method sets the activity's content view to the Code Scanner Activity, then
     * instantiates the PreviewView (mCameraView).
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

        mCameraView = findViewById(R.id.scan_qr_code_camera_view);
        mCameraProvider = ProcessCameraProvider.getInstance(CodeScannerActivity.this);

        buttonEventHandler();
        openCamera();
    }

    /**
     * This method sets the button to be invisible.
     * Then adds onClick listener method to redirect the activity to the QR code URL when the button is clicked.
     */
    private void buttonEventHandler(){
        mQRCodeButton = findViewById(R.id.qr_code_button);
        mQRCodeButton.setVisibility(View.INVISIBLE);

        mQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(mQRCode);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                Log.i(CodeScannerActivity.class.getSimpleName(), "QR Code Found: " + mQRCode);
            }
        });
    }

    /**
     * This method initializes the preview from the camera using a ProcessCameraProvider.
     */
    private void openCamera() {
        mCameraProvider.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = mCameraProvider.get();
                bindCameraPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Error starting the camera ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    /**
     * This method sets up and initiates the camera preview (mCameraPreview) to be displayed inside the PreviewView widget.
     * Then, it uses an instance of our custom image analyzer (FrameAnalyzer) class to override codeFound and codeNotFound methods.
     * These methods will be used to store the text we got from scanning the QR codes in the activity class then,
     * update the button with this text.
     * @param cameraProvider
     */
    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        mCameraView.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);
        mPreview = new Preview.Builder().build();
        mPreview.setSurfaceProvider(mCameraView.createSurfaceProvider());

        mCamSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        mImage = new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        mImage.setAnalyzer(ContextCompat.getMainExecutor(this), new FrameAnalyzer(new QRCodeListener() {
            @Override
            public void codeFound(String QRCode) {
                mQRCode = QRCode;
                mQRCodeButton.setText(mQRCode);
                mQRCodeButton.setVisibility(View.VISIBLE);
                Log.i("CodeScannerQRCode", "Found " + mQRCode);
                sleep(100);
            }

            @Override
            public void codeNotFound() {
                mQRCodeButton.setVisibility(View.INVISIBLE);
            }
        }));

        cameraProvider.bindToLifecycle((LifecycleOwner)this, mCamSelector, mImage, mPreview);

    }
}