package com.example.campuseventpasssystem.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuseventpasssystem.R;
import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.ui.event.EventDetailsActivity;

import java.util.List;

public class EventCardAdapter extends RecyclerView.Adapter<EventCardAdapter.EventViewHolder> {
    private final Context context;
    private final List<Event> eventList;

    public EventCardAdapter(Context context, List<Event> eventList) {

        this.context = context;
        this.eventList = eventList;

    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_student_event, parent, false);

        return new EventViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        Event event = eventList.get(position);

        holder.tvEventName.setText(event.getEventName());

        holder.tvEventInfo.setText(event.getEventDate() + " • " + event.getEventVenue());

        holder.tvViewDetails.setOnClickListener(v -> {

            Intent intent = new Intent(context, EventDetailsActivity.class);

            intent.putExtra("EVENT_ID", event.getEventId());

            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {

        return eventList.size();

    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName;
        TextView tvEventInfo;
        TextView tvViewDetails;

        public EventViewHolder(@NonNull View itemView) {

            super(itemView);

            tvEventName = itemView.findViewById(R.id.tvEventName);

            tvEventInfo = itemView.findViewById(R.id.tvEventInfo);

            tvViewDetails = itemView.findViewById(R.id.tvViewDetails);

        }

    }

}