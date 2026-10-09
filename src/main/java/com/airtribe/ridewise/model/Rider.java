package com.airtribe.ridewise.model;

import java.util.Objects;
import java.util.UUID;

public class Rider {

    private final UUID id;
    private final String name;
    private String location;

    public Rider(UUID id, String name, String location) {
        this.id = Objects.requireNonNull(id, "Rider ID cannot be null");
        this.name = Objects.requireNonNull(name, "Rider name cannot be null");
        this.location = Objects.requireNonNull(location, "Rider location cannot be null");
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public void updateLocation(String location) {
        this.location = Objects.requireNonNull(
                location,
                "Rider location cannot be null"
        );
    }
}