package com.example.campuseventpasssystem.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    private DateTimeUtils() {
        // Prevent instantiation
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(new Date());
    }

    public static String getCurrentDateTime() {
        return new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault()).format(new Date());
    }
}