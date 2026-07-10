package com.example.campuseventpasssystem;
import com.example.campuseventpasssystem.ui.auth.LoginActivity;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Admin;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btnStart = findViewById(R.id.btnStart);

        Admin admin = DatabaseClient.getInstance(getApplicationContext()).adminDao().getAdminById("ADMIN001");

        if (admin == null) {

            Admin defaultAdmin = new Admin("ADMIN001", "System Admin", "admin@college.com", "Admin@123");

            DatabaseClient.getInstance(getApplicationContext()).adminDao().insertAdmin(defaultAdmin);
        }

        btnStart.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);

        });
    }
}