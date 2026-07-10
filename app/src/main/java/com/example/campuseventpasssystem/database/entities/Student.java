package com.example.campuseventpasssystem.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {
    private String profileImageUri;
    @PrimaryKey
    @NonNull
    private String rollNumber;
    @NonNull
    private String name;
    @NonNull
    private String department;
    @NonNull
    private String password;

    public Student(
            String rollNumber,
            String name,
            String department,
            String password,
            String profileImageUri
    ) {

        this.rollNumber = rollNumber;
        this.name = name;
        this.department = department;
        this.password = password;
        this.profileImageUri = profileImageUri;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }

    @NonNull
    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(@NonNull String rollNumber) {
        this.rollNumber = rollNumber;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDepartment() {
        return department;
    }

    public void setDepartment(@NonNull String department) {
        this.department = department;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }
}