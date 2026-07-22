package com.example.campuseventpasssystem.ui.qr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campuseventpasssystem.R;

public class VerificationResultActivity extends AppCompatActivity {

    private ImageView imgStatus;
    private TextView tvStatus;
    private TextView tvDetails;
    private Button btnScanAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verification_result);

        imgStatus = findViewById(R.id.imgStatus);

        tvStatus = findViewById(R.id.tvStatus);

        tvDetails = findViewById(R.id.tvDetails);

        btnScanAgain = findViewById(R.id.btnScanAgain);

        ImageView btnBack = findViewById(R.id.btnBack);

        // ==========================
        // Back
        // ==========================


        btnBack.setOnClickListener(v -> finish());

        String status = getIntent().getStringExtra("status");
        String details = getIntent().getStringExtra("details");

        if ("SUCCESS".equals(status)) {

            imgStatus.setImageResource(R.drawable.baseline_check_circle_24);

            tvStatus.setText(R.string.verification_successful);

        }

        else {

            imgStatus.setImageResource(R.drawable.baseline_cancel_24);

            tvStatus.setText(R.string.verification_failed);

        }

        tvDetails.setText(details);

        // ==========================
        // Scan Again
        // ==========================

        btnScanAgain.setOnClickListener(v -> {

            Intent intent = new Intent(VerificationResultActivity.this, ScanPassActivity.class);

            startActivity(intent);

            finish();

        });

    }

}