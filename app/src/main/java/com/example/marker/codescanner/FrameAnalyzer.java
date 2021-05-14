package com.example.marker.codescanner;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import java.nio.ByteBuffer;


public class FrameAnalyzer implements ImageAnalysis.Analyzer {
    private QRCodeListener mListener;
    private ByteBuffer mBuffer;

    public FrameAnalyzer(QRCodeListener listener) {
        mListener = listener;
    }

    /**
     * This methods converts the frames (images) into a byte buffer (mBuffer).
     * Then generates BinaryBitmap that can be decoded to scan the QR codes.
     * @param image
     */

    @Override
    public void analyze(@NonNull ImageProxy image) {
        mBuffer = image.getPlanes()[0].getBuffer();
        byte[] imageData = new byte[mBuffer.capacity()];
        mBuffer.get(imageData);

        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(imageData, image.getWidth(), image.getHeight(),
                0, 0, image.getWidth(), image.getHeight(), false);

        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new QRCodeMultiReader().decode(binaryBitmap);
            mListener.codeFound(result.getText());
            Log.i("FrameAnalyzer", "QR Code Found " + result.getText());

        } catch (FormatException | ChecksumException | NotFoundException e) {
            mListener.codeNotFound();
        }

        image.close();
    }
}