package com.example.campuseventpasssystem.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Registration;
import com.example.campuseventpasssystem.database.entities.Student;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {
    private final Context context;
    private final List<Registration> registrationList;

    public ParticipantAdapter(Context context, List<Registration> registrationList) {

        this.context = context;
        this.registrationList = registrationList;

    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_participant, parent, false);

        return new ParticipantViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {

        Registration registration = registrationList.get(position);

        Student student = DatabaseClient.getInstance(context).studentDao().getStudentByRollNumber(registration.getStudentRollNumber());

        if (student != null) {

            holder.tvName.setText(student.getName());

        } else {

            holder.tvName.setText(registration.getStudentRollNumber());

        }

        holder.tvRoll.setText(context.getString(
                        R.string.roll_number_format,
                        registration.getStudentRollNumber()
                ));



        if ("USED".equalsIgnoreCase(registration.getPassStatus())) {

            holder.tvStatus.setText(context.getString(R.string.checked_in));

            holder.tvStatus.setBackgroundResource(R.drawable.status_verified_bg);

        }

        else {

            holder.tvStatus.setText(context.getString(R.string.not_checked_in));

            holder.tvStatus.setBackgroundResource(R.drawable.status_invalid_bg);

        }

    }

    @Override
    public int getItemCount() {

        return registrationList.size();

    }

    public static class ParticipantViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvRoll;
        TextView tvStatus;

        public ParticipantViewHolder(@NonNull View itemView) {

            super(itemView);

            tvName = itemView.findViewById(R.id.tvParticipantName);

            tvRoll = itemView.findViewById(R.id.tvParticipantRoll);

            tvStatus = itemView.findViewById(R.id.tvParticipantStatus);

        }

    }

}