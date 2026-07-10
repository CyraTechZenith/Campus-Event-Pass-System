package com.example.campuseventpasssystem.utils;

public class QrUtils {

    private QrUtils() {
        // Prevent instantiation
    }

    public static String generateQrData(String rollNumber, int eventId) {

        return rollNumber + "_" + eventId + "_" + System.currentTimeMillis();
    }
}