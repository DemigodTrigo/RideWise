package com.airtribe.ridewise.model;

import java.util.Objects;
import java.util.UUID;

public class Driver {

    private final UUID id;
    private final String name;
    private String currentLocation;
    private boolean available;
    private final VehicleType vehicleType;
    private int activeRideCount;

    public Driver(
            UUID id,
            String name,
            String currentLocation,
            VehicleType vehicleType
    ) {
        this.id = Objects.requireNonNull(id, "Driver ID cannot be null");
        this.name = Objects.requireNonNull(name, "Driver name cannot be null");
        this.currentLocation = Objects.requireNonNull(
                currentLocation,
                "Driver location cannot be null"
        );
        this.vehicleType = Objects.requireNonNull(
                vehicleType,
                "Vehicle type cannot be null"
        );

        this.available = true;
        this.activeRideCount = 0;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public boolean isAvailable() {
        return available;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getActiveRideCount() {
        return activeRideCount;
    }

    public void updateLocation(String currentLocation) {
        this.currentLocation = Objects.requireNonNull(
                currentLocation,
                "Driver location cannot be null"
        );
    }

    public void markAvailable() {
        this.available = true;
    }

    public void markUnavailable() {
        this.available = false;
    }

    public void incrementActiveRideCount() {
        activeRideCount++;
        available = false;
    }

    public void decrementActiveRideCount() {
        if (activeRideCount > 0) {
            activeRideCount--;
        }

        if (activeRideCount == 0) {
            available = true;
        }
    }
}