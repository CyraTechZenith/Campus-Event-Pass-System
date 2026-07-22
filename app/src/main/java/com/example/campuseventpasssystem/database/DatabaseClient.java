package com.example.campuseventpasssystem.database;

import android.content.Context;
import com.example.campuseventpasssystem.utils.Constants;
import androidx.room.Room;

public class DatabaseClient {
    private static AppDatabase database;
    private DatabaseClient() {
        // Prevent instantiation
    }

    public static AppDatabase getInstance(Context context) {

        if (database == null) {
            database = Room.databaseBuilder(context, AppDatabase.class, "CampusEventDatabase").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }

        return database;
    }
}