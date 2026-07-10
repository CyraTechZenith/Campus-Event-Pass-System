package com.example.campuseventpasssystem.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {

    public static final String ACTIVE = "ACTIVE";
    public static final String COMPLETED = "COMPLETED";
    public static final String CANCELLED = "CANCELLED";
    @PrimaryKey(autoGenerate = true)
    private int eventId;
    @NonNull
    private String eventName;
    @NonNull
    private String eventDescription;
    @NonNull
    private String eventDate;
    @NonNull
    private String eventTime;
    @NonNull
    private String eventVenue;
    private String eventBannerUri;
    private int participationLimit;
    private int registeredCount = 0;
    @NonNull
    private String eventStatus;

    public Event(
            @NonNull String eventName,
            @NonNull String eventDescription,
            @NonNull String eventDate,
            @NonNull String eventTime,
            @NonNull String eventVenue,
            String eventBannerUri,
            int participationLimit,
            @NonNull String eventStatus) {

        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventVenue = eventVenue;
        this.eventBannerUri = eventBannerUri;
        this.participationLimit = participationLimit;
        this.registeredCount = 0;
        this.eventStatus = eventStatus;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    @NonNull
    public String getEventName() {
        return eventName;
    }

    public void setEventName(@NonNull String eventName) {
        this.eventName = eventName;
    }

    @NonNull
    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(@NonNull String eventDescription) {
        this.eventDescription = eventDescription;
    }

    @NonNull
    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(@NonNull String eventDate) {
        this.eventDate = eventDate;
    }

    @NonNull
    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(@NonNull String eventTime) {
        this.eventTime = eventTime;
    }

    @NonNull
    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(@NonNull String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventBannerUri() {
        return eventBannerUri;
    }

    public void setEventBannerUri(String eventBannerUri) {
        this.eventBannerUri = eventBannerUri;
    }

    public int getParticipationLimit() {
        return participationLimit;
    }

    public void setParticipationLimit(int participationLimit) {
        this.participationLimit = participationLimit;
    }

    public int getRegisteredCount() {
        return registeredCount;
    }

    public void setRegisteredCount(int registeredCount) {
        this.registeredCount = registeredCount;
    }

    @NonNull
    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(@NonNull String eventStatus) {
        this.eventStatus = eventStatus;
    }
}