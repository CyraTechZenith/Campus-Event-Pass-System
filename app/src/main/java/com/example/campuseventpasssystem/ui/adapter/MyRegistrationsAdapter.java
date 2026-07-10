package com.example.campuseventpasssystem.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.database.entities.Registration;
import com.example.campuseventpasssystem.ui.student.MyPassActivity;
import com.example.campuseventpasssystem.database.model.RegistrationItem;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Student;
import com.example.campuseventpasssystem.utils.PassDownloadUtils;
import com.example.campuseventpasssystem.utils.SessionManager;

import java.util.List;

public class MyRegistrationsAdapter extends RecyclerView.Adapter<MyRegistrationsAdapter.ViewHolder> {

    private final Context context;
    private final List<RegistrationItem> registrationItems;

    public MyRegistrationsAdapter(Context context, List<RegistrationItem> registrationItems) {

        this.context = context;
        this.registrationItems = registrationItems;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_registration_card, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RegistrationItem item = registrationItems.get(position);

        Registration registration = item.getRegistration();

        Event event = item.getEvent();

        holder.tvEventName.setText(event.getEventName());

        holder.tvDate.setText(event.getEventDate());

        holder.tvTime.setText(event.getEventTime());

        holder.tvVenue.setText(event.getEventVenue());

        holder.tvStatus.setText(registration.getPassStatus());

        holder.cardRegistration.setOnClickListener(v -> {

            Intent intent = new Intent(context, MyPassActivity.class);

            intent.putExtra("EVENT_ID", event.getEventId());

            context.startActivity(intent);

        });

        holder.tvDownloadPass.setOnClickListener(v -> {

            Student student = DatabaseClient.getInstance(context).studentDao().getStudentByRollNumber(SessionManager.getCurrentStudentRollNumber(context));

            PassDownloadUtils.downloadPass(context, registration, event, student);

            Toast.makeText(context, R.string.pass_downloaded, Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public int getItemCount() {

        return registrationItems.size();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View cardRegistration;
        TextView tvEventName;
        TextView tvDate;
        TextView tvTime;
        TextView tvVenue;
        TextView tvStatus;
        TextView tvDownloadPass;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            cardRegistration = itemView.findViewById(R.id.cardRegistration);

            tvEventName = itemView.findViewById(R.id.tvEventName);

            tvDate = itemView.findViewById(R.id.tvDate);

            tvTime = itemView.findViewById(R.id.tvTime);

            tvVenue = itemView.findViewById(R.id.tvVenue);

            tvStatus = itemView.findViewById(R.id.tvStatus);

            tvDownloadPass = itemView.findViewById(R.id.tvDownloadPass);

        }

    }

}