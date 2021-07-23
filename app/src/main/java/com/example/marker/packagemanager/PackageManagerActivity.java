package com.example.marker.packagemanager;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.marker.Dashboard;
import com.example.marker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PackageManagerActivity extends Activity {

    private String path;
    private MarkerPackageManager pkgManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_package_manager);

        ListView mListView =(ListView) findViewById(R.id.listView);
        TextView noPkgFound = (TextView) findViewById(R.id.noPkgFound);

        path=this.getExternalFilesDir(null).getPath();
        pkgManager = MarkerPackageManager.getInstance();

//        String url = "https://dd45575cb9c7.ngrok.io/media/package/packages/mobileTest.zip";
//        String pkgName = "mobileTest.zip";
//        pkgManager.downloadPackage(PackageManagerActivity.this,url,pkgName);

        //pkgManager.setActive(PackageManagerActivity.this,"TestPackageName");

        setTitle("Packages");

        ArrayList<String> packages = pkgManager.getPackages(PackageManagerActivity.this);

        if(packages.size()>0) {
            noPkgFound.setVisibility(View.INVISIBLE);
        }

        Log.i("packages", String.valueOf(packages.size()));
        for(int i = 0; i < packages.size(); i++) {
            Log.i("packages",packages.get(i));
        }

        CustomAdapter adapter = new CustomAdapter(this,R.layout.activity_package_list, packages);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PackageManagerActivity.this, PackageItemActivity.class);
                Log.i("Pkg","Position: "+position);
                Log.i("Pkg", (String) parent.getItemAtPosition(position));
                String pkgName = (String) parent.getItemAtPosition(position);
                intent.putExtra("EXTRA_PACKAGE_NAME",pkgName);
                Log.i("Pkg","EXTRA_PACKAGE_NAME in activity 1: "+intent.getStringExtra("EXTRA_PACKAGE_NAME"));
                startActivity(intent);
            }
        });




    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(PackageManagerActivity.this, Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}
