package com.example.campuseventpasssystem.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.database.entities.Registration;
import com.example.campuseventpasssystem.database.entities.Student;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PassDownloadUtils {

    public static void downloadPass(Context context, Registration registration, Event event, Student student) {

        try {

            View passView = LayoutInflater.from(context).inflate(R.layout.layout_pass, null, false);

            ImageView imgQrCode = passView.findViewById(R.id.imgQrCode);

            TextView tvEventName = passView.findViewById(R.id.tvEventName);

            TextView tvVenue = passView.findViewById(R.id.tvVenue);

            TextView tvDate = passView.findViewById(R.id.tvDate);

            TextView tvTime = passView.findViewById(R.id.tvTime);

            TextView tvStudentName = passView.findViewById(R.id.tvStudentName);

            TextView tvRollNumber = passView.findViewById(R.id.tvRollNumber);

            TextView tvPassStatus = passView.findViewById(R.id.tvPassStatus);

            tvEventName.setText(event.getEventName());

            tvVenue.setText(event.getEventVenue());

            tvDate.setText(event.getEventDate());

            tvTime.setText(event.getEventTime());

            tvStudentName.setText(student.getName());

            tvRollNumber.setText(context.getString(
                            R.string.roll_number_format,
                            student.getRollNumber()
                    ));

            tvPassStatus.setText(registration.getPassStatus());


            // Generate QR Code

            imgQrCode.setImageBitmap(QrCodeUtils.generateQr(registration.getQrData()));


            // Measure Layout

            int width = 1080;

            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);

            int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

            passView.measure(widthSpec, heightSpec);

            passView.layout(0, 0, passView.getMeasuredWidth(), passView.getMeasuredHeight());


            // Create High Resolution Bitmap

            int scale = 3;

            Bitmap bitmap = Bitmap.createBitmap(passView.getMeasuredWidth() * scale, passView.getMeasuredHeight() * scale, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);

            canvas.scale(scale, scale);

            passView.draw(canvas);

            String fileName = event.getEventName().replaceAll("[^a-zA-Z0-9]", "_") + "_Pass.png";

            OutputStream outputStream;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                ContentValues values = new ContentValues();

                values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);

                values.put(MediaStore.Downloads.MIME_TYPE, "image/png");

                values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

                if (uri == null) {
                    throw new Exception("Unable to create download file.");
                }

                outputStream = context.getContentResolver().openOutputStream(uri);

            } else {

                File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                if (!downloadsFolder.exists()) {
                    downloadsFolder.mkdirs();
                }

                File file = new File(downloadsFolder, fileName);

                outputStream = new FileOutputStream(file);

            }

            if (outputStream == null) {
                throw new Exception("Output stream is null.");
            }

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            outputStream.flush();
            outputStream.close();

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

}