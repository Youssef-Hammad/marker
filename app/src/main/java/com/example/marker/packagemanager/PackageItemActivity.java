package com.example.marker.packagemanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.marker.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

public class PackageItemActivity extends AppCompatActivity {

    private TextView pkgTextView;
    private Button activeBtn;
    private Button deleteBtn;

    private MarkerPackageManager pkgManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_item);

        pkgManager = MarkerPackageManager.getInstance();

        String pkgName = getIntent().getStringExtra("EXTRA_PACKAGE_NAME");
        Log.i("Pkg", "text in 2nd activity: "+pkgName);
        pkgTextView = (TextView) findViewById(R.id.itemPkgName);
        activeBtn = (Button) findViewById(R.id.setActiveBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);

        pkgTextView.setText("Package Name: "+pkgName);
        if(pkgManager.getActive()!=null) {
            Log.i("viewActive","Active Package: "+pkgManager.getActive().getPackageName());
        }

        activeBtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {
                    pkgManager.setActive(PackageItemActivity.this,pkgName);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Log.i("Pkg",pkgManager.getActive());
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                pkgManager.deletePackage(PackageItemActivity.this,pkgName);
                Intent intent = new Intent(PackageItemActivity.this,PackageManagerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Log.i("Pkg", "Pkg Name in 2nd activity: "+pkgName);
    }
}