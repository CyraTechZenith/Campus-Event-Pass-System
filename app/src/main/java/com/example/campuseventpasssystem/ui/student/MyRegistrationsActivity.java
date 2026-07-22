package com.example.campuseventpasssystem.ui.student;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.database.entities.Registration;
import com.example.campuseventpasssystem.database.model.RegistrationItem;
import com.example.campuseventpasssystem.ui.adapter.MyRegistrationsAdapter;
import com.example.campuseventpasssystem.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MyRegistrationsActivity extends AppCompatActivity {
    private RecyclerView rvRegistrations;
    private LinearLayout layoutEmpty;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_registrations);

        rvRegistrations = findViewById(R.id.rvRegistrations);

        layoutEmpty = findViewById(R.id.layoutEmpty);

        btnBack = findViewById(R.id.btnBack);

        rvRegistrations.setLayoutManager(new LinearLayoutManager(this));

        // ==========================
        // Back
        // ==========================

        btnBack.setOnClickListener(v -> finish());

        // ==========================
        // Load Registrations
        // ==========================

        loadRegistrations();

    }


    // ==========================
    // Refresh Registrations
    // ==========================

    @Override
    protected void onResume() {
        super.onResume();
        loadRegistrations();
    }

    // ==========================
    // Load Registrations
    // ==========================

    private void loadRegistrations() {

        String rollNumber = SessionManager.getCurrentStudentRollNumber(this);

        List<Registration> registrations = DatabaseClient.getInstance(getApplicationContext()).registrationDao().getRegistrationsByStudent(rollNumber);

        List<RegistrationItem> registrationItems = new ArrayList<>();

        for (Registration registration : registrations) {

            Event event = DatabaseClient.getInstance(getApplicationContext()).eventDao().getEventById(registration.getEventId());

            if (event != null) {

                registrationItems.add(new RegistrationItem(registration, event));

            }

        }

        if (registrationItems.isEmpty()) {

            layoutEmpty.setVisibility(View.VISIBLE);

            rvRegistrations.setVisibility(View.GONE);

        }

        else {

            layoutEmpty.setVisibility(View.GONE);

            rvRegistrations.setVisibility(View.VISIBLE);

            MyRegistrationsAdapter adapter = new MyRegistrationsAdapter(this, registrationItems);

            rvRegistrations.setAdapter(adapter);

        }

    }

}