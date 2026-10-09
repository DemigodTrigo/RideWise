package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.RideWiseException;
import com.airtribe.ridewise.model.*;
import com.airtribe.ridewise.service.*;
import com.airtribe.ridewise.strategy.*;

import java.util.Scanner;
import java.util.UUID;

/*
 * Author: Vishal Verma
 * Purpose: Provides the interactive console menu for RideWise.
 */

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final RiderService RIDER_SERVICE = new RiderService();
    private static final DriverService DRIVER_SERVICE = new DriverService();

    private static final RideService RIDE_SERVICE = new RideService(
            RIDER_SERVICE,
            DRIVER_SERVICE,
            new NearestDriverStrategy(),
            new DefaultFareStrategy()
    );

    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("       Welcome to RideWise");
        System.out.println("================================");

        boolean running = true;

        while (running) {
            printMenu();

            try {
                int choice = readInt("Choose an option: ");

                switch (choice) {
                    case 1 -> registerRider();
                    case 2 -> registerDriver();
                    case 3 -> requestRide();
                    case 4 -> completeRide();
                    case 5 -> cancelRide();
                    case 6 -> viewRiders();
                    case 7 -> viewDrivers();
                    case 8 -> viewRides();
                    case 9 -> viewFareReceipt();
                    case 0 -> {
                        running = false;
                        System.out.println("Thank you for using RideWise!");
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (RideWiseException | IllegalArgumentException exception) {
                System.out.println("Error: " + exception.getMessage());
            } catch (Exception exception) {
                System.out.println("Unexpected error: " + exception.getMessage());
            }

            System.out.println();
        }

        SCANNER.close();
    }

    private static void printMenu() {
        System.out.println("""
                
                -------- RideWise Menu --------
                1. Register Rider
                2. Register Driver
                3. Request Ride
                4. Complete Ride
                5. Cancel Ride
                6. View Riders
                7. View Drivers
                8. View Rides
                9. View Fare Receipt
                0. Exit
                -------------------------------
                """);
    }

    private static void registerRider() {
        String name = readText("Enter rider name: ");
        String location = readText("Enter rider location (numeric position): ");

        Rider rider = RIDER_SERVICE.registerRider(name, location);

        System.out.println("Rider registered successfully!");
        System.out.println("Rider ID: " + rider.getId());
    }

    private static void registerDriver() {
        String name = readText("Enter driver name: ");
        String location = readText("Enter driver location (numeric position): ");

        VehicleType vehicleType = VehicleType.valueOf(
                readText("Vehicle type (BIKE, AUTO, CAR): ").toUpperCase()
        );

        Driver driver = DRIVER_SERVICE.registerDriver(
                name, location, vehicleType
        );

        System.out.println("Driver registered successfully!");
        System.out.println("Driver ID: " + driver.getId());
    }

    private static void requestRide() {
        UUID riderId = readUUID("Enter rider ID: ");
        double distance = readDouble("Enter ride distance: ");

        Ride ride = RIDE_SERVICE.requestRide(riderId, distance);

        System.out.println("Ride requested successfully!");
        System.out.println("Ride ID: " + ride.getId());
        System.out.println("Assigned driver: " + ride.getDriver().getName());
        System.out.println("Status: " + ride.getStatus());
    }

    private static void completeRide() {
        UUID rideId = readUUID("Enter ride ID: ");

        Ride ride = RIDE_SERVICE.completeRide(rideId);

        System.out.println("Ride completed successfully!");
        System.out.println("Final fare: "
                + ride.getFareReceipt().getAmount());
        System.out.println("Receipt: " + ride.getFareReceipt());
    }

    private static void cancelRide() {
        UUID rideId = readUUID("Enter ride ID: ");

        Ride ride = RIDE_SERVICE.cancelRide(rideId);

        System.out.println("Ride cancelled successfully!");
        System.out.println("Status: " + ride.getStatus());
    }

    private static void viewRiders() {
        RIDER_SERVICE.getAllRiders().forEach(rider ->
                System.out.println(
                        "ID: " + rider.getId()
                                + " | Name: " + rider.getName()
                                + " | Location: " + rider.getLocation()
                )
        );
    }

    private static void viewDrivers() {
        DRIVER_SERVICE.getAllDrivers().forEach(driver ->
                System.out.println(
                        "ID: " + driver.getId()
                                + " | Name: " + driver.getName()
                                + " | Vehicle: " + driver.getVehicleType()
                                + " | Location: " + driver.getCurrentLocation()
                                + " | Available: " + driver.isAvailable()
                                + " | Active rides: " + driver.getActiveRideCount()
                )
        );
    }

    private static void viewRides() {
        RIDE_SERVICE.getAllRides().forEach(ride ->
                System.out.println(
                        "Ride ID: " + ride.getId()
                                + " | Rider: " + ride.getRider().getName()
                                + " | Driver: " + (ride.getDriver() == null
                                ? "Not assigned"
                                : ride.getDriver().getName())
                                + " | Distance: " + ride.getDistance()
                                + " | Status: " + ride.getStatus()
                )
        );
    }

    private static void viewFareReceipt() {
        UUID rideId = readUUID("Enter ride ID: ");
        Ride ride = RIDE_SERVICE.getRideById(rideId);

        if (ride.getFareReceipt() == null) {
            System.out.println("No fare receipt available for this ride.");
            return;
        }

        System.out.println(ride.getFareReceipt());
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    private static int readInt(String prompt) {
        try {
            return Integer.parseInt(readText(prompt));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Please enter a valid integer.");
        }
    }

    private static double readDouble(String prompt) {
        try {
            return Double.parseDouble(readText(prompt));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Please enter a valid number.");
        }
    }

    private static UUID readUUID(String prompt) {
        try {
            return UUID.fromString(readText(prompt));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Please enter a valid UUID.");
        }
    }
}
