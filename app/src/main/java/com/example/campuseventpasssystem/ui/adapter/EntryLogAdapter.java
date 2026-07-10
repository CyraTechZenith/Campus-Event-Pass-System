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
import com.example.campuseventpasssystem.database.entities.EntryLog;
import com.example.campuseventpasssystem.database.entities.Student;

import java.util.List;

public class EntryLogAdapter extends RecyclerView.Adapter<EntryLogAdapter.LogViewHolder> {
    private final Context context;
    private final List<EntryLog> logList;

    public EntryLogAdapter(Context context, List<EntryLog> logList) {

        this.context = context;

        this.logList = logList;

    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_entry_log, parent, false);

        return new LogViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {

        EntryLog log = logList.get(position);

        Student student = DatabaseClient.getInstance(context).studentDao().getStudentByRollNumber(log.getStudentRollNumber());

        if (student != null) {

            holder.tvStudentName.setText(student.getName());

        } else {

            holder.tvStudentName.setText(log.getStudentRollNumber());

        }

        holder.tvRoll.setText(context.getString(R.string.roll_number_format, log.getStudentRollNumber()));

        holder.tvEvent.setText(R.string.event_name);

        holder.tvTime.setText(log.getScanTime());

        // Avatar (First 2 letters)

        String avatar = log.getStudentRollNumber().length() >= 2 ? log.getStudentRollNumber().substring(0, 2).toUpperCase() : log.getStudentRollNumber().toUpperCase();

        holder.tvAvatar.setText(avatar);

        // Status Badge

        switch (log.getStatus()) {

            case EntryLog.VERIFIED:

                holder.tvStatus.setText(context.getString(R.string.verified));

                holder.tvStatus.setBackgroundResource(R.drawable.status_verified_bg);

                break;

            case EntryLog.ALREADY_CHECKED_IN:

                holder.tvStatus.setText(context.getString(
                                R.string.already_checked
                        ));

                holder.tvStatus.setBackgroundResource(R.drawable.status_checked_bg);

                break;

            case EntryLog.INVALID_QR:

                holder.tvStatus.setText(context.getString(
                                R.string.invalid_qr
                        ));

                holder.tvStatus.setBackgroundResource(R.drawable.status_invalid_bg);

                break;

            case EntryLog.PASS_NOT_FOUND:

                holder.tvStatus.setText(context.getString(
                                R.string.pass_not_found
                        ));

                holder.tvStatus.setBackgroundResource(R.drawable.status_not_found_bg);

                break;

            default:

                holder.tvStatus.setText(log.getStatus());

                holder.tvStatus.setBackgroundResource(R.drawable.status_not_found_bg);

                break;

        }

    }

    @Override
    public int getItemCount() {

        return logList.size();

    }

    static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tvAvatar;
        TextView tvStudentName;
        TextView tvRoll;
        TextView tvEvent;
        TextView tvTime;
        TextView tvStatus;

        public LogViewHolder(@NonNull View itemView) {

            super(itemView);

            tvAvatar = itemView.findViewById(R.id.tvAvatar);

            tvStudentName = itemView.findViewById(R.id.tvStudentName);

            tvRoll = itemView.findViewById(R.id.tvRoll);

            tvEvent = itemView.findViewById(R.id.tvEvent);

            tvTime = itemView.findViewById(R.id.tvTime);

            tvStatus = itemView.findViewById(R.id.tvStatus);

        }

    }

}