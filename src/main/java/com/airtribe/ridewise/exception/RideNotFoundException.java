package com.airtribe.ridewise.exception;

import java.util.UUID;

public class RideNotFoundException extends RideWiseException {

    public RideNotFoundException(UUID rideId) {
        super("Ride not found with ID: " + rideId);
    }
}