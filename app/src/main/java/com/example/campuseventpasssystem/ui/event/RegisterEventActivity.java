package com.example.campuseventpasssystem.ui.event;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.database.entities.Registration;
import com.example.campuseventpasssystem.database.entities.Student;
import com.example.campuseventpasssystem.ui.student.MyRegistrationsActivity;
import com.example.campuseventpasssystem.utils.SessionManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterEventActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private ImageView imgProfile;
    private TextView tvInitials;
    private TextView tvStudentName;
    private TextView tvStudentInfo;
    private TextView tvEventName;
    private EditText etEmail;
    private EditText etContactNumber;
    private Button btnCancel;
    private Button btnRegister;
    private Student student;
    private Event event;
    private int eventId;
    private boolean alreadyRegistered = false;
    private boolean registrationClosed = false;
    private boolean eventFull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_event);

        btnBack = findViewById(R.id.btnBack);

        imgProfile = findViewById(R.id.imgProfile);

        tvInitials = findViewById(R.id.tvInitials);

        tvEventName = findViewById(R.id.tvEventName);

        tvStudentName = findViewById(R.id.tvStudentName);

        tvStudentInfo = findViewById(R.id.tvStudentInfo);

        etEmail = findViewById(R.id.etEmail);

        etContactNumber = findViewById(R.id.etContactNumber);

        btnCancel = findViewById(R.id.btnCancel);

        btnRegister = findViewById(R.id.btnRegister);

        eventId = getIntent().getIntExtra("EVENT_ID", -1);

        student = DatabaseClient.getInstance(getApplicationContext()).studentDao().getStudentByRollNumber(SessionManager.getCurrentStudentRollNumber(this));

        event = DatabaseClient.getInstance(getApplicationContext()).eventDao().getEventById(eventId);

        // ==========================
        // Load Student
        // ==========================

        loadStudent();

        loadEvent();

        // ==========================
        // Update Register Button
        // ==========================

        updateRegisterButton();

        // ==========================
        // Button Actions
        // ==========================

        btnBack.setOnClickListener(v -> finish());

        btnCancel.setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> {

            if (alreadyRegistered) {

                Toast.makeText(this, R.string.already_registered, Toast.LENGTH_SHORT).show();

                return;

            }

            if (registrationClosed) {

                Toast.makeText(this, R.string.event_over, Toast.LENGTH_SHORT).show();

                return;

            }

            if (eventFull) {

                Toast.makeText(this, R.string.event_full, Toast.LENGTH_SHORT).show();

                return;

            }

            registerStudent();

        });

    }

    // ==========================
    // Load Data
    // ==========================

    private void loadStudent() {

        if (student == null) {

            Toast.makeText(this, R.string.student_not_found, Toast.LENGTH_SHORT).show();

            finish();

            return;

        }

        tvStudentName.setText(student.getName());

        tvStudentInfo.setText(getString(
                        R.string.roll_number_format,
                        student.getRollNumber()
                ));


        if (student.getProfileImageUri() != null && !student.getProfileImageUri().isEmpty()) {

            imgProfile.setVisibility(View.VISIBLE);
            tvInitials.setVisibility(View.GONE);

            try{

                imgProfile.setImageURI(Uri.fromFile(new File(student.getProfileImageUri())));

            }catch(Exception e){

                imgProfile.setVisibility(View.GONE);
                tvInitials.setVisibility(View.VISIBLE);

            }

        } else {

            imgProfile.setVisibility(View.GONE);
            tvInitials.setVisibility(View.VISIBLE);

            String[] names = student.getName().trim().split("\\s+");

            String initials;

            if (names.length == 1) {
                initials = names[0].substring(0, Math.min(2, names[0].length())).toUpperCase();
            } else {
                initials = ("" + names[0].charAt(0) + names[1].charAt(0)).toUpperCase();
            }

            tvInitials.setText(initials);


        }

    }

    private void loadEvent() {

        if (event == null) {

            Toast.makeText(this, R.string.invalid_event, Toast.LENGTH_SHORT).show();

            finish();

            return;

        }

        tvEventName.setText(event.getEventName());

    }

    private void updateRegisterButton() {

        btnRegister.setEnabled(true);

        btnRegister.setBackgroundResource(R.drawable.gradient_button);

        btnRegister.setText(R.string.register);

        int participantCount = DatabaseClient.getInstance(getApplicationContext()).registrationDao().getParticipantCount(eventId);

        int participationLimit = event.getParticipationLimit();

        alreadyRegistered = DatabaseClient.getInstance(getApplicationContext()).registrationDao().getRegistrationCount(student.getRollNumber(), eventId) > 0;

        registrationClosed = false;

        try {
            String dateTime = event.getEventDate() + " " + event.getEventTime();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            Date eventDateTime = format.parse(dateTime);

            if (eventDateTime != null && eventDateTime.before(new Date())) {

                registrationClosed = true;

            }

        } catch (Exception e) {

            registrationClosed = true;

        }

        eventFull = participationLimit > 0 && participantCount >= participationLimit;

        if (alreadyRegistered) {

            btnRegister.setBackgroundResource(R.drawable.rounded_disabled_button);

            btnRegister.setText(R.string.already_registered);

            return;

        }

        if (registrationClosed) {

            btnRegister.setBackgroundResource(R.drawable.rounded_disabled_button);

            btnRegister.setText(R.string.event_ended);

            return;

        }

        if (eventFull) {

            btnRegister.setBackgroundResource(R.drawable.rounded_disabled_button);

            btnRegister.setText(R.string.event_full);

            return;

        }
    }

    // ==========================
    // Register Student
    // ==========================

    private void registerStudent() {

        String rollNumber = student.getRollNumber();

        String email = etEmail.getText().toString().trim();

        String contactNumber = etContactNumber.getText().toString().trim();

        if (email.isEmpty()) {

            etEmail.setError(getString(R.string.enter_email));
            etEmail.requestFocus();
            return;
        }

        if (email.contains(" ")) {

            etEmail.setError(getString(R.string.invalid_email_format));
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            etEmail.setError(getString(
                            R.string.invalid_email_format
                    ));

            etEmail.requestFocus();

            return;

        }

        if (contactNumber.isEmpty()) {

            etContactNumber.setError(getString(
                            R.string.contact_number_invalid
                    ));

            etContactNumber.requestFocus();

            return;

        }

        if (!contactNumber.matches("\\d{10}")) {

            etContactNumber.setError(getString(R.string.contact_number_invalid));

            etContactNumber.requestFocus();

            return;

        }

        String registrationDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        String qrData = rollNumber + "_" + eventId;

        Registration registration = new Registration(rollNumber, eventId, email, contactNumber, registrationDate, Registration.VALID, qrData);

        DatabaseClient.getInstance(getApplicationContext()).registrationDao().insertRegistration(registration);

        DatabaseClient.getInstance(getApplicationContext()).eventDao().incrementRegisteredCount(eventId);

        Toast.makeText(this, R.string.registration_successful, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RegisterEventActivity.this, MyRegistrationsActivity.class);

        intent.putExtra("EVENT_ID", eventId);

        startActivity(intent);

        finish();

    }

}