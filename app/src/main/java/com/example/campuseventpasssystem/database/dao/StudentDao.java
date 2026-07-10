package com.example.campuseventpasssystem.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.campuseventpasssystem.database.entities.Student;

@Dao
public interface StudentDao {

    @Insert
    void insertStudent(Student student);

    @Update
    void updateStudent(Student student);

    @Query("SELECT * FROM students WHERE rollNumber = :rollNumber")
    Student getStudentByRollNumber(String rollNumber);

    @Query("SELECT * FROM students WHERE rollNumber = :rollNumber AND password = :password")
    Student loginStudent(String rollNumber, String password);

    @Query("UPDATE students SET password = :newPassword WHERE rollNumber = :rollNumber")
    void updatePassword(String rollNumber, String newPassword);

    @Query("UPDATE students SET profileImageUri = :imageUri WHERE rollNumber = :rollNumber")
    void updateProfileImage(String rollNumber, String imageUri);

    @Query("UPDATE students SET name = :name WHERE rollNumber = :rollNumber")
    void updateStudentName(String rollNumber, String name);
}