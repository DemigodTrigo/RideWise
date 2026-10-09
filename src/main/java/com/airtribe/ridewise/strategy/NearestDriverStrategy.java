package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class NearestDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        Objects.requireNonNull(rider, "Rider cannot be null");
        Objects.requireNonNull(drivers, "Driver list cannot be null");

        return drivers.stream()
                .filter(Driver::isAvailable)
                .min(Comparator.comparingDouble(
                        driver -> calculateDistance(
                                rider.getLocation(),
                                driver.getCurrentLocation()
                        )
                ))
                .orElseThrow(() ->
                        new NoDriverAvailableException(
                                "No available drivers found."
                        )
                );
    }

    private double calculateDistance(String riderLocation,
                                     String driverLocation) {
        // Temporary assumption: locations are numeric coordinates.
        try {
            double riderPosition = Double.parseDouble(riderLocation);
            double driverPosition = Double.parseDouble(driverLocation);
            return Math.abs(riderPosition - driverPosition);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Locations must be numeric coordinates for distance calculation.",
                    exception
            );
        }
    }
}