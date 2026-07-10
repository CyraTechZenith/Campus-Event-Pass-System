package com.example.campuseventpasssystem.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

import java.util.List;

public class ManageEventsActivity extends AppCompatActivity {

    private RecyclerView rvEvents;
    private LinearLayout layoutEmpty;
    private Button btnCreateEvent;
    private Button btnCreateEventBottom;
    private ImageView btnBack;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_events);

        rvEvents = findViewById(R.id.rvEvents);

        layoutEmpty = findViewById(R.id.layoutEmpty);

        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        btnCreateEventBottom = findViewById(R.id.btnCreateEventBottom);

        btnBack = findViewById(R.id.btnBack);

        rvEvents.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        View.OnClickListener createListener = v -> {

            Intent intent = new Intent(ManageEventsActivity.this, CreateEventActivity.class);

            startActivity(intent);

        };

        btnCreateEvent.setOnClickListener(createListener);

        btnCreateEventBottom.setOnClickListener(createListener);

        loadEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEvents();
    }

    private void loadEvents() {

        eventList = DatabaseClient.getInstance(getApplicationContext()).eventDao().getAllEvents();

        if (eventList.isEmpty()) {

            layoutEmpty.setVisibility(View.VISIBLE);
            rvEvents.setVisibility(View.GONE);
            btnCreateEventBottom.setVisibility(View.GONE);

        }
        else {

            layoutEmpty.setVisibility(View.GONE);
            rvEvents.setVisibility(View.VISIBLE);
            btnCreateEventBottom.setVisibility(View.VISIBLE);

            ManageEventsAdapter adapter = new ManageEventsAdapter(this, eventList);

            rvEvents.setAdapter(adapter);

        }

    }

}