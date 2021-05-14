package com.example.marker.codescanner;

public interface QRCodeListener {
    void codeFound(String QR_Code);

    void codeNotFound();
}
