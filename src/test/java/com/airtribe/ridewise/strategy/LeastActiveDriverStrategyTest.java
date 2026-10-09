package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Vishal Verma
 * Purpose: Tests least-active driver selection.
 */

class LeastActiveDriverStrategyTest {

    @Test
    void shouldSelectAvailableDriver() {
        Rider rider = new Rider(UUID.randomUUID(), "Vishal", "10");
        Driver driver = new Driver(UUID.randomUUID(), "Rahul", "12", VehicleType.CAR);

        LeastActiveDriverStrategy strategy = new LeastActiveDriverStrategy();

        assertEquals(driver, strategy.findDriver(rider, List.of(driver)));
    }

    @Test
    void shouldThrowWhenNoDriverIsAvailable() {
        Rider rider = new Rider(UUID.randomUUID(), "Vishal", "10");
        Driver driver = new Driver(UUID.randomUUID(), "Rahul", "12", VehicleType.CAR);
        driver.markUnavailable();

        LeastActiveDriverStrategy strategy = new LeastActiveDriverStrategy();

        assertThrows(NoDriverAvailableException.class, () -> strategy.findDriver(rider, List.of(driver)));
    }
}
