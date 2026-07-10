package com.example.campuseventpasssystem.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;

import com.example.campuseventpasssystem.database.entities.Admin;

@Dao
public interface AdminDao {
    @Insert
    void insertAdmin(Admin admin);

    @Query("SELECT * FROM admins WHERE email = :email AND password = :password")
    Admin loginAdmin(String email, String password);

    @Query("SELECT * FROM admins WHERE adminId = :adminId")
    Admin getAdminById(String adminId);

    @Query("SELECT * FROM admins WHERE email = :email")
    Admin getAdminByEmail(String email);

    @Query("UPDATE admins SET password = :newPassword WHERE adminId = :adminId")
    void updatePassword(String adminId, String newPassword);

    @Query("UPDATE admins SET password = :newPassword WHERE email = :email")
    void updatePasswordByEmail(String email, String newPassword);
}