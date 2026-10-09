# RideWise Requirements

**Author:** Vishal Verma  
**Purpose:** Define functional scope, business rules, and assumptions.

## 1. Purpose and scope
RideWise is a console-based ride-booking simulation intended to demonstrate object-oriented design, interchangeable strategies, validation, exception handling, and testing.

## 2. Functional requirements

| ID | Requirement | Expected behavior |
|---|---|---|
| FR-01 | Register rider | Accept a non-blank name and location; generate a unique rider ID. |
| FR-02 | Retrieve rider | Find a rider by ID or report that the rider does not exist. |
| FR-03 | List riders | Show registered riders or a helpful empty-state message. |
| FR-04 | Register driver | Accept a non-blank name/location and valid vehicle type. |
| FR-05 | Retrieve driver | Find a driver by ID or report that the driver does not exist. |
| FR-06 | List drivers | Show ID, name, vehicle, location, availability, and active ride count. |
| FR-07 | Request ride | Validate rider ID and positive finite distance, select a driver, assign it, and return the ride. |
| FR-08 | Match driver | Use the configured matching strategy to select an eligible driver. |
| FR-09 | Calculate fare | Use the configured fare strategy. |
| FR-10 | Complete ride | Complete an assigned ride and generate a fare receipt. |
| FR-11 | Cancel ride | Cancel a requested/assigned ride and release an assigned driver when appropriate. |
| FR-12 | View rides | Display ride, rider, driver, distance, and status information. |
| FR-13 | View receipt | Show the receipt if available; otherwise explain that none exists yet. |
| FR-14 | Handle invalid input | Show a clear error and keep the menu usable for expected errors. |
| FR-15 | Exit | Exit cleanly when option `0` is selected. |

## 3. Business rules
1. Names and locations must not be null or blank.
2. Vehicle type must be `BIKE`, `AUTO`, or `CAR`.
3. Ride distance must be finite and greater than zero.
4. A new ride starts as `REQUESTED`.
5. Only a requested ride may be assigned a driver.
6. Only an assigned ride may be completed.
7. Only a requested or assigned ride may be cancelled.
8. A completed ride must have a receipt associated with the same ride ID.
9. Completion/cancellation must release the driver's active ride allocation exactly once.
10. Unavailable drivers must not be selected.
11. UUIDs identify riders, drivers, and rides.

Confirm these rules against the actual assignment brief and final implementation.

## 4. Non-functional requirements
- **Maintainability:** Separate model, service, strategy, exception, and utility concerns.
- **Testability:** Business logic should run without console interaction.
- **Clarity:** Expected errors should be explained without showing stack traces to users.
- **Reliability:** Rejected operations should not corrupt ride/driver state.
- **Extensibility:** Matching and fare algorithms should be replaceable through interfaces.

## 5. Out of scope
Database persistence, REST APIs, authentication, payment processing, real GPS/map integration, production surge pricing, and distributed/concurrent ride allocation.

## 6. Assumptions and open decisions
- Numeric locations are placeholders for a simple distance comparison.
- Sample fare rates (15.00 default, 20.00 peak) must be checked against the assignment.
- Duplicate-name rejection is a current policy choice; confirm whether duplicate names should be allowed.
- If drivers with active rides are always unavailable, the least-active strategy may not balance load meaningfully. Clarify the expected capacity rules.
