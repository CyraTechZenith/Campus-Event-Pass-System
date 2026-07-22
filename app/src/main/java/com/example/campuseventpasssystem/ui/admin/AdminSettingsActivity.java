package com.example.campuseventpasssystem.ui.admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Admin;
import com.example.campuseventpasssystem.ui.auth.LoginActivity;
import com.example.campuseventpasssystem.utils.SessionManager;

public class AdminSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_settings);

        TextView tvAdminName = findViewById(R.id.tvAdminName);

        TextView tvInitials = findViewById(R.id.tvInitials);

        LinearLayout btnChangePassword = findViewById(R.id.btnChangePassword);

        Button btnLogout = findViewById(R.id.btnLogout);

        String email = SessionManager.getCurrentAdminEmail(this);

        // ==========================
        // Load Admin
        // ==========================

        Admin admin = DatabaseClient.getInstance(getApplicationContext()).adminDao().getAdminByEmail(email);

        if (admin != null) {

            tvAdminName.setText(admin.getAdminName());

            String initials = "";

            for (String word : admin.getAdminName().split(" ")) {
                initials += word.substring(0, 1).toUpperCase();
            }

            tvInitials.setText(initials);

        }

        // ==========================
        // Change Password
        // ==========================

        btnChangePassword.setOnClickListener(v -> {

            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_change_password, null);

            EditText etCurrentPassword = dialogView.findViewById(R.id.etCurrentPassword);

            EditText etNewPassword = dialogView.findViewById(R.id.etNewPassword);

            EditText etConfirmPassword = dialogView.findViewById(R.id.etConfirmPassword);

            new AlertDialog.Builder(this).setTitle(R.string.change_password).setView(dialogView).setPositiveButton(R.string.update, (dialog, which) -> {

                        String currentPassword = etCurrentPassword.getText().toString().trim();

                        String newPassword = etNewPassword.getText().toString().trim();

                        String confirmPassword = etConfirmPassword.getText().toString().trim();

                        Admin currentAdmin = DatabaseClient.getInstance(getApplicationContext()).adminDao().getAdminByEmail(email);

                        if (!currentAdmin.getPassword().equals(currentPassword)) {

                            Toast.makeText(this, R.string.current_password_incorrect, Toast.LENGTH_SHORT).show();

                            return;
                        }

                        if (newPassword.equals(currentPassword)) {

                            Toast.makeText(this, R.string.password_different_requirement, Toast.LENGTH_SHORT).show();

                            return;
                        }

                        if (newPassword.length() < 8) {

                            Toast.makeText(this, R.string.password_requirements, Toast.LENGTH_SHORT).show();

                            return;
                        }

                        if (!newPassword.equals(confirmPassword)) {

                            Toast.makeText(this, R.string.password_mismatch, Toast.LENGTH_SHORT).show();

                            return;
                        }

                        DatabaseClient.getInstance(getApplicationContext()).adminDao().updatePasswordByEmail(email, newPassword);

                        Toast.makeText(this, R.string.password_updated, Toast.LENGTH_SHORT).show();

                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();

        });

        // ==========================
        // Logout
        // ==========================

        btnLogout.setOnClickListener(v -> {

            new AlertDialog.Builder(this).setTitle(R.string.logout).setMessage(R.string.logout_confirmation).setPositiveButton(R.string.logout, (dialog, which) -> {

                        SessionManager.logout(this);

                        Intent intent = new Intent(AdminSettingsActivity.this, LoginActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);

                        finish();

                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();

        });
    }

}