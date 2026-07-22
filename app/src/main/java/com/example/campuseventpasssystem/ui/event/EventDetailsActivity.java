package com.example.campuseventpasssystem.ui.event;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Event;

public class EventDetailsActivity extends AppCompatActivity {
    private ImageView imgBanner;
    private ImageView btnBack;
    private TextView tvEventName;
    private TextView tvEventDate;
    private TextView tvEventVenue;
    private TextView tvEventDescription;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_details);

        // ==========================
        // Initialize Views
        // ==========================

        btnBack = findViewById(R.id.btnBack);

        imgBanner = findViewById(R.id.imgBanner);

        tvEventName = findViewById(R.id.tvEventName);

        tvEventDate = findViewById(R.id.tvEventDate);

        tvEventVenue = findViewById(R.id.tvEventVenue);

        tvEventDescription = findViewById(R.id.tvEventDescription);

        btnRegister = findViewById(R.id.btnRegister);

        int eventId = getIntent().getIntExtra("EVENT_ID", 1);

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

            String dateTime = event.getEventDate() + " | " + event.getEventTime();

            tvEventDate.setText(dateTime);

            tvEventVenue.setText(event.getEventVenue());

            tvEventDescription.setText(event.getEventDescription());

            String bannerUri = event.getEventBannerUri();

            if (bannerUri != null && !bannerUri.trim().isEmpty()) {

                try {
                    imgBanner.setImageURI(Uri.parse(bannerUri));
                } catch (Exception e) {
                    e.printStackTrace();
                    imgBanner.setImageResource(R.drawable.default_event_banner);
                }

            } else {

                imgBanner.setImageResource(R.drawable.default_event_banner);

            }

        }


        // ==========================
        // Register Button
        // ==========================

        btnRegister.setOnClickListener(v -> {

            Intent intent = new Intent(EventDetailsActivity.this, RegisterEventActivity.class);

            intent.putExtra("EVENT_ID", eventId);

            startActivity(intent);

        });
    }

}