package com.example.marker.packagemanager;

import android.content.Context;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PackageExtractor {
    /**
     * Gets the path to the cache directory and pass it to deleteDir
     * @param context
     */
    public static void deleteCache(Context context) {
        try {
            File dir = context.getExternalCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }


    /**
     * Recursively deletes all the content of a given directory
     * @param dir
     * @return true if the deletion was successful
     */
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    /**
     * Extracts ONLY the 3d models of the folder to the cache dir
     * @param zipPath: Path of the zip file to extract
     * @param destPath: Path to extract the content of the zip file to
     */
    public void extractPackage(String zipPath, String destPath) throws IOException {
        File destDir = new File(destPath);
        if(!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream((zipPath)));
        ZipEntry entry = zipIn.getNextEntry();
        while(entry != null) {
            String filePath = destPath+File.separator+entry.getName();
            //filePath = filePath.replaceAll(" ","_");
            if(!entry.isDirectory()) {
                Log.i("extract","Package Name: "+entry.getName());
                Log.i("extract",filePath);
                String[] temp = entry.getName().split("\\.");
                if(validExtension(temp[temp.length-1].toLowerCase())) {
                    extractFile(zipIn,filePath);
                }
                //extractFile(zipIn,filePath);
            } else {
                File dir = new File(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();;
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    /**
     * Extracts a single file given the path to the file
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    public void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        final int BUFFER_SIZE = (int) 1e8;
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    /**
     * Checks if extension is one of the accepted extensions
     * @param extension
     * @return true if the extension is among the accepted extensions
     */
    public Boolean validExtension(String extension) {
        ArrayList<String> acceptedExtensions = new ArrayList<>();
        acceptedExtensions.add("obj");
        acceptedExtensions.add("mtl");
//        acceptedExtensions.add("md2");
//        acceptedExtensions.add("g3d");
//        acceptedExtensions.add("g3dt");

        //For testing purposes
        acceptedExtensions.add("png");
        acceptedExtensions.add("jpg");

        Log.i("extract","Extension: "+extension);

        for(int i = 0; i < acceptedExtensions.size(); i++) {
            if(extension.equals(acceptedExtensions.get(i)))
                return true;
        }
        return false;
    }
}
