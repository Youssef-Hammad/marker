package com.example.marker.packagemanager;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PackageHandler {
    /**
     * This method receives a link from the Download Module
     * downloads and stores the package specified
     * @param url: DIRECT LINK to download a specific package (i.e link that starts the download when visited)
     * @param PackageName: Name of the package
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public Boolean downloadPackage(Context context, String url, String PackageName) {
        String[] fileChecker = url.split("\\.");
        if(!fileChecker[fileChecker.length-1].equals("zip")) {
            return false;
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Package Download");
        request.setDescription("Downloading Package...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,null, "Packages/"+PackageName);

        DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        return true;
    }

    /**
     * This method returns true if the passed package can be set to active
     * @param context: The activity that should be passed when calling this function
     * @param PackageName: Name of the package
     */
    //TODO: Testing
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Boolean setActive(Context context, String PackageName) throws IOException {
        PackageExtractor extractor = new PackageExtractor();
        extractor.deleteCache(context);
        File packageToSetActive = new File(context.getExternalFilesDirs(null)[0],"Packages/"+PackageName);
        //Log.i("packages","zip path: "+packageToSetActive.getPath()+"\ndestination path:"+context.getExternalCacheDir().getPath()+"/Packages/"+PackageName);
        Log.i("extract",PackageName);
        extractor.extractPackage(packageToSetActive.getPath(),context.getExternalCacheDir().getPath());
        Log.i("package",packageToSetActive.getAbsolutePath());
        return packageToSetActive.exists(); //Package exists
    }

    /**
     * This method receives a path to a specified on the local memory
     * and deletes it form memory
     * @param context is the activity that should be passed when calling this function
     * @param PackageName: Name of the package
     */
    //TODO: Testing
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Boolean deletePackage(Context context, String PackageName) {
        File packageToDelete = new File(context.getExternalFilesDirs(null)[0],"Packages/"+PackageName);
        Log.i("directory",packageToDelete.getPath());
        if(packageToDelete.exists()) {
            boolean deleted = packageToDelete.delete();

            if(deleted) {
                Log.i("directory", PackageName + " Deleted Successfully");
                return true;
            } else {
                Log.i("directory","Failed to delete "+PackageName);
            }
        }
        Log.i("directory","Package \""+PackageName+"\" does not exist");
        return false;
    }

    /**
     * This method returns an ArrayList of strings
     * each string denoting the path of each package stored on the local memory
     */
    //TODO
    public ArrayList<String> getPackages(Context context) {
        ArrayList<String> ret = new ArrayList<>();
        File packagesDirectory = new File(context.getExternalFilesDir(null)+"/Packages");
        Log.i("directory",packagesDirectory.getPath());
        String[] filesList = packagesDirectory.list();
        Log.i("packages","FilesList Size: "+filesList.length);
        if(filesList!=null) {
            for(String pkg : filesList) {
                if(!pkg.startsWith("."))
                    ret.add(pkg);
            }
        }
        Collections.sort(ret);
        return ret;
    }
}
