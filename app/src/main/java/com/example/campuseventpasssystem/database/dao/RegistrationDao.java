package com.example.campuseventpasssystem.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.example.campuseventpasssystem.database.entities.Registration;

@Dao
public interface RegistrationDao {

    @Insert
    void insertRegistration(Registration registration);

    @Query("SELECT * FROM registrations WHERE studentRollNumber = :rollNumber")
    List<Registration> getRegistrationsByStudent(String rollNumber);

    @Query("SELECT * FROM registrations WHERE registrationId = :registrationId")
    Registration getRegistrationById(int registrationId);

    @Query("SELECT * FROM registrations WHERE qrData = :qrData")
    Registration getRegistrationByQrData(String qrData);

    @Query("SELECT COUNT(*) FROM registrations WHERE studentRollNumber = :rollNumber AND eventId = :eventId")
    int getRegistrationCount(String rollNumber, int eventId);

    @Query("SELECT COUNT(*) FROM registrations WHERE eventId = :eventId")
    int getParticipantCount(int eventId);

    @Query("UPDATE registrations SET passStatus = :status WHERE registrationId = :registrationId")
    void updatePassStatus(int registrationId, String status);

    @Query("SELECT * FROM registrations WHERE eventId = :eventId")
    List<Registration> getRegistrationsByEvent(int eventId);

    @Query("SELECT * FROM registrations")
    List<Registration> getAllRegistrations();

    @Query("SELECT * FROM registrations ORDER BY registrationId DESC LIMIT 1")
    Registration getLatestRegistration();
    @Query("SELECT * FROM registrations WHERE studentRollNumber = :rollNumber AND eventId = :eventId LIMIT 1")
    Registration getRegistration(String rollNumber, int eventId);
}