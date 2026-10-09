package com.airtribe.ridewise.model;

import com.airtribe.ridewise.exception.InvalidInputException;
import com.airtribe.ridewise.exception.InvalidRideStateException;

import java.util.Objects;
import java.util.UUID;

public class Ride {

    private final UUID id;
    private final Rider rider;
    private final double distance;

    private Driver driver;
    private RideStatus status;
    private FareReceipt fareReceipt;

    public Ride(UUID id, Rider rider, double distance) {
        this.id = Objects.requireNonNull(id, "Ride ID cannot be null");
        this.rider = Objects.requireNonNull(rider, "Rider cannot be null");

        if (!Double.isFinite(distance) || distance <= 0) {
            throw new InvalidInputException(
                    "Ride distance must be a finite, positive value."
            );
        }

        this.distance = distance;
        this.status = RideStatus.REQUESTED;
    }

    public UUID getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public double getDistance() {
        return distance;
    }

    public RideStatus getStatus() {
        return status;
    }

    public FareReceipt getFareReceipt() {
        return fareReceipt;
    }

    public void assignDriver(Driver driver) {
        requireStatus(RideStatus.REQUESTED);

        this.driver = Objects.requireNonNull(
                driver,
                "Driver cannot be null"
        );
        this.status = RideStatus.ASSIGNED;
    }

    public void complete(FareReceipt receipt) {
        requireStatus(RideStatus.ASSIGNED);

        if (driver == null) {
            throw new InvalidRideStateException(
                    "Cannot complete a ride without an assigned driver."
            );
        }

        this.fareReceipt = Objects.requireNonNull(
                receipt,
                "Fare receipt cannot be null"
        );

        if (!id.equals(receipt.getRideId())) {
            throw new InvalidInputException(
                    "Fare receipt must belong to this ride."
            );
        }

        this.status = RideStatus.COMPLETED;
    }

    public void cancel() {
        if (status != RideStatus.REQUESTED
                && status != RideStatus.ASSIGNED) {
            throw new InvalidRideStateException(
                    "Only requested or assigned rides can be cancelled."
            );
        }

        this.status = RideStatus.CANCELLED;
    }

    private void requireStatus(RideStatus expectedStatus) {
        if (status != expectedStatus) {
            throw new InvalidRideStateException(
                    "Expected ride status " + expectedStatus
                            + " but found " + status + "."
            );
        }
    }
}