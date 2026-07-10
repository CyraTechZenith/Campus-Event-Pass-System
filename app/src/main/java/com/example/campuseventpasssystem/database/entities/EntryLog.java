package com.example.campuseventpasssystem.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "entry_logs")
public class EntryLog {

    public static final String VERIFIED = "VERIFIED";
    public static final String ALREADY_CHECKED_IN = "ALREADY_CHECKED_IN";
    public static final String INVALID_QR = "INVALID_QR";
    public static final String PASS_NOT_FOUND = "PASS_NOT_FOUND";
    @PrimaryKey(autoGenerate = true)
    private int logId;
    @NonNull
    private String studentRollNumber;
    private int eventId;
    @NonNull
    private String scanTime;
    @NonNull
    private String status;

    public EntryLog(
            @NonNull String studentRollNumber,
            int eventId,
            @NonNull String scanTime,
            @NonNull String status) {

        this.studentRollNumber = studentRollNumber;
        this.eventId = eventId;
        this.scanTime = scanTime;
        this.status = status;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    @NonNull
    public String getStudentRollNumber() {
        return studentRollNumber;
    }

    public void setStudentRollNumber(@NonNull String studentRollNumber) {
        this.studentRollNumber = studentRollNumber;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    @NonNull
    public String getScanTime() {
        return scanTime;
    }

    public void setScanTime(@NonNull String scanTime) {
        this.scanTime = scanTime;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }
}