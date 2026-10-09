package com.airtribe.ridewise.exception;

import java.util.UUID;

public class DriverUnavailableException extends RideWiseException {

    public DriverUnavailableException(UUID driverId) {
        super("Driver is currently unavailable: " + driverId);
    }
}