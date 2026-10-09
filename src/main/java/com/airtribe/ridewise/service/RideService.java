package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.InvalidInputException;
import com.airtribe.ridewise.exception.InvalidRideStateException;
import com.airtribe.ridewise.exception.RideNotFoundException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
 * Author: Vishal Verma
 * Purpose: Manages ride requests, driver assignment,
 * fare calculation, completion, and cancellation.
 */

public class RideService {

    private final RiderService riderService;
    private final DriverService driverService;
    private final RideMatchingStrategy matchingStrategy;
    private final FareStrategy fareStrategy;
    private final Map<UUID, Ride> rides = new LinkedHashMap<>();

    public RideService(
            RiderService riderService,
            DriverService driverService,
            RideMatchingStrategy matchingStrategy,
            FareStrategy fareStrategy
    ) {
        this.riderService = riderService;
        this.driverService = driverService;
        this.matchingStrategy = matchingStrategy;
        this.fareStrategy = fareStrategy;
    }

    public Ride requestRide(UUID riderId, double distance) {
        Rider rider = riderService.getRiderById(riderId);

        if (!Double.isFinite(distance) || distance <= 0) {
            throw new InvalidInputException(
                    "Distance must be a finite, positive value."
            );
        }

        Ride ride = new Ride(IdGenerator.generateId(), rider, distance);

        Driver driver = matchingStrategy.findDriver(
                rider,
                driverService.getAvailableDrivers()
        );

        if (driver.getActiveRideCount() < 0) {
            throw new InvalidRideStateException(
                    "Driver active ride count cannot be negative."
            );
        }

        ride.assignDriver(driver);
        driver.incrementActiveRideCount();
        rides.put(ride.getId(), ride);

        return ride;
    }

    public Ride completeRide(UUID rideId) {
        Ride ride = getRideById(rideId);

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new InvalidRideStateException(
                    "Only assigned rides can be completed."
            );
        }

        double amount = fareStrategy.calculateFare(ride);
        FareReceipt receipt = new FareReceipt(ride.getId(), amount);

        ride.complete(receipt);
        ride.getDriver().decrementActiveRideCount();

        return ride;
    }

    public Ride cancelRide(UUID rideId) {
        Ride ride = getRideById(rideId);

        Driver driver = ride.getDriver();
        boolean wasAssigned = ride.getStatus() == RideStatus.ASSIGNED;

        ride.cancel();

        if (wasAssigned && driver != null) {
            driver.decrementActiveRideCount();
        }

        return ride;
    }

    public Ride getRideById(UUID rideId) {
        if (rideId == null) {
            throw new InvalidInputException("Ride ID cannot be null.");
        }

        Ride ride = rides.get(rideId);

        if (ride == null) {
            throw new RideNotFoundException(rideId);
        }

        return ride;
    }

    public List<Ride> getAllRides() {
        return List.copyOf(rides.values());
    }
}