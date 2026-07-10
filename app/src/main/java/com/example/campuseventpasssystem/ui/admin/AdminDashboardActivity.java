package com.example.campuseventpasssystem.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.ui.adapter.ManageEventsAdapter;
import com.example.campuseventpasssystem.ui.event.CreateEventActivity;
import com.example.campuseventpasssystem.ui.qr.ScanPassActivity;

import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    private Button btnCreateEvent;
    private Button btnScanQr;
    private ImageButton btnSettings;
    private RecyclerView rvActiveEvents;
    private LinearLayout layoutNoEvents;
    private TextView tvViewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        btnScanQr = findViewById(R.id.btnScanQr);

        btnSettings = findViewById(R.id.btnSettings);

        rvActiveEvents = findViewById(R.id.rvActiveEvents);

        layoutNoEvents = findViewById(R.id.layoutNoEvents);

        tvViewAll = findViewById(R.id.tvViewAll);

        rvActiveEvents.setLayoutManager(new LinearLayoutManager(this));

        // ==========================
        // Create Event
        // ==========================

        btnCreateEvent.setOnClickListener(v -> {

            Intent intent = new Intent(AdminDashboardActivity.this, CreateEventActivity.class);

            startActivity(intent);

        });


        // ==========================
        // Scan QR
        // ==========================

        btnScanQr.setOnClickListener(v -> {

            Intent intent = new Intent(AdminDashboardActivity.this, ScanPassActivity.class);

            startActivity(intent);

        });


        // ==========================
        // Settings
        // ==========================

        btnSettings.setOnClickListener(v -> {

            Intent intent = new Intent(AdminDashboardActivity.this, AdminSettingsActivity.class);

            startActivity(intent);

        });


        // ==========================
        // View All Events
        // ==========================

        tvViewAll.setOnClickListener(v -> {

            Intent intent = new Intent(AdminDashboardActivity.this, ManageEventsActivity.class);

            startActivity(intent);

        });


        loadActiveEvents();

    }


    @Override
    protected void onResume() {

        super.onResume();

        loadActiveEvents();

    }


    private void loadActiveEvents() {

        List<Event> activeEvents = DatabaseClient.getInstance(getApplicationContext()).eventDao().getActiveEvents();

        if (activeEvents.isEmpty()) {

            layoutNoEvents.setVisibility(View.VISIBLE);

            rvActiveEvents.setVisibility(View.GONE);

            tvViewAll.setVisibility(View.GONE);

        }
        else {

            layoutNoEvents.setVisibility(View.GONE);

            rvActiveEvents.setVisibility(View.VISIBLE);

            tvViewAll.setVisibility(View.VISIBLE);

            ManageEventsAdapter adapter = new ManageEventsAdapter(this, activeEvents);

            rvActiveEvents.setAdapter(adapter);

        }

    }

}