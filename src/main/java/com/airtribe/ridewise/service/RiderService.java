package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.DuplicateRiderException;
import com.airtribe.ridewise.exception.InvalidInputException;
import com.airtribe.ridewise.exception.RiderNotFoundException;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class RiderService {

    private final Map<UUID, Rider> riders = new LinkedHashMap<>();

    public Rider registerRider(String name, String location) {
        validateText(name, "Rider name");
        validateText(location, "Rider location");

        boolean duplicate = riders.values().stream()
                .anyMatch(rider ->
                        rider.getName().equalsIgnoreCase(name.trim()));

        if (duplicate) {
            throw new DuplicateRiderException(
                    "A rider with this name already exists."
            );
        }

        Rider rider = new Rider(
                IdGenerator.generateId(),
                name.trim(),
                location.trim()
        );

        riders.put(rider.getId(), rider);
        return rider;
    }

    public Rider getRiderById(UUID riderId) {
        if (riderId == null) {
            throw new InvalidInputException("Rider ID cannot be null.");
        }

        Rider rider = riders.get(riderId);

        if (rider == null) {
            throw new RiderNotFoundException(riderId);
        }

        return rider;
    }

    public Collection<Rider> getAllRiders() {
        return java.util.List.copyOf(riders.values());
    }

    private void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidInputException(
                    fieldName + " cannot be null or blank."
            );
        }
    }
}