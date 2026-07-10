package com.example.campuseventpasssystem.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "registrations")
public class Registration {
    public static final String VALID = "VALID";
    public static final String EXPIRED = "EXPIRED";
    public static final String USED = "USED";
    @PrimaryKey(autoGenerate = true)
    private int registrationId;
    @NonNull
    private String studentRollNumber;
    private int eventId;
    @NonNull
    private String email;
    private String contactNumber;
    @NonNull
    private String registrationDate;
    @NonNull
    private String passStatus;
    @NonNull
    private String qrData;

    public Registration(
            @NonNull String studentRollNumber,
            int eventId,
            @NonNull String email,
            String contactNumber,
            @NonNull String registrationDate,
            @NonNull String passStatus,
            @NonNull String qrData) {

        this.studentRollNumber = studentRollNumber;
        this.eventId = eventId;
        this.email = email;
        this.contactNumber = contactNumber;
        this.registrationDate = registrationDate;
        this.passStatus = passStatus;
        this.qrData = qrData;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
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
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @NonNull
    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(@NonNull String registrationDate) {
        this.registrationDate = registrationDate;
    }

    @NonNull
    public String getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(@NonNull String passStatus) {
        this.passStatus = passStatus;
    }

    @NonNull
    public String getQrData() {
        return qrData;
    }

    public void setQrData(@NonNull String qrData) {
        this.qrData = qrData;
    }
}