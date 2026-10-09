package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.exception.InvalidInputException;
import com.airtribe.ridewise.model.Ride;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class DefaultFareStrategy implements FareStrategy {

    private static final BigDecimal RATE_PER_DISTANCE =
            new BigDecimal("15.00");

    private static final int CURRENCY_SCALE = 2;

    @Override
    public double calculateFare(Ride ride) {
        Objects.requireNonNull(ride, "Ride cannot be null");

        double distance = ride.getDistance();

        if (!Double.isFinite(distance) || distance <= 0) {
            throw new InvalidInputException(
                    "Ride distance must be a finite, positive value."
            );
        }

        return BigDecimal.valueOf(distance)
                .multiply(RATE_PER_DISTANCE)
                .setScale(CURRENCY_SCALE, RoundingMode.HALF_UP)
                .doubleValue();
    }
}