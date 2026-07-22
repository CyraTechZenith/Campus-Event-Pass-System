package com.example.campuseventpasssystem.ui.qr;

import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.app.AlertDialog;
import android.net.Uri;
import android.provider.Settings;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Registration;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.example.campuseventpasssystem.database.entities.EntryLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScanPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_pass);

        checkCameraPermission();
    }

    // ==========================
    // Camera Permission
    // ==========================

    private void checkCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            startScanner();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);

        }

    }

    // ==========================
    // Start Scanner
    // ==========================

    private void startScanner() {

        IntentIntegrator integrator = new IntentIntegrator(this);

        integrator.setCaptureActivity(CaptureActivityPortrait.class);

        integrator.setPrompt("Align QR code inside frame");

        integrator.setBeepEnabled(true);

        integrator.setOrientationLocked(true);

        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {

            if (result.getContents() == null) {

                finish();

            } else {

                verifyPass(result.getContents());

            }

            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(

            int requestCode,

            String[] permissions,

            int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startScanner();

            }

            else {

                showPermissionDialog();

            }

        }

    }

    private void showPermissionDialog() {
        new AlertDialog.Builder(this)

                .setTitle("Camera Access Denied")

                .setMessage("You need to allow camera permission\n" + "to scan participants.")

                .setPositiveButton(R.string.go_to_settings, (dialog, which) -> {

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                            intent.setData(Uri.fromParts("package", getPackageName(), null));

                            startActivity(intent);

                        })

                .setNegativeButton(R.string.cancel, null).show();
    }

    // ==========================
    // Verify Pass
    // ==========================

    private void verifyPass(String qrData) {

        Registration registration = DatabaseClient.getInstance(getApplicationContext()).registrationDao().getRegistrationByQrData(qrData);

        if (registration == null) {

            Intent intent = new Intent(ScanPassActivity.this, VerificationResultActivity.class);

            intent.putExtra("status", "FAILED");

            intent.putExtra("details", "Invalid QR Code\n\nEntry Denied");

            startActivity(intent);

            finish();

            return;
        }

        if (registration.getPassStatus().equals(Registration.USED)) {

            Intent intent = new Intent(ScanPassActivity.this, VerificationResultActivity.class);

            intent.putExtra("status", "FAILED");

            intent.putExtra("details", "Pass Already Used\n\nRoll Number : " + registration.getStudentRollNumber() + "\nEvent ID : " + registration.getEventId());

            startActivity(intent);

            finish();

            return;
        }

        DatabaseClient.getInstance(getApplicationContext()).registrationDao().updatePassStatus(registration.getRegistrationId(), Registration.USED);

        String scanTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        EntryLog entryLog = new EntryLog(registration.getStudentRollNumber(), registration.getEventId(), scanTime, EntryLog.VERIFIED);

        DatabaseClient.getInstance(getApplicationContext()).entryLogDao().insertEntryLog(entryLog);

        Intent intent = new Intent(ScanPassActivity.this, VerificationResultActivity.class);

        intent.putExtra("status", "SUCCESS");

        intent.putExtra("details", "Roll Number : " + registration.getStudentRollNumber() + "\n\nEvent ID : " + registration.getEventId() + "\n\nEntry Granted");

        startActivity(intent);

        finish();
    }
}