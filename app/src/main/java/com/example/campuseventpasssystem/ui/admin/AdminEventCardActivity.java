package com.example.campuseventpasssystem.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.ui.event.AdminEventDetailsActivity;
import com.example.campuseventpasssystem.ui.event.EventEntryLogsActivity;

public class AdminEventCardActivity extends AppCompatActivity {
    private ImageView btnBack;
    private TextView tvEventName;
    private TextView tvEventDate;
    private TextView tvEventVenue;
    private Button btnEventDetails;
    private Button btnEntryLogs;
    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_event_card);

        // ==========================
        // Initialize Views
        // ==========================

        btnBack = findViewById(R.id.btnBack);

        tvEventName = findViewById(R.id.tvEventName);

        tvEventDate = findViewById(R.id.tvEventDate);

        tvEventVenue = findViewById(R.id.tvEventVenue);

        btnEventDetails = findViewById(R.id.btnEventDetails);

        btnEntryLogs = findViewById(R.id.btnEntryLogs);

        eventId = getIntent().getIntExtra("EVENT_ID", -1);


        // ==========================
        // Back
        // ==========================

        btnBack.setOnClickListener(v -> finish());

        // ==========================
        // Load Event
        // ==========================

        Event event = DatabaseClient.getInstance(getApplicationContext()).eventDao().getEventById(eventId);

        if (event != null) {

            tvEventName.setText(event.getEventName());

            tvEventDate.setText(event.getEventDate() + " | " + event.getEventTime());

            tvEventVenue.setText(event.getEventVenue());

        }

        // ==========================
        // Event Details
        // ==========================

        btnEventDetails.setOnClickListener(v -> {

            Intent intent = new Intent(AdminEventCardActivity.this, AdminEventDetailsActivity.class);

            intent.putExtra("EVENT_ID", eventId);

            startActivity(intent);

        });

        // ==========================
        // Entry Logs
        // ==========================

        btnEntryLogs.setOnClickListener(v -> {

            Intent intent = new Intent(AdminEventCardActivity.this, EventEntryLogsActivity.class);

            intent.putExtra("EVENT_ID", eventId);

            startActivity(intent);

        });
    }

}