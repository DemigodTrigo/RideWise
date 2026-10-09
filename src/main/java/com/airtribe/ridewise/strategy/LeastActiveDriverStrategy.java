package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        Objects.requireNonNull(rider, "Rider cannot be null");
        Objects.requireNonNull(drivers, "Driver list cannot be null");

        return drivers.stream()
                .filter(Driver::isAvailable)
                .min(Comparator.comparingInt(Driver::getActiveRideCount))
                .orElseThrow(() ->
                        new NoDriverAvailableException(
                                "No available drivers found."
                        )
                );
    }
}