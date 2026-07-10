package com.example.campuseventpasssystem.ui.auth;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.campuseventpasssystem.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Button btnStudent = findViewById(R.id.btnStudent);

        Button btnAdmin = findViewById(R.id.btnAdmin);

        btnStudent.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, StudentLoginActivity.class);

            startActivity(intent);
        });

        btnAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);

            startActivity(intent);
        });


    }
}