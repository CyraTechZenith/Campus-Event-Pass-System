package com.example.campuseventpasssystem.database.model;

import com.example.campuseventpasssystem.database.entities.Event;
import com.example.campuseventpasssystem.database.entities.Registration;

public class RegistrationItem {
    private final Registration registration;
    private final Event event;

    public RegistrationItem(Registration registration, Event event) {

        this.registration = registration;
        this.event = event;

    }

    public Registration getRegistration() {
        return registration;
    }

    public Event getEvent() {
        return event;
    }

}