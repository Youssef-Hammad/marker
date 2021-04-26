package com.example.marker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!OpenCVLoader.initDebug())
            Log.e("OpenCv", "Unable to load OpenCV");
        else
        {
            Log.d("OpenCv", "OpenCV loaded");
            final Handler handler = new Handler(Looper.getMainLooper());
            Intent intent = new Intent(this, HomeActivity.class);
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    startActivity(intent);
                }
            }, 1000);
        }
    }
}