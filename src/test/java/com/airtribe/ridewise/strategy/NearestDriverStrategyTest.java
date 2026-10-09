package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Vishal Verma
 * Purpose: Tests nearest available driver selection.
 */

class NearestDriverStrategyTest {

    private NearestDriverStrategy strategy;
    private Rider rider;

    @BeforeEach
    void setUp() {
        strategy = new NearestDriverStrategy();
        rider = new Rider(UUID.randomUUID(), "Vishal", "10");
    }

    @Test
    void shouldSelectNearestAvailableDriver() {
        Driver far = new Driver(
                UUID.randomUUID(), "Rahul", "30", VehicleType.CAR);
        Driver near = new Driver(
                UUID.randomUUID(), "Aman", "12", VehicleType.BIKE);

        assertEquals(near, strategy.findDriver(rider, List.of(far, near)));
    }

    @Test
    void shouldIgnoreUnavailableDrivers() {
        Driver driver = new Driver(
                UUID.randomUUID(), "Rahul", "11", VehicleType.CAR);
        driver.markUnavailable();

        assertThrows(NoDriverAvailableException.class,
                () -> strategy.findDriver(rider, List.of(driver)));
    }

    @Test
    void shouldThrowWhenNoDriversExist() {
        assertThrows(NoDriverAvailableException.class,
                () -> strategy.findDriver(rider, List.of()));
    }
}