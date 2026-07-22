package com.example.campuseventpasssystem.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

public class QrCodeUtils {

    private QrCodeUtils() {
        // Prevent object creation
    }

    public static Bitmap generateQr(String qrData) {

        try {
            QRCodeWriter writer = new QRCodeWriter();

            BitMatrix matrix = writer.encode(qrData, BarcodeFormat.QR_CODE, 700, 700);

            Bitmap bitmap = Bitmap.createBitmap(700, 700, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < 700; x++) {

                for (int y = 0; y < 700; y++) {

                    bitmap.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);

                }

            }

            return bitmap;

        }

        catch (Exception e) {

            e.printStackTrace();

            return null;

        }

    }

}