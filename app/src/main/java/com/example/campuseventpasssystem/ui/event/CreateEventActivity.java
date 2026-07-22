package com.example.campuseventpasssystem.ui.event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Event;

import java.util.Calendar;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {
    private ImageView imgEventBanner;
    private EditText etEventDate;
    private EditText etEventTime;
    private ActivityResultLauncher<Intent> bannerPickerLauncher;
    private String bannerUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_event);

        EditText etEventName = findViewById(R.id.etEventName);

        EditText etEventDescription = findViewById(R.id.etEventDescription);

        EditText etVenue = findViewById(R.id.etVenue);

        EditText etParticipationLimit = findViewById(R.id.etParticipationLimit);

        Button btnChooseBanner = findViewById(R.id.btnChooseBanner);

        Button btnCreateEvent = findViewById(R.id.btnCreateEvent);

        etEventDate = findViewById(R.id.etEventDate);

        etEventTime = findViewById(R.id.etEventTime);

        imgEventBanner = findViewById(R.id.imgEventBanner);

        // ================= Date Picker =================

        etEventDate.setOnClickListener(v -> {

            etEventDate.clearFocus();

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog dialog =
                    new DatePickerDialog(this, (view, year, month, day) -> {

                                String date = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);

                                etEventDate.setText(date);

                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    );

            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            dialog.show();

        });



        // ================= Time Picker =================

        etEventTime.setOnClickListener(v -> {

            etEventTime.clearFocus();

            Calendar calendar = Calendar.getInstance();

            TimePickerDialog dialog =
                    new TimePickerDialog(
                            this,
                            (view, hour, minute) -> {

                                String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

                                etEventTime.setText(time);

                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                    );

            dialog.show();

        });



        // ================= Banner Picker =================

        bannerPickerLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {

                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                                Uri uri = result.getData().getData();

                                if (uri != null) {

                                    try {
                                        getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                        Toast.makeText(
                                                this,
                                                "Permission saved",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    } catch (SecurityException e) {

                                        e.printStackTrace();
                                        Toast.makeText(
                                                this,
                                                e.toString(),
                                                Toast.LENGTH_LONG
                                        ).show();

                                    }

                                    bannerUri = uri.toString();

                                    imgEventBanner.setVisibility(View.VISIBLE);

                                    imgEventBanner.setImageURI(uri);

                                }

                            }

                        });

        btnChooseBanner.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

            intent.addCategory(Intent.CATEGORY_OPENABLE);

            intent.setType("image/*");

            bannerPickerLauncher.launch(intent);

        });



        // ================= Create Event =================

        btnCreateEvent.setOnClickListener(v -> {

            String name = etEventName.getText().toString().trim();

            String description = etEventDescription.getText().toString().trim();

            String date = etEventDate.getText().toString().trim();

            String time = etEventTime.getText().toString().trim();

            String venue = etVenue.getText().toString().trim();

            String limitText = etParticipationLimit.getText().toString().trim();



            if (name.isEmpty() || date.isEmpty() || time.isEmpty() || venue.isEmpty()) {

                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();

                return;
            }



            if (name.length() < 3) {

                Toast.makeText(this, R.string.short_event_name, Toast.LENGTH_SHORT).show();

                return;
            }


            if (name.length() > 60) {

                Toast.makeText(this, R.string.long_event_name, Toast.LENGTH_SHORT).show();

                return;
            }


            if (venue.length() < 3) {

                Toast.makeText(this, R.string.invalid_venue, Toast.LENGTH_SHORT).show();

                return;
            }


            int limit = 0;

            if (!limitText.isEmpty()) {

                try {
                    limit = Integer.parseInt(limitText);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, R.string.invalid_participant_limit, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (limit <= 0) {

                    Toast.makeText(this, R.string.participant_limit_validation, Toast.LENGTH_SHORT).show();

                    return;
                }

            }


            Event event = new Event(name, description, date, time, venue, bannerUri, limit, Event.ACTIVE);

            DatabaseClient.getInstance(getApplicationContext()).eventDao().insertEvent(event);


            Toast.makeText(this, R.string.event_created, Toast.LENGTH_SHORT).show();

            finish();

        });
    }

}