package com.example.campuseventpasssystem.ui.student;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Student;
import com.example.campuseventpasssystem.utils.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class StudentProfileActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private ImageButton btnEditImage;
    private ImageButton btnBack;
    private TextView tvName;
    private TextView tvRollNumber;
    private TextView tvPassword;
    private Button btnChangePassword;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private String rollNumber;
    private TextView tvInitials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_profile);

        btnBack = findViewById(R.id.btnBack);

        imgProfile = findViewById(R.id.imgProfile);

        btnEditImage = findViewById(R.id.btnEditImage);

        tvName = findViewById(R.id.tvName);

        tvRollNumber = findViewById(R.id.tvRollNumber);

        tvPassword = findViewById(R.id.tvPassword);

        btnChangePassword = findViewById(R.id.btnChangePassword);

        rollNumber = SessionManager.getCurrentStudentRollNumber(this);

        tvInitials = findViewById(R.id.tvInitials);

        loadStudent();

        initialiseImagePicker();

        btnBack.setOnClickListener(v -> finish());

        btnEditImage.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            imagePickerLauncher.launch(intent);

        });

        btnChangePassword.setOnClickListener(v -> {

            showChangePasswordDialog();

        });

    }

    private void loadStudent() {

        Student student = DatabaseClient.getInstance(getApplicationContext()).studentDao().getStudentByRollNumber(rollNumber);

        if (student == null) {

            Toast.makeText(this, R.string.student_not_found, Toast.LENGTH_SHORT).show();

            finish();

            return;
        }

        tvName.setText(student.getName());

        tvRollNumber.setText(student.getRollNumber());

        tvPassword.setText(R.string.hidden_password);

        if (student.getProfileImageUri() != null && !student.getProfileImageUri().isEmpty()) {

            imgProfile.setVisibility(View.VISIBLE);
            tvInitials.setVisibility(View.GONE);

            try {

                imgProfile.setImageURI(Uri.fromFile(new File(student.getProfileImageUri())));

            } catch (Exception e) {

                imgProfile.setImageResource(R.drawable.baseline_account_circle_24);

            }

        } else {

            imgProfile.setVisibility(View.GONE);
            tvInitials.setVisibility(View.VISIBLE);

            String[] names = student.getName().split("\\s+");

            String initials = "";

            if (names.length >= 1) initials += names[0].substring(0, 1).toUpperCase();

            if (names.length >= 2) initials += names[1].substring(0, 1).toUpperCase();

            tvInitials.setText(initials);

        }

    }

    private void initialiseImagePicker() {

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                                Uri imageUri = result.getData().getData();

                                imgProfile.setImageURI(imageUri);
                                imgProfile.setVisibility(View.VISIBLE);
                                tvInitials.setVisibility(View.GONE);

                                String savedPath = saveImageToInternalStorage(imageUri);

                                DatabaseClient.getInstance(getApplicationContext()).studentDao().updateProfileImage(rollNumber, savedPath);

                                Toast.makeText(this, R.string.profile_image_updated, Toast.LENGTH_SHORT).show();

                            }

                        });

    }


    private String saveImageToInternalStorage(Uri uri) {

        try {

            InputStream inputStream = getContentResolver().openInputStream(uri);

            File file = new File(getFilesDir(), "profile_" + rollNumber + ".jpg");

            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];

            int length;

            while ((length = inputStream.read(buffer)) > 0) {

                outputStream.write(buffer, 0, length);

            }

            inputStream.close();

            outputStream.close();

            return file.getAbsolutePath();

        } catch (Exception e) {

            e.printStackTrace();

            return "";

        }

    }

    private void showChangePasswordDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_password, null);

        EditText etCurrent = view.findViewById(R.id.etCurrentPassword);

        EditText etNew = view.findViewById(R.id.etNewPassword);

        EditText etConfirm = view.findViewById(R.id.etConfirmPassword);

        new AlertDialog.Builder(this).setTitle(R.string.change_password).setView(view).setPositiveButton(R.string.save, (dialog, which) -> {

                    String current = etCurrent.getText().toString().trim();

                    String newPassword = etNew.getText().toString().trim();

                    String confirmPassword = etConfirm.getText().toString().trim();

                    Student student = DatabaseClient.getInstance(getApplicationContext()).studentDao().getStudentByRollNumber(rollNumber);

                    if (!student.getPassword().equals(current)) {

                        Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();

                        return;
                    }

                    if (newPassword.length() < 8) {

                        Toast.makeText(this, R.string.password_requirements, Toast.LENGTH_SHORT).show();

                        return;
                    }

                    if (!newPassword.equals(confirmPassword)) {

                        Toast.makeText(this, R.string.password_mismatch, Toast.LENGTH_SHORT).show();

                        return;

                    }

                    DatabaseClient.getInstance(getApplicationContext()).studentDao().updatePassword(rollNumber, newPassword);

                    Toast.makeText(this, R.string.password_updated, Toast.LENGTH_SHORT).show();

                })

                .setNegativeButton(R.string.cancel, null)

                .show();

    }
}