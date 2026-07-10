package com.example.campuseventpasssystem.ui.admin;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Registration;
import com.example.campuseventpasssystem.database.entities.Student;
import com.example.campuseventpasssystem.ui.adapter.ParticipantAdapter;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsListActivity extends AppCompatActivity {
    private RecyclerView rvParticipants;
    private EditText etSearch;
    private ImageView btnBack;
    private TextView tvParticipantCount;
    private ParticipantAdapter adapter;
    private List<Registration> participantList;
    private List<Registration> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_participants_list);

        btnBack = findViewById(R.id.btnBack);

        tvParticipantCount = findViewById(R.id.tvParticipantCount);

        etSearch = findViewById(R.id.etSearch);

        rvParticipants = findViewById(R.id.rvParticipants);

        rvParticipants.setLayoutManager(new LinearLayoutManager(this));

        int eventId = getIntent().getIntExtra("EVENT_ID", -1);

        participantList = DatabaseClient.getInstance(getApplicationContext()).registrationDao().getRegistrationsByEvent(eventId);

        tvParticipantCount.setText(getString(
                        R.string.participants_count,
                        participantList.size()
                ));

        filteredList = new ArrayList<>(participantList);

        adapter = new ParticipantAdapter(this, filteredList);

        rvParticipants.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                filterParticipants(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });
    }

    private void filterParticipants(String query) {

        filteredList.clear();

        if (query == null || query.trim().isEmpty()) {

            filteredList.addAll(participantList);

        } else {

            String search = query.toLowerCase().trim();

            for (Registration registration : participantList) {

                boolean matchesRoll = registration.getStudentRollNumber().toLowerCase().contains(search);

                boolean matchesName = false;

                Student student = DatabaseClient.getInstance(getApplicationContext()).studentDao().getStudentByRollNumber(registration.getStudentRollNumber());

                if (student != null) {

                    matchesName = student.getName().toLowerCase().contains(search);

                }

                if (matchesRoll || matchesName) {

                    filteredList.add(registration);

                }

            }

        }

        tvParticipantCount.setText(getString(
                        R.string.participants_count,
                        filteredList.size()
                ));

        adapter.notifyDataSetChanged();

    }

}