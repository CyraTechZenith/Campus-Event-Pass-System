package com.example.campuseventpasssystem.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.ui.admin.ParticipantsListActivity;
import com.example.campuseventpasssystem.ui.qr.ScanPassActivity;

public class AdminEventDetailsActivity extends AppCompatActivity {
    private ImageView btnBack;
    private TextView tvEventName;
    private TextView tvEventDate;
    private TextView tvEventVenue;
    private TextView tvRegistered;
    private Button btnViewParticipants;
    private Button btnStartScanning;
    private Button btnEditEvent;
    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_event_details);

        // ==========================
        // Initialize Views
        // ==========================

        btnBack = findViewById(R.id.btnBack);

        tvEventName = findViewById(R.id.tvEventName);

        tvEventDate = findViewById(R.id.tvEventDate);

        tvEventVenue = findViewById(R.id.tvEventVenue);

        tvRegistered = findViewById(R.id.tvRegistered);

        btnViewParticipants = findViewById(R.id.btnViewParticipants);

        btnStartScanning = findViewById(R.id.btnStartScanning);

        btnEditEvent = findViewById(R.id.btnEditEvent);

        eventId = getIntent().getIntExtra("EVENT_ID", -1);

        // ==========================
        // Back
        // ==========================

        btnBack.setOnClickListener(v -> finish());

        // ==========================
        // Load Event
        // ==========================

        Event event = DatabaseClient.getInstance(getApplicationContext()) .eventDao() .getEventById(eventId);

        if (event != null) {

            tvEventName.setText(event.getEventName());

            tvEventDate.setText(event.getEventDate() + " | " + event.getEventTime());

            tvEventVenue.setText(event.getEventVenue());

            int registered = DatabaseClient.getInstance(getApplicationContext()).registrationDao().getParticipantCount(eventId);

            int limit = event.getParticipationLimit();

            if (limit > 0) {

                tvRegistered.setText(registered + " / " + limit);

            } else {

                tvRegistered.setText(String.valueOf(registered));

            }

        }

        // ==========================
        // View Participants
        // ==========================

        btnViewParticipants.setOnClickListener(v -> {

            Intent intent = new Intent(AdminEventDetailsActivity.this, ParticipantsListActivity.class);

            intent.putExtra("EVENT_ID", eventId);

            startActivity(intent);

        });

        // ==========================
        // Start Scanning
        // ==========================

        btnStartScanning.setOnClickListener(v -> {

            Intent intent = new Intent(AdminEventDetailsActivity.this, ScanPassActivity.class);

            intent.putExtra("EVENT_ID", eventId);

            startActivity(intent);

        });

        // ==========================
        // Edit Event
        // ==========================

        btnEditEvent.setOnClickListener(v -> {

            Intent intent = new Intent(AdminEventDetailsActivity.this, EditEventActivity.class);

            intent.putExtra("EVENT_ID", eventId);

            startActivity(intent);

        });
    }

}