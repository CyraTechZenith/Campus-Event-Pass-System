package com.example.campuseventpasssystem.ui.auth;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.ui.admin.AdminDashboardActivity;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Admin;
import com.example.campuseventpasssystem.utils.SessionManager;

public class AdminLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login);

        EditText etEmail = findViewById(R.id.etEmail);

        EditText etPassword = findViewById(R.id.etPassword);

        CheckBox cbShowPassword = findViewById(R.id.cbShowPassword);

        Button btnLogin = findViewById(R.id.btnLogin);

        cbShowPassword.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {

                        etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    } else {

                        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }

                    etPassword.setSelection(etPassword.getText().length());

                });

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();

            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(this, R.string.enter_email_password, Toast.LENGTH_SHORT).show();

                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                Toast.makeText(this, R.string.invalid_email_format, Toast.LENGTH_SHORT).show();

                return;
            }

            Admin admin = DatabaseClient.getInstance(getApplicationContext()).adminDao().loginAdmin(email, password);

            if (admin == null) {

                Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();

                return;
            }

            SessionManager.loginAdmin(this, admin.getEmail());

            Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);

            startActivity(intent);

            finish();
        });
    }
}