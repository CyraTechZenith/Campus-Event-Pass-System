package com.example.campuseventpasssystem.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Student;
import com.example.campuseventpasssystem.ui.student.StudentDashboardActivity;
import com.example.campuseventpasssystem.utils.SessionManager;

public class StudentLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_login);

        EditText etRollNumber = findViewById(R.id.etRollNumber);

        EditText etPassword = findViewById(R.id.etPassword);

        CheckBox cbShowPassword = findViewById(R.id.cbShowPassword);

        Button btnLogin = findViewById(R.id.btnLogin);

        Button btnSignUp = findViewById(R.id.btnSignUp);

        // ==========================
        // Show Password
        // ==========================

        cbShowPassword.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {

                        etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    } else {

                        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }

                    etPassword.setSelection(etPassword.getText().length());

                });

        // ==========================
        // Sign Up
        // ==========================

        btnSignUp.setOnClickListener(v -> {

            Intent intent = new Intent(StudentLoginActivity.this, StudentSignUpActivity.class);

            startActivity(intent);

        });

        // ==========================
        // Login
        // ==========================

        btnLogin.setOnClickListener(v -> {

            String rollNumber = etRollNumber.getText().toString().trim();

            String password = etPassword.getText().toString().trim();

            if (rollNumber.isEmpty() || password.isEmpty()) {

                Toast.makeText(this, R.string.enter_roll_number_and_password, Toast.LENGTH_SHORT).show();

                return;

            }

            Student student = DatabaseClient.getInstance(getApplicationContext()).studentDao().loginStudent(rollNumber, password);

            if (student == null) {

                Toast.makeText(this, R.string.invalid_roll_number_or_password, Toast.LENGTH_SHORT).show();

                return;

            }

            SessionManager.login(this, rollNumber);

            Toast.makeText(this, R.string.login_successful, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(StudentLoginActivity.this, StudentDashboardActivity.class);

            startActivity(intent);

            finish();

        });
    }

}