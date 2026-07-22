package com.example.campuseventpasssystem.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.DatabaseClient;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.ui.admin.ManageEventsActivity;
import com.example.campuseventpasssystem.ui.admin.ParticipantsListActivity;
import com.example.campuseventpasssystem.ui.admin.AdminEventCardActivity;
import com.example.campuseventpasssystem.ui.qr.ScanPassActivity;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ManageEventsAdapter extends RecyclerView.Adapter<ManageEventsAdapter.EventViewHolder> {
    private final Context context;
    private final List<Event> eventList;

    public ManageEventsAdapter(Context context, List<Event> eventList) {

        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_event, parent, false);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        Event event = eventList.get(position);

        holder.tvEventName.setText(event.getEventName());

        holder.tvDateTime.setText(event.getEventDate() + " | " + event.getEventTime());

        holder.tvVenue.setText(event.getEventVenue());

        boolean active = Event.ACTIVE.equals(event.getEventStatus());

        // Check whether the event has already ended

        try {
            String dateTime = event.getEventDate() + " " + event.getEventTime();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            Date eventDateTime = format.parse(dateTime);

            if (eventDateTime != null && eventDateTime.before(new Date())) {

                active = false;

            }

        } catch (Exception e) {

            active = false;

        }

        if (active) {

            holder.tvStatus.setText(R.string.active);

            holder.tvStatus.setBackgroundResource(R.drawable.status_active_bg);

        } else {

            holder.tvStatus.setText(R.string.inactive);

            holder.tvStatus.setBackgroundResource(R.drawable.status_inactive_bg);

        }

        int participants = DatabaseClient.getInstance(context).registrationDao().getParticipantCount(event.getEventId());

        int limit = event.getParticipationLimit();

        if (limit > 0) {

            holder.tvParticipants.setText(context.getString(R.string.participants_count, participants));

        } else {

            holder.tvParticipants.setText(context.getString(R.string.registered_count, participants));
        }


        // Banner

        if (event.getEventBannerUri() != null && !event.getEventBannerUri().isEmpty()) {

            try {
                holder.imgBanner.setImageURI(Uri.parse(event.getEventBannerUri()));
            } catch (Exception e) {
                holder.imgBanner.setImageResource(R.drawable.default_event_banner);
            }

        } else {

            holder.imgBanner.setImageResource(R.drawable.default_event_banner);

        }


        // Open Event Details

        holder.cardRoot.setOnClickListener(v -> {

            Intent intent;

            boolean scanMode = false;

            if (context instanceof ManageEventsActivity) {

                scanMode = ((ManageEventsActivity) context).getIntent().getBooleanExtra("SCAN_MODE", false);

            }

            if (scanMode) {

                intent = new Intent(context, ScanPassActivity.class);

            } else {

                intent = new Intent(context, AdminEventCardActivity.class);

            }

            intent.putExtra("EVENT_ID", event.getEventId());

            context.startActivity(intent);

        });


        // View Participants

        holder.tvViewParticipants.setOnClickListener(v -> {

            Intent intent = new Intent(context, ParticipantsListActivity.class);

            intent.putExtra("EVENT_ID", event.getEventId());

            context.startActivity(intent);

        });

        // Delete Event

        holder.cardRoot.setOnLongClickListener(v -> {

            new AlertDialog.Builder(context).setTitle(R.string.delete_event).setMessage(context.getString(R.string.delete_event_message,event.getEventName())).setPositiveButton(R.string.delete, (dialog, which) -> {

                        int registrations = DatabaseClient.getInstance(context).registrationDao().getParticipantCount(event.getEventId());

                        if (registrations > 0) {

                            Toast.makeText(context, R.string.students_already_registered, Toast.LENGTH_SHORT).show();

                            return;
                        }

                        DatabaseClient.getInstance(context).eventDao().deleteEvent(event.getEventId());

                        int adapterPosition = holder.getAdapterPosition();

                        if (adapterPosition != RecyclerView.NO_POSITION) {

                            eventList.remove(adapterPosition);

                            notifyItemRemoved(adapterPosition);
                        }

                        Toast.makeText(context, R.string.event_deleted, Toast.LENGTH_SHORT).show();

                    }).setNegativeButton(R.string.cancel, null).show();

            return true;

        });

    }

    @Override
    public int getItemCount() {

        return eventList.size();
    }


    static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBanner;
        TextView tvEventName;
        TextView tvDateTime;
        TextView tvVenue;
        TextView tvParticipants;
        TextView tvStatus;
        TextView tvViewParticipants;
        CardView cardRoot;

        public EventViewHolder(@NonNull View itemView) {

            super(itemView);

            cardRoot = itemView.findViewById(R.id.cardRoot);

            imgBanner = itemView.findViewById(R.id.imgBanner);

            tvEventName = itemView.findViewById(R.id.tvEventName);

            tvDateTime = itemView.findViewById(R.id.tvDateTime);

            tvVenue = itemView.findViewById(R.id.tvVenue);

            tvParticipants = itemView.findViewById(R.id.tvParticipants);

            tvStatus = itemView.findViewById(R.id.tvStatus);

            tvViewParticipants = itemView.findViewById(R.id.tvViewParticipants);

        }
    }
}