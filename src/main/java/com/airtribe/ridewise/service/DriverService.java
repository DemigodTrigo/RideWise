package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.DriverNotFoundException;
import com.airtribe.ridewise.exception.DuplicateDriverException;
import com.airtribe.ridewise.exception.InvalidInputException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DriverService {

    private final Map<UUID, Driver> drivers = new LinkedHashMap<>();

    public Driver registerDriver(
            String name,
            String location,
            VehicleType vehicleType
    ) {
        validateText(name, "Driver name");
        validateText(location, "Driver location");

        if (vehicleType == null) {
            throw new InvalidInputException("Vehicle type cannot be null.");
        }

        boolean duplicate = drivers.values().stream()
                .anyMatch(driver ->
                        driver.getName().equalsIgnoreCase(name.trim()));

        if (duplicate) {
            throw new DuplicateDriverException(
                    "A driver with this name already exists."
            );
        }

        Driver driver = new Driver(
                IdGenerator.generateId(),
                name.trim(),
                location.trim(),
                vehicleType
        );

        drivers.put(driver.getId(), driver);
        return driver;
    }

    public Driver getDriverById(UUID driverId) {
        if (driverId == null) {
            throw new InvalidInputException("Driver ID cannot be null.");
        }

        Driver driver = drivers.get(driverId);

        if (driver == null) {
            throw new DriverNotFoundException(driverId);
        }

        return driver;
    }

    public List<Driver> getAvailableDrivers() {
        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .toList();
    }

    public Collection<Driver> getAllDrivers() {
        return List.copyOf(drivers.values());
    }

    public void updateAvailability(UUID driverId, boolean available) {
        Driver driver = getDriverById(driverId);

        if (available) {
            if (driver.getActiveRideCount() > 0) {
                throw new InvalidInputException(
                        "A driver with active rides cannot be marked available."
                );
            }
            driver.markAvailable();
        } else {
            driver.markUnavailable();
        }
    }

    public void updateLocation(UUID driverId, String location) {
        validateText(location, "Driver location");
        getDriverById(driverId).updateLocation(location.trim());
    }

    private void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidInputException(
                    fieldName + " cannot be null or blank."
            );
        }
    }
}