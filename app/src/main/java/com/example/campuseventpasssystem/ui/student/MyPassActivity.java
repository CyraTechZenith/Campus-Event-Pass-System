package com.example.campuseventpasssystem.ui.student;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.database.entities.Registration;
import com.example.campuseventpasssystem.database.entities.Student;
import com.example.campuseventpasssystem.utils.PassDownloadUtils;
import com.example.campuseventpasssystem.utils.QrCodeUtils;
import com.example.campuseventpasssystem.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MyPassActivity extends AppCompatActivity {

    private ImageView btnBack;
    private ImageView btnDownload;
    private ImageView imgQrCode;
    private TextView tvEventName;
    private TextView tvVenue;
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvStudentName;
    private TextView tvRollNumber;
    private TextView tvPassStatus;
    private Event event;
    private Student student;
    private Registration registration;
    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_pass);

        btnBack = findViewById(R.id.btnBack);

        btnDownload = findViewById(R.id.btnDownload);

        imgQrCode = findViewById(R.id.imgQrCode);

        tvEventName = findViewById(R.id.tvEventName);

        tvVenue = findViewById(R.id.tvVenue);

        tvDate = findViewById(R.id.tvDate);

        tvTime = findViewById(R.id.tvTime);

        tvStudentName = findViewById(R.id.tvStudentName);

        tvRollNumber = findViewById(R.id.tvRollNumber);

        tvPassStatus = findViewById(R.id.tvPassStatus);

        eventId = getIntent().getIntExtra("EVENT_ID", -1);

        student = DatabaseClient.getInstance(getApplicationContext()).studentDao().getStudentByRollNumber(SessionManager.getCurrentStudentRollNumber(this));

        event = DatabaseClient.getInstance(getApplicationContext()).eventDao().getEventById(eventId);

        registration = DatabaseClient.getInstance(getApplicationContext()).registrationDao().getRegistration(student.getRollNumber(), eventId);

        if (student == null || event == null || registration == null) {

            finish();

            return;

        }

        // ==========================
        // Load Pass
        // ==========================

        loadPass();

        btnBack.setOnClickListener(v -> finish());

        btnDownload.setOnClickListener(v -> {

            if (isEventOver()) {

                Toast.makeText(this, R.string.event_over, Toast.LENGTH_SHORT).show();

                return;
            }

            PassDownloadUtils.downloadPass(this, registration, event, student);

            Toast.makeText(this, R.string.pass_downloaded, Toast.LENGTH_SHORT).show();

        });

    }

    private void loadPass() {

        tvEventName.setText(event.getEventName());

        tvVenue.setText(event.getEventVenue());

        tvDate.setText(event.getEventDate());

        tvTime.setText(event.getEventTime());

        tvStudentName.setText(student.getName());

        tvRollNumber.setText(getString(R.string.roll_number_format, student.getRollNumber()));

        if (isEventOver()) {

            tvPassStatus.setText(R.string.expired);

            tvPassStatus.setBackgroundResource(R.drawable.status_expired_bg);

            tvPassStatus.setTextColor(Color.DKGRAY);

        } else {

            tvPassStatus.setText(registration.getPassStatus());

            tvPassStatus.setBackgroundResource(R.drawable.status_verified_bg);

            tvPassStatus.setTextColor(Color.parseColor("#2E7D32"));

        }

        generateQr();

    }

    private void generateQr() {

        imgQrCode.setImageBitmap(QrCodeUtils.generateQr(registration.getQrData()));

    }

    private boolean isEventOver() {

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Calendar today = Calendar.getInstance();

            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            return format
                    .parse(event.getEventDate())
                    .before(today.getTime());

        } catch (Exception e) {

            return false;

        }

    }
}