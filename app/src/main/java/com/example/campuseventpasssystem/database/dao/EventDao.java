package com.example.campuseventpasssystem.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.campuseventpasssystem.database.entities.Event;

@Dao
public interface EventDao {

    @Insert
    void insertEvent(Event event);

    @Update
    void updateEvent(Event event);

    @Query("DELETE FROM events WHERE eventId = :eventId")
    void deleteEvent(int eventId);

    @Query("SELECT * FROM events WHERE eventId = :eventId")
    Event getEventById(int eventId);

    @Query("SELECT * FROM events")
    List<Event> getAllEvents();

    @Query("SELECT * FROM events WHERE eventStatus = 'ACTIVE'")
    List<Event> getActiveEvents();

    @Query("SELECT * FROM events WHERE eventStatus = 'COMPLETED'")
    List<Event> getCompletedEvents();

    @Query("SELECT * FROM events WHERE eventStatus = 'CANCELLED'")
    List<Event> getCancelledEvents();

    @Query("SELECT COUNT(*) FROM events")
    int getTotalEventsCount();

    @Query("SELECT COUNT(*) FROM events WHERE eventStatus = 'ACTIVE'")
    int getActiveEventsCount();

    @Query("SELECT COUNT(*) FROM events WHERE eventStatus = 'COMPLETED'")
    int getCompletedEventsCount();

    @Query("UPDATE events SET registeredCount = registeredCount + 1 WHERE eventId = :eventId")
    void incrementRegisteredCount(int eventId);

    @Query("SELECT registeredCount FROM events WHERE eventId = :eventId")
    int getRegisteredCount(int eventId);

    @Query("UPDATE events SET eventStatus = :status WHERE eventId = :eventId")
    void updateEventStatus(int eventId, String status);

    @Query("SELECT participationLimit FROM events WHERE eventId = :eventId")
    int getParticipationLimit(int eventId);

    @Query("UPDATE events SET eventStatus = 'COMPLETED' WHERE eventDate < :today AND eventStatus = 'ACTIVE'")
    void autoCompleteEvents(String today);
}