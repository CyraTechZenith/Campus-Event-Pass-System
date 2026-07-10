package com.example.campuseventpasssystem.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "admins")
public class Admin {
    @PrimaryKey
    @NonNull
    private String adminId;
    @NonNull
    private String adminName;
    @NonNull
    private String email;
    @NonNull
    private String password;

    public Admin(@NonNull String adminId,
                 @NonNull String adminName,
                 @NonNull String email,
                 @NonNull String password) {

        this.adminId = adminId;
        this.adminName = adminName;
        this.email = email;
        this.password = password;
    }

    @NonNull
    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(@NonNull String adminId) {
        this.adminId = adminId;
    }

    @NonNull
    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(@NonNull String adminName) {
        this.adminName = adminName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }
}