 package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

 /*
  * Author: Vishal Verma
  * Purpose: Tests peak-hour ride fare calculation.
  */

class PeakHourFareStrategyTest {

    @Test
    void shouldCalculatePeakHourFareCorrectly() {
        Rider rider = new Rider(
                UUID.randomUUID(), "Vishal", "10");

        Ride ride = new Ride(
                UUID.randomUUID(), rider, 10);

        PeakHourFareStrategy strategy = new PeakHourFareStrategy();

        assertEquals(200.00, strategy.calculateFare(ride), 0.001);
    }
}
