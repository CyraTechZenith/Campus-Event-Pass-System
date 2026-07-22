package com.example.campuseventpasssystem.ui.student;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.database.entities.Student;
import com.example.campuseventpasssystem.ui.adapter.EventCardAdapter;
import com.example.campuseventpasssystem.ui.auth.LoginActivity;
import com.example.campuseventpasssystem.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class StudentDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton btnMenu;
    private RecyclerView rvEvents;
    private LinearLayout layoutEmpty;
    private TextView tvStudentName;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);

        // ==========================
        // Initialize Views
        // ==========================

        TextView tvWelcome = findViewById(R.id.tvWelcome);

        drawerLayout = findViewById(R.id.drawerLayout);

        navigationView = findViewById(R.id.navigationView);

        btnMenu = findViewById(R.id.btnMenu);

        rvEvents = findViewById(R.id.rvEvents);

        layoutEmpty = findViewById(R.id.layoutEmpty);

        tvStudentName = findViewById(R.id.tvStudentName);

        // ==========================
        // Toolbar
        // ==========================

        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // ==========================
        // Get Logged In Student
        // ==========================

        student = DatabaseClient.getInstance(getApplicationContext()).studentDao() .getStudentByRollNumber(SessionManager.getCurrentStudentRollNumber(this));

        if (student != null) {

            tvWelcome.setText(getString(
                            R.string.welcome_student,
                            student.getName()
                    ));

            tvStudentName.setText(student.getName());

        }

        // ==========================
        // RecyclerView
        // ==========================

        rvEvents.setLayoutManager(new LinearLayoutManager(this));

        loadEvents();

        // ==========================
        // Drawer Header
        // ==========================

        if (student != null) {

            android.view.View headerView = navigationView.getHeaderView(0);

            ImageView ivDrawerProfile = headerView.findViewById(R.id.ivDrawerProfile);

            TextView tvDrawerInitials = headerView.findViewById(R.id.tvDrawerInitials);

            TextView tvDrawerName = headerView.findViewById(R.id.tvDrawerName);

            tvDrawerName.setText(student.getName());

            if (student.getProfileImageUri() != null && !student.getProfileImageUri().isEmpty()) {

                ivDrawerProfile.setImageURI(Uri.parse(student.getProfileImageUri()));

                tvDrawerInitials.setVisibility(android.view.View.GONE);

            } else {

                String[] names = student.getName().trim().split("\\s+");

                String initials;

                if (names.length == 1) {

                    initials = names[0].substring(0, Math.min(2, names[0].length())).toUpperCase();

                } else {

                    initials = ("" + names[0].charAt(0) + names[1].charAt(0)).toUpperCase();

                }

                tvDrawerInitials.setText(initials);

            }

        }

        // ==========================
        // Navigation Drawer
        // ==========================

        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.menuProfile) {

                startActivity(new Intent(StudentDashboardActivity.this, StudentProfileActivity.class));

            } else if (id == R.id.menuMyRegistrations) {

                startActivity(new Intent(StudentDashboardActivity.this, MyRegistrationsActivity.class));

            } else if (id == R.id.menuLogout) {

                showLogoutDialog();

            }

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;

        });
    }

    // ==========================
    // Load Events
    // ==========================

    private void loadEvents () {

        List<Event> eventList = DatabaseClient.getInstance(getApplicationContext()).eventDao().getActiveEvents();

        if (eventList.isEmpty()) {

            layoutEmpty.setVisibility(android.view.View.VISIBLE);

            rvEvents.setVisibility(android.view.View.GONE);

            return;

        }

        layoutEmpty.setVisibility(android.view.View.GONE);

        rvEvents.setVisibility(android.view.View.VISIBLE);

        EventCardAdapter adapter = new EventCardAdapter(this, eventList);

        rvEvents.setAdapter(adapter);

    }

    // ==========================
    // Logout
    // ==========================

    private void showLogoutDialog () {

        new AlertDialog.Builder(this)

                .setTitle(R.string.logout)

                .setMessage(R.string.logout_confirmation)

                .setPositiveButton(R.string.logout, (dialog, which) -> {

                            SessionManager.logout(this);

                            Intent intent = new Intent(StudentDashboardActivity.this, LoginActivity.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                            finish();

                        })

                .setNegativeButton(R.string.cancel, null).show();

    }
}