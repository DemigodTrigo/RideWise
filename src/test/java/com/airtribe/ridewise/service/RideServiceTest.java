package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.InvalidInputException;
import com.airtribe.ridewise.exception.InvalidRideStateException;
import com.airtribe.ridewise.model.*;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Vishal Verma
 * Purpose: Tests ride requests, completion, cancellation, and fare calculation.
 */

class RideServiceTest {

    private RiderService riderService;
    private DriverService driverService;
    private RideService rideService;
    private Rider rider;
    private Driver driver;

    @BeforeEach
    void setUp() {
        riderService = new RiderService();
        driverService = new DriverService();

        rider = riderService.registerRider("Vishal", "10");
        driver = driverService.registerDriver(
                "Rahul", "12", VehicleType.CAR);

        rideService = new RideService(
                riderService,
                driverService,
                new NearestDriverStrategy(),
                new DefaultFareStrategy()
        );
    }

    @Test
    void shouldRequestRideAndAssignDriver() {
        Ride ride = rideService.requestRide(rider.getId(), 10);

        assertEquals(RideStatus.ASSIGNED, ride.getStatus());
        assertEquals(driver.getId(), ride.getDriver().getId());
        assertFalse(driver.isAvailable());
    }

    @Test
    void shouldCompleteRideAndGenerateFareReceipt() {
        Ride ride = rideService.requestRide(rider.getId(), 10);

        Ride completedRide = rideService.completeRide(ride.getId());

        assertEquals(RideStatus.COMPLETED, completedRide.getStatus());
        assertNotNull(completedRide.getFareReceipt());
        assertEquals(150.00,
                completedRide.getFareReceipt().getAmount(), 0.001);
        assertTrue(driver.isAvailable());
    }

    @Test
    void shouldCancelAssignedRideAndReleaseDriver() {
        Ride ride = rideService.requestRide(rider.getId(), 10);

        Ride cancelledRide = rideService.cancelRide(ride.getId());

        assertEquals(RideStatus.CANCELLED, cancelledRide.getStatus());
        assertTrue(driver.isAvailable());
    }

    @Test
    void shouldNotCompleteRideTwice() {
        Ride ride = rideService.requestRide(rider.getId(), 10);
        rideService.completeRide(ride.getId());

        assertThrows(InvalidRideStateException.class,
                () -> rideService.completeRide(ride.getId()));
    }

    @Test
    void shouldRejectInvalidRideDistance() {
        assertThrows(InvalidInputException.class,
                () -> rideService.requestRide(rider.getId(), -5));
    }

    @Test
    void shouldRejectUnknownRideId() {
        assertThrows(RuntimeException.class,
                () -> rideService.getRideById(UUID.randomUUID()));
    }
}
