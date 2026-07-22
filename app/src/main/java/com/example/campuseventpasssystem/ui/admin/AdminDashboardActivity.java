package com.example.campuseventpasssystem.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.ui.adapter.ManageEventsAdapter;
import com.example.campuseventpasssystem.ui.event.CreateEventActivity;

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

        // ==========================
        // Initialize Views
        // ==========================

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

            Toast.makeText(this, R.string.select_an_event_to_scan_qr, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AdminDashboardActivity.this, ManageEventsActivity.class);

            intent.putExtra("SCAN_MODE", true);

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

        // ==========================
        // Load Active Events
        // ==========================

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