package com.example.campuseventpasssystem.ui.event;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import android.graphics.Color;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.entities.EntryLog;
import com.example.campuseventpasssystem.ui.adapter.EntryLogAdapter;
import com.example.campuseventpasssystem.database.DatabaseClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventEntryLogsActivity extends AppCompatActivity {
    private RecyclerView rvEntryLogs;
    private EntryLogAdapter adapter;
    private EditText etSearch;
    private ImageButton btnFilter;
    private Button btnAll;
    private Button btnVerified;
    private Button btnFailed;
    private TextView tvEntryCount;
    private ImageView btnBack;
    private LinearLayout layoutToday;
    private ImageView imgTodayArrow;
    private int eventId;
    private final List<EntryLog> allLogs = new ArrayList<>();
    private final List<EntryLog> filteredLogs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_entry_logs);

        etSearch = findViewById(R.id.etSearch);

        btnFilter = findViewById(R.id.btnFilter);

        btnAll = findViewById(R.id.btnAll);

        btnVerified = findViewById(R.id.btnVerified);

        btnFailed = findViewById(R.id.btnFailed);

        tvEntryCount = findViewById(R.id.tvEntryCount);

        btnBack = findViewById(R.id.btnBack);

        layoutToday = findViewById(R.id.layoutToday);

        rvEntryLogs = findViewById(R.id.rvEntryLogs);

        imgTodayArrow = findViewById(R.id.imgTodayArrow);

        eventId = getIntent().getIntExtra("EVENT_ID", -1);

        rvEntryLogs.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EntryLogAdapter(this, filteredLogs);

        rvEntryLogs.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        filterLogs("");

        updateFilterButtons(btnAll);

        btnAll.setOnClickListener(v -> {

            updateFilterButtons(btnAll);

            filterLogs("");

        });

        btnVerified.setOnClickListener(v -> {

            updateFilterButtons(btnVerified);

            filterStatus(EntryLog.VERIFIED);

        });

        btnFailed.setOnClickListener(v -> {

            updateFilterButtons(btnFailed);

            filterFailed();

        });

        etSearch.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        filterLogs(s.toString());

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }

                });


        btnFilter.setOnClickListener(v -> {

            showSortMenu(v);

        });

        layoutToday.setOnClickListener(v -> {

            if (rvEntryLogs.getVisibility() == View.VISIBLE) {

                rvEntryLogs.setVisibility(View.GONE);

                imgTodayArrow.setRotation(-90);

            } else {

                rvEntryLogs.setVisibility(View.VISIBLE);

                imgTodayArrow.setRotation(0);

            }

        });

        allLogs.clear();

        allLogs.addAll(DatabaseClient.getInstance(this).entryLogDao().getEntryLogsByEvent(eventId));

        filterLogs("");

    }


    // ==========================
    // Search
    // ==========================

    private void filterLogs(String query) {

        filteredLogs.clear();

        for (EntryLog log : allLogs) {

            if (log.getStudentRollNumber().toLowerCase().contains(query.toLowerCase())) {

                filteredLogs.add(log);

            }

        }

        adapter.notifyDataSetChanged();

        tvEntryCount.setText(getString(
                        R.string.entries,
                        filteredLogs.size()
                ));
    }

    // ==========================
    // Verified Filter
    // ==========================

    private void filterStatus(String status) {

        filteredLogs.clear();

        for (EntryLog log : allLogs) {

            if (log.getStatus().equals(status)) {

                filteredLogs.add(log);

            }

        }

        adapter.notifyDataSetChanged();

        tvEntryCount.setText(filteredLogs.size() + " Entries");

    }

    // ==========================
    // Failed Filter
    // ==========================

    private void filterFailed() {

        filteredLogs.clear();

        for (EntryLog log : allLogs) {

            if (!log.getStatus().equals(EntryLog.VERIFIED)) {

                filteredLogs.add(log);

            }

        }

        adapter.notifyDataSetChanged();

        tvEntryCount.setText(filteredLogs.size() + " Entries");

    }

    // ==========================
    // Filter Buttons
    // ==========================

    private void updateFilterButtons(Button selectedButton) {

        Button[] buttons = {btnAll, btnVerified, btnFailed};

        for (Button button : buttons) {

            if (button == selectedButton) {

                button.setBackground(ContextCompat.getDrawable(EventEntryLogsActivity.this, R.drawable.gradient_button));

                button.setTextColor(Color.WHITE);

            } else {

                button.setBackground(ContextCompat.getDrawable(EventEntryLogsActivity.this, R.drawable.filter_chip));

                button.setTextColor(Color.BLACK);
            }
        }
    }

    // ==========================
    // Sort Popup Menu
    // ==========================

    private void showSortMenu(View anchor) {

        Context wrapper = new ContextThemeWrapper(this, R.style.PopupMenuStyle);

        PopupMenu popupMenu = new PopupMenu(wrapper, anchor);

        popupMenu.getMenu().add(0, 1, 0, getString(R.string.newest_first));

        popupMenu.getMenu().add(0, 2, 1, getString(R.string.oldest_first));

        popupMenu.getMenu().add(0, 3, 2, getString(R.string.verified_first));

        popupMenu.getMenu().add(0, 4, 3, getString(R.string.failed_first));

        popupMenu.setOnMenuItemClickListener(item -> {switch (item.getItemId()) {

                        case 1:

                            Collections.reverse(filteredLogs);

                            adapter.notifyDataSetChanged();

                            return true;

                        case 2:

                            Collections.sort(filteredLogs, Comparator.comparing(EntryLog::getScanTime));

                            adapter.notifyDataSetChanged();

                            return true;

                        case 3:

                            Collections.sort(filteredLogs, (a, b) -> {

                                        if (a.getStatus().equals(EntryLog.VERIFIED)) {

                                            return -1;

                                        }

                                        if (b.getStatus().equals(EntryLog.VERIFIED)) {

                                            return 1;

                                        }

                                        return 0;

                                    }
                            );

                            adapter.notifyDataSetChanged();

                            return true;

                        case 4:

                            Collections.sort(filteredLogs, (a, b) -> {

                                        if (!a.getStatus().equals(EntryLog.VERIFIED)) {

                                            return -1;

                                        }

                                        if (!b.getStatus().equals(EntryLog.VERIFIED)) {

                                            return 1;

                                        }

                                        return 0;

                                    }
                            );

                            adapter.notifyDataSetChanged();

                            return true;

                    }

                    return false;

                });

        popupMenu.show();

    }

}