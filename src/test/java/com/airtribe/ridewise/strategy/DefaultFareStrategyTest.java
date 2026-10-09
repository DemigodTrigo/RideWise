package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.exception.InvalidInputException;
import com.airtribe.ridewise.model.FareReceipt;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Vishal Verma
 * Purpose: Tests default ride fare calculation.
 */

class DefaultFareStrategyTest {

    @Test
    void shouldCalculateFareCorrectly() {
        Rider rider = new Rider(UUID.randomUUID(), "Vishal", "10");
        Ride ride = new Ride(UUID.randomUUID(), rider, 10);

        DefaultFareStrategy strategy = new DefaultFareStrategy();

        assertEquals(150.00, strategy.calculateFare(ride), 0.001);
    }

    @Test
    void shouldRejectInvalidDistance() {
        Rider rider = new Rider(UUID.randomUUID(), "Vishal", "10");

        assertThrows(InvalidInputException.class,
                () -> new Ride(UUID.randomUUID(), rider, -5));
    }
}
