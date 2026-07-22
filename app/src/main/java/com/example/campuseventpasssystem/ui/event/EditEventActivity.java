package com.example.campuseventpasssystem.ui.event;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.ui.admin.AdminDashboardActivity;

import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditEventActivity extends AppCompatActivity {
    private ImageView imgEventBanner;
    private EditText etEventName;
    private EditText etEventDescription;
    private EditText etEventDate;
    private EditText etEventTime;
    private EditText etVenue;
    private EditText etParticipationLimit;
    private TextView tvRegistrationCount;
    private Button btnChooseBanner;
    private Button btnSaveChanges;
    private Button btnDeactivate;
    private Button btnDeleteEvent;
    private Event event;
    private int eventId;
    private String bannerUri = "";
    private ActivityResultLauncher<Intent> bannerPickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_event);

        //=============================
        // Initialise Views
        //=============================

        ImageView btnBack = findViewById(R.id.btnBack);

        etEventName = findViewById(R.id.etEventName);

        etEventDescription = findViewById(R.id.etEventDescription);

        etEventDate = findViewById(R.id.etEventDate);

        etEventTime = findViewById(R.id.etEventTime);

        etVenue = findViewById(R.id.etVenue);

        etParticipationLimit = findViewById(R.id.etParticipationLimit);

        imgEventBanner = findViewById(R.id.imgEventBanner);

        tvRegistrationCount = findViewById(R.id.tvRegistrationCount);

        btnChooseBanner = findViewById(R.id.btnChooseBanner);

        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        btnDeactivate = findViewById(R.id.btnDeactivate);

        btnDeleteEvent = findViewById(R.id.btnDeleteEvent);

        eventId = getIntent().getIntExtra("EVENT_ID", -1);

        event = DatabaseClient.getInstance(this).eventDao().getEventById(eventId);

        try {
            String dateTime = event.getEventDate() + " " + event.getEventTime();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            Date eventDateTime = format.parse(dateTime);

            boolean eventEnded = eventDateTime != null && eventDateTime.before(new Date());

            if (eventEnded) {

                btnDeactivate.setVisibility(View.GONE);

            } else if (Event.CANCELLED.equals(event.getEventStatus())) {

                btnDeactivate.setText(R.string.reactivate);

            } else {

                btnDeactivate.setText(R.string.deactivate);

            }

        } catch (Exception e) {

            btnDeactivate.setVisibility(View.GONE);

        }

        if (event != null) {

            etEventName.setText(event.getEventName());

            etEventDescription.setText(event.getEventDescription());

            etEventDate.setText(event.getEventDate());

            etEventTime.setText(event.getEventTime());

            etVenue.setText(event.getEventVenue());

            if (event.getParticipationLimit() > 0) {

                etParticipationLimit.setText(String.valueOf(event.getParticipationLimit()));

            }

            bannerUri = event.getEventBannerUri();

            if (bannerUri != null && !bannerUri.isEmpty()) {

                try {
                    imgEventBanner.setImageURI(Uri.parse(bannerUri));
                    imgEventBanner.setVisibility(View.VISIBLE);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    imgEventBanner.setImageResource(R.drawable.default_event_banner);
                    imgEventBanner.setVisibility(View.VISIBLE);
                    bannerUri = "";
                }

            } else {

                imgEventBanner.setImageResource(R.drawable.default_event_banner);

                imgEventBanner.setVisibility(View.VISIBLE);

            }

        }

        //=============================
        // Back Button
        //=============================

        btnBack.setOnClickListener(v -> finish());

        //=============================
        // Registration Count
        //=============================

        int registrations = DatabaseClient.getInstance(this).registrationDao().getParticipantCount(eventId);

        if (event.getParticipationLimit() > 0) {

            tvRegistrationCount.setText(registrations + " / " + event.getParticipationLimit());

        } else {

            tvRegistrationCount.setText(String.valueOf(registrations));

        }



        //=============================
        // Banner Picker
        //=============================

        bannerPickerLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(), result -> {

                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                                Uri uri = result.getData().getData();

                                if (uri != null) {

                                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

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

        //=============================
        // Date Picker
        //=============================

        etEventDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {

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


        //=============================
        // Time Picker
        //=============================

        etEventTime.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            TimePickerDialog dialog = new TimePickerDialog(this, (view, hour, minute) -> {

                                String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

                                etEventTime.setText(time);

                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                    );

            dialog.show();

        });

        //=============================
        // Save Changes
        //=============================

        btnSaveChanges.setOnClickListener(v -> {

            String name = etEventName.getText().toString().trim();

            String description = etEventDescription.getText().toString().trim();

            String date = etEventDate.getText().toString().trim();

            String time = etEventTime.getText().toString().trim();

            String venue = etVenue.getText().toString().trim();

            String limitText = etParticipationLimit.getText().toString().trim();


            //=============================
            // Validations
            //=============================

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


            //=============================
            // Update Event Object
            //=============================

            event.setEventName(name);

            event.setEventDescription(description);

            event.setEventDate(date);

            event.setEventTime(time);

            event.setEventVenue(venue);

            event.setParticipationLimit(limit);

            event.setEventBannerUri(bannerUri);

            DatabaseClient.getInstance(this).eventDao().updateEvent(event);

            Toast.makeText(this, R.string.event_updated_cofirmation, Toast.LENGTH_SHORT).show();
            finish();

        });

        //=============================
        // Deactivate Event
        //=============================

        btnDeactivate.setOnClickListener(v -> {

            if (Event.CANCELLED.equals(event.getEventStatus())) {

                new AlertDialog.Builder(this)

                        .setTitle(R.string.reactivate)

                        .setMessage("Students will be able to register for this event again.\nContinue?")

                        .setNegativeButton(R.string.cancel, null)

                        .setPositiveButton(R.string.reactivate, (dialog, which) -> {

                            event.setEventStatus(Event.ACTIVE);

                            DatabaseClient.getInstance(this)
                                    .eventDao()
                                    .updateEvent(event);

                            Toast.makeText(this,
                                    R.string.event_reactivated_confirmation,
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(
                                    EditEventActivity.this,
                                    AdminDashboardActivity.class);

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent);

                            finish();

                        })

                        .show();

            } else {

                new AlertDialog.Builder(this)

                        .setTitle(R.string.deactivate_event)

                        .setMessage("Students will no longer be able to register for this event.\nContinue?")

                        .setNegativeButton(R.string.cancel, null)

                        .setPositiveButton(
                                R.string.deactivate,
                                (dialog, which) -> {

                                    event.setEventStatus(Event.CANCELLED);

                                    DatabaseClient.getInstance(this)
                                            .eventDao()
                                            .updateEvent(event);

                                    Toast.makeText(this,
                                            R.string.event_deactivated,
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(
                                            EditEventActivity.this,
                                            AdminDashboardActivity.class);

                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    startActivity(intent);

                                    finish();

                                })

                        .show();

            }

        });


        //=============================
        // Delete Event
        //=============================

        btnDeleteEvent.setOnClickListener(v -> {

            int participants = DatabaseClient.getInstance(this).registrationDao().getParticipantCount(eventId);

            if (participants > 0) {

                new AlertDialog.Builder(this)

                        .setTitle(R.string.cannot_delete)

                        .setMessage("Students are already registered for this event.\n\nDeactivate it instead.")

                        .setPositiveButton(R.string.ok, null).show();

                return;

            }



            new AlertDialog.Builder(this)

                    .setTitle(R.string.delete_event)

                    .setMessage("This event has no registrations.\n\nDelete permanently?")

                    .setNegativeButton(R.string.cancel, null)

                    .setPositiveButton(
                            R.string.delete, (dialog, which) -> {

                                DatabaseClient.getInstance(this).eventDao().deleteEvent(eventId);

                                Toast.makeText(this, R.string.event_deleted, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(EditEventActivity.this, AdminDashboardActivity.class);

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(intent);

                                finish();

                            })

                    .show();

        });
    }

}