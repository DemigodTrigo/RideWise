package com.airtribe.ridewise.exception;

import java.util.UUID;

public class RiderNotFoundException extends RideWiseException {

    public RiderNotFoundException(UUID riderId) {
        super("Rider not found with ID: " + riderId);
    }
}