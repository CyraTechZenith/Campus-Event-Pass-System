package com.example.campuseventpasssystem.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.Editable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Student;

public class StudentSignUpActivity extends AppCompatActivity {
    private ImageView ivProfilePhoto;
    private TextView tvInitials;
    private Uri selectedImageUri;
    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if (uri == null) return;

                        selectedImageUri = uri;

                        ivProfilePhoto.setImageURI(uri);

                        ivProfilePhoto.setVisibility(View.VISIBLE);

                        tvInitials.setVisibility(View.GONE);

                        try {

                            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        } catch (Exception ignored) {

                        }


                    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_sign_up);

        EditText etRollNumber = findViewById(R.id.etRollNumber);

        EditText etName = findViewById(R.id.etName);

        EditText etDepartment = findViewById(R.id.etDepartment);

        EditText etPassword = findViewById(R.id.etPassword);

        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);

        CheckBox cbShowPassword = findViewById(R.id.cbShowPassword);

        Button btnSignUp = findViewById(R.id.btnSignUp);

        Button btnLogin = findViewById(R.id.btnLogin);

        ImageButton btnAddPhoto = findViewById(R.id.btnAddPhoto);

        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);

        tvInitials = findViewById(R.id.tvInitials);

        etName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String name = s.toString().trim();

                if (name.isEmpty()) {
                    tvInitials.setText("");
                    return;
                }

                String[] parts = name.split("\\s+");

                String initials = "";

                if (parts.length >= 1) initials += parts[0].substring(0,1).toUpperCase();

                if (parts.length >= 2) initials += parts[1].substring(0,1).toUpperCase();

                tvInitials.setText(initials);
            }

            @Override
            public void afterTextChanged(Editable s) {}

        });

        // ==========================
        // Already have account
        // ==========================

        btnLogin.setOnClickListener(v -> finish());

        btnAddPhoto.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        // ==========================
        // Show Password
        // ==========================

        cbShowPassword.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {

                        etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                        etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    } else {

                        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                        etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }

                    etPassword.setSelection(etPassword.getText().length());

                    etConfirmPassword.setSelection(etConfirmPassword.getText().length());

                });

        // ==========================
        // Sign Up
        // ==========================

        btnSignUp.setOnClickListener(v -> {

            String rollNumber = etRollNumber.getText().toString().trim();

            String name = etName.getText().toString().trim();

            String department = etDepartment.getText().toString().trim();

            String password = etPassword.getText().toString().trim();

            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (rollNumber.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {

                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();

                return;

            }

            if (!rollNumber.matches("[A-Za-z0-9]{5,20}")) {

                Toast.makeText(this, getString(R.string.invalid_roll_number), Toast.LENGTH_SHORT).show();

                return;
            }

            if (!name.matches("[a-zA-Z ]+")) {

                Toast.makeText(this, getString(R.string.invalid_name), Toast.LENGTH_SHORT).show();

                return;

            }

            if (!department.isEmpty() && department.length() < 2) {

                Toast.makeText(this, getString(R.string.invalid_department), Toast.LENGTH_SHORT).show();

                return;

            }

            if (!password.equals(confirmPassword)) {

                Toast.makeText(this, getString(R.string.password_mismatch), Toast.LENGTH_SHORT).show();

                return;

            }

            if (password.length() < 8 || !password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*") || !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {

                Toast.makeText(this, getString(R.string.password_requirements), Toast.LENGTH_LONG).show();

                return;
            }

            Student existingStudent = DatabaseClient.getInstance(getApplicationContext()).studentDao().getStudentByRollNumber(rollNumber);

            if (existingStudent != null) {

                Toast.makeText(this, getString(R.string.roll_already_registered), Toast.LENGTH_SHORT).show();

                return;

            }

            Student student = new Student(rollNumber, name, department, password, selectedImageUri == null ? "" : selectedImageUri.toString());

            DatabaseClient.getInstance(getApplicationContext()).studentDao().insertStudent(student);

            Toast.makeText(this, getString(R.string.registration_successful), Toast.LENGTH_SHORT).show();

            startActivity(new Intent(StudentSignUpActivity.this, StudentLoginActivity.class));

            finish();

        });
    }

}