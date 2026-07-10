package com.example.campuseventpasssystem.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.campuseventpasssystem.database.dao.AdminDao;
import com.example.campuseventpasssystem.database.dao.EntryLogDao;
import com.example.campuseventpasssystem.database.dao.EventDao;
import com.example.campuseventpasssystem.database.dao.RegistrationDao;
import com.example.campuseventpasssystem.database.dao.StudentDao;

import com.example.campuseventpasssystem.database.entities.Admin;
import com.example.campuseventpasssystem.database.entities.EntryLog;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.database.entities.Registration;
import com.example.campuseventpasssystem.database.entities.Student;

@Database(entities = {Student.class, Admin.class, Event.class, Registration.class, EntryLog.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    public abstract StudentDao studentDao();
    public abstract AdminDao adminDao();
    public abstract EventDao eventDao();
    public abstract RegistrationDao registrationDao();
    public abstract EntryLogDao entryLogDao();
}