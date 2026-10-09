package com.airtribe.ridewise.exception;

import java.util.UUID;

public class DriverNotFoundException extends RideWiseException {

    public DriverNotFoundException(UUID driverId) {
        super("Driver not found with ID: " + driverId);
    }
}