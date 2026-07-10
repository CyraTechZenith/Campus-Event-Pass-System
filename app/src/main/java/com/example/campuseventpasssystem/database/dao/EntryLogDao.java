package com.example.campuseventpasssystem.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.example.campuseventpasssystem.database.entities.EntryLog;

@Dao
public interface EntryLogDao {

    @Insert
    void insertEntryLog(EntryLog entryLog);

    @Query("SELECT * FROM entry_logs")
    List<EntryLog> getAllEntryLogs();

    @Query("SELECT * FROM entry_logs WHERE eventId = :eventId")
    List<EntryLog> getEntryLogsByEvent(int eventId);

    @Query("SELECT * FROM entry_logs WHERE studentRollNumber = :rollNumber")
    List<EntryLog> getEntryLogsByStudent(String rollNumber);

    @Query("SELECT COUNT(*) FROM entry_logs WHERE studentRollNumber = :rollNumber AND eventId = :eventId")
    int getEntryCount(String rollNumber, int eventId);
}