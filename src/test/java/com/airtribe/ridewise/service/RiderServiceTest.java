package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.DuplicateRiderException;
import com.airtribe.ridewise.exception.InvalidInputException;
import com.airtribe.ridewise.exception.RiderNotFoundException;
import com.airtribe.ridewise.model.Rider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Vishal Verma
 * Purpose: Tests rider registration, validation, and retrieval.
 */

class RiderServiceTest {

    private RiderService riderService;

    @BeforeEach
    void setUp() {
        riderService = new RiderService();
    }

    @Test
    void shouldRegisterAndRetrieveRider() {
        Rider rider = riderService.registerRider("Vishal", "10");

        assertNotNull(rider.getId());
        assertEquals("Vishal", riderService
                .getRiderById(rider.getId()).getName());
    }

    @Test
    void shouldRejectBlankRiderName() {
        assertThrows(InvalidInputException.class,
                () -> riderService.registerRider(" ", "10"));
    }

    @Test
    void shouldRejectDuplicateRiderName() {
        riderService.registerRider("Vishal", "10");

        assertThrows(DuplicateRiderException.class,
                () -> riderService.registerRider("Vishal", "20"));
    }

    @Test
    void shouldThrowWhenRiderDoesNotExist() {
        assertThrows(RiderNotFoundException.class,
                () -> riderService.getRiderById(UUID.randomUUID()));
    }
}