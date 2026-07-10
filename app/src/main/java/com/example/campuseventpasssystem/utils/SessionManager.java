package com.example.campuseventpasssystem.utils;

import android.content.Context;

public class SessionManager {

    private static final String PREF_NAME = "CampusEventSession";
    private static final String KEY_STUDENT_ROLL = "student_roll";
    private static final String KEY_ADMIN_EMAIL = "admin_email";


    // ==========================
    // Student
    // ==========================

    public static void login(Context context,
                             String rollNumber) {

        context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                )
                .edit()
                .putString(
                        KEY_STUDENT_ROLL,
                        rollNumber
                )
                .apply();

    }



    public static String getCurrentStudentRollNumber(Context context) {

        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_STUDENT_ROLL, null);

    }



    // ==========================
    // Admin
    // ==========================

    public static void loginAdmin(Context context, String email) {

        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putString(KEY_ADMIN_EMAIL, email).apply();

    }


    public static String getCurrentAdminEmail(Context context) {

        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_ADMIN_EMAIL, null);

    }



    // ==========================
    // Logout
    // ==========================

    public static void logout(Context context) {

        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) .edit().clear().apply();

    }

}