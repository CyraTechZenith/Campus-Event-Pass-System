package com.example.campuseventpasssystem.utils;

public class Constants {

    private Constants() {
        // Prevent instantiation
    }

    // User Roles
    public static final String ADMIN = "ADMIN";
    public static final String STUDENT = "STUDENT";

    // Event Status
    public static final String ACTIVE = "ACTIVE";
    public static final String COMPLETED = "COMPLETED";
    public static final String CANCELLED = "CANCELLED";

    // Pass Status
    public static final String VALID = "VALID";
    public static final String EXPIRED = "EXPIRED";
    public static final String USED = "USED";

    // Entry Verification Status
    public static final String VERIFIED = "VERIFIED";
    public static final String ALREADY_CHECKED_IN = "ALREADY_CHECKED_IN";
    public static final String INVALID_QR = "INVALID_QR";
    public static final String PASS_NOT_FOUND = "PASS_NOT_FOUND";

    // Database
    public static final String DATABASE_NAME = "campus_event_pass_db";
}