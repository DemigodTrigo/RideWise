
package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.DuplicateDriverException;
import com.airtribe.ridewise.exception.InvalidInputException;
import com.airtribe.ridewise.exception.DriverNotFoundException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.VehicleType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Vishal Verma
 * Purpose: Tests driver registration, availability, and retrieval.
 */

class DriverServiceTest {

    private DriverService driverService;

    @BeforeEach
    void setUp() {
        driverService = new DriverService();
    }

    @Test
    void shouldRegisterDriverAsAvailable() {
        Driver driver = driverService.registerDriver(
                "Rahul", "10", VehicleType.CAR);

        assertNotNull(driver.getId());
        assertTrue(driver.isAvailable());
    }

    @Test
    void shouldRejectBlankDriverName() {
        assertThrows(InvalidInputException.class,
                () -> driverService.registerDriver(
                        " ", "10", VehicleType.CAR));
    }

    @Test
    void shouldRejectDuplicateDriverName() {
        driverService.registerDriver("Rahul", "10", VehicleType.CAR);

        assertThrows(DuplicateDriverException.class,
                () -> driverService.registerDriver(
                        "Rahul", "20", VehicleType.BIKE));
    }

    @Test
    void shouldReturnOnlyAvailableDrivers() {
        Driver available = driverService.registerDriver(
                "Rahul", "10", VehicleType.CAR);

        Driver unavailable = driverService.registerDriver(
                "Aman", "20", VehicleType.BIKE);

        driverService.updateAvailability(unavailable.getId(), false);

        assertEquals(1, driverService.getAvailableDrivers().size());
        assertEquals(available.getId(),
                driverService.getAvailableDrivers().get(0).getId());
    }

    @Test
    void shouldThrowWhenDriverDoesNotExist() {
        assertThrows(DriverNotFoundException.class,
                () -> driverService.getDriverById(UUID.randomUUID()));
    }
}